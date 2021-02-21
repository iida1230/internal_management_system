package utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import models.CalendarTime;
import models.DepartmentName;
import models.Employee;

public class AttendanceUtil {
    public static List<CalendarTime>getAttendance(Employee e,Employee specified_e, EntityManager em,int pattern, int time_month, java.util.Date time_date){

        List<CalendarTime> CalendarTime = new ArrayList<CalendarTime>();
        List<CalendarTime> CalendarTime1= new ArrayList<CalendarTime>();
        List<CalendarTime> attendance= new ArrayList<CalendarTime>();
        List<DepartmentName>  dn  = DepartmentUtil.getMyDepatmentName(e,em);

        for(int i=0 ; i<dn.size(); i++){
            //未確認の勤怠記録の場合
            if(pattern==1){
               attendance  = em.createNamedQuery("getNotUpdate", CalendarTime.class)
                            .setParameter("dn",  dn.get(i).getId())
                            .getResultList();
               CalendarTime1.addAll(attendance);
            }
            //承認待ち有給申請の場合
            if(pattern==2){
                attendance  = em.createNamedQuery("getNotUpdateVacation", CalendarTime.class)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //従業員のみを指定の場合
            if(pattern==3){
                attendance  = em.createNamedQuery("getTimeEmployee", CalendarTime.class)
                                .setParameter("employee", specified_e)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //従業員＋指定した月の場合
            if(pattern==4){
                attendance  = em.createNamedQuery("getTimeEmployeeMonth", CalendarTime.class)
                                .setParameter("employee", specified_e)
                                .setParameter("time_month", time_month)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //従業員＋日月指定の場合
            if(pattern==5){
                attendance  = em.createNamedQuery("getTimeEmployeeDate", CalendarTime.class)
                                .setParameter("employee", specified_e)
                                .setParameter("time_date", time_date)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //月指定の場合
            if(pattern==6){
                attendance  = em.createNamedQuery("getTimeMonth", CalendarTime.class)
                                .setParameter("time_month", time_month)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //日月指定の場合
            if(pattern==7){
                attendance  = em.createNamedQuery("getTimeDate", CalendarTime.class)
                                .setParameter("time_date", time_date)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //アラート表示　5日過ぎている同じ部署の未確認の有給申請のがある場合取得
            if(pattern==8){
                attendance  = em.createNamedQuery("getNotUpdateVacationHeder", CalendarTime.class)
                                .setParameter("time",time_date)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //アラート表示　1か月と10日過ぎている同じ部署の未確認の勤怠記録がある場合取得
            if(pattern==9){
                Timestamp upTimestamp = new Timestamp(time_date.getTime());
                attendance  = em.createNamedQuery("getNotUpdateDateHeder", CalendarTime.class)
                                .setParameter("time",upTimestamp)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }
            //カレンダーモーダル　同じ部署の勤怠記録取得
            if(pattern==10){
                attendance  = em.createNamedQuery("getCalendar", CalendarTime.class)
                                .setParameter("time",time_month)
                                .setParameter("dn",  dn.get(i).getId())
                                .getResultList();
                CalendarTime1.addAll(attendance);

            }

        }
        //重複があれば削除
        CalendarTime = CalendarTime1.stream().distinct().collect(Collectors.toList());
        //提出の昇順に変更
        Comparator<CalendarTime> compare = Comparator.comparing(models.CalendarTime::getId);
        CalendarTime.sort(compare);


        return CalendarTime;

    }
    //ページネーションの処理
    public static List<CalendarTime>getPagination(List<CalendarTime> c,int page){
        List<CalendarTime> attendance_page = new ArrayList<CalendarTime>();

        if(c.size()>4&&page==1){ //5件以上に場合
            for(int i=0; i<5 ;i++){//表示件数は5件に設定
            attendance_page.add(c.get(i));
        }
            return attendance_page;
        }else if(page>1){
            for(int i=(page-1)*5; i<c.size();i++ ){
                attendance_page.add(c.get(i));
                if(attendance_page.size()==5){  //5件取得でループ終了
                    break;
                }
            }return attendance_page;
        }else{
            return c;
        }



}
}