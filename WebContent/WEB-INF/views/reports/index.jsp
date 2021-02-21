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
         <c:if test="${flushError!= null}">
            <div id="flush_error">
               <c:out value="${flushError}"></c:out>
            </div>
             </c:if>
             <%--承認待ち日報一覧or承認済み日報一覧 --%>
                <c:choose>
                    <c:when test="${approvalTitle != null}">
                      <h2><c:out value="${approvalTitle}"></c:out></h2>
                    </c:when>
                    <c:otherwise>
                        <h2>日報　一覧</h2>
                    </c:otherwise>
                </c:choose>
                        <table id="report_list">
                            <tbody>
                                <tr>
                                    <th class="report_name">氏名</th>
                                    <th class="report_date">日付</th>
                                   <c:if test="${approvalTitle == null}">
                                    <th class="report_departmentName">所属部署</th>
                                   </c:if>
                                    <th class="report_title">タイトル</th>
                                     <c:choose>
                                        <c:when test="${approvalTitle != null}">
                                          <th class="report_approval">承認状況</th>
                                        </c:when>
                                        <c:otherwise>
                                          <th class="report_good">いいね数</th>
                                        </c:otherwise>
                                     </c:choose>
                                   <th class="report_action">操作</th>
                                </tr>
                                 <c:forEach var="report" items="${reports}" varStatus="status">
                                  <tr class="row${status.count % 2}">
                                    <td class="report_name"><c:out value="${report.employee.name}" /></td>
                                    <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                                   <c:if test="${approvalTitle== null}">
                                    <td class="report_departmentName"> ${department_name[status.index]}</td>
                                   </c:if>
                                    <td class="report_title">${report.title}</td>
                                     <c:choose>
                                        <c:when test="${approvalTitle != null}">
                                          <c:choose>
                                            <c:when test="${report.approval==0}">
                                               <td class="report_approval"><a class="report_approvalActive" data-approval="再提出"  href="<c:url value='/approval/show?id=${report.id}'/>">再提出</a></td>
                                            </c:when>
                                            <c:when test="${report.approval==1}">
                                                <td class="report_approval"><a class="report_approvalActive" data-approval="課長承認待ち" href="<c:url value='/approval/show?id=${report.id}'/>">課長承認待ち</a></td>
                                            </c:when>
                                            <c:when test="${report.approval==2}">
                                               <td class="report_approval"><a class="report_approvalActive" data-approval="部長承認待ち" href="<c:url value='/approval/show?id=${report.id}'/>">部長承認待ち</a></td>
                                            </c:when>
                                            <c:otherwise>
                                               <td class="report_approval"><a class="report_approvalActive" data-approval="承認済み" href="<c:url value='/approval/show?id=${report.id}'/>">承認済み</a></td>
                                            </c:otherwise>
                                          </c:choose>
                                        </c:when>
                                      <c:otherwise>
                                        <td class="report_good"><a href="<c:url value='/reports/goodName?id=${report.id}'/>"><c:out value="${report.good}"/></a></td>
                                      </c:otherwise>
                                    </c:choose>
                                  <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                                 </tr>
                                </c:forEach>
                           </tbody>
                       </table>
                        <c:choose>
                            <c:when test="${approvalTitle != null}">
                              <div id="pagination">
                                    （全 ${reports_count} 件）<br />
                                    <c:forEach var="i" begin="1" end="${((reports_count - 1) / 5) + 1}" step="1">
                                        <c:choose>
                                            <c:when test="${i == page}">
                                                <c:out value="${i}" />&nbsp;
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value='/reports/approval?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div id="pagination">
                                    （全 ${reports_count} 件）<br />
                                    <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                                        <c:choose>
                                            <c:when test="${i == page}">
                                                <c:out value="${i}" />&nbsp;
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
          </c:param>
</c:import>

