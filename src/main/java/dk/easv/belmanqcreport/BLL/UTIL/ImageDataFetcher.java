package dk.easv.belmanqcreport.BLL.UTIL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.belmanqcreport.DAL.DBConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ImageDataFetcher {

    private final DBConnection dbConnection;

    public ImageDataFetcher() throws IOException{
        this.dbConnection = new DBConnection();
        }

    public BufferedImage getImageFromDatabase(int imageID) {
        String sql = "SELECT imagePath FROM [Image] WHERE imageID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1) bind the parameter
            stmt.setInt(1, imageID);

            // 2) then execute
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    try (InputStream bin = rs.getBinaryStream("imagePath")) {
                        return ImageIO.read(bin);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getImageByPathFromDatabase(int imageID) throws SQLException {
        String sql = "SELECT imagePath FROM [Image] WHERE imageID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, imageID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String imagePath = rs.getString("imagePath");
                    File imageFile = new File(imagePath);

                    if (imageFile.exists()) {
                        return ImageIO.read(imageFile);
                    }
                }
                String imagePath = rs.getString("imagePath");
                File imageFile = new File(imagePath);

                if (imageFile.exists()) {
                    return ImageIO.read(imageFile);
            }
        } catch (SQLException | IOException e) {
                e.printStackTrace();
}


        }
        return null;
    }

    public String getCommentByImageID(int imageID) {
        String sql = "SELECT comment FROM [Image] WHERE imageID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            stmt.setInt(1, imageID);
            if (rs.next()) {
                return rs.getString("comment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getOrderIDByImageID(int imageID) {
        String sql = "SELECT orderID FROM [Image] WHERE imageID = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            stmt.setInt(1, imageID);
            if (rs.next()) {
                return rs.getInt("orderID");
            }
        } catch (SQLException e) {
        }
        return -1;
    }
}
