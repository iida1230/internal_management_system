<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
<div class="department_new">


    <h2 class="dapartment_heading">部署選択画面</h2>
  <c:if test="${ getDepartment!=null}" >
  <%--部署選択フォーム --%>
    <div class="department_selectForm">
      <form  method="POST" action="<c:url value='/department/new'/>">
          <label for="department">部署選択</label><br />
             <select  name="department">
              <option disabled selected >選択してください</option>
                <c:forEach var="department"  items="${getDepartment}">
                  <option value="${department.id}">${department.name}</option>
                </c:forEach>
               </select>
             <button type="submit" >編集</button>
        </form>
      </div>
   </c:if>

<%--新規部署作成フォーム --%>
     <div class="department_newFrom">
        <form  method="POST" action="<c:url value='/department/new'/>">
           新規部署作成
             <label for="name"></label><br />
               <input type="text" name="name"  />
               <button type="submit" >作成</button>
        </form>
      </div>
       <br>
  <a href=" <c:url value='/employees/index?'/>">従業員編集画面に戻る</a>
</div>
    </c:param>
    </c:import>