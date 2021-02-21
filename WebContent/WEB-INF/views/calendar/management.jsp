<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:import url="../layout/app.jsp">
 <c:param name="content">
 <%--変更後の内容のフラッシュメッセージ --%>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
<%--検索パターン --%>
   <div id="employee_list">
     <form  method="POST" action="<c:url value='/time/management'  />">
      <div id="app">
        <label><input class="input" type="radio" name="radio" value="1" checked>日付指定なし</label>
        <label><input class="input" type="radio" name="radio" value="2">年月指定</label>
        <label><input class="input" type="radio" name="radio" value="3">年月日指定</label>
        <label><input class="input" type="radio" name="radio" value="4">未確認の勤怠記録</label>
        <label><input class="input" type="radio" name="radio" value="5">承認待ち有給申請</label>
       </div>
         <label for="employees">同部署従業員一覧</label><br />
             <select  class="employees"  name="employees" >
             <option value="0" >従業員を指定しない</option>
               <c:forEach var="employee" items="${e}">
                 <option value="${employee.id}">${employee.code} : ${employee.name}</option>
               </c:forEach>
              </select>
              <input class="month" type="month"   name="month" value="month">
              <input class="date" type="date" name="date" value="date" />
             <button type="submit" >決定</button>
        </form>
       </div>
        <table>
            <tr>
               <th class="name">氏名</th>
               <th class="time_date">
                <c:choose>
                 <%--有給記録の場合 --%>
                 <c:when test="${radio==5}">
                     希望日
                 </c:when>
                 <%--勤怠記録の場合 --%>
                 <c:otherwise>
                     出勤日
                 </c:otherwise>
                </c:choose>
               </th>
               <th class="attendance_at">出勤時間</th>
               <th class="retired_at">退勤時間</th>
               <th class="approval">勤務状況</th>
               <th class="action">確認状況</th>
            </tr>
            <c:forEach var="getCalendarDate" items="${getTimeEmployee}" varStatus="status">
             <tr class="row${status.count % 2} newtest" id="newtest" data-option="${status.index}">
                <td> ${getCalendarDate.employee.name}</td>
                <td>
            <c:choose>
               <c:when test="${getCalendarDate.time_date!=null }">
                     ${getCalendarDate.time_date}
               </c:when>
               <c:otherwise>
                     ${getCalendarDate.vacation}
               </c:otherwise>
            </c:choose>
                </td>
                <td > <fmt:formatDate value='${getCalendarDate.attendance_at}' pattern='HH:mm' /></td>
                <td > <fmt:formatDate value='${getCalendarDate.retired_at}' pattern='HH:mm' /></td>
                <td>
            <c:choose>
                 <c:when test="${getCalendarDate.stay==2 }">
                      欠勤
                    </c:when>
                 <c:when test="${getCalendarDate.attendance_at!=null and getCalendarDate.retired_at!=null}">
                      勤務終了
                 </c:when>
                 <c:when test="${getCalendarDate.stay== 0 and getCalendarDate.retired_at!=null}">
                     在席中
                 </c:when>
                 <c:when test="${getCalendarDate.stay==1 and getCalendarDate.retired_at!=null}">
                     離席中
                 </c:when>
                 <c:when test="${getCalendarDate.stay==3 and getCalendarDate.updated_at==null}">
                      有給申請中
                 </c:when>
                 <c:when test="${getCalendarDate.stay==4 and getCalendarDate.updated_at==null}">
                     有給申請却下
                 </c:when>
                 <c:when test="${getCalendarDate.stay==3}">
                     有給休暇承認済み
                 </c:when>
                 <c:when test="${getCalendarDate.stay==5}">
                     有給休暇消化済み
                    </c:when>
             </c:choose>
                </td>
              <c:choose>
                 <c:when test="${getCalendarDate.updated_at!=null }">
                     <td> 確認済み</td>
                    </c:when>
                  <c:when test="${ getCalendarDate.stay==4 }">
                     <td>申請却下 </td>
                  </c:when>
                  <c:otherwise>
                     <td>   未確認</td>
                  </c:otherwise>
               </c:choose>
                  </tr>
               </c:forEach>
        </table>

        <div id="pagination">
            （全 ${count} 件）<br />
            <c:forEach var="i" begin="1" end="${((count - 1) / 5) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                    <form class="pagination" name="page"  action="<c:url value='/time/management?page=${i}' />" method="post">
                        <a href="#" onClick="document.page.submit();"><c:out value="${i}" /></a>&nbsp;
                        <c:if test="${employees!=0 and employees!=null }">
                            <input type="hidden" name="employees" value="${employees}" />
                            <input type="hidden" name="month" value="" />
                            <input type="hidden" name="date" value="" />
                        </c:if>
                        <c:if test="${month!=null}">
                            <input type="hidden" name="month" value="${month}" />
                            <input type="hidden" name="employees" value="0" />
                            <input type="hidden" name="date" value="" />
                        </c:if>
                        <c:if test="${date!=null}">
                            <input type="hidden" name="date" value="${date}" />
                            <input type="hidden" name="employees" value="0" />
                            <input type="hidden" name="month" value="" />
                        </c:if>

                        <c:if test="${radio!=null }">
                        <input type="hidden" name="radio" value="${radio}" />
                         <input type="hidden" name="employees" value="0" />
                         <input type="hidden" name="date" value="" />
                         <input type="hidden" name="month" value="" />
                        </c:if>
                        </form>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

