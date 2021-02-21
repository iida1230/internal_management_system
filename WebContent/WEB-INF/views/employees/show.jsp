<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <h2>id : ${employee.id} の従業員情報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>社員番号</th>
                            <td><c:out value="${employee.code}" /></td>
                        </tr>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>有給残日数</th>
                            <td><c:out value="${employee.vacation}" /></td>
                        </tr>
                        <tr>
                            <th>権限</th>
                            <td>
                            <c:set var="k" value="${employee.admin_flag}"/>
                                <c:choose>
                                    <c:when test="${employee.admin_flag == 0}">一般</c:when>
                                     <c:when test="${employee.admin_flag == 1}">部長</c:when>
                                 <c:otherwise>管理者</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                           <th>役職</th>
                           <td>
                           <c:set var="approval" value="${employee.approval}"/>
                                <c:choose>
                                    <c:when test="${employee.approval == 1}">一般</c:when>
                                     <c:when test="${employee.approval == 2}">課長</c:when>
                                 <c:otherwise>部長</c:otherwise>
                                </c:choose>
                           </td>
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
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${employee.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${employee.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                    </tbody>
                </table>

                <p><a href="<c:url value='/employees/edit?id=${employee.id}' />">この従業員情報を編集する</a></p>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/employees/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>