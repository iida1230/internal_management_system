package controllers.approval;

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
import models.ReportApproval;
import utils.DBUtil;

/**
 * Servlet implementation class ApprovalShow
 */
@WebServlet("/approval/show")
public class ApprovalShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApprovalShow() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //日報レビュー詳細ページ
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //日報レビューの内容をクエリで取得
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        List <ReportApproval> ra =em.createNamedQuery("getMyAllApprovalContent",ReportApproval.class)
                                    .setParameter("id", r)
                                    .setFirstResult(5 * (page - 1))
                                    .setMaxResults(5)
                                    .getResultList();
        long approval_count =em.createNamedQuery("getMyAllApprovalContentCount",Long.class)
                                  .setParameter("id", r)
                                  .getSingleResult();
        em.close();
        //0の場合トップページに戻す
        if(ra.size() == 0){
            request.getSession().setAttribute("flushError", "まだレビューされていません。");
            response.sendRedirect(request.getContextPath()+"/index.html");
         //取得したクエリの数が1以上の場合
        }else{
            request.setAttribute("id",request.getParameter("id"));
            request.setAttribute("page",page);
        request.setAttribute("approval_count",approval_count);
        request.setAttribute("ra",ra);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/reportReview.jsp");
        rd.forward(request, response);
        }
    }



}
