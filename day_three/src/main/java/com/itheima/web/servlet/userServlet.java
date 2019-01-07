package com.itheima.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.exception.UserMsgError;
import com.itheima.model.User;
import com.itheima.service.UserService;
import com.itheima.util.Md5Util;
import org.apache.commons.beanutils.BeanUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "userServlet", urlPatterns = "/user")
public class userServlet extends BaseServlet {
    private UserService userService = new UserService();

    /**
     * 用户退出
     * @param request
     * @param response
     * @throws IOException
     */
    private void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("index.html");
    }

    /**
     * 网页头部 登陆信息
     * @param request
     * @param response
     */
    private void headerLogMsg(HttpServletRequest request, HttpServletResponse response) {
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String loginMsg = mapper.writeValueAsString(loginUser);
            response.getWriter().println(loginMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 用户登陆
     * @param request
     * @param response
     */
    private void login(HttpServletRequest request, HttpServletResponse response)  {
        try {

            //        输入验证码: 在请求域中获取系统验证码验证,(先验证)
            String sysCheck = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            String formcheck = request.getParameter("check");
            if (!sysCheck.equalsIgnoreCase(formcheck)) {
                request.setAttribute("error", "验证码错误");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
//        输入账号: 根据账号查询数据可中的内容,在将其写进会话域();提示,账号不存在
            String username = request.getParameter("username");
            User loginuser = userService.getUserByUserName(username);
            if (loginuser == null) {
                request.getSession().setAttribute("error", "用户名不存在");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            request.getSession().setAttribute("loginUser", loginuser);
//        输入密码: 在会话域中获取用户密码,对比,提示密码错误
            String password = request.getParameter("password");
            String md5 = Md5Util.getMD5(password);
            User loginUser = (User) request.getSession().getAttribute("loginUser");
            String realPassword = loginUser.getPassword();
            if (!md5.equalsIgnoreCase(realPassword)) {
                request.getSession().setAttribute("error", "密码错误");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
//          登陆 --->成功:跳转至首页
            response.sendRedirect("index.html");
//          失败:提示错误信息,转发给login.jsp,用EL获取错误信息.

        } catch (UserMsgError error) {
            error.printStackTrace();
        } catch (Exception e) {
            try {
                response.getWriter().println("服务器忙......");
            } catch (IOException e1) {
            }
        }

    }


    /**
     * 用户注册
     * @param request
     * @param response
     */
    protected void register(HttpServletRequest request, HttpServletResponse response)  {
        try {
            //验证验证码
            String formCheck = request.getParameter("check");
            //session里面放的都是object对象
            String sysCheck = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            if (!formCheck.equalsIgnoreCase(sysCheck)) {
                //如果验证码不正确 : 使用自定义异常在注册按钮旁边显示错误信息
                request.setAttribute("error", "验证码有误");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            //验证用户名
            String username = request.getParameter("username");
            User register = userService.getUserByUserName(username);
            if (register != null) {
                request.setAttribute("error", "用户名已存在");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            //对密码进行加密
            String password = request.getParameter("password");
            if (password == null || password == "") {
                request.setAttribute("error", "密码不能为空");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            String md5 = Md5Util.getMD5(password);
            //将表单数据封装成User对象
            User user = new User();
            BeanUtils.populate(user, request.getParameterMap());
            user.setPassword(md5);
            //调用业务层,注册用户

            int result = userService.register(user);
            //注册结果
            if (result == 0) {
                throw new UserMsgError("服务器忙");
            }
            response.sendRedirect("register_ok.html");
        } catch (UserMsgError e) {
            request.getSession().setAttribute("error", e.getMessage());
        }  catch (Exception e) {
            try {
                response.getWriter().println("服务器忙");
            } catch (IOException e1) {
            }
        }


    }
}

