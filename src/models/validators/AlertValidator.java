package models.validators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import models.CalendarTime;
import models.Employee;
import models.Report;
import models.ReportApproval;
import utils.AttendanceUtil;
import utils.DBUtil;
import utils.DepartmentReportUtil;

public class AlertValidator {

    public static List <String> validate(Employee login_employee){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<String> arert = new ArrayList<String>();

        String  day5_NoUpdate_Vacation = validateDay5_NoUpdate_Vacation(login_employee.getAdmin_flag(),login_employee);
        if(!day5_NoUpdate_Vacation.equals("")) {
            arert.add(day5_NoUpdate_Vacation);
        }

        String noUpdate_Work  =validateNoUpdate_Work(login_employee.getAdmin_flag(),login_employee);
        if(!noUpdate_Work.equals("")) {
            arert.add(noUpdate_Work);
        }

        String approval_resubmit=validateApproval_resubmit(login_employee, timestamp);
        if(!approval_resubmit.equals("")) {
            arert.add(approval_resubmit);
        }

        String manager_approval=validateManager_approval(login_employee,login_employee.getApproval(), timestamp);
        if(!manager_approval.equals("")) {
            arert.add(manager_approval);
        }

        String director_approval=validateDirector_approval(login_employee, login_employee.getApproval(),timestamp);
        if(!director_approval.equals("")) {
            arert.add(director_approval);
        }
        String update_vacation=validateUpdate_vacation(login_employee);
        if(!update_vacation.equals("")) {
            arert.add(update_vacation);
        }
        return arert;

    }

    private static String validateDay5_NoUpdate_Vacation(Integer admin_flag, Employee login_employee) {
      //権限が管理者か確認
        if(admin_flag==2){
           EntityManager em = DBUtil.createEntityManager();
            //5日前の日付取得
           Date vacation = new Date(System.currentTimeMillis());
           Calendar vc = Calendar.getInstance();
           vc.add(Calendar.DAY_OF_MONTH,-5);
           vacation = vc.getTime();

           //5日過ぎている同じ部署の未確認の有給申請のがある場合取得
           List<CalendarTime> query_attendance = AttendanceUtil.getAttendance(login_employee,null,em,8,0,vacation);
           em.close();
           //1件以上ならアラート表示
           if(query_attendance.size()!=0){

               return   "5日前に提出された 有給申請が"+query_attendance.size()+"件あります。";
           }
       }

        return "";
    }

    private static String validateNoUpdate_Work(Integer admin_flag, Employee login_employee) {
      //権限が管理者か確認
        if(admin_flag==2){
            EntityManager em = DBUtil.createEntityManager();
            //1か月と10日前の日付取得
            Date update = new Date(System.currentTimeMillis());
            Calendar upCa = Calendar.getInstance();
            upCa.add(Calendar.MONTH, -1);
            upCa.set(Calendar.DATE, 10);
            update = upCa.getTime();

            //1か月と10日過ぎている同じ部署の未確認の勤怠記録がある場合取得
            List<CalendarTime> query_attendance = AttendanceUtil.getAttendance(login_employee,null,em,9,0,update);
            em.close();
            //1件以上ならアラート表示
            if(query_attendance.size()!=0){

                return   "期限内に確認されていない勤怠管理が"+query_attendance.size()+"件あります。";
            }
        }
        return "";
    }

    private static String validateApproval_resubmit(Employee login_employee, Timestamp timestamp) {
        //１日前の日付作成
        EntityManager em = DBUtil.createEntityManager();
        //1日前の再提出されたの日報取得
        List   < ReportApproval> approval0_time =em.createNamedQuery("Heder_Approval0Date",ReportApproval.class)
                                                    .setParameter("time",timestamp)
                                                    .setParameter("employee",login_employee)
                                                    .setFirstResult(0)
                                                    .setMaxResults(1)
                                                    .getResultList();
         em.close();
        if(approval0_time.size()!=0){
            return    "１日前に再提出された 日報が "+approval0_time.size()+"件あります。";
        }
        return "";
    }

    private static String validateManager_approval(Employee login_employee, Integer approval,Timestamp timestamp) {
       //管理職が課長か確認


        if(approval==2){
            List<Report> r=DepartmentReportUtil.getReport(login_employee, approval,timestamp);

            if(r.size()!=0){
                return    "１日前に提出された課長承認待ち 日報が"+r.size()+"件あります。";
            }
        }
        return "";
    }

    private static String validateDirector_approval(Employee login_employee,Integer approval, Timestamp timestamp) {
        // 管理職が部長か確認

        if(approval==3){
            List<Report> r=DepartmentReportUtil.getReport(login_employee, approval,timestamp);
            if(r.size()!=0){
                return    "１日前に提出された部長承認待ち 日報が"+r.size()+"件あります。";
            }
        }

        return "";
    }
    private static String validateUpdate_vacation(Employee login_employee) {
        EntityManager em = DBUtil.createEntityManager();
        List   <Integer> Heder_MyUpVacationId =em.createNamedQuery("Heder_MyUpVacationId",Integer.class)
                                                    .setParameter("employee",login_employee)
                                                    .getResultList();

      //取得数が1件以上なら、アラートを表示
        if(Heder_MyUpVacationId.size()!=0){
            for(int i=0 ; i<Heder_MyUpVacationId.size(); i++){
                CalendarTime c = em.find(CalendarTime.class,Heder_MyUpVacationId.get(i));
               c.setCheckId(1);
               em.getTransaction().begin();
               em.getTransaction().commit();

            }
            em.close();
            return    "有給休暇が "+Heder_MyUpVacationId.size()+"件承認されました。";
            }
        em.close();
        return "";
    }
}
