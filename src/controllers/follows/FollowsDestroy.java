package controllers.follows;

import java.io.IOException;

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
 * Servlet implementation class FollowsDestroy
 */
@WebServlet("/follows/destroy")
public class FollowsDestroy extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsDestroy() {
        super();
        // TODO Auto-generated constructor stub
    }

    //フォロー解除の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Follow f = em.find(Follow.class, Integer.parseInt(request.getParameter("id")));
        Employee e;
        try{
         e=em.find(Employee.class, Integer.parseInt(request.getParameter("employee_id")));
         request.getSession().setAttribute("employee_id",request.getParameter("employee_id"));
      }catch(Exception e2){
            e =(Employee)request.getSession().getAttribute("login_employee");
      }
        Follow de = em.createNamedQuery("getMyFollowID",Follow.class)
                        .setParameter("employee",f.getEmployee())
                        .setParameter("myId",e)
                        .getSingleResult();
        em.getTransaction().begin();
        em.remove(de);       // データ削除
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush","フォロー解除しました。");
        //管理署による解除の場合
        try{
            if(Integer.parseInt(request.getParameter("0"))==0 )
            response.sendRedirect(request.getContextPath() + "/employees/followShow");

        }catch (Exception e2){
        request.getSession().removeAttribute("employee_id");

        response.sendRedirect(request.getContextPath() + "/reports/index");

        }



    }
}
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */



