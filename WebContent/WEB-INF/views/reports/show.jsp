<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
     <c:if test="${goodFlush != null}">
            <div id="flush_success">
                <c:out value="${goodFlush}"></c:out>

            </div>
             </c:if>
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>所属部署</th>
                            <td>
                             <select  name="department">
                               <c:forEach var="department"  items="${dn}">
                                 <option value="${department.id}">${department.name}</option>
                               </c:forEach>
                             </select>
                        </td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <textarea  rows="8" cols="60"><c:out value="${report.content}" /></textarea>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>いいね数</th>
                            <td><c:out value="${report.good}"/></td>
                        </tr>
                    </tbody>
                </table>
                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                  <c:set var="a" value="1" />
                    <p><a href="<c:url value="/reports/good?goodCount=${a}&id=${report.id}"/>">いいね</a></p>
                </c:if>
                    <p><a href="<c:url value="/reports/goodName?id=${report.id}" />">いいねした人の詳細を見る。</a></p>
                <c:if test="${sessionScope.login_employee.id != report.employee.id}">
                    <p><a href="<c:url value="/follows/create?id=${report.employee.id}"/>">フォローする</a></p>
                </c:if>

                <c:if test="${sessionScope.login_employee.id == report.employee.id}">
                    <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>
