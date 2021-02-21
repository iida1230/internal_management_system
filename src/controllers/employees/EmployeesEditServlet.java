package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DepartmentName;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //従業員編集の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //所属部署を取得
        List <DepartmentName>  dn= em.createNamedQuery("getMyDepartments",DepartmentName.class)
                .setParameter("e", e)
                .getResultList();
        //所属していない部署を取得
        List <DepartmentName>  No_dn= em.createNamedQuery("getMyNoDepartment",DepartmentName.class)
                .setParameter("e", e)
                .getResultList();
        em.close();
        request.setAttribute("employee", e);
        request.setAttribute("_token", request.getSession().getId());
        request.getSession().setAttribute("employee_id", e.getId());

        request.getSession().setAttribute("dn_show", dn);

        request.getSession().setAttribute("No_dn", No_dn);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        rd.forward(request, response);
    }

}
