package controllers.approval;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import models.ReportApproval;
import utils.DBUtil;

/**
 * Servlet implementation class ApprovalCreate
 */
@WebServlet("/approval/create")
public class ApprovalCreate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApprovalCreate() {
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
  //日報レビュー作成
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em= DBUtil.createEntityManager();
        //jspで編集した日報の取得
        Report r = em.find(Report.class,Integer.parseInt(request.getParameter("id")));
        int approval=Integer.parseInt(request.getParameter("approval"));
        //日報レビュー後の承認状況を変更
        r.setApproval(Integer.parseInt(request.getParameter("title")));
        //新規日報レビュー作成
            ReportApproval ra = new ReportApproval();
            ra.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            ra.setReport(r);
            ra.setTitle(request.getParameter("title"));
            ra.setContent(request.getParameter("content"));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ra.setCreated_at(currentTime);
            ra.setUpdated_at(currentTime);
            em.getTransaction().begin();
            em.persist(ra);
            em.getTransaction().commit();
            //承認状況に合わせて、フラッシュメッセージを変更
            if((Integer.parseInt(request.getParameter("title"))==0)){
                request.getSession().setAttribute("flushError", "承認を却下しました。");
            }else{
                request.getSession().setAttribute("flush", "承認が完了しました。");
            }
            //再度編集できるよう管理職の番号をスコープにセットしをリダイレクト
             request.getSession().setAttribute("approval", approval);
             response.sendRedirect(request.getContextPath() + "/reports/approval");

     }
}