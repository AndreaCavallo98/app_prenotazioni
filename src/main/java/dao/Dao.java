package dao;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.awt.print.Book;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Dao {

    private final String url;
    private final String user;
    private final String password;
    private static Connection conn;

    public Dao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        registerDriver();
    }

    public void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver registered correctly");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
    public void createConnection(){
        try {
            conn = DriverManager.getConnection(url, user, password);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void closeConnection(){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
        }
    }

    // => METHODS TO MANAGE USER
    public User login(String username, String password){
        User user = null;
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "' AND active = 1");
            if (rs.next()) {

                user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"), rs.getString("email"), rs.getString("role"), rs.getString("image_name"), true);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return user;
    }
    public int register(String name, String surname, String username, String email, String password){
        createConnection();
        int retNewUserId = -1;
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO user (name, surname, username, email, password, role) VALUES ('" + name +  "', '" + surname + "', '" + username + "', '" + email + "', '" + password + "', 'user')",
                    Statement.RETURN_GENERATED_KEYS);

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    retNewUserId = (int)generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            return retNewUserId;
        }
    }
    public String checkExistingUsernameOrEmail(String username, String email){
        String ret = "";
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user WHERE username = '" + username + "' OR email = '" + email + "'");
            if (rs.isBeforeFirst()) {
                while (rs.next()){
                    if(rs.getString("username").equals(username)){
                        ret = "username";
                    }
                    else if(rs.getString("email").equals(email)){
                        ret = "email";
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  ret;
    }

    public ArrayList<User> getUsersAdmin() {
        ArrayList<User> users_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"), rs.getString("email"), rs.getString("role"),rs.getString("image_name"), rs.getBoolean("active"));
                users_list.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return users_list;
    }

    // => METHODS TO MANAGE COURSES
    public long addCourse(Course course){
        long retCourseNewId = -1;
        createConnection();
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO course (title, color) VALUES ('" + course.getTitle() +  "', '" + course.getColor() + "')",
                    Statement.RETURN_GENERATED_KEYS);

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    retCourseNewId = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            return retCourseNewId;
        }
    }
    public void toggleCourse(int idCourse){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE course SET active = !active WHERE id = " + idCourse);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
    }

    /*public ArrayList<Teacher> getCourseTeacher(int idCourse) {
        ArrayList<Teacher> teachers_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM teacher INNER JOIN rel_course_teacher ON teacher.id = rel_course_teacher.id_teacher WHERE rel_course_teacher.id_course = " + idCourse);
            while (rs.next()) {
                Teacher teacher = new Teacher(rs.getInt("teacher.id"),rs.getString("name"), rs.getString("surname"), rs.getBoolean("active"));
                teachers_list.add(teacher);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            closeConnection();
        }
        return teachers_list;
    }*/

    public ArrayList<Course> getCourses(boolean admin) {
        ArrayList<Course> courses_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = admin ? st.executeQuery("SELECT * FROM course") : st.executeQuery("SELECT * FROM course WHERE active = 1");
            while (rs.next()) {
                Course course = new Course(rs.getInt("id"),rs.getString("title"),rs.getString("color"),rs.getString("image_name"), rs.getBoolean("active"));
                courses_list.add(course);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return courses_list;
    }

    // => METHODS TO MANAGE TEACHERS
    public long addTeacher(String name, String surname, String description, Double hourly_rate, int mainTeacheedCourse){
        long retTeacherNewId = -1;
        createConnection();
        try {

            PreparedStatement st = conn.prepareStatement("INSERT INTO teacher (name, surname, description, hourly_rate) VALUES ('" + name +  "', '" + surname + "', '" + description + "', " + hourly_rate +")",
                    Statement.RETURN_GENERATED_KEYS);

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    retTeacherNewId = generatedKeys.getLong(1);

                    // Create association with main course selected
                    associateCourseToteacher(mainTeacheedCourse, (int)retTeacherNewId);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            return retTeacherNewId;
        }
    }
    public void toggleTeacher(int idTeacher){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE teacher SET active = !active WHERE id = " + idTeacher);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getNumTeacherLecturesGiven(int idTeacher){
        int num_lectures_given = -1;
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as num_lectures_given FROM booking WHERE confirmed = true AND id_teacher = " + idTeacher);
            rs.next();
            num_lectures_given = rs.getInt("num_lectures_given");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return num_lectures_given;
    }
    private int getNumTeacherReview(int idTeacher){
        int num_teacher_review = -1;
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as num_teacher_review FROM booking_review LEFT JOIN booking ON booking.id = booking_review.id_booking WHERE booking.id_teacher = " + idTeacher);
            rs.next();
            num_teacher_review = rs.getInt("num_teacher_review");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return num_teacher_review;
    }

    private double getTeacherReviewsAverage(int idTeacher, int numTeacherReview){
        double reviews_average = -1;
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(rate),1) as sum_rate_teacher_review FROM booking_review LEFT JOIN booking ON booking.id = booking_review.id_booking WHERE booking.id_teacher = " + idTeacher);
            rs.next();
            double sum_rate = rs.getDouble("sum_rate_teacher_review");
            reviews_average = numTeacherReview == 0? 0 : sum_rate / numTeacherReview;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reviews_average;
    }
    public ArrayList<Teacher> getTeachers(boolean topFive, int filterCourseId, String filterAvaliableDate, int filterMaxHourlyRate, boolean admin) {
        ArrayList<Teacher> teachers_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM teacher";
            if(filterCourseId != -1){
                query += " LEFT JOIN rel_course_teacher ON rel_course_teacher.id_teacher = teacher.id";
            }
            if(!admin){
                query += " WHERE teacher.active = 1";
            }else{
                query += " WHERE true = true";
            }

            if(filterCourseId != -1){
                query += " AND rel_course_teacher.id_course = " + filterCourseId;
            }
            if(filterMaxHourlyRate != 0){
                query += " AND teacher.hourly_rate <= " + filterMaxHourlyRate;
            }
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                int num_lectures_given = getNumTeacherLecturesGiven(rs.getInt("id"));
                int num_teacher_review = getNumTeacherReview(rs.getInt("id"));
                double reviews_average = getTeacherReviewsAverage(rs.getInt("id"), num_teacher_review);
                ArrayList<Course> teacherCourseList = getTeacherCourses(rs.getInt("id"));

                Teacher teacher = new Teacher(rs.getInt("id"),rs.getString("name"),rs.getString("surname"),rs.getString("description"), teacherCourseList, rs.getDouble("hourly_rate"),num_lectures_given,num_teacher_review,reviews_average, rs.getString("image_name"), rs.getBoolean("active"));
                if(filterAvaliableDate != ""){
                    if(checkTeacherAvaliabilityDate(teacher.getId(), filterAvaliableDate)){
                        teachers_list.add(teacher);
                    }
                }
                else{
                    teachers_list.add(teacher);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/

        Collections.sort(teachers_list, new CustomTeacherComparator());

        ArrayList<Teacher> topFiveList = null;
        if(topFive){
            topFiveList = new ArrayList<Teacher>(teachers_list.subList(teachers_list.size() -5, teachers_list.size()));
            Collections.sort(topFiveList, Collections.reverseOrder(new CustomTeacherComparator()));
        }

        return topFive && teachers_list.size() >= 5 ?  topFiveList : teachers_list;
    }

    public Teacher getTeacher(int teacherId){
        Teacher teacher = null;
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from teacher WHERE id = " + teacherId);
            if(rs.isBeforeFirst()){
                rs.next();

                int num_lectures_given = getNumTeacherLecturesGiven(teacherId);
                int num_teacher_review = getNumTeacherReview(teacherId);
                double reviews_average = getTeacherReviewsAverage(teacherId, num_teacher_review);
                ArrayList<Course> teacherCourseList = getTeacherCourses(teacherId);
                teacher = new Teacher(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("description"),
                    teacherCourseList,
                    rs.getDouble("hourly_rate"),
                    num_lectures_given,
                    num_teacher_review,
                    reviews_average,
                    rs.getString("image_name"),
                    rs.getBoolean("active")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return teacher;
    }

    public ArrayList<Course> getTeacherCourses(int idTeacher){
        ArrayList<Course> course_list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course INNER JOIN rel_course_teacher ON course.id = rel_course_teacher.id_course WHERE rel_course_teacher.id_teacher = " + idTeacher);
            while (rs.next()) {
                Course course = new Course(rs.getInt("course.id"),rs.getString("title"), rs.getString("color"), rs.getString("image_name"), rs.getBoolean("active"));
                course_list.add(course);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return course_list;
    }

    public Boolean addReview(int idBooking, int rate, String title, String text){
        createConnection();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO booking_review (id_booking, rate, title, text, creation_date) VALUES (" + idBooking +  ", " + rate + ", '" + title + "', '" + text + "', '" + dtf.format(now) + "')");
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            //closeConnection();
            return true;
        }
    }

    public ArrayList<Review> getTeacherReviews(int idTeacher){
        ArrayList<Review> review_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT " +
                                                "booking_review.id, " +
                                                "user.name, " +
                                                "user.surname," +
                                                "user.image_name," +
                                                "booking_review.rate, " +
                                                "booking_review.title, " +
                                                "booking_review.text, " +
                                                "booking_review.creation_date " +
                                                "FROM booking_review " +
                                                "LEFT JOIN booking ON booking.id = booking_review.id_booking  " +
                                                "LEFT JOIN teacher ON teacher.id = booking.id_teacher " +
                                                "LEFT JOIN user ON user.id = booking.id_user " +
                                                "WHERE booking.id_teacher = " + idTeacher);
            while (rs.next()) {
                Review review = new Review(
                    rs.getInt("id"),
                    rs.getString("user.name"),
                    rs.getString("user.surname"),
                    rs.getString("user.image_name"),
                    rs.getInt("booking_review.rate"),
                    rs.getString("booking_review.title"),
                    rs.getString("booking_review.text"),
                    rs.getString("booking_review.creation_date")
                );
                review_list.add(review);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return review_list;
    }

    private Boolean checkTeacherAvaliabilityDate(int idTeacher, String date){
        Boolean ret = false;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as num_theacher_commitments FROM booking WHERE deleted = false AND id_teacher = " + idTeacher + " AND booking_date = '" + date + "'");
            rs.next();
            int num_teacher_commitments = rs.getInt("num_theacher_commitments");
            ret = num_teacher_commitments < 4;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            return ret;
        }
    }

    public ArrayList<BookingSlot> getDailyTeacherSlots(int idTeacher, String dateDay, int userId){
        ArrayList<BookingSlot> booking_slot_list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE deleted = false AND id_teacher = " + idTeacher + " AND booking_date = '" + dateDay + "' ORDER BY booking_time_start ASC");

            org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTime dateDayDate = formatter.parseDateTime(dateDay);


            for(int i = 15; i < 19; i++) {

                if(dateDayDate.plusHours(i).compareTo(DateTime.now()) > 0 && !checkIfUserHaveAlreadyBookedForSlot(userId, dateDay, i)){
                    BookingSlot bookingSlot = new BookingSlot(dateDay, i, i+1, true);
                    booking_slot_list.add(bookingSlot);
                }
            }

            while (rs.next()) {
                for (BookingSlot slot:booking_slot_list) {
                    if(slot.getFrom() == rs.getInt("booking_time_start")){
                        slot.setAvaliable(false);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return booking_slot_list;

    }

    public int getMaxHourlyRate(){
        int maxHourlyRate = 1;
        createConnection();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT CEILING(COALESCE(MAX(hourly_rate),1)) as max_hourly_rate FROM teacher");
            rs.next();
            maxHourlyRate = rs.getInt("max_hourly_rate");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return maxHourlyRate;
    }

    private boolean checkIfUserHaveAlreadyBookedForSlot(int userId, String date, int startTime){
        boolean ret = false;
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE deleted = false AND id_user = " + userId + " AND booking_date = '" + date + "' AND booking_time_start = " + startTime);
            if (rs.isBeforeFirst()) {
                ret = true;
            }
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  ret;
    }

    private class CustomTeacherComparator implements Comparator<Teacher> {
        @Override
        public int compare(Teacher o1, Teacher o2) {
            return Double.compare(o1.getReviews_average(), o2.getReviews_average());
        }
    }


    // => METHODS TO MANAGE ASSOCIATIONS BEETWEEN COURSES AND TEACHER
    public void associateCourseToteacher(int idCourse, int idTeacher){
        createConnection();
        try {
            // elimina tutte le precedenti associazioni
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO rel_course_teacher (id_course, id_teacher) VALUES (" + idCourse +  ", " + idTeacher + ")");
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
    }
    public void removeCourseTeacherAssociation(int idTeacher){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM rel_course_teacher WHERE id_teacher = " + idTeacher);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public ArrayList<RelCourseTeacher> getAssociations() {
        ArrayList<RelCourseTeacher> rel_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT rel_course_teacher.id, course.title, teacher.name, teacher.surname " +
                                                "FROM rel_course_teacher " +
                                                "INNER JOIN course ON rel_course_teacher.id_course = course.id " +
                                                "INNER JOIN teacher ON rel_course_teacher.id_teacher = teacher.id");
            while (rs.next()) {
                RelCourseTeacher rel = new RelCourseTeacher(rs.getInt("id"),rs.getString("course.title"),rs.getString("teacher.name"), rs.getString("teacher.surname"));
                rel_list.add(rel);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return rel_list;
    }

    // => METHODS TO MANAGE BOOKING

    /* TO DO:
    * managing errors requests[SI]
    * errore se ho gia prenotato l'index si sposta e da errore
    * tutorial iniziale
    * errore ogni tanto quando elimino o aggiungo prenotazione non mi carica subito la lista con la modifica effettuata[SI]
    * when user click on confirm booking check effective availability[SI]
    * null sound...
    * */

    public long addBooking(int idCourse, int idTeacher, int idUser, String date, int startTime, int endTime){
        long retBookingNewId = -1;
        createConnection();
        if(checkTeacherAvaliabilityDateTime(idTeacher, date, startTime)){
            try {
                PreparedStatement st = conn.prepareStatement("INSERT INTO booking (id_course, id_teacher, id_user, booking_date, booking_time_start, booking_time_end) VALUES (" + idCourse +  ", " + idTeacher + ", " + idUser + ", '" + date + "', " + startTime + ", " + endTime + ")",
                        Statement.RETURN_GENERATED_KEYS);

                int affectedRows = st.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        retBookingNewId = generatedKeys.getLong(1);
                    }
                    else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            finally {
                closeConnection();
                return retBookingNewId;
            }

        }
        else{
            return retBookingNewId;
        }

    }
    public Boolean checkTeacherAvaliabilityDateTime(int idTeacher, String date, int startTime){
        Boolean ret = false;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE deleted = false AND id_teacher = " + idTeacher + " AND booking_date = '" + date + "' AND booking_time_start = " + startTime);
            if (!rs.isBeforeFirst() ) {
                ret = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            return ret;
        }
    }

    public void removeBooking(int idBooking){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE booking SET deleted = 1 WHERE id = " + idBooking);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
    }

    public void markBookingAsDone(int idBooking){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE booking SET confirmed = 1 WHERE id = " + idBooking);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
    }

    /*public ArrayList<Booking> getBooking() {
        ArrayList<Booking> bookings_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking");
            while (rs.next()) {
                Booking book = new Booking(rs.getInt("id"),rs.getInt("id_course"),rs.getInt("id_teacher"), rs.getInt("id_user"), rs.getString("booking_date"), rs.getInt("booking_time_start"), rs.getInt("booking_time_end"));
                bookings_list.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }
        return bookings_list;
    }*/

    public ArrayList<Booking> getUsersBooking(int userId) {
        ArrayList<Booking> my_bookings_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT booking.id, " +
                        "course.title, " +
                        "course.color, " +
                        "teacher.name, " +
                        "teacher.surname, " +
                        "booking.id_user, " +
                        "booking.booking_date," +
                        "STR_TO_DATE(booking.booking_date, '%d/%m/%Y') as booking_date_converted," +
                        "booking.booking_time_start," +
                        "booking.booking_time_end, " +
                        "booking.confirmed, " +
                        "booking.deleted, " +
                        "booking_review.id " +
                        "FROM booking " +
                        "LEFT JOIN teacher ON teacher.id = booking.id_teacher " +
                        "LEFT JOIN course ON course.id = booking.id_course " +
                        "LEFT JOIN booking_review ON booking_review.id_booking = booking.id " +
                        "WHERE booking.id_user = " + userId + " " +
                        "ORDER BY booking_date_converted DESC");

            while (rs.next()) {
                boolean hasReview = false;
                if(rs.getString("booking_review.id") != null){
                    hasReview = true;
                }
                Booking myBook = new Booking(
                        rs.getInt("booking.id"),
                        rs.getString("course.title"),
                        rs.getString("course.color"),
                        (rs.getString("teacher.name") + " " + rs.getString("teacher.surname")),
                        rs.getInt("booking.id_user"),
                        rs.getString("booking.booking_date"),
                        rs.getInt("booking.booking_time_start"),
                        rs.getInt("booking.booking_time_end"),
                        rs.getBoolean("booking.confirmed"),
                        rs.getBoolean("booking.deleted"),
                        hasReview
                );

                my_bookings_list.add(myBook);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return my_bookings_list;
    }

    public ArrayList<Booking> getUsersUpcomingDailyBooking(int userId) {
        ArrayList<Booking> my_bookings_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT booking.id, " +
                            "course.title, " +
                            "course.color, " +
                            "teacher.name, " +
                            "teacher.surname, " +
                            "booking.id_user, " +
                            "booking.booking_date," +
                            "STR_TO_DATE(booking.booking_date, '%d/%m/%Y') as booking_date_converted," +
                            "booking.booking_time_start," +
                            "booking.booking_time_end, " +
                            "booking.confirmed, " +
                            "booking.deleted, " +
                            "booking_review.id " +
                            "FROM booking " +
                            "LEFT JOIN teacher ON teacher.id = booking.id_teacher " +
                            "LEFT JOIN course ON course.id = booking.id_course " +
                            "LEFT JOIN booking_review ON booking_review.id_booking = booking.id " +
                            "WHERE booking.id_user = " + userId + " " +
                            "AND STR_TO_DATE(booking.booking_date, '%d/%m/%Y') = CURDATE() " +
                            "AND booking.confirmed = false " +
                            "AND booking.deleted = false " +
                            "ORDER BY booking.booking_time_start ASC");

            while (rs.next()) {
                Booking myBook = new Booking(
                        rs.getInt("booking.id"),
                        rs.getString("course.title"),
                        rs.getString("course.color"),
                        (rs.getString("teacher.name") + " " + rs.getString("teacher.surname")),
                        rs.getInt("booking.id_user"),
                        rs.getString("booking.booking_date"),
                        rs.getInt("booking.booking_time_start"),
                        rs.getInt("booking.booking_time_end"),
                        rs.getBoolean("booking.confirmed"),
                        rs.getBoolean("booking.deleted"),
                        false
                );

                my_bookings_list.add(myBook);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return my_bookings_list;
    }

    public ArrayList<Booking> getUsersBookingAdmin() {
        ArrayList<Booking> my_bookings_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT booking.id, " +
                            "course.title, " +
                            "course.color, " +
                            "teacher.name, " +
                            "teacher.surname, " +
                            "booking.id_user, " +
                            "user.name, " +
                            "user.surname, " +
                            "booking.booking_date," +
                            "STR_TO_DATE(booking.booking_date, '%d/%m/%Y') as booking_date_converted," +
                            "booking.booking_time_start," +
                            "booking.booking_time_end, " +
                            "booking.confirmed, " +
                            "booking.deleted, " +
                            "booking_review.id " +
                            "FROM booking " +
                            "LEFT JOIN teacher ON teacher.id = booking.id_teacher " +
                            "LEFT JOIN course ON course.id = booking.id_course " +
                            "LEFT JOIN user ON user.id = booking.id_user " +
                            "LEFT JOIN booking_review ON booking_review.id_booking = booking.id " +
                            "ORDER BY booking_date_converted DESC");

            while (rs.next()) {
                boolean hasReview = false;
                if(rs.getString("booking_review.id") != null){
                    hasReview = true;
                }
                Booking myBook = new Booking(
                        rs.getInt("booking.id"),
                        rs.getString("course.title"),
                        rs.getString("course.color"),
                        (rs.getString("teacher.name") + " " + rs.getString("teacher.surname")),
                        rs.getInt("booking.id_user"),
                        rs.getString("user.name") + " " + rs.getString("user.surname"),
                        rs.getString("booking.booking_date"),
                        rs.getInt("booking.booking_time_start"),
                        rs.getInt("booking.booking_time_end"),
                        rs.getBoolean("booking.confirmed"),
                        rs.getBoolean("booking.deleted"),
                        hasReview
                );

                my_bookings_list.add(myBook);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return my_bookings_list;
    }

    public List<DailyAvaliability> getSlotAvaliability(String day, int time){

        List<DailyAvaliability> listAvaliability = new ArrayList<>();
        List<Course> courseList = getCourses(false);
        List<Teacher> teacherList = getTeachers(false, -1, "", -1, false);
        createConnection();
        for(Teacher t: teacherList){
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE id_teacher = " + t.getId() + " AND booking_date = '" + day + "' AND booking_time_start = " + time);
                if (!rs.isBeforeFirst() ) {
                    for(Course c: courseList){
                        // Chek if prof teach this course
                        rs = st.executeQuery("SELECT * FROM rel_course_teacher WHERE id_course = " + c.getId() + " AND id_teacher = " + t.getId());
                        if(rs.isBeforeFirst()){
                            listAvaliability.add(new DailyAvaliability(c.getTitle(), t.getName() + " " + t.getSurname(), c.getColor()));
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        closeConnection();

        return listAvaliability;
    }




}
