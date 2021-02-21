package controllers.department;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Department;
import models.DepartmentName;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class Create
 */
@WebServlet("/department/create")
public class DepartmentCreate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */ //新規作成　管理職の部長、課長を追加
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        DepartmentName dn =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("id")));
            Department de2 = new Department();
            Employee e2 = em.find(Employee.class,Integer.parseInt(request.getParameter("approval2")));
            de2.setEmployee(e2);
            de2.setDepartmentName(dn);
            Department de3 = new Department();
            Employee e3 = em.find(Employee.class,Integer.parseInt(request.getParameter("approval3")));
            de3.setEmployee(e3);
            de3.setDepartmentName(dn);
            em.getTransaction().begin();
            em.persist(de2);
            em.persist(de3);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "新規部署の登録が完了しました。");
            request.getSession().setAttribute("dn", dn);
            response.sendRedirect(request.getContextPath() + "/department/new");
    }
    //部署に従業員追加
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           EntityManager em = DBUtil.createEntityManager();

           DepartmentName dn =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("id")));
           //部署名を変更する場合
           if(request.getParameter("name")!=null){
               dn.setName(request.getParameter("name"));
               em.getTransaction().begin();
               em.getTransaction().commit();
               em.close();
               request.getSession().setAttribute("flush", "部署名の変更が完了しました。");
               request.getSession().setAttribute("dn",dn);
               response.sendRedirect(request.getContextPath() + "/department/new");
               return;
           }
           Employee e = em.find(Employee.class,Integer.parseInt(request.getParameter("employees")));
            Department de = new Department();
            de.setEmployee(e);
            de.setDepartmentName(dn);
            em.getTransaction().begin();
            em.persist(de);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "登録が完了しました。");
            request.getSession().setAttribute("dn", dn);
            response.sendRedirect(request.getContextPath() + "/department/new");
    }
 }
