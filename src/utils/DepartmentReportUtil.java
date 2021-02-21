package utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import models.DepartmentName;
import models.Employee;
import models.Report;

public class DepartmentReportUtil {

    public static List<Report>getReport(Employee e,int approval, Timestamp timestamp){

    EntityManager em = DBUtil.createEntityManager();
    List <DepartmentName>  dn= em.createNamedQuery("getMyDepartments",DepartmentName.class)
                                .setParameter("e", e)
                                .getResultList();

    //リストの初期化
    List< Report> reports1 = new ArrayList<Report>();
    List<Report> reports= new ArrayList<Report>();

   //所属している部署の日報レビュー待ちリストを取得

    if(dn.size()!=0){
        //日付指定かつ管理職が課長の場合
        if(timestamp!=null && approval==2){
            for(int i=0 ; i<dn.size(); i++){
                List<Report> reports_test  = em.createNamedQuery("Heder_Approval1Date",Report.class)
                                                .setParameter("dn",  dn.get(i).getId())
                                                .setParameter("time",timestamp)
                                                .getResultList();
                reports1.addAll( reports_test);
            }
          //日付指定かつ管理職が部長の場合
        }else if(timestamp!=null && approval==3){
                for(int i=0 ; i<dn.size(); i++){
                    List<Report> reports_test  = em.createNamedQuery("Heder_Approval2Date",Report.class)
                                                    .setParameter("dn",  dn.get(i).getId())
                                                    .setParameter("time",timestamp)
                                                    .getResultList();
                    reports1.addAll( reports_test);
                }
           //ヘッダーのレイアウト変更時
        }else if(timestamp==null && approval==0){
            for(int i=0 ; i<dn.size(); i++){
                List<Report> reports_test  = em.createNamedQuery("getApprovalPendingDepartmentReport",Report.class)
                                                .setParameter("approval", e.getApproval()-1)
                                                .setParameter("dn",  dn.get(i).getId())
                                                .getResultList();
                reports1.addAll( reports_test);
            }
         //管理職ごとの該当する部署の日報レビュー待ちリストを取得
        }else{
            for(int i=0 ; i<dn.size(); i++){
                List<Report> reports_test  = em.createNamedQuery("getApprovalPendingDepartmentReport",Report.class)
                                                .setParameter("approval", approval)
                                                .setParameter("dn",  dn.get(i).getId())
                                                .getResultList();

                reports1.addAll(reports_test);
            }
        }
       //重複があれば削除
       reports = reports1.stream().distinct().collect(Collectors.toList());
       //提出の昇順に変更
       Comparator<Report> compare = Comparator.comparing(models.Report::getId);
       reports.sort(compare);
       em.close();
       return reports;
    }
      em.close();
      return null;
   }
}
