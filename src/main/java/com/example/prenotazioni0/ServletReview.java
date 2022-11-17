package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.Course;
import dao.Dao;
import dao.Review;

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

    }
}
