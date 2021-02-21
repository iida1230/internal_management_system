<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
  <c:param name="content">
    <h2>フォロワー　日報詳細ページ</h2>
       <c:choose>
         <c:when test="${follower_report!= null}">
            <table id="report_list">
              <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_departmentName">所属部署</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_good">いいね数</th>
                    <th class="report_action">操作</th>
                </tr>

                <c:forEach var="report" items="${follower_report}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_departmentName"> ${department_name[status.index]}</td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_good"><a href="<c:url value='/reports/goodName?id=${report.id}'/>"><c:out value="${report.good}"/></a></td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                   </tr>
                </c:forEach>
              </tbody>
            </table>
         <div id="pagination">
         （全 ${reports_count} 件）<br />
             <c:forEach var="i" begin="1" end="${((reports_count - 1) / 5) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/followerReport/show?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
         </div>
        </c:when>
       <c:otherwise>
         <h2>フォロワーは日報を投稿していません。</h2>
       </c:otherwise>
     </c:choose>
     <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
   </c:param>
</c:import>
