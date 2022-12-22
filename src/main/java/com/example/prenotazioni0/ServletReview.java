package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Course;
import dao.Dao;
import dao.Review;
import jwt.JWTHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletReview", value = "/ServletReview")
public class ServletReview extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        String idTeacher = request.getParameter("idteacher");

        if(idTeacher != null){
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();

            ArrayList<Review> reviewList = dao.getTeacherReviews(Integer.parseInt(idTeacher));
            String jsonString = gson.toJson(reviewList);
            out.print(jsonString);
        }
        else{
            response.sendError(500, "parameters not completed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String bookingId = request.getParameter("bookingid");
        String rate = request.getParameter("rate");
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        if(bookingId != null && rate != null && title != null && text != null){
            String jwt = request.getHeader("Authorization");

            try{
                JWTHelper.decodeJwt(jwt);
                // => from here user is authenticated
                boolean bookingCommited = dao.addReview(Integer.parseInt(bookingId),Integer.parseInt(rate),title,text);

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
