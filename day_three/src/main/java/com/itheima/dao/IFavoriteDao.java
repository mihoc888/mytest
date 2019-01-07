package com.itheima.dao;

import com.itheima.model.Favorite;
import com.itheima.model.RouteImg;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface IFavoriteDao {
    public Map<String, Object> findRouteByRid(String rid);
    public List<RouteImg> findImagesByRid(String rid);

    Favorite findFavorateStatus(String rid, String uid);

    int updateFavoriteByUidAndRid(JdbcTemplate jdbcTemplate,String rid, String date, String uid);

    int updateCountByRid(JdbcTemplate jdbcTemplate, String rid);

    List<Map<String, Object>> findFavoriteByPage(String uid , int preRoute, int pageSize);

    int findAllFavoriteByUid(int uid);

}
