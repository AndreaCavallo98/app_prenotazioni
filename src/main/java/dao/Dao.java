package dao;

import java.awt.print.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"), rs.getString("image_name"), true);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
        return user;
    }

    // => METHODS TO MANAGE COURSES
    public void addCourse(Course course){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO course (title) VALUES ('" + course.getTitle() +  "')");
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
    }
    public void removeCourse(int idCourse){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE course SET active = 0 WHERE id = " + idCourse);
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

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE active = 1");
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
    public void addTeacher(Teacher teacher){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO teacher (name, surname) VALUES ('" + teacher.getName() +  "', '" + teacher.getSurname() + "')");
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
           closeConnection();
        }
    }
    public void removeTeacher(int idTeacher){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE teacher SET active = 0 WHERE id = " + idTeacher);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       /* finally {
            closeConnection();
        }*/
    }
    private int getNumTeacherLecturesGiven(int idTeacher){
        int num_lectures_given = -1;
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as num_lectures_given FROM booking WHERE id_teacher = " + idTeacher);
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
    public ArrayList<Teacher> getTeachers(boolean topFive, int filterCourseId, String filterAvaliableDate, int filterMaxHourlyRate) {
        ArrayList<Teacher> teachers_list = new ArrayList<>();
        createConnection();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM teacher";
            if(filterCourseId != -1){
                query += " LEFT JOIN rel_course_teacher ON rel_course_teacher.id_teacher = teacher.id";
            }
            query += " WHERE teacher.active = 1";
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
            closeConnection();
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
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as num_theacher_commitments FROM booking WHERE id_teacher = " + idTeacher + " AND booking_date = '" + date + "'");
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

    public ArrayList<BookingSlot> getDailyTeacherSlots(int idTeacher, String dateDay){
        ArrayList<BookingSlot> booking_slot_list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE id_teacher = " + idTeacher + " AND booking_date = '" + dateDay + "' ORDER BY booking_time_start ASC");

            for(int i = 15; i < 19; i++) {
                BookingSlot bookingSlot = new BookingSlot(i, i+1, true);
                booking_slot_list.add(bookingSlot);
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
    public void removeCourseTeacherAssociation(int idRel){
        createConnection();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM rel_course_teacher WHERE id = " + idRel);
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*finally {
            closeConnection();
        }*/
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
    // to do: quando clicca button conferma prenotazione controllare ancora l'effettiva disponibilita (servlet threaded safe)
    // to do: null sound...
    // to do: btm rimuovi filtri

    public Boolean addBooking(int idCourse, int idTeacher, int idUser, String date, int startTime, int endTime){
        createConnection();
        if(checkTeacherAvaliabilityDateTime(idTeacher, date, startTime)){
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO booking (id_course, id_teacher, id_user, booking_date, booking_time_start, booking_time_end) VALUES (" + idCourse +  ", " + idTeacher + ", " + idUser + ", '" + date + "', " + startTime + ", " + endTime + ")");
                st.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            finally {
                closeConnection();
                return true;
            }

        }
        else{
            return false;
        }

    }
    public Boolean checkTeacherAvaliabilityDateTime(int idTeacher, String date, int startTime){
        Boolean ret = false;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM booking WHERE id_teacher = " + idTeacher + " AND booking_date = '" + date + "' AND booking_time_start = " + startTime);
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

    public List<DailyAvaliability> getSlotAvaliability(String day, int time){

        List<DailyAvaliability> listAvaliability = new ArrayList<>();
        List<Course> courseList = getCourses();
        List<Teacher> teacherList = getTeachers(false, -1, "", -1);
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
