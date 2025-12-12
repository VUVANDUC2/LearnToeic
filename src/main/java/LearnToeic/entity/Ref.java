package LearnToeic.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "refs")
public class Ref {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_id")
    private Integer refId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "ref_type", length = 30)
    private String refType;


    @Column(name = "path", columnDefinition = "text")
    private String path;

    @Column(name = "start")
    private Integer start;

    @Column(name = "end")
    private Integer end;

    public Ref(){}

    public Ref(Integer refId, Test test, String refType, String path, Integer start, Integer end) {
        this.refId = refId;
        this.test = test;
        this.refType = refType;
        this.path = path;
        this.start = start;
        this.end = end;
    }

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
