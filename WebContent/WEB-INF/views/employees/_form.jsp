<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<label for="code">社員番号</label><br />
<input type="text" name="code" value="${employee.code}" />
<br /><br />

<label for="name">氏名</label><br />
<input type="text" name="name" value="${employee.name}" />
<br /><br />

<label for="password">パスワード</label><br />
<input type="password" name="password" />
<br /><br />

<label for="vacation">有給残日数（${employee.vacation}）</label><br />
<input type="text" name="vacation" value="${employee.vacation}" />
<br /><br />

<label for="admin_flag">権限</label><br />
<select  name="admin_flag">
    <option value="0"<c:if test="${employee.admin_flag == 0}"> selected</c:if>>一般</option>
    <option value="1"<c:if test="${employee.admin_flag == 1}"> selected</c:if>>部長</option>
    <option value="2"<c:if test="${employee.admin_flag == 2}"> selected</c:if>>管理者</option>

</select>
<br /><br />

<label for="approval">役職</label><br />
<select  name="approval">
    <option value="1"<c:if test="${employee.approval == 1}"> selected</c:if>>一般</option>
    <option value="2"<c:if test="${employee.approval == 2}"> selected</c:if>>課長</option>
    <option value="3"<c:if test="${employee.approval == 3}"> selected</c:if>>部長</option>
</select>
<c:choose>
 <c:when test="${No_dn==null}">
   <label for="department">部署選択</label><br />
      <select  name="department">
        <option disabled selected >選択してください</option>
         <c:forEach var="department"  items="${getDepartment_show}">
           <option value="${department.id}">${department.name}</option>
          </c:forEach>
      </select>
      <br /><br />
 </c:when>
 <c:otherwise>

   <label for="department">所属部署追加</label><br />
        <select  name="department">
         <option value="0">選択しない</option>
            <c:forEach var="department"  items="${No_dn}">
              <option value="${department.id}">${department.name}</option>
            </c:forEach>
         </select>
         <br /><br />

 <label for="deparment_delete">所属部署削除</label>
        <select name="department_delete">
         <option value="0" >選択しない</option>
           <c:forEach var="department"  items="${dn_show}">
             <option value="${department.id}">${department.name}</option>
           </c:forEach>
        </select>
        <br /><br />
 </c:otherwise>
</c:choose>

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿  </button>