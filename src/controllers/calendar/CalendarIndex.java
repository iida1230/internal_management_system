package controllers.calendar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.CalendarTime;
import models.Employee;
import utils.AttendanceUtil;
import utils.DBUtil;

/**
 * Servlet implementation class CalendarIndex
 */
@WebServlet("/calendar/index")
public class CalendarIndex extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarIndex() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // 勤怠カレンダー作成
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Calendar calendar = Calendar.getInstance();

     //日付セット
       try{

           String  y=request.getParameter("y");
           String  m=request.getParameter("m");
           calendar.set(Integer.parseInt(y), Integer.parseInt(m) - 1, 1);
           request.setAttribute("y",Integer.parseInt(y));
           request.setAttribute("m",Integer.parseInt(m));
       }catch(Exception e){
           int y = calendar.get(Calendar.YEAR);
           int m = calendar.get(Calendar.MONTH);
           calendar.set(y,m , 1);
           request.setAttribute("y",y);
           request.setAttribute("m",m+1);
       }
       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
       //カレンダーの月の勤務データ取得
       int   time_month   = Integer.parseInt(sdf.format(calendar.getTime()));
       List<CalendarTime> query_attendance = AttendanceUtil.getAttendance((Employee)request.getSession().getAttribute("login_employee"),null,em,10,time_month,null);

       int firstDay = calendar.get(Calendar.DAY_OF_WEEK);//最初の日付取得
       int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//全日数取得
       calendar.set(Calendar.DATE,dayCount );
       int lastDate =calendar.get(Calendar.DAY_OF_WEEK);//最終日の曜日取得
       int f=count(firstDay);
       int l=count(lastDate);
       int count=6-l;

       // 日にちラベルを生成
       String[] dayLabel = new String[dayCount+f+count];
       for (int i = 0; i < dayLabel.length; i++) {
       dayLabel[i] = "";
       }
       for(int i = f; i < f +dayLabel.length-count-1; i++){
       dayLabel[i] = i - f + 1 + "";

       }

       request.setAttribute("getCalendar",query_attendance);
       request.setAttribute("dayLabel",dayLabel);
       RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/index.jsp");
       rd.forward(request, response);
    }
  //曜日に応じてカウンタを回す
    private int count(int count) {
        int k = 0;
        switch(count){
        case Calendar.SUNDAY: k += 0;break;
        case Calendar.MONDAY: k += 1;break;
        case Calendar.TUESDAY: k += 2;break;
        case Calendar.WEDNESDAY: k += 3;break;
        case Calendar.THURSDAY: k += 4;break;
        case Calendar.FRIDAY: k += 5;break;
        case Calendar.SATURDAY: k += 6;break;
        }
        return k;


    }
}
