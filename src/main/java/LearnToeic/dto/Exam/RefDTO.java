package LearnToeic.dto.Exam;

import LearnToeic.entity.Ref;

public class RefDTO {
    private Integer testId;     // lấy từ RefId
    private Integer refId;      // lấy từ RefId
    private RefType refType;
    private String path;        // file path (đường dẫn ảnh/âm thanh/text)
    private Integer startQ;     // bắt đầu áp dụng từ câu số...
    private Integer endQ;       // kết thúc áp dụng đến câu số...

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
        var id = ref.getId();
        return new RefDTO(
            id != null ? id.getTestId() : null,
            id != null ? id.getRefId()  : null,
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