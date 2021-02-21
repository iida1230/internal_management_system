package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesFollowListShow
 */
@WebServlet("/employees/followShow")
public class EmployeesFollowListShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesFollowListShow() {
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
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followManeger.jsp");
        rd.forward(request, response);

    }
   //管理者による新規フォロー作成のための処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        Employee employee_id ;
        try{
            employee_id = em.find(Employee.class, Integer.parseInt((String) request.getSession().getAttribute("employee_id")));
            request.getSession().removeAttribute("employee_id");
        } catch(Exception e) {
            employee_id= em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        }
        //フォローしている人取得
        List <Follow> follow_namesList = em.createNamedQuery("getMyAllFollowsName",Follow.class)
                                            .setParameter("myId",employee_id)
                                            .setFirstResult(5 * (page - 1))
                                            .setMaxResults(5)
                                            .getResultList();
        //フォロー可能な従業員取得
        List <Employee> followable = em.createNamedQuery("followable",Employee.class)
                                            .setParameter("myId", employee_id)
                                            .getResultList();
        //フォローの件数取得
        long follows_count =em.createNamedQuery("getMyAllFollowsCount",Long.class)
                                            .setParameter("myId",employee_id)
                                            .getSingleResult();
        em.close();
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        request.setAttribute("followable",followable);
        request.setAttribute("employee_id",employee_id);
        request.setAttribute("follows_count",follows_count);
        if(follow_namesList.size()!=0){
        request.setAttribute("follow_namesList",follow_namesList);
        }
        request.setAttribute("page",page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followManager.jsp");
        rd.forward(request, response);
    }
    }


