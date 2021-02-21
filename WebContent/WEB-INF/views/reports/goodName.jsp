<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
      <h2><c:out value="${report.employee.name}" />(${report.title})さんの いいね　詳細ページ</h2>
         <table id="report_list">
            <tbody>
                <tr>
                  <th class="goodName_name">いいねした人の名前</th>
                  <th class="goodName_date">いいねした日時</th>
                </tr>
                  <c:forEach var="goodName" items="${goodNames}" varStatus="status">
                      <tr class="row${status.count % 2}">
                       <td class="goodName_name"><c:out value="${goodName.employee.name}" /></td>
                       <td class="goodName_date"><fmt:formatDate value='${goodName.created_at}' pattern='yyyy-MM-dd HH:mm:ss' /></td>
                     </tr>
                 </c:forEach>
            </tbody>
          </table>
         <div id="pagination">
            （全 ${goodNames_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((goodNames_count - 1) / 5) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/reports/goodName?page=${i}&id=${report.id} "/>"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
   </c:param>
</c:import>