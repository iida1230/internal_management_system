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

import models.Department;
import models.DepartmentName;
import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    //新規従業員作成
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
             EntityManager em = DBUtil.createEntityManager();
             Employee e =new Employee();
             DepartmentName dn=null;
             e.setCode(request.getParameter("code"));
             e.setName(request.getParameter("name"));
             String vacation = (request.getParameter("vacation"));
             String department = (request.getParameter("department"));
             e.setPassword(EncryptUtil.getPasswordEncrypt
                     (request.getParameter("password"),
                             (String) getServletContext().getAttribute("pepper")));
             e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
             e.setApproval(Integer.parseInt(request.getParameter("approval")));
             Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                e.setCreated_at(currentTime);
                e.setUpdated_at(currentTime);
                e.setDelete_flag(0);
                //作成時に不具合がないかチェック
                List<String> errors = EmployeeValidator.validate(e, true, true, vacation,department,dn,false,false);
                if(errors.size() > 0) {
                    em.close();
                    request.setAttribute("_token", request.getSession().getId());
                    request.setAttribute("employee", e);
                    request.setAttribute("errors", errors);
                    request.setAttribute("getDepartment", request.getParameter("getDepartment"));
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
                    rd.forward(request, response);
                    return;
                }else{
                    dn =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department")));
                    Department de = new Department();
                    de.setDepartmentName(dn);
                    de.setEmployee(e);
                    e.setVacation(Integer.parseInt(vacation));
                    em.getTransaction().begin();
                    em.persist(e);
                    em.persist(de);
                    em.getTransaction().commit();
                    request.getSession().setAttribute("flush", "登録が完了しました。");
                    em.close();
                    request.getSession().removeAttribute("getDepartment_show");
                    response.sendRedirect(request.getContextPath() + "/employees/index");
                    return;
               }
     }
    }
}
