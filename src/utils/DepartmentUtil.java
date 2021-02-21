package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import models.DepartmentName;
import models.Employee;

//所属部署取得
public class DepartmentUtil {
    public static void getDepatmentName ( Employee employee, int i,int page,String st_de,String[] department_name){
        EntityManager em = DBUtil.createEntityManager();
        List<String> d = em.createNamedQuery("getReportDepartment",String.class)
                            .setParameter("e", employee)
                            .getResultList();
        for (int j = 0; j < d.size(); j++) {
            if(j==0){
                st_de = st_de + d.get(j);
            }else{
                st_de = st_de + "、" + d.get(j);
            }
        }
        //日報承認待ち画面のページネーションから遷移の場合
        if(page>1){
            department_name[i-(5*(page-1))]=st_de;

        }else{
            department_name[i]=st_de;
        }
        em.close();
            st_de="";
    }
    //所属部署取得
    public static List<DepartmentName> getMyDepatmentName (Employee employee,EntityManager em){
           return em.createNamedQuery("getMyDepartments",DepartmentName.class)
                     .setParameter("e", employee)
                     .getResultList();

   }
   //所属部署が同じ従業員取得
    public static  List<Employee>  getMyDepatmentEmployee ( Employee employee,List<DepartmentName> dn,EntityManager em ){
        List<Employee> e= new ArrayList<Employee>();
        List<Employee> e1= new ArrayList<Employee>();
        for(int i=0 ; i<dn.size(); i++){

            List<Employee>   ee =em.createNamedQuery("getAllDepartmentEmployees",Employee.class)
              .setParameter("dn",  dn.get(i).getId())
              .getResultList();
            e1.addAll(ee);
       }
        //重複があれば削除
        e = e1.stream().distinct().collect(Collectors.toList());
        //idの昇順に変更
        Comparator<Employee> compare = Comparator.comparing(models.Employee::getId);
        e.sort(compare);

        return e;
    }

}


