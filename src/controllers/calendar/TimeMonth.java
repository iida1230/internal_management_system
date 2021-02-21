package controllers.calendar;

import java.io.IOException;
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
import utils.DBUtil;

/**
 * Servlet implementation class TimeMonth
 */
@WebServlet("/time/month")
public class TimeMonth extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeMonth() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
  //月毎の確認済みの勤怠データ
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        int page;
        //ページネーション
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception ex) {
            page = 1;
        }
        String st_month =request.getParameter("month");
        String st_montha =st_month.replace("-", "年"); //jsp側での表示名
        request.setAttribute("month",st_montha);
        String str= request.getParameter("month");
        int time_month = Integer.parseInt(str.replace("-", ""));
        Employee ep=em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //月毎の確認済みの勤怠データを取得
        List<CalendarTime> getMyTimeMonth = em.createNamedQuery("getMyTimeMonth", CalendarTime.class)
                                                 .setParameter("employee", ep)
                                                 .setParameter("time_month", time_month)
                                                 .setFirstResult(15 * (page - 1))
                                                 .setMaxResults(15)
                                                 .getResultList();
        long getMyTimeMonthCount = (long)em.createNamedQuery("getMyTimeMonthCount", Long.class)
                                                .setParameter("employee", ep)
                                                .setParameter("time_month", time_month)
                                                .getSingleResult();
        request.setAttribute("getMyTimeMonth",getMyTimeMonth);
        request.setAttribute("getMyTimeMonthCount",getMyTimeMonthCount);
        request.setAttribute("month",request.getParameter("month"));
        request.setAttribute("page",page);
        em.close();
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/month.jsp");
        rd.forward(request, response);
    }

}
