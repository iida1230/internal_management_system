package controllers.follows;

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
 * Servlet implementation class FollowsShow
 */
@WebServlet("/followsReport/show")
public class FollowsReportShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsReportShow() {
        super();
        // TODO Auto-generated constructor stub
    }

    //フォローした従業員の日報取得の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
       EntityManager em = DBUtil.createEntityManager();
       int page;
       try{
           page=Integer.parseInt(request.getParameter("page"));
       }catch(Exception e) {
           page = 1;
       }
     //フォローした従業員の日報取得
       List <Report> follows_reports = em.createNamedQuery("getMyAllFollowsReport", Report.class)
                               .setParameter("employee",request.getSession().getAttribute("login_employee"))
                               .setFirstResult(5*(page-1))
                               .setMaxResults(5)
                               .getResultList();
       //0件の場合
      if(follows_reports.size()==0){
          em.close();
          RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followsReport.jsp");
          rd.forward(request, response);
          return;

      }
      //フォローした従業員の日報の件数取得
      long reports_count = (long)em.createNamedQuery("getMyAllFollowsReportCount", Long.class)
                                      .setParameter("employee",request.getSession().getAttribute("login_employee"))
                                      .getSingleResult();
      //日報の件数分配列作成
      String[] department_name = new String[follows_reports.size()];
      String st_de ="";
      //所属部署取得
      for(int i=0; i<follows_reports.size() ;i++){
          DepartmentUtil.getDepatmentName(follows_reports.get(i).getEmployee(), i,1, st_de, department_name);
      }

      em.close();
      request.setAttribute("department_name",department_name);
      request.setAttribute("follows_reports", follows_reports);
      request.setAttribute("reports_count", reports_count);
      request.setAttribute("page", page);
      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followsReport.jsp");
      rd.forward(request, response);
    }





}
