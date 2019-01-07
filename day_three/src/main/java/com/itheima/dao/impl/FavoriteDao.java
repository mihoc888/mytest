package com.itheima.dao.impl;

import com.itheima.dao.IFavoriteDao;
import com.itheima.model.Favorite;
import com.itheima.model.RouteImg;
import com.itheima.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class FavoriteDao implements IFavoriteDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    /**
     * 根据 rid 获取多表信息 : `tab_route` r,`tab_category` c,`tab_seller` s
     * @param rid
     * @return  Map<String, Object>  : 多表查询结果
     */
    @Override
    public Map<String, Object> findRouteByRid(String rid) {
        try {
            String sql = "SELECT * FROM `tab_route` r,`tab_category` c,`tab_seller` s WHERE r.`cid`=c.`cid` AND r.`sid`=s.`sid` AND rid=?;";
//            Map<String, Object> stringObjectMap
            return jdbcTemplate.queryForMap(sql, rid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**
     * 根据 rid 获取路线的图片
     * @param rid
     * @return   List<RouteImg>   :   当前线路图片集
     */
    @Override
    public List<RouteImg> findImagesByRid(String rid) {
        try {
            String sql = "SELECT * FROM `tab_route_img` WHERE rid=?;";
            //这里查到的数据是一个list,全部信息能用 RouteImg 封装,是一个对象集合.
//        List<RouteImg> query =
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**
     * 根据 rid uid 查询用户线路收藏情况
     *
     * @param uid
     * @return
     */
    @Override
    public Favorite findFavorateStatus(String rid, String uid) {
        try {
            String sql = "SELECT * FROM `tab_favorite` WHERE rid=? and uid=?;";
            //一个表多条数据 +  每条数据是Favorite对象  :   query
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class),rid, uid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }




    /**
     * 根据 rid uid 更新用户收藏表
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public int updateFavoriteByUidAndRid(JdbcTemplate jdbcTemplate,String rid, String date, String uid) {
        //INSERT INTO `tab_favorite` VALUES (4,'2018-12-18',1);
        String sql = "INSERT INTO `tab_favorite` VALUES (?,?,?);";
        return jdbcTemplate.update(sql, rid, date.toString(), uid);
    }


    /**
     * 根据rid 更新路线收藏量
     * @param rid
     * @return
     */
    @Override
    public int updateCountByRid(JdbcTemplate jdbcTemplate,String rid) {
        String sql = "UPDATE  `tab_route` SET COUNT=COUNT+1 WHERE rid=?;";
        return jdbcTemplate.update(sql, rid);
    }

    /**
     * 分页查询用户收藏线路
     * @param uid
     * @param preRoute
     * @param pageSize
     * @return List<Favorite>
     */
    @Override
    public List<Map<String, Object>> findFavoriteByPage(String uid, int preRoute, int pageSize) {
        String sql = "SELECT * FROM `tab_route` r,`tab_favorite` f WHERE r.rid=f.rid  AND uid=? ORDER BY DATE DESC LIMIT ?,?;";
        return jdbcTemplate.queryForList(sql, uid, preRoute, pageSize);
    }


    /**
     * 查询用户总收藏数
     * @param uid
     * @return
     */
    @Override
    public int findAllFavoriteByUid(int uid) {
        String sql = "SELECT COUNT(*) FROM `tab_favorite` WHERE uid=?;";
        return jdbcTemplate.queryForObject(sql,Integer.class,uid);
    }


}
