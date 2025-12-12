package LearnToeic.dto.Exam;

import LearnToeic.entity.Ref;

public class RefDTO {
    private Integer testId;
    private Integer refId;
    private RefType refType;
    private String path;
    private Integer startQ;
    private Integer endQ;

    public RefDTO() {}

    public RefDTO(Integer testId, Integer refId, RefType refType, String path, Integer startQ, Integer endQ) {
        this.testId = testId;
        this.refId = refId;
        this.refType = refType;
        this.path = path;
        this.startQ = startQ;
        this.endQ = endQ;
    }

    // Factory map từ Entity -> DTO
    public static RefDTO fromEntity(Ref ref) {
        Integer testId = ref.getTest() != null ? ref.getTest().getTestId() : null;
        return new RefDTO(
            testId,
            ref.getRefId(),
            RefType.from(ref.getRefType()),
            ref.getPath(),
            ref.getStart(),
            ref.getEnd()
        );
    }

    /** Câu qn có nằm trong phạm vi áp dụng không? (null = không giới hạn) */
    public boolean appliesTo(Integer qn) {
        if (qn == null) return false;
        boolean okStart = (startQ == null) || (qn >= startQ);
        boolean okEnd   = (endQ   == null) || (qn <= endQ);
        return okStart && okEnd;
    }

    // Getters/Setters
    public Integer getTestId() { return testId; }
    public void setTestId(Integer testId) { this.testId = testId; }
    public Integer getRefId() { return refId; }
    public void setRefId(Integer refId) { this.refId = refId; }
    public RefType getRefType() { return refType; }
    public void setRefType(RefType refType) { this.refType = refType; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public Integer getStartQ() { return startQ; }
    public void setStartQ(Integer startQ) { this.startQ = startQ; }
    public Integer getEndQ() { return endQ; }
    public void setEndQ(Integer endQ) { this.endQ = endQ; }
}
