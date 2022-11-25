package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.AuthResponse;
import dao.Dao;
import dao.Teacher;
import dao.User;
import jwt.JWTHelper;

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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AuthResponse authResponse;
        if(username != null && password != null){

            User user = dao.login(username, password);
            if(user != null){

                //HttpSession s = request.getSession();
                //s.setAttribute("userUsername", user.getUsername());
                //s.setAttribute("userId", user.getId());
                //s.setAttribute("userRole", user.getRole());

                String jwtToken = JWTHelper.createJwt(Integer.toString(user.getId()), user.getUsername(), user.getRole());
                authResponse = new AuthResponse(user.getId(),user.getUsername(),jwtToken,user.getImage_name(),"");
            }
            else{

                authResponse = new AuthResponse(-1,"","","","Credentials not correct!");
            }
        }
        else{
            authResponse = new AuthResponse(-1,"","","","Generic Error");
        }

        String jsonString = gson.toJson(authResponse);
        out.print(jsonString);
    }
}
