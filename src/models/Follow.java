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

@Table(name="Follows")
@NamedQueries({

    @NamedQuery(name= "getMyAllFollowsReport",query= "SELECT r FROM Report AS r, Follow AS f WHERE  f.myId =:employee AND  r.employee.id=f.employee.id AND r.approval=3 ORDER BY r.id DESC"),
    @NamedQuery(name= "getMyAllFollowsReportCount",query= "SELECT COUNT (r) FROM Report AS r, Follow AS f WHERE  f.myId =:employee AND  r.employee.id=f.employee.id AND r.approval=3 ORDER BY r.id DESC"),
    @NamedQuery(name= "checkFollowID",query="SELECT COUNT(f) FROM Follow AS f WHERE f.employee=:employee AND f.myId=:myId "),
    @NamedQuery(name= "getMyAllFollowsName",query="SELECT f FROM Follow AS f WHERE f.myId=:myId ORDER BY f.id DESC"),
    @NamedQuery(name= "getMyAllFollowsCount",query="SELECT COUNT (f) FROM Follow AS f WHERE f.myId=:myId"),
    @NamedQuery(name= "getMyFollowID",query="SELECT f FROM Follow AS f WHERE f.employee=:employee AND f.myId=:myId "),
    @NamedQuery(name= "getMyAllFollowerName",query="SELECT f.myId FROM Follow AS f WHERE f.employee=:myId ORDER BY f.id DESC"),
    @NamedQuery(name= "getMyAllFollowerCount",query="SELECT  COUNT(f) FROM Follow AS f WHERE f.employee=:myId"),
    @NamedQuery(name= "checkMyAllFollowName",query="SELECT f.employee FROM Follow AS f WHERE f.myId=:myId"),
    @NamedQuery(name= "followable",query=" SELECT e FROM Employee AS e WHERE NOT EXISTS(SELECT f.employee FROM Follow AS f WHERE   f.employee.id=e.id AND f.myId=:myId ) AND e <>:myId AND e.delete_flag<>1 ORDER BY e.id DESC"),
    @NamedQuery(name= "getMyAllFollowerReports",query= "SELECT r FROM Report AS r WHERE  EXISTS(SELECT f.myId FROM Follow AS f WHERE f.myId.id=r.employee.id AND f.employee =:myId ) AND r.approval=3 ORDER BY r.id DESC"),
    @NamedQuery(name= "MyAllFollowerReportsCount",query= "SELECT COUNT(r) FROM Report AS r WHERE  EXISTS(SELECT f FROM Follow AS f WHERE f.myId.id=r.employee.id AND f.employee =:myId ) AND r.approval=3  "),
})
@Entity
public class Follow {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
         @JoinColumn
       (name = "follow_name", nullable = false)
        private Employee employee;

        @ManyToOne
        @JoinColumn(name = "myId", nullable = false)
        private Employee myId;


        @Column(name = "created_at")
        private Timestamp created_at;

        @Column(name = "updated_at" )
        private Timestamp updated_at;

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

        public Employee getMyId() {
            return myId;
        }

        public void setMyId(Employee myId) {
            this.myId = myId;
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
}
