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
 * Servlet implementation class EmployeesUpdateServlet
 */
@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    //従業員アップデート
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));
             //管理署か確認
            Boolean position_check = false;
            if(e.getApproval()>1){
               position_check = true;
            }
             //所属部署を編集する場合
            DepartmentName dn = null;
            if(Integer.parseInt(request.getParameter("department_delete"))!=0){
               dn =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department_delete")));
            }
            //役職を変更する場合
            Boolean approval_check = true;
            int a =Integer.parseInt(request.getParameter("approval"));
              if(e.getApproval()==a || e.getApproval()==1){
                approval_check = false;
              }
            // 現在の値と異なる社員番号が入力されていたら
            // 重複チェックを行う指定をする
            Boolean code_duplicate_check = true;
            if(e.getCode().equals(request.getParameter("code"))) {
                code_duplicate_check = false;
            } else {
                e.setCode(request.getParameter("code"));
            }

            // パスワード欄に入力があったら
            // パスワードの入力値チェックを行う指定をする
            Boolean password_check_flag = true;
            String password = request.getParameter("password");
            if(password == null || password.equals("")) {
                password_check_flag = false;
            } else {
                e.setPassword(
                        EncryptUtil.getPasswordEncrypt(
                                password,
                                (String)this.getServletContext().getAttribute("pepper")
                                )
                        );
            }
            String department = request.getParameter("department");
            e.setName(request.getParameter("name"));
            String vacation = (request.getParameter("vacation"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            e.setDelete_flag(0);
            //編集に不具合が無いかチェック
            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag,vacation,department,dn,position_check,approval_check);
            if(errors.size() > 0) {
                em.close();
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
                rd.forward(request, response);
            //編集可能な場合
            }else{
                //部署追加の場合
                if(Integer.parseInt(request.getParameter("department"))!=0){
                  DepartmentName dn_create =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department")));
                  Department de = new Department();
                  de.setDepartmentName(dn_create);
                  de.setEmployee(e);

                  em.persist(de);
                }
                //所属部署削除の場合
                if(Integer.parseInt(request.getParameter("department_delete"))!=0){
                    DepartmentName department_delete =em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department_delete")));
                    Department  d = em.createNamedQuery("getDepartmentsEdit", Department.class)
                            .setParameter("dn", department_delete)
                            .setParameter("e", e)
                            .getSingleResult();
                    em.remove(d);
                }
                   e.setVacation(Integer.parseInt(vacation));
                   e.setApproval(Integer.parseInt(request.getParameter("approval")));
                   em.getTransaction().begin();
                   em.getTransaction().commit();
                   em.close();
                  request.getSession().setAttribute("flush", "更新が完了しました。");
                  request.getSession().removeAttribute("employee_id");
                if(request.getSession().getAttribute("dn_show") != null) {
                   request.getSession().removeAttribute("dn_show");
                }
                if(request.getSession().getAttribute("No_dn") != null) {
                   request.getSession().removeAttribute("No_dn");
                }
                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
   }

}
