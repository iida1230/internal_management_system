package controllers.department;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Department;
import models.DepartmentName;
import models.Employee;
import models.validators.DepartmentCheck;
import utils.DBUtil;

/**
 * Servlet implementation class DepartmentEdit
 */
@WebServlet("/department/edit")
public class DepartmentEdit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    //部署編集処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();

        DepartmentName dn =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("id")));

        Employee e =em.find(Employee.class,Integer.parseInt(request.getParameter("department")));

        //管理職を確認
        Boolean position_check = false;
        if(e.getApproval()>1){
            position_check = true;
        }
        //バリデーションチェック
        String error = DepartmentCheck.validate(dn,e, position_check,false);
        if(!error.equals("")) {
        em.close();
        //部署からの編集の場合
        if(request.getParameter("employee")==null){
            request.getSession().setAttribute("error",error);
            request.getSession().setAttribute("dn",dn);
            response.sendRedirect(request.getContextPath() + "/department/new");
            return;
         //従業員管理からの編集の場合
        }else{
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("employee", e);
        request.setAttribute("error", error);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        rd.forward(request, response);
        return;
        }
        //削除の処理
        }else{
        Department  d = em.createNamedQuery("getDepartmentsEdit", Department.class)
                        .setParameter("dn", dn)
                        .setParameter("e", e)
                        .getSingleResult();
            em.getTransaction().begin();
            em.remove(d);
            em.getTransaction().commit();
            em.close();
            if(request.getParameter("employee")==null){
            request.getSession().setAttribute("flush", "登録が完了しました。");
            request.getSession().setAttribute("dn",dn);
            response.sendRedirect(request.getContextPath() + "/department/new");
            return;

            }else{
                request.getSession().setAttribute("flush", "更新が完了しました。");
                request.getSession().removeAttribute("employee_id");
                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
        }
        }




