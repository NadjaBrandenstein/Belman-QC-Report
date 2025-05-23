package dk.easv.belmanqcreport.BE;

import java.time.LocalDateTime;

public class Log {
    private int logID;
    private Integer  orderItemID;
    private String   imagePosition;
    private String   action;
    private String   username;
    private LocalDateTime timestamp;

    public Log(int logID, Integer orderItemID, String imagePosition, String action, String username, LocalDateTime timestamp) {
        this.logID = logID;
        this.orderItemID = orderItemID;
        this.imagePosition = imagePosition;
        this.action = action;
        this.username = username;
        this.timestamp = timestamp;
    }

    public Log() {

    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public Integer getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Integer orderItemID) {
        this.orderItemID = orderItemID;
    }

    public String getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(String imagePosition) {
        this.imagePosition = imagePosition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
