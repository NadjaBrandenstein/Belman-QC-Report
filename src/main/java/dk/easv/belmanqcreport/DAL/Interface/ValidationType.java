package dk.easv.belmanqcreport.DAL.Interface;

public enum ValidationType {
    APPROVED(1), DENIED(2), AWAITING(3);
    private int id;
    ValidationType(int id) { this.id = id; }
    public int getId() { return id; }

    public static ValidationType fromId(int id) {
        for (ValidationType v : values()) {
            if (v.id == id) return v;
        }
        return AWAITING;
    }

}
