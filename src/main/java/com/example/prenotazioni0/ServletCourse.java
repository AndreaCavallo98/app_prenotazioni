package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Course;
import dao.Dao;

import javax.security.auth.login.CredentialException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletCourse", value = "/ServletCourse")
public class ServletCourse extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        ArrayList<Course> coursesList = dao.getCourses();
        String jsonString = gson.toJson(coursesList);
        out.print(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
