package controllers.follows;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsNamesListShow
 */
@WebServlet("/follows/followListShow")
public class FollowsFollowListShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsFollowListShow() {
        super();
        // TODO Auto-generated constructor stub
    }

    //フォロー確認の処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();
        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //フォローしている従業員取得
        List <Follow> follow_namesList = em.createNamedQuery("getMyAllFollowsName",Follow.class)
                                             .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                             .setFirstResult(5 * (page - 1))
                                             .setMaxResults(5)
                                             .getResultList();
        //0件の場合
        if(follow_namesList.size()==0){
            em.close();
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/namesListShow.jsp");
            rd.forward(request, response);
            return;
        }
        //フォローしている従業員の件数、取得
        long follows_count =em.createNamedQuery("getMyAllFollowsCount",Long.class)
                .setParameter("myId",request.getSession().getAttribute("login_employee"))
                .getSingleResult();
        em.close();
        request.setAttribute("follows_count",follows_count);
        request.setAttribute("follow_namesList",follow_namesList);
        request.setAttribute("page",page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/namesListShow.jsp");
        rd.forward(request, response);


    }

}
