package com.google.web;

import com.google.pojo.User;
import com.google.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    private final UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 获取复选框数据
        String remember = request.getParameter("remember");

        User user = service.login(username, password);
        if (user != null) {
            if ("1".equals(remember)) {
                Cookie c_username = new Cookie("username", username);
                Cookie c_password = new Cookie("password", password);

                c_username.setMaxAge(60 * 60 * 24 * 7);
                c_password.setMaxAge(60 * 60 * 24 * 7);

                response.addCookie(c_username);
                response.addCookie(c_password);
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/selectAllServlet");
        } else {
            request.setAttribute("login_msg", "用户名或密码错误！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
