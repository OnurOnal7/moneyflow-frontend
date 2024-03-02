package ta4_1.MoneyFlow_Backend.Expenses;

import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

/**
 * @author Kemal Yavuz
 */

@Entity
public class Expenses {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private double personal;
    private double work;
    private double home;
    private double other;

    public Expenses() {
    }

    public Expenses(double personal, double work, double home, double other) {
        this.personal = personal;
        this.work = work;
        this.home = home;
        this.other = other;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPersonal() {
        return personal;
    }

    public void setPersonal(double personal) {
        this.personal = personal;
    }

    public double getWork() {
        return work;
    }

    public void setWork(double work) {
        this.work = work;
    }

    public double getHome() {
        return home;
    }

    public void setHome(double home) {
        this.home = home;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }
}
