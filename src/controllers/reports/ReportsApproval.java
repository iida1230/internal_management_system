package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DepartmentName;
import models.Employee;
import models.Report;
import utils.DBUtil;
import utils.DepartmentReportUtil;
import utils.DepartmentUtil;

/**
 * Servlet implementation class ReportsApprovel
 */
@WebServlet("/reports/approval")
public class ReportsApproval extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsApproval() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
  //レビュー待ち日報リストの処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //承認待ち日報のレビュー状況の遷移先の場合
        if(request.getParameter("review")!=null){
            List <Report> reports =em.createNamedQuery("getMyApprovalPending",Report.class)
                    .setParameter("employee", request.getSession().getAttribute("login_employee"))
                    .setFirstResult(5 * (page - 1))
                    .setMaxResults(5)
                    .getResultList();
            long reports_count =em.createNamedQuery("getMyApprovalPendingCount",Long.class)
                    .setParameter("employee", request.getSession().getAttribute("login_employee"))
                    .getSingleResult();
            em.close();
            if(reports.size() != 0){
            request.setAttribute("approvalTitle","承認待ち日報一覧");
            request.setAttribute("reports",reports);
            request.setAttribute("reports_count",reports_count);
            request.setAttribute("page",page);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
            rd.forward(request, response);
            return;
            }else{
             request.getSession().setAttribute("flushError", "承認待ち日報はありません。");
             response.sendRedirect(request.getContextPath() + "/index.html");
             return;
            }
        }
        //管理職確認
        int approval  ;
       //編集後のリダイレクトの場合
        try{
            approval = (Integer)request.getSession().getAttribute("approval");
            request.getSession().removeAttribute("approval");
            if(request.getSession().getAttribute("flush") != null) {
                request.setAttribute("flush", request.getSession().getAttribute("flush"));
                request.getSession().removeAttribute("flush");
            }
            if(request.getSession().getAttribute("flushError") != null) {
                request.setAttribute("flushError", request.getSession().getAttribute("flushError"));
                request.getSession().removeAttribute("flushError");
            }
        //直接遷移の場合
        } catch(Exception e1) {
            approval=Integer.parseInt(request.getParameter("approval"));
          }
        Timestamp timestamp = null;
      //所属している部署の日報レビュー待ちリストを取得
        List<Report> r=DepartmentReportUtil.getReport((Employee)request.getSession().getAttribute("login_employee"), approval,timestamp);
      //0件の場合
        if(r.size()==0 ){
               em.close();
               request.setAttribute("flushError", "承認待ち日報はありません。");
               RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/reportManager.jsp");
               rd.forward(request, response);
               return;
           }
         //リストの初期化
           List<DepartmentName> DepartmentName= new ArrayList<DepartmentName>();
           List<DepartmentName> DepartmentNameList= new ArrayList<DepartmentName>();

          //日報レビュー待ちの所属部署取得
           for(int i=0 ; i<r.size(); i++){
               List<DepartmentName> d = em.createNamedQuery("getMyDepartments",DepartmentName.class)
                                       .setParameter("e", r.get(i).getEmployee())
                                       .getResultList();
               DepartmentName.addAll(d);

               //重複があれば削除
               DepartmentNameList = DepartmentName.stream().distinct().collect(Collectors.toList());
           }

           //ページ毎のリスト作成
           List<Report> report_page = new ArrayList<Report>();

           //表示を5件までに設定しているため、配列は5つ作成
           String[] department_name = new String[5];
           String st_de ="";
           //承認待ち日報が5件以上の多い場合
           if(r.size()>4&&page==1){
              for(int i=0; i<5 ;i++){
                  report_page.add(r.get(i));
                  DepartmentUtil.getDepatmentName(r.get(i).getEmployee(), i,page, st_de, department_name);
              }

              request.setAttribute("department_name",department_name);
              request.setAttribute("reports",report_page);

            //ページから遷移の場合
           }else if(page>1){
               for(int i=(page-1)*5; i<r.size();i++ ){
                   report_page.add(r.get(i));
                   DepartmentUtil.getDepatmentName(r.get(i).getEmployee(), i,page, st_de, department_name);

                    if(report_page.size()==5){  //5件取得でループ終了
                       break;
                    }
              }
               request.setAttribute("department_name",department_name);
               request.setAttribute("reports",report_page);
            //リストが5件以下の場合
            }else{
               for(int i=0; i<r.size() ;i++){
                   DepartmentUtil.getDepatmentName(r.get(i).getEmployee(), i,page, st_de, department_name);
               }

               request.setAttribute("department_name",department_name);
               request.setAttribute("reports",r);
            }
               //所属部署をjspで呼び出す
                request.setAttribute("DepartmentNameList",DepartmentNameList);
                request.setAttribute("approval",approval);
                request.setAttribute("count",r.size());
                request.setAttribute("page", page);

                em.close();
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/reportManager.jsp");
                rd.forward(request, response);
                return;
            }

  //所属部署が同じ日報レビュー待ちリスト取得の処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        //ページネーション
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //所属部署取得
        List <DepartmentName>  getDepartment= em.createNamedQuery("getMyDepartments",DepartmentName.class)
                .setParameter("e",request.getSession().getAttribute("login_employee"))
                .getResultList();
        //検索した部署のデータ取得
        DepartmentName dn = em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department")));
        List <Report> reports = em.createNamedQuery("getApprovalPendingDepartmentReport",Report.class)
                .setParameter("approval",Integer.parseInt(request.getParameter("approval")))
                .setParameter("dn", dn.getId())
                .getResultList();
        long approval_count =em.createNamedQuery("getApprovalPendingDepartmentCount",Long.class)
                .setParameter("approval", Integer.parseInt(request.getParameter("approval")))
                .setParameter("dn", dn.getId())
                .getSingleResult();
        em.close();
        request.setAttribute("getDepartment",getDepartment);
        request.setAttribute("dn",dn);
        request.setAttribute("reports",reports);
        request.setAttribute("approval",request.getParameter("approval"));
        request.setAttribute("approval_count",approval_count);
        request.setAttribute("page",page);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/reportManager.jsp");
        rd.forward(request, response);


    }



}

