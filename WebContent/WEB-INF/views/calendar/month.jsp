<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
   <h2>${month}月の勤務記録</h2>
          <c:choose>
            <c:when test="${getMyTimeMonth!= null}">
             <table>
                 <tr>
                    <th class="calendar_name">氏名</th>
                    <th class="calendar_date">出勤日</th>
                    <th class="calendar_attendance_at">出勤時間</th>
                    <th class="calendar_retired_at">退勤時間</th>
                    <th class="calendar_stay">勤務状況</th>
                </tr>
                  <c:forEach var="getCalendarDate" items="${getMyTimeMonth}" varStatus="status">
                   <tr class="row${status.count % 2} newtest" >
                     <td> ${getCalendarDate.employee.name}</td>
                      <td>
                       <%--勤務していたら勤務日を表示 --%>
                       <c:choose>
                        <c:when test="${getCalendarDate.time_date!=null }">
                          ${getCalendarDate.time_date}
                        </c:when>
                        <%--勤務状況が休暇なら有給の日時を表示 --%>
                         <c:otherwise>
                           ${getCalendarDate.vacation}
                        </c:otherwise>
                       </c:choose>
                      </td>
                     <td> <fmt:formatDate value='${getCalendarDate.attendance_at}' pattern='HH:mm' /></td>
                     <td><fmt:formatDate value='${getCalendarDate.retired_at}' pattern='HH:mm' /></td>
                     <td>
                      <c:choose>
                       <c:when test="${getCalendarDate.attendance_at!=null and getCalendarDate.retired_at!=null}">
                         勤務終了
                        </c:when>
                        <c:when test="${getCalendarDate.stay== 0 and getCalendarDate.retired_at!=null}">
                         在席中
                        </c:when>
                        <c:when test="${getCalendarDate.stay==1 and getCalendarDate.retired_at!=null}">
                         離席中
                        </c:when>
                        <c:when test="${getCalendarDate.stay==2 }">
                          欠勤
                        </c:when>
                         <c:when test="${getCalendarDate.stay==3 and getCalendarDate.updated_at==null}">
                         有給申請中
                        </c:when>
                        <c:when test="${getCalendarDate.stay==3}">
                         有給休暇承認済み
                        </c:when>
                        <c:when test="${getCalendarDate.stay==4}">
                         有給承認却下
                        </c:when>
                        <c:when test="${getCalendarDate.stay==5}">
                         有給消化済み
                        </c:when>
                       </c:choose>
                     </td>
                  </tr>
                </c:forEach>
           </table>

           <div id="pagination">
            （全 ${getMyTimeMonthCount} 件）<br />
            <c:forEach var="i" begin="1" end="${((getMyTimeMonthCount - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                     <form  name="page"  action="<c:url value='/time/month?page=${i}' />" method="post">
                         <a href="#" onClick="document.page.submit();"><c:out value="${i}" /></a>&nbsp;
                         </form>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
          </div>
         </c:when>
        <c:otherwise>
         <h2>勤務データはありません。</h2>
         <p><a href="<c:url value='/reports/index' />">日報一覧に戻る</a></p>
        </c:otherwise>
      </c:choose>
    </c:param>
    </c:import>