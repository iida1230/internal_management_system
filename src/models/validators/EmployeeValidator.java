package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.DepartmentName;
import models.Employee;
import utils.DBUtil;

public class EmployeeValidator {
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag, String vacation, String department,DepartmentName dn, Boolean position_check,Boolean approval_check ) {
        List<String> errors = new ArrayList<String>();

        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")) {
            errors.add(code_error);
        }
        String name_error = _validateName(e.getName());
        if(!name_error.equals("")) {
            errors.add(name_error);
        }
        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")) {
            errors.add(password_error);
        }
        String vacation_error =_validateVacation(vacation);
        if(!vacation_error.equals("")) {
            errors.add(vacation_error);
        }
        String department_error = validateDepartment(department);
        if(!department_error.equals("")) {
            errors.add(department_error);
        }
        String approval_change_error =validateChange(dn,e,position_check,approval_check);
        if(!approval_change_error.equals("")) {
            errors.add(approval_change_error);
        }
        String delete_error=validateDelete(dn,e,position_check,approval_check);
        if(!delete_error.equals("")) {
            errors.add(delete_error);
        }
        return errors;
    }
    //社員番号
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if(code == null || code.equals("")) {
            return "社員番号を入力してください。";
        }

        // すでに登録されている社員番号との重複チェック
        if(code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                                           .setParameter("code", code)
                                             .getSingleResult();
            em.close();
            if(employees_count > 0) {
                return "入力された社員番号の情報はすでに存在しています。";
            }
        }
        return "";
    }
    //社員名の必須入力チェック
    private static String _validateName(String name) {
        if(name == null || name.trim().equals("")) {
            return "氏名を入力してください。";
        }
        return "";
    }
    //パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {
        // パスワードを変更する場合のみ実行
        if(password_check_flag && (password == null || password.equals(""))) {
            return "パスワードを入力してください。";
        }
        return "";
    }
    //有給日数入力チェック
    private static String _validateVacation(String vacation) {
        if(vacation == null || vacation.trim().equals("")) {
            return "有給休暇日数を入力してください。";

        }if (!checkVacation(vacation)) {
            return "半角数字を入力してください。";
        }
        return "";
    }
    //入力された数字が文字では無いかチェック
    private  static boolean checkVacation(String vacation) {
        try {
           Integer.parseInt(vacation);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
    //所属部署の入力をチェック
    private static String validateDepartment(String department) {
        if(department==null){
           return "所属部署を選択してください";
        }
        return "";
    }
    //編集時に所属部署を削除できるかチャック
    private static String validateDelete(DepartmentName dn, Employee e, Boolean position_check,Boolean approval_check) {
        if(dn!=null){
            return   DepartmentCheck.validate(dn, e,position_check,false);
        }
        return "";
    }
    //編集時に役職を変更できるかチェック
    private static String validateChange(DepartmentName dn, Employee e, Boolean position_check, Boolean approval_check) {
       if(approval_check){
        return   DepartmentCheck.validate(dn, e,false,approval_check);
       }
        return "";
    }

 }





