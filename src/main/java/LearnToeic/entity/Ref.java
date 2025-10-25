package LearnToeic.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "refs")
public class Ref {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refId;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;


    private String refType;

    private String path;

    private Integer start;

    private Integer end;

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
