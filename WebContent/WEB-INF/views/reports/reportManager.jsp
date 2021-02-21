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


             <c:if test="${DepartmentNameList!=null}">
                承認待ち所属部署一覧<br>
               <c:forEach var="department"  items="${DepartmentNameList}">
                ・${department.name}<br>
               </c:forEach>
             </c:if>
               <c:choose>
                 <c:when test="${approval == 1}">
                    <h2>課長承認待ち日報一覧</h2>
                 </c:when>
                 <c:otherwise>
                   <h2>部長承認待ち日報一覧</h2>
                </c:otherwise>
              </c:choose>

             <c:choose>
                   <c:when test="${approval!= null}">
                    <table id="report_list">
                     <tbody>
                       <tr>
                        <th class="report_name">氏名</th>
                        <th class="report_date">日付</th>
                        <th class="report_departmentName">所属部署</th>
                        <th class="report_title">タイトル</th>
                        <th class="report_approval">承認状況</th>

                      </tr>
                       <c:forEach var="report" items="${reports}" varStatus="status">
                          <tr class="row${status.count % 2} newtest"  data-option="${status.index}">
                                 <td class="report_name"><c:out value="${report.employee.name}" /></td>
                             <c:choose>
                               <c:when test="${report.created_at==report.updated_at }">
                                 <td class="report_date"><fmt:formatDate value='${report.created_at}' pattern='yyyy-MM-dd HH:mm:ss' /></td>
                               </c:when>
                               <c:otherwise>
                                 <td class="report_date"><fmt:formatDate value='${report.updated_at}' pattern='yyyy-MM-dd HH:mm:ss' /></td>
                               </c:otherwise>
                             </c:choose>
                                 <td class="report_departmentName"> ${department_name[status.index]}</td>
                                 <td class="report_title"><c:out value="${report.title}"/></td>
                                 <td class="report_app"><a href="#">詳細を見る</a></td>
                           </tr>
                      </c:forEach>
                     </tbody>
                    </table>

                             <div id="pagination">
                                （全 ${count} 件）<br />
                                <c:forEach var="i" begin="1" end="${((count - 1) / 5) + 1}" step="1">
                                    <c:choose>
                                        <c:when test="${i == page}">
                                            <c:out value="${i}" />&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='/reports/approval?page=${i}&approval=${approval}' />"><c:out value="${i}" /></a>&nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                        </c:when>
                    <c:otherwise>
                       <h2>承認待ち日報は見つかりませんでした。</h2>
                    </c:otherwise>
            </c:choose>

          <%--フォームモーダル表示 --%>
                <c:forEach var="report" items="${reports}" varStatus="status">
                   <ul class="modals" >
                     <li class="defaultModal"  data-option="${status.index}" >
                      <div class="modal_show">
                         <div class="close-modal">
                           <i class="fa fa-2x fa-times"></i>
                         </div>
                        <h2>日報レビュー</h2>
                          <form  id="formApproval" class="formApproval" action="<c:url value='/approval/create' />" method="post">
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
                                            <th>内容</th>
                                            <td>
                                            <textarea rows="8" cols="60" ><c:out value="${report.content}" /></textarea>
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
                                            <th>所属部署</th>
                                            <td><c:out value=" ${department_name[status.index]}"/></td>
                                        </tr>
                                    </tbody>
                                 </table>

                                 <label for="title"></label><br />
                                  <select class="title"name="title">
                                    <option value="0">再提出</option>
                                    <option value="${report.approval+1}">承認</option>
                                  </select><br />
                                  <p id="error-message" class="error-message"></p>

                                 <label  for="content">コメント</label><br />
                                 <textarea id="report-content"   name="content" rows="8" cols="25"></textarea>
                                    <br /><br />

                           <div class="btn_area">
                             <input type="hidden" name="id" value="${report.id}" />
                             <input type="hidden" name="approval" value="${approval}" />
                               <c:if test="${getDepartment!=null}">
                                  <input type="hidden" name="getDepartment" value="${getDepartment}" />
                                </c:if>
                             <input type="submit"  value="入力内容を確定する">
                           </div>
                      </form>
                   </div>
                 </li>
              </ul>
            </c:forEach>
        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>
