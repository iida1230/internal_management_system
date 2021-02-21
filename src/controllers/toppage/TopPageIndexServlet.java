package controllers.toppage;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import models.validators.AlertValidator;
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
  //TOPページ
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
          }
        //自分の日報取得
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();


        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();


        List<String> arert = AlertValidator.validate(login_employee);

        //トップページ遷移時　アラート表示
        if(arert.size() > 0) {
            request.setAttribute("arert", arert);

        }
        em.close();

        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);


        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        if(request.getSession().getAttribute("flushError") != null) {
            request.setAttribute("flushError", request.getSession().getAttribute("flushError"));
            request.getSession().removeAttribute("flushError");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
