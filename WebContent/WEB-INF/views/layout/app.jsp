<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>日報勤怠管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style1.css' />">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    </head>
     <body>
        <div id="wrapper">
            <div id="header">
             <c:choose>
               <c:when test="${sessionScope.login_employee == null}">
                 <h1 class=login_header><a href="<c:url value='/' />">日報勤怠管理システム</a></h1>
               </c:when>
               <c:otherwise>
                <div id="header_menu">
                 <ul class="menu">
                  <li class="menu__single">
                    <c:choose>
                      <c:when test="${Heder_resubmit!=null}">
                         <h1 class=new-active><a href="<c:url value='/' />">日報管理</a></h1> <%--再提出の日報があればレイアウト変更 --%>
                      </c:when>
                      <c:otherwise>
                        <h1><a href="<c:url value='/' />">日報管理</a></h1>
                      </c:otherwise>
                    </c:choose>

                      <ul class="menu__second-level">
                        <li id="report-show">日報投稿</li>
                         <c:if test="${sessionScope.login_employee.approval<3 }">
                          <li> <a href="<c:url value="/reports/approval?review=review"/>">承認待ち日報 </a> </li>
                         </c:if>
                     </ul>
                  </li>
                     <c:if test="${sessionScope.login_employee.admin_flag >0}">
                       <li class="menu__single">
                         <c:choose>
                           <c:when test="${Heder_vacationUpDate!=null}">
                            <h1 class=new-active><a href="<c:url value='/employees/index' />" class="init-bottom">従業員管理</a></h1> <%--同じ部署の従業員が有給申請を提出したら レイアウト変更--%>
                           </c:when>
                           <c:otherwise>
                            <h1> <a href="<c:url value='/employees/index' />" class="init-bottom">従業員管理</a></h1>
                           </c:otherwise>
                         </c:choose>
                          <ul class="menu__second-level">
                           <li>  <a href="<c:url value="/time/management"/>">従業員勤怠管理 </a> </li>
                          </ul>
                       </li>
                     </c:if>

                      <c:if test="${sessionScope.login_employee.approval==2}">
                         <c:choose>
                             <c:when test="${Heder_manager.size() >0}">
                                <li class="new-active"> <a href="<c:url value='/reports/approval?approval=1' />">課長承認待ち新規日報</a></li> <%--同じ部署で課長の承認待ち日報があれが　レイアウト変更 --%>
                             </c:when>
                             <c:otherwise>
                                <li> <a href="<c:url value='/reports/approval?approval=1' />">課長承認待ち新規日報</a></li>
                            </c:otherwise>
                         </c:choose>
                     </c:if>

                       <c:if test="${sessionScope.login_employee.approval==3}">
                          <c:choose>
                            <c:when test="${Heder_director.size() >0}">
                              <li class="new-active"> <a href="<c:url value='/reports/approval?approval=2' />">部長承認待ち新規日報</a></li><%--同じ部署で部長の承認待ち日報があれが　レイアウト変更 --%>
                            </c:when>
                            <c:otherwise>
                              <li> <a href="<c:url value='/reports/approval?approval=2' />">部長承認待ち新規日報</a></li>
                            </c:otherwise>
                          </c:choose>
                       </c:if>

                           <li class="menu__single">
                             <a href="<c:url value='/reports/index' />" class="init-bottom">日報一覧</a>
                              <ul class="menu__second-level">
                                <li>  <a href="<c:url value="/followsReport/show"/>">フォロー中の日報一覧 </a> </li>
                                <li>  <a href="<c:url value="/followerReport/show"/>">フォロワーの日報一覧 </a> </li>
                              </ul>
                           </li>

                         <li class="menu__single">
                          <a href="#" class="init-bottom">フォロー確認</a>
                           <ul class="menu__second-level">
                             <li><a href="<c:url value="/follows/followListShow"/>">フォロー中(<c:out value="${Heder_follows_count}"/>)人</a></li>
                             <li><a href ="<c:url value="/follows/followerListShow"/>">フォロワー(<c:out value="${Heder_follower_count}"/>)人</a></li>
                           </ul>
                         </li>

                         <li class="menu__single">
                           <c:choose>
                             <c:when test="${Heder_MyNoVacation != null}">
                               <h1 class=new-active><a href="<c:url value='/time/new'/>" class="init-bottom">勤怠管理</a></h1><%--提出した有給申請が却下されたらレイアウト変更 --%>
                             </c:when>
                             <c:otherwise>
                               <h1><a href="<c:url value='/time/new'/>" class="init-bottom">勤怠管理</a></h1>
                             </c:otherwise>
                           </c:choose>
                             <ul class="menu__second-level">
                               <li><a href="<c:url value="/calendar/index"/>">勤怠記録</a></li>
                             </ul>
                        </li>
                    </ul>
              </div>
           <c:if test="${sessionScope.login_employee != null}">
             <div id="employee_name">
               <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん
                <a href="<c:url value='/logout' />">ログアウト</a>
             </div>
          </c:if>
          </c:otherwise>
         </c:choose>
        </div>

          <div id="content">
            ${param.content}
          </div>
          <div id="footer">
            by iida.
          </div>
     </div>

     <%--モーダル　日報登録 --%>
          <div class="report-creat-modal" id="report-creat">
             <div class="modal">
               <div class="close-modal">
                <i class="fa fa-2x fa-times"></i>
               </div>
                <div id="report-creat-form">
                 <form method="POST" action="<c:url value='/reports/create' />">
                   <label for="report_date">日付</label><br />
                     <input type="date" name="report_date" value="<fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' />" /> <br /><br />
                   <label for="name">氏名</label><br />　
                     <c:out value="${sessionScope.login_employee.name}" /><br /><br />
                       <c:choose>
                          <c:when test="${sessionScope.login_employee.approval==1}">
                             <input type="hidden" name="approval" value="1" />
                          </c:when>
                          <c:when test="${sessionScope.login_employee.approval==2}">
                             <input type="hidden" name="approval" value="2" />
                          </c:when>
                          <c:otherwise>
                             <input type="hidden" name="approval" value="3" />
                          </c:otherwise>
                        </c:choose>
                          <label for="title">タイトル</label><br />
                          <input type="text" name="title" value="${report.title}" /><br /><br />
                          <label for="content">内容</label><br />
                          <textarea id="report-content" name="content" rows="10" cols="50">${report.content}</textarea><br /><br />
                          <input type="hidden" name="_token" value="${_token}" />
                        <button type="submit">投稿</button>
                   </form>
                </div>
             </div>
           </div>

       <script src="<c:url value='/js/script.js' />"></script>
    </body>
</html>
