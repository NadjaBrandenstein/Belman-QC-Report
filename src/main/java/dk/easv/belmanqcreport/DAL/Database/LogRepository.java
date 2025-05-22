package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.Log;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    @Override
    public Log update(Log item) throws Exception {
        return null;
    }

    @Override
    public void delete(Log item) throws Exception {

    }
}
