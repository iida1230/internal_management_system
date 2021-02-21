package controllers.calendar;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.CalendarTime;
import models.DepartmentName;
import models.Employee;
import utils.AttendanceUtil;
import utils.DBUtil;
import utils.DepartmentUtil;

/**
 * Servlet implementation class TimeManagement
 */
@WebServlet("/time/management")
public class TimeManagement extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Date time_date = new Date(System.currentTimeMillis());

        //所属部署取得
        List<DepartmentName>  dn  = DepartmentUtil.getMyDepatmentName((Employee)request.getSession().getAttribute("login_employee"),em);
        //所属部署が同じ従業員取得
        List<Employee> e=DepartmentUtil.getMyDepatmentEmployee((Employee)request.getSession().getAttribute("login_employee"), dn, em);

        request.setAttribute("e", e);
        request.setAttribute("time_date", time_date);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/management.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        //所属部署取得
        List<DepartmentName>  dn  = DepartmentUtil.getMyDepatmentName((Employee)request.getSession().getAttribute("login_employee"),em);
        //所属部署が同じ従業員取得
        List<Employee> e=DepartmentUtil.getMyDepatmentEmployee((Employee)request.getSession().getAttribute("login_employee"), dn, em);

        request.setAttribute("e", e);

       //勤怠記録　確認の処理
   if(request.getParameter("update")!=null){
       CalendarTime ct = em.find(CalendarTime.class,  Integer.parseInt(request.getParameter("id")));
    //休暇の場合の処理
       if(Integer.parseInt(request.getParameter("stay"))==3){
        ct.setStay(Integer.parseInt(request.getParameter("stay")));
        ct.setRetired_at(null);
        ct.setAttendance_at(null);
        Date time_date = new Date(System.currentTimeMillis());
        time_date = Date.valueOf(request.getParameter("time_date"));
        ct.setTime_date(time_date);
        ct.setVacation(time_date);
        ct.setRemarks(request.getParameter("remarks"));
        ct.setCreated_at(time_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        ct.setTime_month(Integer.parseInt(sdf.format(time_date.getTime())));
        ct.setUpdated_at(new Timestamp(System.currentTimeMillis()));
      //未承認の場合の処理
    }else if(Integer.parseInt(request.getParameter("stay"))==4){
        ct.setRemarks(request.getParameter("remarks"));
        ct.setStay(Integer.parseInt(request.getParameter("stay")));
       //削除の場合の処理
    }else if(Integer.parseInt(request.getParameter("stay"))==5){
        em.remove(ct);
        //確認済みの場合の処理
    }else{
        String str_a= request.getParameter("attendance_at");
        String attendance = str_a.replace("T", " ");
        Timestamp  attendance_at = Timestamp.valueOf(attendance);
        ct.setAttendance_at(attendance_at);
        Date time_date = new Date(System.currentTimeMillis());
        time_date = Date.valueOf(request.getParameter("time_date"));
        ct.setTime_date(time_date);
        ct.setRemarks(request.getParameter("remarks"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        ct.setTime_month(Integer.parseInt(sdf.format(time_date.getTime())));
        String str_r= request.getParameter("retired_at");
        String retired = str_r.replace("T", " ");
        Timestamp  retired_at = Timestamp.valueOf(retired);
        ct.setRetired_at(retired_at);
        ct.setUpdated_at(new Timestamp(System.currentTimeMillis()));
     if(Integer.parseInt(request.getParameter("stay"))!=0){
         ct.setStay(Integer.parseInt(request.getParameter("stay")));
        }
    }
         em.getTransaction().begin();
         em.getTransaction().commit();
         request.setAttribute("flush", "更新が完了しました。");

}
        int pattern;
        int page;
        int radio;
        //ページネーション
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception ex1) {
            page = 1;
        }
        try{
            radio = Integer.parseInt(request.getParameter("radio"));
        } catch(Exception ex1) {
            radio = 0;
        }
        //条件で分岐
         //未確認の勤怠記録の場合
            if(radio==4){
                pattern=1;
                //所属部署が同じ未確認の勤務データ取得
                List<CalendarTime> query_attendance=AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"), null, em,pattern,0,null);
                //ページネーションの処理
                List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );


                request.setAttribute("getTimeEmployee",getAttendance);
                request.setAttribute("count",query_attendance.size());
                request.setAttribute("radio",request.getParameter("radio"));
                request.setAttribute("page", page);
            }
          //承認待ち有給申請の場合
            if(radio==5){
                pattern=2;
                //所属部署が同じ未確認の勤務データ取得
                List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),null,  em,pattern,0,null);
                //ページネーションの処理
                List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );

                request.setAttribute("getTimeEmployee",getAttendance);
                request.setAttribute("count",query_attendance.size());
                request.setAttribute("radio",request.getParameter("radio"));
                request.setAttribute("page", page);
            }


         //従業員のみを指定の場合
        if(Integer.parseInt(request.getParameter("employees"))!=0 && request.getParameter("month")==""&&request.getParameter("date")=="" ){
            pattern=3;
            Employee ep=em.find(Employee.class, Integer.parseInt(request.getParameter("employees")));
            //所属部署が同じ未確認の勤務データ取得
            List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),ep,em,pattern,0,null);
            //ページネーションの処理
            List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );

            request.setAttribute("getTimeEmployee",getAttendance);
            request.setAttribute("count",query_attendance.size());
            request.setAttribute("employees",Integer.parseInt(request.getParameter("employees")));
            request.setAttribute("page", page);
        }
        //従業員＋指定した月の場合
        if(Integer.parseInt(request.getParameter("employees"))!=0 && request.getParameter("month")!="" &&request.getParameter("date")==""){
           pattern=4;
            String str= request.getParameter("month");
            int time_month = Integer.parseInt(str.replace("-", ""));
            Employee ep=em.find(Employee.class, Integer.parseInt(request.getParameter("employees")));
            //所属部署が同じ未確認の勤務データ取得
            List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),ep,em,pattern,time_month,null);
            //ページネーションの処理
            List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );


           request.setAttribute("getTimeEmployee",getAttendance);
           request.setAttribute("count",query_attendance.size());
           request.setAttribute("employees",request.getParameter("employees"));
           request.setAttribute("month",request.getParameter("month"));
           request.setAttribute("page", page);
        }
      //従業員＋日月指定の場合
      if(Integer.parseInt(request.getParameter("employees"))!=0 && request.getParameter("month")=="" &&request.getParameter("date")!=""){
         pattern=5;
         Date time_date = new Date(System.currentTimeMillis());
         Employee ep=em.find(Employee.class, Integer.parseInt(request.getParameter("employees")));
         time_date = Date.valueOf(request.getParameter("date"));
         //所属部署が同じ未確認の勤務データ取得
         List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),ep,em,pattern,0,time_date);
         //ページネーションの処理
         List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );

         request.setAttribute("getTimeEmployee",getAttendance);
         request.setAttribute("count",query_attendance.size());
         request.setAttribute("month",request.getParameter("employees"));
         request.setAttribute("date",request.getParameter("date"));
         request.setAttribute("page", page);
        }
     //月指定の場合
     if(Integer.parseInt(request.getParameter("employees"))==0 && request.getParameter("month")!="" &&request.getParameter("date")==""){
         pattern=6;
         String str= request.getParameter("month");
         int time_month = Integer.parseInt(str.replace("-", ""));
         //所属部署が同じ未確認の勤務データ取得
         List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),null,em,pattern,time_month,null);
         //ページネーションの処理
         List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );


         request.setAttribute("getTimeEmployee",getAttendance);
         request.setAttribute("count",query_attendance.size());
         request.setAttribute("month",request.getParameter("month"));
         request.setAttribute("page", page);
     }
     //日月指定の場合
     if(Integer.parseInt(request.getParameter("employees"))==0 && request.getParameter("month")=="" &&request.getParameter("date")!=""){
         pattern=7;
         Date time_date = new Date(System.currentTimeMillis());
         time_date = Date.valueOf(request.getParameter("date"));
         //所属部署が同じ未確認の勤務データ取得
         List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),null,em,pattern,0,time_date);
         //ページネーションの処理
         List<CalendarTime>getAttendance=AttendanceUtil.getPagination(query_attendance, page );

         request.setAttribute("getTimeEmployee",getAttendance);
         request.setAttribute("count",query_attendance.size());
         request.setAttribute("date",request.getParameter("date"));
         request.setAttribute("page", page);
     }
        em.close();
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/management.jsp");
        rd.forward(request, response);

    }

}
