package com.itheima.service;

import com.itheima.model.Favorite;
import com.itheima.model.PageBean;
import com.itheima.model.Route;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface IFavoriteService {
    public Route findRouteByRid( String rid);

    public int updateTabFavorite(String rid, String uid) throws SQLException;

    public Favorite findFavorateStatus(String rid, String uid);

    //根据,uid,curpage,获得用户收藏 Pagebean
    public PageBean<Favorite> findFavoritePageBean(String uid, String curPage) throws InvocationTargetException, IllegalAccessException, Exception;
}
