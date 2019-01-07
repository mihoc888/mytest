package com.itheima.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.CategoryDao;
import com.itheima.model.Category;
import com.itheima.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();

    public CategoryService() {
    }

    public String findAllCategory() throws Exception {
        Jedis jedis = JedisUtil.getJedis();
        String category = jedis.get("category");
        if (category == null) {
            List<Category> allCategory = categoryDao.findAllCategory();
            //包装数据,并返回
            ObjectMapper mapper = new ObjectMapper();
            category = mapper.writeValueAsString(allCategory);
            jedis.set("category", category);
        }
        return category;
    }
}