<%--モーダル --%>
    <c:forEach var="getCalendarDate" items="${getTimeEmployee}" varStatus="status">

              <ul class="modals" >
                   <li class="defaultModal"  data-option="${status.index}" >
                <div class="modal_show">
                    <div class="close-modal">
                        <i class="fa fa-2x fa-times"></i>
                    </div>
                    <h2>編集画面</h2>
               <form   action="<c:url value='/time/management' />" method="post">
                  <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                              <td><c:out value="${getCalendarDate.employee.name}" /></td>
                        </tr>
                          <%--有給申請詳細の場合 --%>
                          <c:choose>
                            <c:when test="${getCalendarDate.stay==3 or getCalendarDate.stay==4}">
                              <tr>
                                <th>提出日時</th>
                                  <td><c:out value="${getCalendarDate.created_at}" /></td>
                             </tr>
                             <tr>
                               <th>希望日</th>
                                <td>更新前:<fmt:formatDate value="${getCalendarDate.vacation}" pattern="yyyy-MM-dd" /><br>
                                  <label  for="time_date"></label>
                                  <input type="date" name="time_date" value="${getCalendarDate.vacation}"/></td>
                            </tr>
                           </c:when>
                          <c:otherwise>
                             <tr>
                               <th>出勤日時</th>
                               <td>更新前:<fmt:formatDate value="${getCalendarDate.time_date}" pattern="yyyy-MM-dd" /><br>
                                 <label  for="time_date"></label>
                                 <input type="date" name="time_date" value="${getCalendarDate.time_date}"/>
                              </td>
                            </tr>
                         </c:otherwise>
                       </c:choose>

                 <c:if test="${getCalendarDate.stay!=3 and getCalendarDate.stay!=4}">
                        <tr>
                            <th>出勤時間</th>
                            <td>
                               <label  for="attendance_at"></label>
                                <c:set var = "attendance_at" value = "${fn:replace(getCalendarDate.attendance_at, ' ', 'T')}"/>
                                更新前:${getCalendarDate.attendance_at}<br>
                                <input type="datetime-local" name="attendance_at" value="${attendance_at}"/>
                            </td>
                        </tr>
                        <tr>
                          <th>退勤時間</th>
                            <td>
                              <label  for="retired_at"></label>
                               更新前:${getCalendarDate.retired_at}<br>
                                <c:choose>
                                 <c:when test="${getCalendarDate.retired_at == null}">
                                   <input type="datetime-local" name="retired_at" value="${attendance_at}"/>
                                 </c:when>
                                 <c:otherwise>
                                   <c:set var = "retired_at" value = "${fn:replace(getCalendarDate.retired_at, ' ', 'T')}"/>
                                   <input type="datetime-local" name="retired_at" value="${retired_at}"/>
                                 </c:otherwise>
                                </c:choose>
                           </td>
                 </c:if>

                     <tr>
                       <%--有給申請の詳細の場合 --%>
                       <c:choose>
                         <c:when test="${getCalendarDate.stay==3 or getCalendarDate.stay==4}">
                           <th>申請項目</th>
                         </c:when>
                       <%--勤怠詳細の場合 --%>
                         <c:otherwise>
                           <th>勤務状況</th>
                         </c:otherwise>
                      </c:choose>
                          <td>更新前:
                             <c:choose>
                                <c:when test="${getCalendarDate.stay==2 }">
                                  欠勤
                                </c:when>
                                <c:when test="${getCalendarDate.attendance_at!=null and getCalendarDate.retired_at!=null}">
                                  勤務終了
                                </c:when>
                                <c:when test="${getCalendarDate.stay== 0 and getCalendarDate.retired_at!=null}">
                                 在席中
                                </c:when>
                                <c:when test="${getCalendarDate.stay==1 and getCalendarDate.retired_at!=null}">
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
                            </c:choose>

                              <c:choose>
                                 <c:when test="${getCalendarDate.stay==3 or getCalendarDate.stay==4 }">
                                   <label for="stay"></label><br />
                                     <select class="stay"name="stay">
                                      <option value="3">承認</option>
                                      <option value="4">未承認</option>
                                    </select>
                                 </c:when>
                                 <c:otherwise>
                                 <label for="stay"></label><br />
                                    <select class="stay"name="stay">
                                      <option value="0">変更なし</option>
                                      <option value="2">欠勤</option>
                                      <option value="3">休暇</option>
                                      <option value="5">削除</option>
                                   </select>
                                </c:otherwise>
                             </c:choose>
                        </td>
                    </tr>
                        <tr>
                            <th>備考</th>
                             <td>
                              <label for="remarks">内容</label><br />
                               <textarea class=report-content name="remarks" rows="5" cols="15">${getCalendarDate.remarks}</textarea>
                            </td>
                        </tr>
                        <tr>
                           <th>確認状況</th>
                             <c:choose>
                                  <c:when test="${getCalendarDate.updated_at!=null }">
                                   <td>確認済み</td>
                                  </c:when>
                                  <c:otherwise>
                                   <td>未確認</td>
                                  </c:otherwise>
                            </c:choose>
                       </tr>
                   </tbody>
               </table>
                <p id="error-message" class="error-message"></p>

                    <div class="btn_area">
                        <c:if test="${employees!=0 and employees!=null }">
                            <input type="hidden" name="employees" value="${employees}" />
                            <input type="hidden" name="month" value="" />
                            <input type="hidden" name="date" value="" />
                        </c:if>
                        <c:if test="${month!=null}">
                            <input type="hidden" name="month" value="${month}" />
                            <input type="hidden" name="employees" value="0" />
                            <input type="hidden" name="date" value="" />
                        </c:if>
                        <c:if test="${date!=null}">
                            <input type="hidden" name="date" value="${date}" />
                            <input type="hidden" name="employees" value="0" />
                            <input type="hidden" name="month" value="" />
                        </c:if>

                        <c:if test="${radio!=null }">
                         <input type="hidden" name="radio" value="${radio}" />
                         <input type="hidden" name="employees" value="0" />
                         <input type="hidden" name="date" value="" />
                         <input type="hidden" name="month" value="" />
                        </c:if>
                         <input type="hidden" name="id" value="${getCalendarDate.id }" />
                         <input type="hidden" name="update" value="update" />
                         <input type="submit"  value="入力内容を確定する">
                      </div>

            </form>
         </div>
        </li>
      </ul>
     </c:forEach>
  </c:param>
</c:import>