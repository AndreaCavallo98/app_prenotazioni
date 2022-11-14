package com.example.prenotazioni0;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.DailyAvaliability;
import dao.Dao;
import dao.Teacher;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Get day of week
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();

        // Get list of 7 days
        List<String> weekday_list = new ArrayList<>();
        weekday_list.add(formatter.format(today));
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
        weekday_list.add(formatter.format(cal.getTime()));

        // print head of table
        PrintWriter out = response.getWriter();
        out.print(
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <title>Hello world da index.html</title>\n" +
        "    <link href=\"bootstrap-4.5.2-dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
        "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js\"></script>\n" +
        "    <script src=\"bootstrap-4.5.2-dist/js/jquery-slim.min.js\"></script>\n" +
        "    <script src=\"bootstrap-4.5.2-dist/js/bootstrap.min.js\"></script>\n" +
        "</head>\n" +
        "<body>\n" +
        "<div class=\"col-12\">\n" +
        "<table class='table'>" +
        "<thead class='thead-dark'>" +
        "<tr>" +
        "<th scope='col'>HOUR</th>");
        for(String s: weekday_list){
            out.print("<th scope='col'>" + s + "</th>");
        }
        out.print(
        "</tr>" +
        "</thead>" +
        "<tbody>");


        out.print("<tr>" +
        "<th scope='row'>15/16</th>");
        for(String s: weekday_list){
            out.print("<td>");
            List<DailyAvaliability> dailyAvaliabilityList = dao.getSlotAvaliability(s, 15);
            for(DailyAvaliability d : dailyAvaliabilityList){
                out.print("<span class='badge badge-primary' style='background-color:" + d.getColor() + "' >" + d.getCourse() + " (" + d.getTeacher() + ")</span>");
            }
            out.print("</td>");
        }
        out.print("</tr>");

        out.print("<tr>" +
        "<th scope='row'>16/17</th>");
        for(String s: weekday_list){
            out.print("<td>");
            List<DailyAvaliability> dailyAvaliabilityList = dao.getSlotAvaliability(s, 16);
            for(DailyAvaliability d : dailyAvaliabilityList){
                out.print("<span class='badge badge-primary' style='background-color:" + d.getColor() + "' >" + d.getCourse() + " (" + d.getTeacher() + ")</span>");
            }
            out.print("</td>");
        }
        out.print("</tr>");

        out.print("<tr>" +
        "<th scope='row'>17/18</th>");
        for(String s: weekday_list){
            out.print("<td>");
            List<DailyAvaliability> dailyAvaliabilityList = dao.getSlotAvaliability(s, 17);
            for(DailyAvaliability d : dailyAvaliabilityList){
                out.print("<span class='badge badge-primary' style='background-color:" + d.getColor() + "' >" + d.getCourse() + " (" + d.getTeacher() + ")</span>");
            }
            out.print("</td>");
        }
        out.print("</tr>");

        out.print("<tr>" +
        "<th scope='row'>18/19</th>");
        for(String s: weekday_list){
            out.print("<td>");
            List<DailyAvaliability> dailyAvaliabilityList = dao.getSlotAvaliability(s, 18);
            for(DailyAvaliability d : dailyAvaliabilityList){
                out.print("<span class='badge badge-primary' style='background-color:" + d.getColor() + "' >" + d.getCourse() + " (" + d.getTeacher() + ")</span>");
            }
            out.print("</td>");
        }
        out.print("</tr>");


        out.print(
        "</tbody>" +
        "</table>" +
        "</div>\n" +
        "</body>\n" +
        "</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession s = request.getSession();

        if(s.getAttribute("userRole") != null){
            if(s.getAttribute("userRole").equals("amministratore")){
                String teacherName = request.getParameter("name");
                String teacherSurname = request.getParameter("surname");
                if(teacherName != null && teacherSurname != null){
                    Teacher teacher = new Teacher(teacherName, teacherSurname);
                    dao.addTeacher(teacher);
                }

                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print(
                        "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Teacher added correctly</h1>" +
                        "</body>\n" +
                        "</html>\n");
            }
            else{
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print(
                        "<!DOCTYPE html>\n" +
                                "<html lang=\"en\">\n" +
                                "<head>\n" +
                                "    <meta charset=\"UTF-8\">\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "<h1>You doesn't have acces to add new teacher! do login first</h1>" +
                                "</body>\n" +
                                "</html>\n");
            }

        }
        else{

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<h1>You doesn't have acces to add new teacher! do login first</h1>" +
                    "</body>\n" +
                    "</html>\n");
        }


    }

    public void destroy() {
    }
}