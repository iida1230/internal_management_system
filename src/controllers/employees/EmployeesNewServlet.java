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
 * Servlet implementation class EmployeesNewServlet
 */
@WebServlet("/employees/new")
public class EmployeesNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //従業員新規作成
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        //所属部署取得
        List <DepartmentName>  getDepartment= em.createNamedQuery("getDepartment",DepartmentName.class)
                .getResultList();

        request.getSession().setAttribute("getDepartment_show",getDepartment);
        request.setAttribute("_token",request.getSession().getId());
        request.setAttribute("employee", new Employee());
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
        rd.forward(request, response);
    }

}
