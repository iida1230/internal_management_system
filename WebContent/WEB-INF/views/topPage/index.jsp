<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
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

             <%--アラートモーダル --%>
              <c:if test="${arert!=null }">
                 <div class="flush_modalIn">
                     <div id="flush_modal">
                        <div class="modal">
                          <div class="close-modal">
                            <i class="fa fa-2x fa-times"></i>
                          </div>
                            <c:forEach var="arerts" items="${arert}"  varStatus="stetus">
                              ・${arerts}
                               <br>
                           </c:forEach>
                       </div>
                    </div>
                 </div>
              </c:if>



        <h2>日報勤怠管理システムへようこそ</h2>
        <h3>【自分の日報　一覧】</h3>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_good">いいね数</th>
                   <c:if test="${sessionScope.login_employee.approval<3 }">
                    <th class="report_approval">承認状況</th>
                   </c:if>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_good"><a href="<c:url value='/reports/goodName?id=${report.id}'/>"><c:out value="${report.good}"/></a></td>
                         <c:if test="${sessionScope.login_employee.approval<3 }">
                          <c:choose>
                            <c:when test="${report.approval==0}">
                               <td class="report_approval"><a href="<c:url value='/approval/show?id=${report.id}'/>">再提出</a></td>
                            </c:when>
                            <c:when test="${report.approval==1}">
                                <td class="report_approval"><a href="<c:url value='/approval/show?id=${report.id}'/>">課長承認待ち</a></td>
                            </c:when>
                            <c:when test="${report.approval==2}">
                               <td class="report_approval"><a href="<c:url value='/approval/show?id=${report.id}'/>">部長承認待ち</a></td>
                            </c:when>
                            <c:otherwise>
                               <td class="report_approval"><a href="<c:url value='/approval/show?id=${report.id}'/>">承認済み</a></td>
                            </c:otherwise>
                         </c:choose>
                        </c:if>
                       <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                   </tr>
               </c:forEach>
          </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>


    </c:param>
</c:import>