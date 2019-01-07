package com.itheima.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Favorite;
import com.itheima.model.PageBean;
import com.itheima.util.FactoryUtils;
import org.junit.Test;

public class test {
    IFavoriteService iFavoriteService = (IFavoriteService) FactoryUtils.getInstance("IFavoriteService");
    @Test
    public void text00() {
        try {
            PageBean<Favorite> favoritePageBean = iFavoriteService.findFavoritePageBean(String.valueOf(1), String.valueOf(1));
            ObjectMapper mapper = new ObjectMapper();
            String string = mapper.writeValueAsString(favoritePageBean);
            System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
