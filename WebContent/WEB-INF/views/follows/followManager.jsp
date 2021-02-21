<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
    <%--変更後の内容のフラッシュメッセージ --%>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <%--選択した従業員のフォローリスト表示 --%>
          <h2>${employee_id.name}さんのフォロー中リスト</h2>
            <c:choose>
               <c:when test="${follow_namesList!= null}">
                 <table>
                   <tbody>
                     <tr>
                      <th class="follow_name">氏名</th>
                      <th class="follow_action">操作</th>
                     <tr>
                       <c:forEach var="follow" items="${follow_namesList}"  varStatus="status">
                        <tr class="row${status.count % 2}">
                         <td class="follow_name"><c:out value="${follow.employee.name }"/></td>
                         <td class="follow_action"><a href="<c:url value='/follows/destroy?id=${follow.id }&employee_id=${employee_id.id}&0=0'/>">フォロー解除</a></td>
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
                                <a href="<c:url value='/employees/followShow?page=${i}&id=${employee_id.id}' />"><c:out value="${i}" /></a>&nbsp;
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
              </c:when>
            <c:otherwise>
             <h2>フォローしている人はいません。</h2>
            </c:otherwise>
        </c:choose>
        <%--フォロー可能な従業員選択フォーム --%>
          <div id="employee_list">
           <form  method="POST" action="<c:url value='/follows/create?employee_id=${employee_id.id}'  />">
            <label for="employees">フォロー可能な従業員一覧</label><br />
             <select  class="employees"  name="employees" >
              <option disabled selected >選択してください</option>
                <c:forEach var="employee" items="${followable}">
                  <option value="${employee.id}">${employee.code} : ${employee.name}</option>
                </c:forEach>
             </select>
            <button type="submit" >フォローする</button>
           </form>
        </div>
      <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
    </c:param>
</c:import>