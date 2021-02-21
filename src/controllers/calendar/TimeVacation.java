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
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class TimeVacation
 */
@WebServlet("/time/vacation")
public class TimeVacation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeVacation() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //有給記録確認の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //有給申請を取得
        List <CalendarTime> getMyVacation = em.createNamedQuery("getMyVacation",CalendarTime.class)
                                            .setParameter("employee", request.getSession().getAttribute("login_employee"))
                                            .setFirstResult(15 * (page - 1))
                                            .setMaxResults(15)
                                            .getResultList();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        long getMyVacationCount = (long)em.createNamedQuery("getMyVacationCount", Long.class)
                                .setParameter("employee", login_employee)
                                .getSingleResult();
        em.close();
        request.setAttribute("page",page);
        request.setAttribute("getMyVacationCount",getMyVacationCount);
        request.setAttribute("employee", login_employee);
        if(getMyVacation.size()!=0){
        request.setAttribute("getMyVacation",getMyVacation);
        }
        if(request.getSession().getAttribute("flush")!=null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/calendar/vacation.jsp");
        rd.forward(request, response);
        }

  //有給申請の編集処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        CalendarTime cal = em.find(CalendarTime.class,Integer.parseInt(request.getParameter("id")));
       //編集、再提出の場合
    if(Integer.parseInt(request.getParameter("update"))==0){
        Date vacation = new Date(System.currentTimeMillis());
        Date created_at = new Date(System.currentTimeMillis());
        vacation = Date.valueOf(request.getParameter("vacation"));
        cal.setUpdated_at(null);
        cal.setStay(3);
        cal.setVacation(vacation);
        cal.setCreated_at(created_at);
        cal.setRemarks(request.getParameter("remarks"));
        em.getTransaction().begin();
        em.getTransaction().commit();
        request.getSession().setAttribute("flush","再提出しました。");
        //削除の場合
       }else{
        em.getTransaction().begin();
        em.remove(cal);
        em.getTransaction().commit();
        request.getSession().setAttribute("flush","削除しました。");
        }
        em.close();
         response.sendRedirect(request.getContextPath() + "/time/vacation");
    }
}
