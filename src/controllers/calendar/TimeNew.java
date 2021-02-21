package controllers.calendar;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.CalendarTime;
import utils.DBUtil;

/**
 * Servlet implementation class TimeNew
 */
@WebServlet("/time/new")
public class TimeNew extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeNew() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
  //出勤時の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //勤務初日に対してのエラー対策
        try{
          //最後の勤怠データを取得
            List<CalendarTime> getMyStay =em.createNamedQuery("getMyStay",CalendarTime.class)
                                            .setParameter("employee",request.getSession().getAttribute("login_employee"))
                                            .setFirstResult(0)
                                            .setMaxResults(1)
                                            .getResultList();
            em.close();
            if(getMyStay.size()!=0){
                request.setAttribute("getMyStay", getMyStay);
            }
        }catch(Exception e){
            em.close();
        }
        Date time_date = new Date(System.currentTimeMillis());
        request.setAttribute("time_date", time_date);
        if(request.getSession().getAttribute("flush")!=null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/new.jsp");
        rd.forward(request, response);
        }
}
