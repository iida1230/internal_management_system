package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.EmployeeGoodname;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateGood
 */
@WebServlet("/reports/good")
public class ReportsUpdateGood extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateGood() {
        super();
        // TODO Auto-generated constructor stub
    }
  //日報に対してのいいねの処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        long employees_count = (long)em.createNamedQuery("registeredGoodName", Long.class)
                                        .setParameter("employee", request.getSession().getAttribute("login_employee"))
                                        .setParameter("report", r)
                                        .getSingleResult();
        //１度日報にいいねしている場合
        if(employees_count > 0){
            request.getSession().setAttribute("flushError", "いいねしています。");
            response.sendRedirect(request.getContextPath() + "/reports/index");
            em.close();
            return;
        }else {
            Integer  a =Integer.parseInt(request.getParameter("goodCount"));
            EmployeeGoodname eg =new EmployeeGoodname();
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            eg.setCreated_at(currentTime);
            eg.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            eg.setReport(r);
            r.setGood(r.getGood()+a);
            em.getTransaction().begin();
            em.persist(eg);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "いいねしました。");
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }

    }


