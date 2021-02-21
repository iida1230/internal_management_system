package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Table(name = "time")

@NamedQueries({

    @NamedQuery(name= "getMyAttendance_at",query= "SELECT c.id FROM CalendarTime AS c WHERE  c.employee =:employee AND c.vacation=null  ORDER BY c.id DESC"),
    @NamedQuery(name= "getMyStay",query= "SELECT c FROM CalendarTime AS c WHERE  c.employee =:employee AND c.vacation=null  ORDER BY c.id DESC"),
    @NamedQuery(name= "getCalendar",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.time_month=:time"),
    @NamedQuery(name= "getTimeEmployee",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.employee=:employee AND c.stay<3 ORDER BY c.id DESC"),
    @NamedQuery(name= "getTimeEmployeeMonth",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.employee=:employee  AND c.time_month=:time_month AND c.stay<3 ORDER BY c.id ASC"),
    @NamedQuery(name= "getTimeEmployeeDate",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.employee=:employee  AND c.time_date=:time_date AND c.stay<3  ORDER BY c.id ASC"),
    @NamedQuery(name= "getTimeMonth",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.time_month=:time_month AND c.stay<3 ORDER BY c.id ASC"),
    @NamedQuery(name= "getTimeDate",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.time_date=:time_date AND c.stay<3 ORDER BY c.id ASC"),
    @NamedQuery(name= "getVacation",query= "SELECT c FROM CalendarTime AS c WHERE  c.stay>2 ORDER BY c.id ASC"),
    @NamedQuery(name= "getNotUpdate",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.updated_at=null AND c.stay<3  ORDER BY c.id DESC"),
    @NamedQuery(name= "getNotUpdateVacation",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.updated_at=null AND c.stay>2  ORDER BY c.id ASC"),
    @NamedQuery(name= "getMyVacation",query="SELECT c FROM CalendarTime AS c WHERE c.employee=:employee AND c.stay>2 ORDER BY c.id DESC "),
    @NamedQuery(name= "getMyVacationCount",query="SELECT COUNT(c) FROM CalendarTime AS c WHERE c.employee=:employee AND c.stay>2 ORDER BY c.id DESC "),
    @NamedQuery(name= "getMyTimeMonth",query= "SELECT c FROM CalendarTime AS c WHERE  c.employee=:employee  AND c.time_month=:time_month  AND c.updated_at<>null ORDER BY c.id ASC"),
    @NamedQuery(name= "getMyTimeMonthCount",query= "SELECT COUNT(c) FROM CalendarTime AS c WHERE  c.employee=:employee  AND c.time_month=:time_month  AND c.updated_at<>null "),
    @NamedQuery(name= "getNotUpdateVacationHeder",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.created_at<:time AND c.stay=3  AND c.updated_at=null"),
    @NamedQuery(name= "getNotUpdateDateHeder",query= "SELECT c FROM CalendarTime AS c WHERE EXISTS(SELECT d FROM Department AS d WHERE c.employee=d.employee AND d.departmentName.id=:dn )AND  c.time_date<:time AND c.stay<3 AND c.updated_at=null "),
    @NamedQuery(name= "Heder_MyNoVacation",query= "SELECT COUNT(c) FROM CalendarTime AS c WHERE  c.employee =:employee AND  c.stay=4 "),
    @NamedQuery(name= "Heder_MyUpVacationId",query= "SELECT  c.id FROM CalendarTime AS c WHERE  c.stay=3 AND c.updated_at<>null AND c.employee =:employee AND c.checkId=0"),
    @NamedQuery(name= "Heder_vacationUpDate",query= "SELECT COUNT(c) FROM CalendarTime AS c WHERE c.stay=3  AND c.updated_at=null"),
    @NamedQuery(name= "vacationCount",query= "SELECT c FROM CalendarTime AS c WHERE c.stay=3  AND c.updated_at<>null AND c.vacation<=:vacation_at"),
})

@Entity
public class CalendarTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "time_date", nullable = true)
    private Date time_date;

    @Column(name = "time_month", nullable = true)
    private Integer time_month;

    @Column(name = "attendance_at", nullable = true)
    private Timestamp attendance_at;


    @Column(name = "retired_at", nullable = true)
    private Timestamp retired_at;



    @Column(name = "updated_at", nullable = true)
    private Timestamp updated_at;

    @Column(name ="stay", nullable = true)
    private Integer stay =0;

    @Column(name = "remarks", length = 255, nullable = true)
    private String remarks;

    @Column(name = "vacation",  nullable = true)
    private Date vacation;

    @Column(name = "created_at", nullable = true)
    private Date created_at;

    @Column(name ="checkId", nullable = true)
    private int checkId ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getTime_date() {
        return time_date;
    }

    public void setTime_date(Date time_date) {
        this.time_date = time_date;
    }

    public Timestamp getAttendance_at() {
        return attendance_at;
    }

    public void setAttendance_at(Timestamp attendance_at) {
        this.attendance_at = attendance_at;
    }

    public Timestamp getRetired_at() {
        return retired_at;
    }

    public void setRetired_at(Timestamp retired_at) {
        this.retired_at = retired_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getStay() {
        return stay;
    }

    public void setStay(Integer stay) {
        this.stay = stay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getVacation() {
        return vacation;
    }

    public void setVacation(Date vacation) {
        this.vacation = vacation;
    }

    public Integer getTime_month() {
        return time_month;
    }

    public void setTime_month(Integer time_month) {
        this.time_month = time_month;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }



}
