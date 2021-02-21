<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
      <c:choose>
         <c:when test="${follow_namesList!= null}">
           <h2>フォロー中リスト</h2>
             <table>
               <tbody>
                 <tr>
                   <th class="follow_name">氏名</th>
                   <th class="follow_action">操作</th>
                 </tr>
                  <c:forEach var="follow" items="${follow_namesList}"  varStatus="status">
                    <tr class="row${status.count % 2}">
                      <td class="follow_name"><c:out value="${follow.employee.name }"/></td>
                      <td class="follow_action"><a href="<c:url value='/follows/destroy?id=${follow.id }'/>">フォロー解除</a></td>
                    </tr>
                 </c:forEach>
              </tbody>
            </table>

          <div id="pagination">
            （全 ${follows_count} 件）<br />
             <c:forEach var="i" begin="1" end="${((follows_count - 1) / 5) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='//follows/followListShow?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
         </div>
      </c:when>
       <c:otherwise>
        <h2>フォローしている人はいません。</h2>
       </c:otherwise>
    </c:choose>
    <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
   </c:param>
</c:import>