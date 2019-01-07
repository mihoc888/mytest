package com.itheima.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.IFavoriteDao;
import com.itheima.model.*;
import com.itheima.service.IFavoriteService;
import com.itheima.util.FactoryUtils;
import com.itheima.util.JdbcUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteService implements IFavoriteService {

    private IFavoriteDao iFavoriteDao = (IFavoriteDao) FactoryUtils.getInstance("IFavoriteDao");


    /**
     * 业务 : 线路详情PageBean
     *
     * @param rid
     * @return Route
     */
    @Override
    public Route findRouteByRid(String rid) {
        //由于业务层是完成一个业务的 -->
        //这里的业务是一次性返回经过包装的页面内容,Route里面有Category,RouteImg,Seller对象
        //所以这一步要把所有信息进行封装了.

        //封装第一句SQL
        Map<String, Object> map = iFavoriteDao.findRouteByRid(rid);
        Route route = new Route();
        Category category = new Category();
        Seller seller = new Seller();
        //第二条SQL
        List<RouteImg> images = iFavoriteDao.findImagesByRid(rid);

        try {
            BeanUtils.populate(route, map);
            BeanUtils.populate(category, map);
            BeanUtils.populate(seller, map);
            route.setCategory(category);
            route.setSeller(seller);
            route.setRouteImgList(images);
            //封装完毕
            return route;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    /**
     * 更新 收藏表 --> 这个是他的业务功能,所有数据在执行该业务功能的时候,都应该经过一层判断
     * @param rid
     * @param uid
     * @return
     */
    //业务 : 用户点击收藏 -->
    public int updateTabFavorite(String rid, String uid) throws SQLException {
        //分析业务逻辑  :   rid    uid
        //已登录,获取 date ,但这里要开启事务.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int ifSuccess = 0;
        DataSource dataSource = JdbcUtils.getDataSource();
        //实例jdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //启动事务管理器（获取datasource操作数据库连接对象并绑定到当前线程中）
        TransactionSynchronizationManager.initSynchronization();
        //从数据源中获取jdbcTemplate操作的当前连接对象
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            //设置连接不自动提交事务
            connection.setAutoCommit(false);
            //调用数据访问接口添加收藏
            iFavoriteDao.updateFavoriteByUidAndRid(jdbcTemplate, rid, String.valueOf(sdf.format(new Date())), uid);
            //更新旅游线路收藏数量+1
            iFavoriteDao.updateCountByRid(jdbcTemplate, rid);
            //手动提交事务
            connection.commit();
            ifSuccess = 1;
        } catch (Exception e) {
            //事务回滚
            connection.rollback();
            System.out.println("回滾了");
            throw e;//抛出异常，说明执行失败
        } finally {
            try {
                //释放当前线程与连接对象的绑定
                TransactionSynchronizationManager.clearSynchronization();
                //重置当前连接为自动提交事务
                connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ifSuccess;
    }

    @Override
    public Favorite findFavorateStatus(String rid, String uid) {
        return iFavoriteDao.findFavorateStatus(rid,uid);
    }

    @Override
    public PageBean<Favorite> findFavoritePageBean(String uid, String curPageStr) throws Exception {
        PageBean<Favorite> pageBean = new PageBean<>();
        int curPage = Integer.parseInt(curPageStr);
        if (curPage >= 1) {
            //封装当前页
            pageBean.setCurPage(Integer.parseInt(curPageStr));
            //封装每页大小
            pageBean.setPageSize(4);
            //封装list
            int preRoute=(pageBean.getCurPage() - 1) * pageBean.getPageSize();//计算当前线路
            pageBean.setDataList(new ArrayList<Favorite>());
            List<Map<String, Object>> favoriteByPage = iFavoriteDao.findFavoriteByPage(uid,preRoute, pageBean.getPageSize());
            ObjectMapper mapper = new ObjectMapper();
            for (Map<String, Object> map : favoriteByPage) {
                Favorite favorite = new Favorite();
                Route route = new Route();
                BeanUtils.populate(favorite, map);
                BeanUtils.populate(route,map);
                favorite.setRoute(route);
                pageBean.getDataList().add(favorite);
            }
            //封装总条数
            int count = iFavoriteDao.findAllFavoriteByUid(Integer.parseInt(uid));
            pageBean.setCount(count);
        }
        return pageBean;
    }

    @Test
    public void text00() {
        IFavoriteDao haha = (IFavoriteDao) FactoryUtils.getInstance("iFavoriteDao");
    }
}
