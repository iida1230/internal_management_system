package controllers.department;

import java.io.IOException;
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
import utils.DBUtil;

/**
 * Servlet implementation class DepartmentNew
 */
@WebServlet("/department/new")
public class DepartmentNew extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentNew() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //部署選択の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                EntityManager em = DBUtil.createEntityManager();
                //部署取得
                List <DepartmentName>  getDepartment= em.createNamedQuery("getDepartment",DepartmentName.class)
                                                        .getResultList();
                if(getDepartment.size()!=0){
                    request.setAttribute("getDepartment",getDepartment);
                }
                if(request.getSession().getAttribute("flush") != null) {
                    request.setAttribute("flush", request.getSession().getAttribute("flush"));
                    request.getSession().removeAttribute("flush");
                }
                if(request.getSession().getAttribute("error") != null) {
                    request.setAttribute("error", request.getSession().getAttribute("error"));
                    request.getSession().removeAttribute("error");
                }
                //部署の従業員を編集する場合
                if(request.getSession().getAttribute("dn")!=null){
                    request.setAttribute("dn",request.getSession().getAttribute("dn"));
                    DepartmentName dn  =(DepartmentName) request.getSession().getAttribute("dn");
                    request.getSession().removeAttribute("dn");
                   //部署一覧取得
                    List <Department> getDepartmentList = em.createNamedQuery("getDepartmentList",Department.class)
                                                            .setParameter("dn", dn)
                                                            .getResultList();
                    request.setAttribute("getDepartmentList",getDepartmentList);
                    //編集している部署に所属していない従業員取得
                    List <Employee> getOtherDepartments = em.createNamedQuery("getOtherDepartments",Employee.class)
                                                            .setParameter("dn", dn)
                                                            .getResultList();
                    request.setAttribute("getOtherDepartments",getOtherDepartments);
                    em.close();
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/department/edit.jsp");
                    rd.forward(request, response);
                    return;
                }
                //取得した部署をセットしjspへ
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/department/new.jsp");
                    rd.forward(request, response);
                    return;
                    }
    //部署編集の処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                  EntityManager em = DBUtil.createEntityManager();
                  //部署編集の場合
                if(request.getParameter("department")!=null){
                    DepartmentName dn = em.find(DepartmentName.class,Integer.parseInt(request.getParameter("department")));
                    request.getSession().setAttribute("dn",dn);
                    response.sendRedirect(request.getContextPath() + "/department/new");
                    em.close();
                    return;

                //新規部署作成
                }else{
                    //課長、部長取得
                    List <Employee> getApproval2 =  em.createNamedQuery("getApproval2",Employee.class)
                                                        .getResultList();
                    List <Employee> getApproval3 =  em.createNamedQuery("getApproval3",Employee.class)
                                                        .getResultList();

                    request.setAttribute("getApproval2",getApproval2);
                    request.setAttribute("getApproval3",getApproval3);
                    DepartmentName dn =new DepartmentName();
                    dn.setName(request.getParameter("name"));
                    em.getTransaction().begin();
                    em.persist(dn);
                    em.getTransaction().commit();
                    request.setAttribute("dn",dn);
                }
                em.close();
                        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/department/edit.jsp");
                        rd.forward(request, response);
                    }
}
