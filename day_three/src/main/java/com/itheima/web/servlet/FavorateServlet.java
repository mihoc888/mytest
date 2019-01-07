package com.itheima.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Favorite;
import com.itheima.model.PageBean;
import com.itheima.model.Route;
import com.itheima.model.User;
import com.itheima.service.IFavoriteService;
import org.apache.commons.collections.FactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "FavorateServlet", urlPatterns = "/favorate")
public class FavorateServlet extends BaseServlet {
    private IFavoriteService iFavoriteService = (IFavoriteService) com.itheima.util.FactoryUtils.getInstance("IFavoriteService");

    protected void updateTabFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String rid = request.getParameter("rid");
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        int ifSuccess = 0;
        if (loginUser != null) {
            //说明用户登陆了
            int uid = loginUser.getUid();
            if (iFavoriteService.updateTabFavorite(rid, String.valueOf(uid)) > 0) {
                Route routeByRid = iFavoriteService.findRouteByRid(rid);
                ifSuccess = routeByRid.getCount();
            }
        }
        response.getWriter().print(ifSuccess);
    }

    protected void findFavorateStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String rid = request.getParameter("rid");
        if (loginUser != null) {
            //登陆了
            int uid = loginUser.getUid();
            Favorite favorate = iFavoriteService.findFavorateStatus(rid, String.valueOf(uid));
            if (favorate != null) {
                response.getWriter().write("true");
            } else {
                response.getWriter().write("noColoct");
                return;
            }
        } else {
            response.getWriter().write("false");
        }
    }

    protected void findFavoritePageBeanByUid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //处理请求数据
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        String curPageStr = request.getParameter("curPage");
        PageBean<Favorite> pageBean = null;
        if (curPageStr == null || curPageStr == "") {
            curPageStr = "1";
        }
        pageBean = iFavoriteService.findFavoritePageBean(String.valueOf(loginUser.getUid()), curPageStr);
        String jsonData = new ObjectMapper().writeValueAsString(pageBean);
        response.getWriter().write(jsonData);
    }


}
