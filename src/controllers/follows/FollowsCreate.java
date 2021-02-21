package controllers.follows;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsCreate
 */
@WebServlet("/follows/create")
public class FollowsCreate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsCreate() {
        super();
        // TODO Auto-generated constructor stub
    }
    //管理者による新規フォロー作成
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            EntityManager em = DBUtil.createEntityManager();
            Follow follow= new Follow();
            Employee e=em.find(Employee.class, Integer.parseInt(request.getParameter("employees")));
            Employee e1=em.find(Employee.class, Integer.parseInt(request.getParameter("employee_id")));
            follow.setEmployee(e);
            Timestamp currenTime =new Timestamp(System.currentTimeMillis());
            follow.setCreated_at(currenTime);
            follow.setUpdated_at(currenTime);
            follow.setMyId(e1);
            em.getTransaction().begin();
            em.persist(follow);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush","フォローしました。");
            //フォローをした人をスコープにセットしリダイレクト
            request.getSession().setAttribute("employee_id",request.getParameter("employee_id"));
            response.sendRedirect(request.getContextPath() + "/employees/followShow");
    }
     //フォロー作成
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Follow follow= new Follow();
        Employee e=em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //フォローしているか確認
        long followChek =(long)em.createNamedQuery("checkFollowID",Long.class)
                                    .setParameter("employee",e)
                                    .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                    .getSingleResult();
        //1件でもある場合
        if(followChek > 0){
            request.getSession().setAttribute("flushError","フォローしています。");
            response.sendRedirect(request.getContextPath()+"/reports/index");
            return;
        }else{
            follow.setEmployee(e);
            Timestamp currenTime =new Timestamp(System.currentTimeMillis());
            follow.setCreated_at(currenTime);
            follow.setUpdated_at(currenTime);
            follow.setMyId((Employee) request.getSession().getAttribute("login_employee"));
            em.getTransaction().begin();
            em.persist(follow);
            em.getTransaction().commit();
            em.close();
            //日報リストページに遷移
            request.getSession().setAttribute("flush","フォローしました。");
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}
