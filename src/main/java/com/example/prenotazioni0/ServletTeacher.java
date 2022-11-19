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
        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String type_request = request.getParameter("type");
        System.out.println(type_request);
        if(!type_request.equals("maxhourlyrate")){
            String filterCourseIdString = request.getParameter("courseid");
            String filterAvaliableDate = request.getParameter("avaliabledate");
            String filterMaxHourlyRateString = request.getParameter("maxhourlyrate");

            if(type_request != null && filterCourseIdString != null && filterAvaliableDate != null && filterMaxHourlyRateString != null) {

                int filterCourseId = Integer.parseInt(filterCourseIdString);
                int filterMaxHourlyRate = Integer.parseInt(filterMaxHourlyRateString);
                ArrayList<Teacher> teacherList = null;

                switch (type_request) {
                    case "all":
                        response.setContentType("application/json");
                        teacherList = dao.getTeachers(false, filterCourseId, filterAvaliableDate, filterMaxHourlyRate);
                        String jsonString = gson.toJson(teacherList);
                        out.print(jsonString);
                        break;

                    case "topfive":
                        response.setContentType("application/json");
                        teacherList = dao.getTeachers(true, filterCourseId, "", 0);
                        String jsonString1 = gson.toJson(teacherList);
                        out.print(jsonString1);
                        break;
                }



            } else {
                response.sendError(500, "parameters not completed");
            }
        }
        else{
            System.out.println("AAAAAAAAA");
            response.setContentType("html/text");
            int maxHourlyRate = dao.getMaxHourlyRate();
            out.print(maxHourlyRate);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
