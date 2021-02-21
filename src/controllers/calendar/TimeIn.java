package controllers.calendar;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.CalendarTime;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class Time
 */
@WebServlet("/time/in")
public class TimeIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeIn() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //出退勤の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        CalendarTime cal = new CalendarTime();
        //有給申請の処理
        if(request.getParameter("vacation")!=null){
            Date vacation = new Date(System.currentTimeMillis());
            Date created_at = new Date(System.currentTimeMillis());
            vacation = Date.valueOf(request.getParameter("vacation"));
            cal.setStay(3);
            if(request.getParameter("remarks")!=null){
                cal.setRemarks(request.getParameter("remarks"));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            cal.setTime_month(Integer.parseInt(sdf.format(vacation.getTime())));
            cal.setVacation(vacation);
            cal.setTime_date(vacation);
            cal.setCreated_at(created_at);
            cal.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            request.getSession().setAttribute("flush","有給申請しました。");

          //出勤時の処理
          }else{
            cal.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            Date time_date = new Date(System.currentTimeMillis());
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            cal.setTime_date(time_date);
            cal.setTime_month(Integer.parseInt(sdf.format(cl.getTime())));
            Timestamp attendance_at = new Timestamp(System.currentTimeMillis());
            cal.setAttendance_at(attendance_at);
            request.getSession().setAttribute("flush","出勤しました。");
         }
          em.getTransaction().begin();
          em.persist(cal);
          em.getTransaction().commit();
          em.close();
          response.sendRedirect(request.getContextPath() + "/time/new");
    }


}
