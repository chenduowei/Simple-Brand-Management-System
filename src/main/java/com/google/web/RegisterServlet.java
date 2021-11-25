package com.google.web;

import com.google.pojo.User;
import com.google.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    private final UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // 获取用户输入的验证码
        String checkCode = request.getParameter("checkCode");
        // 获取程序生成的验证码
        HttpSession session = request.getSession();
        String checkCodeGen = (String) session.getAttribute("checkCodeGen");
        // 比对
        if (!checkCodeGen.equalsIgnoreCase(checkCode)) {
            request.setAttribute("register_msg", "验证码错误");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            // 不允许注册
            return;
        }

        boolean flag = service.register(user);
        if (flag) {
            request.setAttribute("register_msg", "注册成功，请登录！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("register_msg", "用户名已存在！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
