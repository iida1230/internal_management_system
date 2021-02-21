<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">


    <c:choose>
      <c:when test="${ra!= null}">
         <c:forEach var="approval" items="${ra}" varStatus="status">
           <div class="code-review">
            <div class="box">
              <c:choose>
               <c:when test="${approval.title==0}">
                 <h2 class="box-title bg-successng">
                  再提出<div class="created_datetime">
                  提出日時：<fmt:formatDate value='${approval.report.updated_at}' pattern='yyyy-MM-dd HH:mm:ss' /></div></h2>
                </c:when>
                <c:when test="${approval.title==2}">
                   <h2 class="box-title bg-success">
                   課長承認済み<div class="created_datetime">
                   提出日時：<fmt:formatDate value='${approval.report.updated_at}' pattern='yyyy-MM-dd HH:mm:ss' /></div></h2>
                 </c:when>
                <c:otherwise>
                <h2 class="box-title bg-success">
                  承認済み<div class="created_datetime">
                  提出日時：<fmt:formatDate value='${approval.report.updated_at}' pattern='yyyy-MM-dd HH:mm:ss' /></div></h2>
                </c:otherwise>
               </c:choose>

               <div class="box-inner">
                <div class="table-wrap table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th class="col-20">氏名</th>
                        <th class="col-20">タイトル</th>
                        <th class="col-20">提出した日報詳細</th>
                      </tr>
                     </thead>
                       <tbody>
                        <tr>
                          <td class="text-top"><c:out value="${approval.report.employee.name}"/></td>
                          <td class="user-comment"><c:out value="${approval.report.title}"/></td>
                          <td class="user-upload-file"><a href="<c:url value='/reports/show?id=${approval.report.id}' />">詳細を見る</a><br></td>
                        </tr>
                       </tbody>
                    </table>
                  </div>
            <div class="review-wrap row">
             <div class="mentor col-md-2">
              <div class="name">担当者：<c:out value="${approval.employee.name}"/></div>
               <div class="reviewed_datetime">レビュー日時：<fmt:formatDate value='${approval.created_at}' pattern='yyyy-MM-dd HH:mm:ss' /></div>
             </div>
                  <div class="comment col-md-10">
                    <div class="markdown">
                     <h2>コメント</h2>
                       <ul>
                         <li><c:out value="${approval.content}"/></li>
                           </ul>
                            <div class="triangle"></div>
                          </div>
                         </div>
                        </div>
                       </div>
                      </div>
                     </div>
                   </c:forEach>
                  </c:when>
               <c:otherwise>
                  <h2>レビュー待ちです。</h2>
               </c:otherwise>
           </c:choose>
       <div id="pagination">
            （全 ${approval_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((approval_count - 1) / 5) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/approval/show?page=${i}&id=${id}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

  <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
 </c:param>
</c:import>
