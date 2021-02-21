
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

import models.EmployeeGoodname;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowGood
 */
@WebServlet("/reports/goodName")
public class ReportsShowGood extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowGood() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //日報にいいねした人詳細の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
       //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //いいねした人を取得
        List<EmployeeGoodname> goodNames = em.createNamedQuery("getMyGoodname", EmployeeGoodname.class)
                                            .setParameter("report",r)
                                            .setFirstResult(5 * (page - 1))
                                            .setMaxResults(5)
                                            .getResultList();

        long goodNames_count = (long)em.createNamedQuery("getMyGoodnameCount", Long.class)
                                            .setParameter("report",r)
                                            .getSingleResult();

        em.close();
        request.setAttribute("report",r);
        request.setAttribute("goodNames",goodNames);
        request.setAttribute("goodNames_count",goodNames_count);
        request.setAttribute("page", page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/goodName.jsp");
        rd.forward(request, response);

    }
}
