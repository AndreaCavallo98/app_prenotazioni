package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.BookingSlot;
import dao.Course;
import dao.Dao;
import jwt.JWTHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletCalendar", value = "/ServletCalendar")
public class ServletCalendar extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String type = request.getParameter("type");
        if(type != null){
            if(type.equals("daily")){
                String teacherId = request.getParameter("teacherid");
                String dateDay = request.getParameter("dateday");
                String userId = request.getParameter("userid");

                if(teacherId != null && dateDay != null && userId != null){
                    String jwt = request.getHeader("Authorization");

                    try{
                        JWTHelper.decodeJwt(jwt);
                        // => from here user is authenticated
                        ArrayList<BookingSlot> bookingSlotList = dao.getDailyTeacherSlots(Integer.parseInt(teacherId), dateDay, Integer.parseInt(userId));
                        String jsonString = gson.toJson(bookingSlotList);
                        out.print(jsonString);
                    } catch (Exception e){
                        response.sendError(401, "Unauthorized");
                    }
                }
                else{
                    response.sendError(500, "parameters not completed");
                }
            }
            else if (type.equals("weekly")){

                String startday = request.getParameter("startday");
                String endday = request.getParameter("endday");
                String teacherId = request.getParameter("teacherid");
                String userId = request.getParameter("userid");
                if(startday != null && endday != null && teacherId != null && userId != null){
                    String jwt = request.getHeader("Authorization");

                    try{
                        JWTHelper.decodeJwt(jwt);
                        // => from here user is authenticated






                        //ArrayList<BookingSlot> bookingSlotList = dao.getDailyTeacherSlots(Integer.parseInt(teacherId), dateDay, Integer.parseInt(userId));
                        //String jsonString = gson.toJson(bookingSlotList);
                        //out.print(jsonString);
                    } catch (Exception e){
                        response.sendError(401, "Unauthorized");
                    }
                }
                else{
                    response.sendError(500, "parameters not completed");
                }

            }
        }
        else{
            response.sendError(500, "parameters not completed");
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
