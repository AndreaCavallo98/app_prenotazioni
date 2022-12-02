package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Booking;
import dao.BookingSlot;
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
import java.util.List;

@WebServlet(name = "ServletBooking", value = "/ServletBooking")
public class ServletBooking extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String userId = request.getParameter("userid");

        if(userId != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                ArrayList<Booking> myBookingList = dao.getUsersBooking(Integer.parseInt(userId));
                String jsonString = gson.toJson(myBookingList);
                out.print(jsonString);
            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String courseId = request.getParameter("courseid");
        String teacherId = request.getParameter("teacherid");
        String userId = request.getParameter("userid");
        String date = request.getParameter("date");
        String startTime = request.getParameter("starttime");
        String endTime = request.getParameter("endtime");
        if(courseId != null && teacherId != null && userId != null && date != null && startTime != null && endTime != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                boolean bookingCommited = dao.addBooking(
                    Integer.parseInt(courseId),
                    Integer.parseInt(teacherId),
                    Integer.parseInt(userId),
                    date,
                    Integer.parseInt(startTime),
                    Integer.parseInt(endTime)
                );

                //out.print();
            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String bookingId = request.getParameter("bookingid");
        if(bookingId != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                dao.removeBooking(Integer.parseInt(bookingId));

                //out.print();
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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String bookingId = request.getParameter("bookingid");
        if(bookingId != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                dao.markBookingAsDone(Integer.parseInt(bookingId));

                //out.print();
            } catch (Exception e){
                response.sendError(401, "Unauthorized");
            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }
}
