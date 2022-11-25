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

        String teacherId = request.getParameter("teacherid");
        String dateDay = request.getParameter("dateday");

        if(teacherId != null && dateDay != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                ArrayList<BookingSlot> bookingSlotList = dao.getDailyTeacherSlots(Integer.parseInt(teacherId), dateDay);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
