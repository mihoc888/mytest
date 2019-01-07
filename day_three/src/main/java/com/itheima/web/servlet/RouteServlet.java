package com.itheima.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.PageBean;
import com.itheima.model.Route;
import com.itheima.service.IFavoriteService;
import com.itheima.service.RouteService;
import com.itheima.util.FactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RouteServlet", urlPatterns = "/route")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteService();
    IFavoriteService iFavoriteService = (IFavoriteService) FactoryUtils.getInstance("IFavoriteService");


    /**
     * 查询收藏排行
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findFavoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String curPage = request.getParameter("curPage");
        String rname = request.getParameter("rname");
        String startPrice = request.getParameter("startPrice");
        String endPrice = request.getParameter("endPrice");

        Map<String, Object> map = new HashMap<>();

        map.put("rname", rname);
        map.put("startPrice", startPrice);
        map.put("endPrice", endPrice);
        PageBean<Route> pageBean = routeService.searchFavoriteCount(map, curPage);
        response.getWriter().write(new ObjectMapper().writeValueAsString(pageBean));
    }

        /**
         * 通过 cid 查询路线列表
         * @param request
         * @param response
         * @throws ServletException
         * @throws IOException
         */
    protected void findRouteListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid1 = request.getParameter("cid");
        //处理请求,得到cid
        int cid = 0;
        if (cid1 != null) {
            cid = Integer.parseInt(request.getParameter("cid"));
        }

        //创建分页对象
        String page = request.getParameter("CurPage");
        String searchWord = request.getParameter("searchWord");
        int CurPage = 1;
        if (page != null) {
            CurPage = Integer.parseInt(page);
        }
        //调用业务层,查询数据
        PageBean pageBean = routeService.getPageBean(CurPage, cid, searchWord);


        ObjectMapper mapper = new ObjectMapper();
        String pageBeanJson = mapper.writeValueAsString(pageBean);

        //返回数据
        response.getWriter().write(pageBeanJson);

    }


    /**
     * 通过路线 rid 查找路线信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findRouteByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = iFavoriteService.findRouteByRid(rid);
        //将获得的数据转为json格式
        String routes= new ObjectMapper().writeValueAsString(route);
        response.getWriter().write(routes);
    }
}

