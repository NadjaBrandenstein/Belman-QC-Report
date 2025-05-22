package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.Log;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LogRepository implements IRepository<Log> {

    private final DBConnection dbConnection;

    public LogRepository() throws IOException {
        this.dbConnection = new DBConnection();
    }

    @Override
    public List<Log> getAll() throws Exception {


        return List.of();
    }


    @Override
    public Log add(Log log) throws Exception {
        String sql = ""
                + "INSERT INTO Log "
                + "(orderItemID, imagePosition, action, username) "
                + "VALUES (?,?,?,?)";

        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(true);
            try (PreparedStatement p = conn.prepareStatement(sql)) {
                if (log.getOrderItemID() != null) {
                    p.setInt(1, log.getOrderItemID());
                } else {
                    p.setNull(1, java.sql.Types.INTEGER);
                }
                p.setString(2, log.getImagePosition());
                p.setString(3, log.getAction());
                p.setString(4, log.getUsername());
                p.executeUpdate();
            }
            return log;
        }
    }

    public List<Log> getByOrderItem(int orderItemID) throws Exception {
        String sql = "SELECT orderItemID, imagePosition, action, username, timestamp "
                + "FROM Log WHERE orderItemID = ?";
        List<Log> out = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, orderItemID);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) {
                    Log l = new Log();
                    l.setOrderItemID(rs.getInt("orderItemID"));
                    l.setImagePosition(rs.getString("imagePosition"));
                    l.setAction(rs.getString("action"));
                    l.setUsername(rs.getString("username"));
                    l.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    out.add(l);
                }
            }
        }
        return out;
    }

    @Override
    public Log update(Log item) throws Exception {
        return null;
    }

    @Override
    public void delete(Log item) throws Exception {

    }
}
