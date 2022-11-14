package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Dao;
import dao.Teacher;
import dao.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ServletAuth", value = "/servlet-auth")
public class ServletAuth extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }


    //@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/html");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        if(username != null && password != null){
            User user = dao.login(username, password);
            if(user != null){

                HttpSession s = request.getSession();
                s.setAttribute("userUsername", user.getUsername());
                s.setAttribute("userId", user.getId());
                s.setAttribute("userRole", user.getRole());

                //response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print("<span class='badge bg-success'>You are correctly authenticated like " + user.getUsername() + ", role: " + user.getRole() + "</span>");
            }
            else{
                //response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print("<span class='badge bg-danger'>Error while authenticating... User or Password are incorrect</span>");
            }
        }
    }
}
