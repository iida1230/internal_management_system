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
        <c:set var="e" value = "${employee}" ></c:set>
          <h2> <c:out value="${e.name }"/>さんの有給申請記録    有給残日数${e.vacation}</h2>
        <c:if test="${getMyVacation == null}">
         有給申請記録はありません。
        </c:if>
          <c:if test="${getMyVacation != null}">
            <table id="report_list">
             <tbody>
                <tr>
                    <th class="vacation_name">氏名</th>
                    <th class="vacation_created_at">申請日付</th>
                    <th class="vacation_date">休暇日</th>
                    <th class="vacation_stay">確認状況</th>
                    <th class="vacation_action">操作</th>
                </tr>
                 <c:forEach var="getMyVacation" items="${getMyVacation}" varStatus="status">
                   <c:choose>
                     <c:when test="${getMyVacation.stay==5}">
                       <tr class="row${status.count % 2} " ><%--有休消化済みは編集出来ないように設定 --%>
                     </c:when>
                      <c:otherwise>
                       <tr class="row${status.count % 2} newtest" id="newtest" data-option="${status.index}">
                      </c:otherwise>
                   </c:choose>
                     <td>${getMyVacation.employee.name}</td>
                     <td>${getMyVacation.created_at}</td>
                     <td>${getMyVacation.vacation}  </td>
                     <td>
                       <%--休暇状況 --%>
                      <c:choose>
                        <c:when test="${getMyVacation.stay==3 and getMyVacation.updated_at==null}">
                         有給申請中
                        </c:when>
                        <c:when test="${getMyVacation.stay==3 and getMyVacation.vacation<date and getMyVacation.updated_at!=null }">
                         有給休暇消化済み
                        </c:when>
                        <c:when test="${getMyVacation.stay==3}">
                         有給休暇承認済み
                        </c:when>
                        <c:when test="${getMyVacation.stay==4}">
                         有給休暇承認却下
                        </c:when>
                        <c:when test="${getMyVacation.stay==5}">
                         有給休暇消化済み
                        </c:when>
                      </c:choose>
                    </td>
                     <td>
                      <c:choose>
                       <c:when test="${getMyVacation.stay==5 }">
                         消化済み
                       </c:when>
                      <c:otherwise>
                         詳細
                      </c:otherwise>
                     </c:choose>
                    </td>
                  </tr>
                 </c:forEach>
                </tbody>
              </table>

           <div id="pagination">
            （全 ${getMyVacationCount} 件）<br />
            <c:forEach var="i" begin="1" end="${((getMyVacationCount - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/time/vacation?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
      </c:if>
     <%--編集モーダル表示 --%>
             <c:forEach var="getMyVacation" items="${getMyVacation}" varStatus="status">
                   <ul class="modals" >
                     <li class="defaultModal"  data-option="${status.index}" >
                      <div class="modal_show">
                        <div class="close-modal">
                         <i class="fa fa-2x fa-times"></i>
                        </div>
                          <h2>有給申請編集画面</h2>
                            <form   action="<c:url value='/time/vacation' />" method="post">
                             <table>
                               <tbody>
                                <tr>
                                 <th>日付</th>
                                   <td>更新前:<fmt:formatDate value="${getMyVacation.vacation}" pattern="yyyy-MM-dd" /><br>
                                     <input type="date" name="vacation" value="${getMyVacation.vacation}"/>
                                  </td>
                                </tr>
                                <tr>
                                  <th>申請項目</th>
                                   <td>
                                     <label for="update"></label>
                                       <select class="update"name="update">
                                         <option value="0">再提出</option>
                                           <option value="1">提出を取り消す</option>
                                       </select>
                                   </td>
                                </tr>
                                 <tr>
                                   <th>備考</th>
                                   <td>
                                     <label for="remarks">内容</label><br />
                                      <textarea class=report-content name="remarks" rows="2" cols="10">${getMyVacation.remarks}</textarea></td>
                                 </tr>
                                  <tr>
                                    <th>確認状況</th>
                                      <c:choose>
                                        <c:when test="${getMyVacation.updated_at!=null }">
                                         <td>確認済み</td>
                                        </c:when>
                                        <c:otherwise>
                                         <td>未確認</td>
                                        </c:otherwise>
                                      </c:choose>
                                  </tr>
                                 </tbody>
                                </table>
                                <div class="btn_area">
                                 <input type="hidden" name="id" value="${getMyVacation.id}" />
                                 <input type="submit"  value="入力内容を確定する">
                                </div>
                             </form>
                         </div>
                    </li>
                 </ul>
             </c:forEach>
    </c:param>
</c:import>