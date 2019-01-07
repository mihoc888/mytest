package com.itheima.web.servlet;

import com.itheima.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CategoryServlet", urlPatterns = "/category")
public class CategoryServlet extends BaseServlet {

    private CategoryService categoryService = new CategoryService();

    protected void getCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
            //处理请求
            //调用业务层获取数据
        String allCategory = categoryService.findAllCategory();
            //响应
        response.getWriter().println(allCategory);
    }
}
