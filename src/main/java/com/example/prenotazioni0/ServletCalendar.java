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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@WebServlet(name = "ServletCalendar", value = "/ServletCalendar")
public class ServletCalendar extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }

    public static List<LocalDate> getDatesBetweenUsingJava8(
            LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

    public static List getDatesBetweenUsingJava7(Date startDate, Date endDate) {
        ArrayList<Date> datesInRange = new ArrayList<>();
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
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

                        Date startDate = new SimpleDateFormat("yyyy/MM/dd").parse(startday);
                        Date endDate = new SimpleDateFormat("yyyy/MM/dd").parse(endday);
                        ArrayList<Date> weekDayList = (ArrayList<Date>)getDatesBetweenUsingJava7(startDate, endDate);

                        ArrayList<BookingSlot> bookingSlotList = new ArrayList<>();

                        for (Date day: weekDayList) {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            bookingSlotList.addAll(dao.getDailyTeacherSlots(Integer.parseInt(teacherId), dateFormat.format(day), Integer.parseInt(userId)));
                        }

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
        }
        else{
            response.sendError(500, "parameters not completed");
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
