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

import models.Employee;
import utils.DBUtil;
import utils.DepartmentUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //従業員取得
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            EntityManager em =DBUtil.createEntityManager();

            int page =1;
            try{
                page=Integer.parseInt(request.getParameter("page"));
            }catch(NumberFormatException e){}
            List <Employee> employees =em.createNamedQuery("getAllEmployees",Employee.class)
                                        .setFirstResult(15 * (page - 1))
                                        .setMaxResults(15)
                                        .getResultList();

           Long employees_count= em.createNamedQuery("getEmployeesCount", Long.class)
                                   .getSingleResult();

           String[] department_name = new String[employees.size()];
           String st_de ="";
           for(int i=0; i<employees.size() ;i++){
               DepartmentUtil.getDepatmentName(employees.get(i), i,1, st_de, department_name);
           }
           em.close();
           request.setAttribute("department_name",department_name);
           request.setAttribute("employees",employees);
           request.setAttribute("employees_count",employees_count);
           request.setAttribute("page",page);
           if(request.getSession().getAttribute("flush")!=null){
               request.setAttribute("flush",request.getSession().getAttribute("flush"));
               request.getSession().removeAttribute("flush");
           }
           RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
           rd.forward(request, response);
    }


}
