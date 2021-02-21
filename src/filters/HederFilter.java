package filters;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import models.Employee;
import models.Report;
import utils.DBUtil;
import utils.DepartmentReportUtil;

/**
 * Servlet Filter implementation class FollowHeder
 */
@WebFilter("/*")
public class HederFilter implements Filter {

    /**
     * Default constructor.
     */
    public HederFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

   //ヘッダーレイアウト変更の処理
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        EntityManager em = DBUtil.createEntityManager();
        HttpSession session = ((HttpServletRequest)request).getSession();
        Employee e = (Employee)session.getAttribute("login_employee");
        //フォローしている数を取得しヘッダーの表示件数変更
         long Heder_follows_count =em.createNamedQuery("getMyAllFollowsCount",Long.class)
                                        .setParameter("myId",e)
                                        .getSingleResult();
         //フォロワーの数を取得しヘッダーの表示件数変更
         long  Heder_follower_count= em.createNamedQuery("getMyAllFollowerCount",Long.class)
                                          .setParameter("myId",e)
                                          .getSingleResult();

       //所属している部署の日報レビュー待ちリストを取得
         Timestamp timestamp=null;
         int approval =0;
         List<Report> r=DepartmentReportUtil.getReport(e, approval,timestamp);
         if(r!=null){
         request.setAttribute("Heder_manager" , r);
         request.setAttribute("Heder_director" , r);
         }
         //再提出がある場合
         long Heder_resubmit =em.createNamedQuery("Heder_resubmit",Long.class)
                                         .setParameter("employee",e)
                                         .getSingleResult();
         if(Heder_resubmit>0){
             request.setAttribute("Heder_resubmit" ,Heder_resubmit);
         }
         //却下された有給申請がある場合
         long Heder_MyNoVacation =em.createNamedQuery("Heder_MyNoVacation",Long.class)
                                      .setParameter("employee",e)
                                      .getSingleResult();
         if(Heder_MyNoVacation>0){
             request.setAttribute("Heder_MyNoVacation" ,Heder_MyNoVacation);
         }
         //新規有給申請がある場合
         long Heder_vacationUpDate =em.createNamedQuery("Heder_vacationUpDate",Long.class)
                                      .getSingleResult();
         if(Heder_vacationUpDate>0){
             request.setAttribute("Heder_vacationUpDate" ,Heder_vacationUpDate);
         }
         em.close();
         request.setAttribute("Heder_follows_count", Heder_follows_count);
         request.setAttribute("Heder_follower_count", Heder_follower_count);
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
