package com.itheima.dao;

import com.itheima.model.Route;
import com.itheima.util.JdbcUtils;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public RouteDao() {
    }


    /**
     * 根据筛选条件查找总条数
     * @param conditions
     * @return int
     */
    public int searchFavoriteCount(Map<String, Object> conditions) {
        // map 中有 : rname  startPrice  endPrice
        String sql = " SELECT count(*) FROM `tab_route` WHERE rflag=1 ";
        List<Object> list = new ArrayList();

        String rname = (String) conditions.get("rname");
        if (rname != null && rname != "") {
            sql += " AND rname LIKE ? ";
            list.add("%" + rname + "%");
        }

        String startPage = (String) conditions.get("startPrice");
        if (startPage != null && startPage != "") {
            sql += " AND price >=? ";
            int startPrice = Integer.valueOf(startPage);
            list.add(startPrice);
        }


        String endPrice = (String) conditions.get("endPrice");
        if (endPrice != null && endPrice != "") {
            sql += " AND price <=? ";
            int endPriceInt = Integer.valueOf(endPrice);
            list.add(endPriceInt);
        }
        Object[] objects = list.toArray();
        return jdbcTemplate.queryForObject(sql, Integer.class, objects);
    }

    /**
     * 根据搜索条件查询路线收藏情况
     *
     * @param conditions
     * @param prePage
     * @param pageSize
     * @return List<Route>
     */
    public List<Route> searchFavoriteRank(Map<String, Object> conditions, int prePage, int pageSize) {
        // map 中有 : rname  startPrice  endPrice
        // sql : SELECT * FROM `tab_route` WHERE rflag=1 AND rname LIKE '%双飞%' AND price >=1000 AND price <=100000 ORDER BY COUNT DESC LIMIT 0,8;
        String sql = " SELECT * FROM `tab_route` WHERE rflag=1 ";
        List<Object> list = new ArrayList();

        String rname = (String) conditions.get("rname");
        if (rname != null && rname != "") {
            sql += " AND rname LIKE ? ";
            list.add("%" + rname + "%");
        }

        String startPage = (String) conditions.get("startPrice");
        if (startPage != null && startPage != "") {
            sql += " AND price >=? ";
            int startPrice = Integer.valueOf(startPage);
            list.add(startPrice);
        }


        String endPrice = (String) conditions.get("endPrice");
        if (endPrice != null && endPrice != "") {
            sql += " AND price <=? ";
            int endPriceInt = Integer.valueOf(endPrice);
            list.add(endPriceInt);
        }

        sql += " ORDER BY COUNT DESC LIMIT ?,?;";
        list.add(prePage);
        list.add(pageSize);
        Object[] objects = list.toArray();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), objects);
    }

    /**
     * 根据当前页,获得路线信息:路线信息,商家信息,分类信息
     *
     * @param cid        路线 cid
     * @param page       页面当前页
     * @param searchWord 搜索字段
     * @return List<Route>
     */
    public List<Route> getRoutesByPage(int cid, int page, String searchWord) {

        try {
            String sql;
            if (cid == 0) {
                sql = "SELECT * FROM `tab_route` WHERE rflag=1 AND rname LIKE ? LIMIT ?,5;";
                return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), "%" + searchWord + "%", page);
            }
            sql = "SELECT * FROM `tab_route` WHERE cid=? AND rname LIKE ?  AND rflag=1 LIMIT ?,5;";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), cid, "%" + searchWord + "%", page);

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 根据路线分类获取路线总数
     *
     * @param cid        路线 cid
     * @param searchWord 搜索字段
     * @return int
     */
    public int getCountByCid(int cid, String searchWord) {
        String sql = "SELECT COUNT(*) FROM `tab_route` WHERE cid=? AND rname LIKE ? AND rflag=1;";
        return jdbcTemplate.queryForInt(sql, cid, "%" + searchWord + "%");
    }

    public void miko() {
    }


}
