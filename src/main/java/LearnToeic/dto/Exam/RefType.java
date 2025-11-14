package LearnToeic.dto.Exam;

public enum RefType  {
    IMAGE, AUDIO, TEXT;

    public static RefType from(String s) {
        if (s == null) return null;
        switch (s.trim().toUpperCase()) {
            case "IMAGE": case "IMG": case "HINH": return IMAGE;
            case "AUDIO": case "SOUND": case "AMTHANH": return AUDIO;
            case "TEXT": case "VANBAN": return TEXT;
            default: return null;
        }
    }
}
