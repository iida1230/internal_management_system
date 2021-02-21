package controllers.calendar;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.CalendarTime;
import utils.DBUtil;

/**
 * Servlet implementation class TimeOut
 */
@WebServlet("/time/out")
public class TimeOut extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeOut() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //退勤時の処理
        EntityManager em = DBUtil.createEntityManager();
        Integer getMyAttendance_at =em.createNamedQuery("getMyAttendance_at",Integer.class)
                                        .setParameter("employee",request.getSession().getAttribute("login_employee"))
                                        .setFirstResult(0)
                                        .setMaxResults(1)
                                        .getSingleResult();
        CalendarTime cal = em.find(CalendarTime.class, getMyAttendance_at);
        Integer stay;
        //離席か在席か判断
        try{
            stay=Integer.parseInt(request.getParameter("stay"));
            cal.setStay(stay);
            if((stay)==1){
                request.getSession().setAttribute("flush","離席しました。");
            }else if((stay)==0){
            request.getSession().setAttribute("flush","在席中です");
            }
       //退勤の場合
        }catch(Exception e){
        Timestamp retired_at = new Timestamp(System.currentTimeMillis());
        cal.setStay(0);
        cal.setRetired_at(retired_at);
        //備考があれば
        if(request.getParameter("remarks")!=null){
        cal.setRemarks(request.getParameter("remarks"));
        }
        request.getSession().setAttribute("flush","退勤しました。");
        }
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
        response.sendRedirect(request.getContextPath() + "/time/new");
    }



}
