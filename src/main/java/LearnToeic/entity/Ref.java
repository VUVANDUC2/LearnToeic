package LearnToeic.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "refs")
public class Ref {

    @EmbeddedId
    private RefId id;

    @MapsId("testId")
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

    protected Ref() {}

    // Constructor tiện dụng khi đã có Test (quan hệ cha) + refId
    public Ref(Test test, Integer refId, String refType, String path, Integer start, Integer end) {
        this.test = test;
        this.id = new RefId(test.getTestId(), refId);
        this.refType = refType;
        this.path = path;
        this.start = start;
        this.end = end;
    }

    // Constructor khi bạn đã có sẵn RefId
    public Ref(RefId id, Test test, String refType, String path, Integer start, Integer end) {
        this.id = id;
        this.test = test;
        this.refType = refType;
        this.path = path;
        this.start = start;
        this.end = end;
    }

    public RefId getId() { return id; }
    public void setId(RefId id) { this.id = id; }

    public Test getTest() { return test; }
    public void setTest(Test test) {
        this.test = test;
        if (this.id == null) {
            this.id = new RefId();
        }
        // đồng bộ testId trong khóa kép
        this.id.setTestId(test != null ? test.getTestId() : null);
    }

    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Integer getStart() { return start; }
    public void setStart(Integer start) { this.start = start; }

    public Integer getEnd() { return end; }
    public void setEnd(Integer end) { this.end = end; }
}
