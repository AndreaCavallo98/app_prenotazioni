package com.example.prenotazioni0;

import com.google.gson.Gson;
import dao.AuthResponse;
import dao.Dao;
import dao.Teacher;
import dao.User;
import jwt.JWTHelper;
import services.EncryptPwdService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ServletAuth", value = "/servlet-auth")
public class ServletAuth extends HttpServlet {

    private Dao dao = null;

    public void init(ServletConfig conf) {
        dao = (Dao) conf.getServletContext().getAttribute("dao");
    }


    //@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        AuthResponse authResponse = null;
        String action = request.getParameter("action");
        if(action != null){
            if(action.equals("login")){
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                if(username != null && password != null){

                    User user = dao.login(username, EncryptPwdService.encryptSHA2(password));
                    if(user != null){

                        //HttpSession s = request.getSession();
                        //s.setAttribute("userUsername", user.getUsername());
                        //s.setAttribute("userId", user.getId());
                        //s.setAttribute("userRole", user.getRole());

                        String jwtToken = JWTHelper.createJwt(Integer.toString(user.getId()), user.getUsername(), user.getRole());
                        authResponse = new AuthResponse(user.getId(),user.getName() + " " + user.getSurname(),  user.getUsername(), user.getEmail(), jwtToken,user.getImage_name(),"");
                    }
                    else{

                        authResponse = new AuthResponse(-1,"","","","","","Credentials not correct!");
                    }
                }
                else{
                    authResponse = new AuthResponse(-1,"","","","","","Generic Error");
                }
            }
            else if(action.equals("register")){

                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                if(name != null && surname != null && username != null && email != null && password != null){

                    String checkExistingUser = dao.checkExistingUsernameOrEmail(username, email);
                    if(checkExistingUser.isEmpty()){

                        String encryptedPwd = EncryptPwdService.encryptSHA2(password);
                        int newUserId = dao.register(name, surname, username, email, encryptedPwd);
                        if(newUserId != -1){
                            String jwtToken = JWTHelper.createJwt(Integer.toString(newUserId), username, "user");
                            authResponse = new AuthResponse(newUserId,name + " " + surname,  username, email, jwtToken,"","");
                        }
                        else{
                            authResponse = new AuthResponse(-1,"","","","","","Error while inserting new user");
                        }

                    }
                    else if(checkExistingUser == "username"){
                        authResponse = new AuthResponse(-1,"","","","","","Username already exist!");
                    }
                    else if(checkExistingUser == "email"){
                        authResponse = new AuthResponse(-1,"","","","","","Email already exist!");
                    }
                }
                else{
                    response.sendError(500, "parameters not completed");
                    authResponse = new AuthResponse(-1,"","","","","","parameters not completed");
                }
            }
        }
        else{
            response.sendError(500, "parameters not completed");
            authResponse = new AuthResponse(-1,"","","","","","parameters not completed");
        }

        String jsonString = gson.toJson(authResponse);
        out.print(jsonString);
    }
}
