
package models;

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

@Table(name="goodName")
@NamedQueries({
    @NamedQuery(name="getMyGoodname",query="SELECT eg FROM EmployeeGoodname AS eg WHERE eg.report=:report ORDER BY eg.id DESC "),
    @NamedQuery(name="getMyGoodnameCount",query = "SELECT COUNT(eg) FROM EmployeeGoodname AS eg WHERE eg.report=:report"),
    @NamedQuery(name="registeredGoodName", query = "SELECT COUNT (eg) FROM EmployeeGoodname AS eg WHERE eg.employee=:employee AND eg.report=:report")})
@Entity
public class EmployeeGoodname {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn
    (name = "good_report")
    private Report report;
    @ManyToOne
     @JoinColumn
   (name = "employee_name", nullable = false)
    private Employee employee;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }








}
