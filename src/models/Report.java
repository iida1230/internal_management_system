package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "reports")
@NamedQueries({


    @NamedQuery(name= "getMyAllReports",query = "SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"),
    @NamedQuery(name= "getMyReportsCount",query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"),
    @NamedQuery(name= "getAllReports",query="SELECT r FROM Report AS r WHERE  r.approval >2 ORDER BY r.id DESC"),
    @NamedQuery(name= "getAllReportsCount",query="SELECT COUNT(r) FROM Report AS r WHERE  r.approval >2 "),
    @NamedQuery(name= "getMyApprovalPending",query="SELECT r FROM Report AS r WHERE r.employee=:employee AND r.approval<3 ORDER BY r.id DESC "),
    @NamedQuery(name= "getMyApprovalPendingCount",query="SELECT COUNT(r) FROM Report AS r WHERE r.employee=:employee AND r.approval<3 "),
    @NamedQuery(name= "getApprovalPendingDepartmentReport",query="SELECT r FROM Report AS r WHERE EXISTS(SELECT d FROM Department AS d WHERE r.employee=d.employee AND d.departmentName.id=:dn )AND r.approval=:approval ORDER BY r.updated_at DESC"),
    @NamedQuery(name= "getApprovalPendingDepartmentCount",query="SELECT COUNT(r) FROM Report AS r WHERE EXISTS(SELECT d FROM Department AS d WHERE r.employee=d.employee AND d.departmentName.id=:dn )AND r.approval=:approval "),
    @NamedQuery(name= "Heder_resubmit",query= "SELECT  COUNT(r) FROM Report AS r WHERE r.employee=:employee AND r.approval=0 "),
    @NamedQuery(name= "Heder_Approval1Date",query= "SELECT   r FROM Report AS r WHERE EXISTS(SELECT d FROM Department AS d WHERE r.employee=d.employee AND d.departmentName.id=:dn )AND  r.approval=1 AND r.report_date <:time"),
    @NamedQuery(name= "Heder_Approval2Date",query= "SELECT   r FROM Report AS r WHERE EXISTS(SELECT d FROM Department AS d WHERE r.employee=d.employee AND d.departmentName.id=:dn )AND  r.approval=2 AND r.updated_at  <:time"),
    @NamedQuery(name= "Heder_Approval0Date",query= "SELECT ra FROM ReportApproval AS ra WHERE EXISTS(SELECT r FROM Report AS r WHERE r.id=ra.report.id AND  r.employee=:employee AND r.approval=0 ) AND ra.updated_at <:time ORDER BY ra.id"),

})
@Entity
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "report_date", nullable = false)
    private Date report_date;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name ="good", nullable = true)
    private Integer good =0;
    @Column(name ="approval", nullable = false)
    private Integer approval=1;

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

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getApproval() {
        return approval;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }
}