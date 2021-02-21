package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;
import utils.DepartmentUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //日報一覧表示の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //承認済みの日報取得
        List <Report> reports =em.createNamedQuery("getAllReports",Report.class)
                                .setFirstResult(15 * (page - 1))
                                .setMaxResults(15)
                                .getResultList();
        long reports_count = (long)em.createNamedQuery("getAllReportsCount", Long.class)
                                .getSingleResult();

        String[] department_name = new String[reports.size()];
        String st_de ="";
        for(int i=0; i<reports.size() ;i++){
            DepartmentUtil.getDepatmentName(reports.get(i).getEmployee(), i,1, st_de, department_name);
        }
        em.close();
        request.setAttribute("department_name",department_name);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("reports", reports);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        if(request.getSession().getAttribute("flushError")!=null){
            request.setAttribute("flushError", request.getSession().getAttribute("flushError"));
            request.getSession().removeAttribute("flushError");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

    }
