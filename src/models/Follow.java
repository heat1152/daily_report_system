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

@Table(name = "follows")
@NamedQueries({

    @NamedQuery(
            name = "getAllFollow",
            query = "SELECT f.follow_id FROM Follow AS f WHERE f.user_id = :login_employee"
            ),
    @NamedQuery(
            name = "getAllFollower",
            query = "SELECT f.user_id FROM Follow AS f WHERE f.follow_id = :login_employee"
            ),
    @NamedQuery(
            name = "getFollowSearch",
            query = "SELECT f FROM Follow AS f WHERE f.user_id = :login_employee AND f.follow_id = :follow_employee"
            ),
    @NamedQuery(
            name = "getAllFollowReports",
            query = "SELECT r FROM Report AS r WHERE r.employee IN (SELECT f.follow_id FROM Follow AS f WHERE f.user_id = :login_employee) "
            ),
    @NamedQuery(
            name = "getFollowReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee IN (SELECT f.follow_id FROM Follow AS f WHERE f.user_id = :login_employee) "
            )
})

@Entity
public class Follow {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Employee user_id;

    @ManyToOne
    @JoinColumn(name = "follow_id",nullable = false)
    private Employee follow_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getUser_id() {
        return user_id;
    }

    public void setUser_id(Employee user_id) {
        this.user_id = user_id;
    }

    public Employee getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(Employee follow_id) {
        this.follow_id = follow_id;
    }

}
