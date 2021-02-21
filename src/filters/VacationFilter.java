package filters;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import models.CalendarTime;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet Filter implementation class VacationFilter
 */
@WebFilter("/*")
public class VacationFilter implements Filter {

    /**
     * Default constructor.
     */
    public VacationFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    //有給数変更の処理
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        EntityManager em = DBUtil.createEntityManager();

        Date vacation_at = new Date(System.currentTimeMillis());
        //現在時刻より過ぎた承認済み有給を取得
        List<CalendarTime> cal = em.createNamedQuery("vacationCount",CalendarTime.class)
                                .setParameter("vacation_at", vacation_at)
                                .getResultList();
        //1件以上なら該当する従業員の有給の数を減数し有給を消化済みに変更
        if(cal.size()!=0){
            for(int i=0 ; i<cal.size(); i++){
                Employee emp = em.find(Employee.class,cal.get(i).getEmployee().getId());
                CalendarTime c = em.find(CalendarTime.class,cal.get(i).getId());
                emp.setVacation(emp.getVacation()-1);
                c.setStay(5);
                em.getTransaction().begin();
                em.getTransaction().commit();
            }

        }
        em.close();
        chain.doFilter(request, response);

    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
