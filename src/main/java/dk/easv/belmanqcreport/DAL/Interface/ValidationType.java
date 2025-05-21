package dk.easv.belmanqcreport.DAL.Interface;

public enum ValidationType {
    APPROVED(1), DENIED(2), AWAITING(3);
    private int id;
    ValidationType(int id) { this.id = id; }
    public int getId() { return id; }
}
