package models;

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

@Table(name="department")
@NamedQueries({
        @NamedQuery(name="getDepartment", query = "SELECT dn FROM DepartmentName AS dn ORDER BY dn.id DESC "),
        @NamedQuery(name="getDepartmentList", query = "SELECT d FROM Department AS d WHERE d.departmentName=:dn ORDER BY d.id DESC "),
        @NamedQuery(name="getOtherDepartments",query=" SELECT e FROM Employee AS e WHERE NOT EXISTS(SELECT d.employee FROM Department AS d WHERE   d.employee=e AND d.departmentName=:dn ) AND e.delete_flag<>1 ORDER BY e.id DESC"),
        @NamedQuery(name="getDepartmentsPosition",query=" SELECT COUNT(d) FROM Department AS d WHERE d.departmentName=:dn AND d.employee.approval=:approval"),
        @NamedQuery(name="getDepartmentsEdit",query=" SELECT d FROM Department AS d WHERE d.departmentName=:dn AND d.employee=:e"),
        @NamedQuery(name="getMyDepartments",query=" SELECT d.departmentName FROM Department AS d WHERE d.employee=:e"),
        @NamedQuery(name="getOtherApproval",query=" SELECT COUNT(d) FROM Department AS d  WHERE EXISTS(SELECT e FROM Employee AS e WHERE d.employee=e AND e.approval=:approval)AND d.departmentName.id=:d   "),
        @NamedQuery(name="getReportDepartment",query="SELECT dn.name FROM DepartmentName AS dn WHERE  EXISTS(SELECT d.departmentName FROM Department AS d  WHERE d.departmentName=dn AND d.employee=:e) " ),

})
@Entity
public class Department {
@Id
@Column(name="id")
@GeneratedValue(strategy= GenerationType.IDENTITY)
private Integer id;
@ManyToOne
@JoinColumn(name = "employee_id", nullable = false)
private Employee employee;
@ManyToOne
@JoinColumn(name="department_name", nullable = false)
private DepartmentName departmentName;
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
public DepartmentName getDepartmentName() {
    return departmentName;
}
public void setDepartmentName(DepartmentName departmentName) {
    this.departmentName = departmentName;
}
}
