package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Course;
import dao.Dao;
import dao.Teacher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletTeacher", value = "/ServletTeacher")
public class ServletTeacher extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String type_request = request.getParameter("type");
        ArrayList<Teacher> teacherList = null;

        switch (type_request){
            case "all":
                teacherList = dao.getTeachers(false);
                break;

            case "topfive":
                teacherList = dao.getTeachers(true);

                break;

            case "bycourse":

                break;
        }

        String jsonString = gson.toJson(teacherList);
        out.print(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
