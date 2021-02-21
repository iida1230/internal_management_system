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
import utils.DepartmentUtil;

/**
 * Servlet implementation class EmployeesShowServlet
 */
@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //従業員詳細の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        //所属している部署を取得
        List <DepartmentName>  dn  = DepartmentUtil.getMyDepatmentName(e,em);

            em.close();
            request.setAttribute("employee", e);
            request.setAttribute("dn", dn);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
            rd.forward(request, response);
        }
}
