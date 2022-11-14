package com.example.prenotazioni0;

import dao.Dao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Timer;
import java.util.TimerTask;

@WebServlet(name = "ServletApp", value = "/ServletApp", loadOnStartup = 1, asyncSupported = true)
public class ServletApp extends HttpServlet {

    private Dao dao = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        String url = ctx.getInitParameter("db-url");
        String user = ctx.getInitParameter("db-user");
        String password = ctx.getInitParameter("db-password");
        dao = new Dao(url, user, password);
        ctx.setAttribute("dao", dao);
        //printEach();
    }

    /*public void printEach(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Print in every 20 second");
            }
        }, 0, 20000);
    }*/


}
