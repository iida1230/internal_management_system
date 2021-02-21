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
         <%--有給申請 --%>
              <div class="vacation_ul">
                <ul class="vacation_from">
                  <c:choose>
                    <c:when test="${sessionScope.login_employee.vacation==0}">
                      <li>有給残日数が0日のため有給は申請できません。</li>
                     </c:when>
                     <c:otherwise>
                      <li  id="vacation" >休暇を申請する</li>
                     </c:otherwise>
                   </c:choose>
                      <li><a  href="<c:url value="/time/vacation?login_employee=${login_employee.id}"/>">有給申請確認 </a></li>
                 </ul>
                </div>

        <%--現在時取得 --%>
         <script>
            function clock() {
            var myDay = new Array("日","月","火","水","木","金","土");
            var now  = new Date();
            var year = now.getFullYear(); // 年
            var month = now.getMonth()+1; // 月
            var date = now.getDate(); // 日
            var day = now.getDay();
            var hour = now.getHours(); // 時
            var min  = now.getMinutes(); // 分
            var sec  = now.getSeconds(); // 秒
            // 数値が1桁の場合、頭に0を付けて2桁で表示する指定
            if(hour < 10) { hour = "0" + hour; }
            if(min < 10) { min = "0" + min; }
            if(sec < 10) { sec = "0" + sec; }
            var clock2 = year + '年' + month + '月' + date + '日' + '（' + myDay[day] + '曜日）'  + hour + '時' + min + '分' + sec + '秒';
            document . getElementById( 'clock-02' ) . innerHTML= clock2 . toLocaleString();
            // 1000ミリ秒ごとに処理を実効
            window . setTimeout( "clock()", 1000);
            }
            window . onload = clock;
            </script>
             <br><br><br>
             <%--現在時間表示 --%>
               <div class="time" id="clock-02"></div>


             <%--勤務初日の場合 --%>
              <div class="attendance">
                 <c:if test="${getMyStay==null}">
                   <h2><a href="<c:url value='/time/in' />">初出勤</a></h2>
                 </c:if>

                  <c:forEach var="getMyStays" items="${getMyStay}" varStatus="status">
                   <c:set var="stay" value = "<fmt:formatDate value='${getMyStays.time_date}' pattern='yyyy-MM-dd' />" /> <%--勤務日--%>
                   <c:set var="time" value = "<fmt:formatDate value='${time_date}' pattern='yyyy-MM-dd' />" /><%--現在の日時 --%>
                     <c:choose>
                     <%--出勤は１日１回に設定 --%>
                       <c:when test="${stay == time and getMyStays.attendance_at!=null and getMyStays.retired_at!=null}">
                        <h2>勤務お疲れ様でした。 </h2>
                       </c:when>
                       <c:when test="${getMyStays.attendance_at!=null and getMyStays.retired_at!=null  }">
                          <h2> <a href="<c:url value='/time/in?stay=1' />">出勤</a></h2>
                        </c:when>
                        <c:when test="${getMyStays.stay==1}">
                          <h2> <a href="<c:url value='/time/out?stay=0' />">在席にする</a></h2>
                        </c:when>
                        <c:when test="${ getMyStays.stay==0 }">
                          <h2> <a href="<c:url value='/time/out?stay=1' />">離席する</a></h2>
                        </c:when>
                     </c:choose>
                    <%--退勤時--%>
                      <c:choose>
                      <%--出勤ボタンがある時だけ表示 --%>
                        <c:when test="${getMyStays.attendance_at!=null and  getMyStays.retired_at==null }">
                          <h2 id="time-out" >退勤</h2>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </div>

                    <%--勤務表閲覧フォーム --%>
                    <form class="shift" method="POST" action="<c:url value='/time/month?id=${login_employee.id}'  />">
                      勤務表閲覧 <input  type="month" name="month" value="month" required/>
                         <button type="submit" >決定</button>
                    </form>
                    <%--有給申請　モーダル --%>
                   <div class="report-creat-modal" id="vacation-creat">
                    <div class="modal">
                      <div class="close-modal">
                        <i class="fa fa-2x fa-times"></i>
                       </div>
                        <div id="calendar-form">
                          <form method="GET" action="<c:url value='/time/in' />">
                           <label for="vacation">日程を選んでください</label><br />
                            <input  type="date"   name="vacation" value="date"><br /><br />
                             <label for="remarks">備考欄</label><br />
                               <textarea id="report-content" name="remarks" rows="3" cols="20"></textarea>
                                 <br />
                                  <br />
                                <button type="submit">申請する</button>
                           </form>
                         </div>
                        </div>
                       </div>


                    <%--退勤時のモーダル --%>
                    <div class="report-creat-modal" id="time-out-creat">
                     <div class="modal">
                          <div class="close-modal">
                            <i class="fa fa-2x fa-times"></i>
                          </div>
                            <div id="calendar-form">
                              <form method="GET" action="<c:url value='/time/out' />">
                                <label for="remarks">備考欄</label><br />
                                  <textarea id="report-content" name="remarks" rows="3" cols="20"></textarea>
                                    <br />
                                     <br />
                                   <button type="submit">退勤する</button>
                               </form>
                            </div>
                          </div>
                        </div>
                 </c:param>
            </c:import>