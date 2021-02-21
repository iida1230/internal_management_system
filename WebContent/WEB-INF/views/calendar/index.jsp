<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
 <c:param name="content">

    <%--指定した月の勤務データ --%>
  <h2 class="calendar_date">${y}年${m}月の勤怠カレンダー</h2>


    <%--前の月、次の月 --%>
        <c:choose>
          <c:when test="${m==1}">
           <a class="last_month" href="<c:url value='/calendar/index?y=${y-1}&m=12' />">先月</a>
          </c:when>
          <c:otherwise>
            <a class="last_month" href="<c:url value='/calendar/index?y=${y}&m=${m-1}' />">先月</a>
          </c:otherwise>
        </c:choose>

        <c:choose>
          <c:when test="${m==12}">
            <a class="next_month" href="<c:url value='/calendar/index?y=${y+1}&m=1' />">翌月</a>
          </c:when>
          <c:otherwise>
            <a class="next_month" href="<c:url value='/calendar/index?y=${y}&m=${m+1}' />">翌月</a>
          </c:otherwise>
         </c:choose>

    <table id="calendar">
       <tr><th class="sunday">日</th><th>月</th><th>火</th><th>水</th><th>木</th><th>金</th><th class="saturday">土</th></tr>
         <tr>
          <c:forEach var="dayLabels" items="${dayLabel}" varStatus="status">
           <td class="calendarDay" id="row${status.count % 7 }" data-option="${dayLabels}" data-month="${m}月"> ${dayLabels}</td>
             <c:if test="${status.count % 7 == 0}">
         </tr>
         <tr>
            </c:if>
          </c:forEach>
         </tr>
    </table>


<%--勤怠記録　モーダル表示 --%>
       <div class="calendar_list">
          <div class="modal_show">
           <h2><div class="day"><div></h2><%--表示している日付 --%>
             <table>
               <div class="close-modal">
                 <i class="fa fa-2x fa-times"></i>
                  </div>
                     <tr>
                       <th class="calendar_name">氏名</th>
                       <th class="calendart_attendance_at">出勤時間</th>
                       <th class="calendar_retired_at">退勤時間</th>
                       <th class="calendar_stay">在席状況</th>
                       <th class="calendar_remarks">備考欄</th>
                       <th class="calendar_action">確認状況</th>
                     </tr>
                      <c:forEach var="getCalendarDate" items="${getCalendar}" varStatus="status">
                        <tr class=" timeDate<fmt:formatDate value='${getCalendarDate.time_date}' pattern='d' />" >
                         <td> ${getCalendarDate.employee.name}</td>
                         <td> <fmt:formatDate value='${getCalendarDate.attendance_at}' pattern='HH:mm' /></td>
                         <td><fmt:formatDate value='${getCalendarDate.retired_at}' pattern='HH:mm' /></td>
                         <td>
                         <%--勤務状況に応じて表示 --%>
                          <c:choose>
                            <c:when test="${getCalendarDate.stay==2 }">
                              欠勤
                            </c:when>
                            <c:when test="${getCalendarDate.attendance_at !=null and getCalendarDate.retired_at!=null}">
                              勤務終了
                            </c:when>
                            <c:when test="${getCalendarDate.stay== 0 }">
                             在席中
                            </c:when>
                            <c:when test="${getCalendarDate.stay==1 }">
                             離席中
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
                            <c:otherwise>
                            有給休暇
                            </c:otherwise>
                         </c:choose>
                      </td>
                        <td> ${getCalendarDate.remarks}</td>
                         <c:choose>
                           <c:when test="${getCalendarDate.updated_at!=null }">
                             <td>確認済み</td>
                           </c:when>
                           <c:otherwise>
                             <td>未確認</td>
                           </c:otherwise>
                         </c:choose>
                     </tr>
                   </c:forEach>
               </table>
             </div>
           </div>
         </c:param>
        </c:import>