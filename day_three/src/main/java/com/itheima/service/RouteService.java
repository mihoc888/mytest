package com.itheima.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.RouteDao;
import com.itheima.model.PageBean;
import com.itheima.model.Route;

import java.util.List;
import java.util.Map;

public class RouteService {
    private RouteDao routeDao = new RouteDao();
    public RouteService() {
    }

    public PageBean getPageBean(int curPage, int cid,String searchWord) throws JsonProcessingException {
        //SELECT * FROM `tab_route` WHERE cid=5 AND rflag=1 LIMIT 0,5;
        // 0 = 当前页-1
        List<Route> routes = routeDao.getRoutesByPage(cid, curPage - 1,searchWord);
        int count = routeDao.getCountByCid(cid,searchWord);
        PageBean pageBean = new PageBean();
        //封装数据-->PageBean
        pageBean.setCurPage(curPage);
        pageBean.setDataList(routes);
        pageBean.setCount(count);
        pageBean.setPageSize(5);
        return pageBean;
    }


    public PageBean<Route> searchFavoriteCount(Map<String, Object> map, String curPageStr) {
        PageBean<Route> pageBean = new PageBean<>();
        //封装当前页
        int curPage = 1;
        if (curPageStr!=null && !curPageStr.equals("")) {
            curPage = Integer.valueOf(curPageStr);
        }
        pageBean.setCurPage(curPage);
        //封装每页大小
        pageBean.setPageSize(8);
        //封装总条数
        int count = routeDao.searchFavoriteCount(map);
        pageBean.setCount(count);
        //封装数据列表
        List<Route> routes = routeDao.searchFavoriteRank(map, pageBean.getPrePage(), pageBean.getPageSize());
        pageBean.setDataList(routes);
        return pageBean;
    }
}
