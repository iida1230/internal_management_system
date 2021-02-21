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
 * Servlet implementation class FollowerReportShow
 */
@WebServlet("/followerReport/show")
public class FollowerReportShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowerReportShow() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //フォロワーの日報取得の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();
           int page;
           //ページネーション
           try{
               page=Integer.parseInt(request.getParameter("page"));
           }catch(Exception e) {
               page = 1;
           }
           //フォロワーの日報取得
          List <Report> follower_report = em.createNamedQuery("getMyAllFollowerReports", Report.class)
                                           .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                           .setFirstResult(5*(page-1))
                                           .setMaxResults(5)
                                           .getResultList();
          //0件の場合
          if(follower_report.size()==0){
              em.close();
              RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followerReport.jsp");
              rd.forward(request, response);
              return;

          }
          //フォロワーの日報の数取得
          long reports_count = (long)em.createNamedQuery("MyAllFollowerReportsCount", Long.class)
                                             .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                             .getSingleResult();
        //日報の件数分配列作成
          String[] department_name = new String[follower_report.size()];
          String st_de ="";
        //所属部署取得
          for(int i=0; i<follower_report.size() ;i++){
              DepartmentUtil.getDepatmentName(follower_report.get(i).getEmployee(), i,1, st_de, department_name);
          }
              em.close();
              request.setAttribute("department_name",department_name);
              request.setAttribute("follower_report", follower_report);
              request.setAttribute("reports_count", reports_count);
              request.setAttribute("page", page);

              RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followerReport.jsp");
              rd.forward(request, response);
    }
}
