package controllers.follows;

import java.io.IOException;
import java.util.ArrayList;
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

/**
 * Servlet implementation class FollowerListShow
 */
@WebServlet("/follows/followerListShow")
public class FollowerListShow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowerListShow() {
        super();
        // TODO Auto-generated constructor stub
    }


  //フォロワーの詳細確認
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        int page;
        //ページネーション
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        //フォロワーを取得
        List <Employee>followerName =em.createNamedQuery("getMyAllFollowerName",Employee.class)
                                        .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                        .setFirstResult(5 * (page - 1))
                                        .setMaxResults(5)
                                        .getResultList();
        //フォロワー件数0の場合
        if(followerName.size()==0){
            em.close();
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followerList.jsp");
            rd.forward(request, response);
            return;
        }
        //フォローしている従業員取得
        List <Employee>followName =em.createNamedQuery("checkMyAllFollowName",Employee.class)
                                        .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                        .getResultList();
        //フォロワーの件数取得
        long follower_count =em.createNamedQuery("getMyAllFollowerCount",Long.class)
                                        .setParameter("myId",request.getSession().getAttribute("login_employee"))
                                        .getSingleResult();

       //jspに送るリスト作成
       List<Boolean> follow_test = new ArrayList<Boolean>();
       //フォロワーをフォローしているか確認しリストに追加
        for(Employee followerCheck :followerName){
            boolean  follow_boolean=followName.contains(followerCheck);
          follow_test.add(follow_boolean);
        }
        request.setAttribute("follow_test",follow_test);
        em.close();
        request.setAttribute("page",page);
        request.setAttribute("follower_count",follower_count);
        request.setAttribute("followerName",followerName);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/followerList.jsp");
        rd.forward(request, response);
    }
    }

