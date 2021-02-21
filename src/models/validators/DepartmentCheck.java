package models.validators;

import java.util.List;

import javax.persistence.EntityManager;

import models.DepartmentName;
import models.Employee;
import utils.DBUtil;

public class DepartmentCheck {
//部署編集する際のバリデーション
    public static String validate(DepartmentName dn,Employee e,Boolean position_check, Boolean approval_check){
        //編集する従業員の所属部署が何件か確認
        EntityManager em = DBUtil.createEntityManager();
        List<DepartmentName> d = em.createNamedQuery("getMyDepartments",DepartmentName.class)
                                .setParameter("e", e)
                                .getResultList();
        //従業員編集ページから役職を変更する場合
        if(approval_check ){
            for(int i=0 ; i<d.size(); i++){
                //所属している同じ部署から同じ役職の従業員を取得
                long dn1 = em.createNamedQuery("getOtherApproval",Long.class)
                            .setParameter("d",d.get(i).getId())
                            .setParameter("approval",e.getApproval())
                            .getSingleResult();
                //1件以下の場合
                if(dn1<2){
                   em.close();
                   return "所属している部署内に役職が同じ人が居ないため変更出来ません。" ;
                }
            }return "";
        }
        //所属部署が1件の場合
        if(d.size()==1 ){
            em.close();
            return  "所属部署が1件のため編集ができません。";
        }
        //管理職か確認
        if(position_check){
        long getDepartmentsPosition =(long)em.createNamedQuery("getDepartmentsPosition", Long.class)
                                        .setParameter("dn", dn)
                                        .setParameter("approval",e.getApproval())
                                        .getSingleResult();
         em.close();
         if(getDepartmentsPosition < 2) {
           if(e.getApproval()==2){
             return "課長が１人しか居ないため編集できません。";
           }else if(e.getApproval()==3){
             return "部長が１人しか居ないため編集できません。";
           }
         }
        }
        return "";
      }
}
