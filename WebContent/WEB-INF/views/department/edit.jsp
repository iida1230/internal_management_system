<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
    <%--バリデーションによるエラー表示 --%>
     <c:if test="${error != null}">
        <div id="flush_error">
            変更内容にエラーがあります。<br />
            <c:forEach var="errors" items="${error}">
                ・<c:out value="${errors}" /><br />
            </c:forEach>
        </div>
     </c:if>

     <%--変更後の内容のフラッシュメッセージ --%>
     <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
      </c:if>

<%-- 部署新規編集の場合　　課長、部長を最初に選択するように設定 --%>
    <c:set var="dn" value="${dn}"></c:set>
    <div class="department_new">
     <c:choose>
       <c:when test="${getDepartmentList==null }">
           <h2 class="dapartment_heading"> <c:out value="${dn.name}"/>の新規編集画面</h2>
         <%--課長　選択 --%>
         <form  method="GET" action="<c:url value='/department/create'/>">
          <div class="department_selectForm">
            <label for="approval2">課長選択</label><br />
                 <select  name="approval2">
                   <option disabled selected >選択してください</option>
                     <c:forEach var="approval2"  items="${getApproval2}">
                       <option value="${approval2.id}">${approval2.name}</option>
                     </c:forEach>
                 </select>
         </div>
         <%--部長　選択 --%>
         <div class="department_selectForm">
                <label for="approval3">部長選択</label><br />
                  <select  name="approval3">
                    <option disabled selected >選択してください</option>
                      <c:forEach var="approval3"  items="${getApproval3}">
                        <option value="${approval3.id}">${approval3.name}</option>
                      </c:forEach>
                  </select>
              <input type="hidden" name="id" value="${dn.id}" />
              <button type="submit" >追加</button>
         </div>
        </form>
       </c:when>
        <%--部署の編集 --%>
          <c:otherwise>
           <h2 class="dapartment_heading"><c:out value="${dn.name}"/> 編集画面</h2>
           <div class="department_selectForm">
            <form  method="POST" action="<c:url value='/department/edit'/>">
                 <label for="department">${dn.name}の従業員選択</label>
                   <select  name="department">
                     <c:forEach var="getDepartmentList"  items="${getDepartmentList}">
                       <option value="${getDepartmentList.employee.id}">役職：
                          <c:choose>
                            <c:when test="${getDepartmentList.employee.approval==1}"> 一般</c:when>
                             <c:when test="${getDepartmentList.employee.approval==2}"> 課長</c:when>
                             <c:when test="${getDepartmentList.employee.approval==3}"> 部長</c:when>
                          </c:choose>
                             ${getDepartmentList.employee.name}
                       </option>
                      </c:forEach>
                    </select>
               <input type="hidden" name="id" value="${dn.id}" />
               <button type="submit" >削除</button>
            </form>
          </div>
        <%--他部署の従業員追加 --%>
        <div class="department_selectForm">
           <form  method="POST" action="<c:url value='/department/create'/>">
                 <label for="employees">他部署の従業員追加</label>
                   <select  name="employees">
                     <c:forEach var="getOtherDepartments"  items="${getOtherDepartments}">
                        <option value="${getOtherDepartments.id}">役職：
                           <c:choose>
                             <c:when test="${getOtherDepartments.approval==1}"> 一般</c:when>
                             <c:when test="${getOtherDepartments.approval==2}"> 課長</c:when>
                             <c:when test="${getOtherDepartments.approval==3}"> 部長</c:when>
                           </c:choose>
                             ${getOtherDepartments.name}
                         </option>
                     </c:forEach>
                    </select>
             <input type="hidden" name="id" value="${dn.id}" />
             <button type="submit" >追加</button>
          </form>
       </div>
        <div class="department_newFrom">
           <form method="post" action="<c:url value='/department/create'/>">
               <label for="name">部署名変更</label><br />
               <input type="text" name="name" value="${dn.name}" />
                <input type="hidden" name="id" value="${dn.id}" />
                <button type="submit" >作成</button>
          </form>
       </div>
      </c:otherwise>
    </c:choose>
    <br>
     <a  href="<c:url value='/department/new' />">部署選択に戻る </a>
   </div>
    </c:param>
    </c:import>