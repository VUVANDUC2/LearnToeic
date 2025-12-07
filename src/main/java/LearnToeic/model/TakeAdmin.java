package LearnToeic.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "takes")
public class TakeAdmin implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "take_id")
    private Integer takeId;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private UserAdmin user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "test_id")
    private TestAdmin test;

    @Column(name = "attempt_no", nullable = false)
    private Integer attemptNo;

    @Column(name = "start_time")
    private Instant startTime = Instant.now();

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "status")
    private String status = "STARTED";

    @Column(name = "elapsed_seconds")
    private Integer elapsedSeconds = 0;

   @Column(name= "score")
   private Integer score;
   
   @Column(name ="taken_on")
   private LocalDate taken_on;

    public Integer getScore() {
    return score;
}

   public void setScore(Integer score) {
    this.score = score;
   }

   public LocalDate getTaken_on() {
    return taken_on;
   }

   public void setTaken_on(LocalDate taken_on) {
    this.taken_on = taken_on;
   }

    //  Getter & Setter
    public Integer getTakeId() {
        return takeId;
    }

    public void setTakeId(Integer takeId) {
        this.takeId = takeId;
    }

    public UserAdmin getUser() {
        return user;
    }

    public void setUser(UserAdmin user) {
        this.user = user;
    }

    public TestAdmin getTest() {
        return test;
    }

    public void setTest(TestAdmin test) {
        this.test = test;
    }

    public Integer getAttemptNo() {
        return attemptNo;
    }

    public void setAttemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(Integer elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
