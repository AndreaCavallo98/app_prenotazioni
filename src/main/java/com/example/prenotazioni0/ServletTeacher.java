package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Course;
import dao.Dao;
import dao.Teacher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jwt.JWTHelper;

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
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String type_request = request.getParameter("type");
        if(!type_request.equals("maxhourlyrate")){

            if(!type_request.equals("teacherdetail")){
                String filterCourseIdString = request.getParameter("courseid");
                String filterAvaliableDate = request.getParameter("avaliabledate");
                String filterMaxHourlyRateString = request.getParameter("maxhourlyrate");

                if(type_request != null && filterCourseIdString != null && filterAvaliableDate != null && filterMaxHourlyRateString != null) {
                    response.setContentType("application/json");
                    int filterCourseId = Integer.parseInt(filterCourseIdString);
                    int filterMaxHourlyRate = Integer.parseInt(filterMaxHourlyRateString);
                    ArrayList<Teacher> teacherList = null;

                    switch (type_request) {
                        case "all":

                            String admin = request.getParameter("admin");

                            if(admin != null && admin.equals("true")){
                                teacherList = dao.getTeachers(false,filterCourseId,filterAvaliableDate, filterMaxHourlyRate, true);
                            }
                            else{
                                teacherList = dao.getTeachers(false, filterCourseId, filterAvaliableDate, filterMaxHourlyRate, false);
                            }

                            String jsonString = gson.toJson(teacherList);
                            out.print(jsonString);
                            break;

                        case "topfive":

                            teacherList = dao.getTeachers(true, filterCourseId, "", 0, false);
                            String jsonString1 = gson.toJson(teacherList);
                            out.print(jsonString1);
                            break;
                    }

                } else {
                    response.sendError(500, "parameters not completed");
                }
            }else{
                String teacherId = request.getParameter("id");
                if(teacherId != null){
                    Teacher teacher = null;
                    response.setContentType("application/json");
                    teacher = dao.getTeacher(Integer.parseInt(teacherId));
                    String jsonString = gson.toJson(teacher);
                    out.print(jsonString);
                }
                else{
                    response.sendError(500, "parameters not completed");
                }
            }
        }
        else{
            response.setContentType("html/text");
            int maxHourlyRate = dao.getMaxHourlyRate();
            out.print(maxHourlyRate);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setContentType("html/text");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String description = request.getParameter("description");
        String hourly_rate = request.getParameter("hourlyrate");
        String mainteachedcourse = request.getParameter("courseid");
        if(name != null && surname != null && description != null && hourly_rate != null && mainteachedcourse != null){
            String jwt = request.getHeader("Authorization");

            try{
                Jws<Claims> claims  = JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                // => check if jwt has admin
                if(claims.getBody().get("role").equals("admin")){
                    int newTeacherId = (int)dao.addTeacher(name, surname, description, Double.parseDouble(hourly_rate), Integer.parseInt(mainteachedcourse));
                    out.print(newTeacherId);
                }
                else{
                    response.sendError(401, "Unauthorized");
                }
            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String teacherid = request.getParameter("teacherid");
        if(teacherid != null){
            String jwt = request.getHeader("Authorization");

            try{
                Jws<Claims> claims  = JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                // => check if jwt has admin
                if(claims.getBody().get("role").equals("admin")){
                    // Update active value
                    dao.toggleTeacher(Integer.parseInt(teacherid));
                }
                else{
                    response.sendError(401, "Unauthorized");
                }
            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }
}
