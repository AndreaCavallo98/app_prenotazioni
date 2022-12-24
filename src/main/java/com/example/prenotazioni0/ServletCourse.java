package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Booking;
import dao.Course;
import dao.Dao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jwt.JWTHelper;

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
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        ArrayList<Course> coursesList = new ArrayList<>();
        String admin = request.getParameter("admin");
        if(admin != null && admin.equals("true")){
            String jwt = request.getHeader("Authorization");

            try{
                Jws<Claims> claims  = JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                // => check if jwt has admin
                if(claims.getBody().get("role").equals("admin")){
                    coursesList = dao.getCourses(true);
                }
                else{
                    response.sendError(401, "Unauthorized");
                }

            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            coursesList = dao.getCourses(false);
        }



        String jsonString = gson.toJson(coursesList);
        out.print(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setContentType("html/text");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String title = request.getParameter("title");
        String color = request.getParameter("color");
        if(title != null && color != null){
            String jwt = request.getHeader("Authorization");

            try{
                Jws<Claims> claims  = JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                // => check if jwt has admin
                if(claims.getBody().get("role").equals("admin")){
                    int newCourseId = (int)dao.addCourse(new Course(title, color));
                    out.print(newCourseId);
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

        String courseid = request.getParameter("courseid");
        if(courseid != null){
            String jwt = request.getHeader("Authorization");

            try{
                Jws<Claims> claims  = JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                // => check if jwt has admin
                if(claims.getBody().get("role").equals("admin")){
                    // Update active value
                    dao.toggleCourse(Integer.parseInt(courseid));
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
