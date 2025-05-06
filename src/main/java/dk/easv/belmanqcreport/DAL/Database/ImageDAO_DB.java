package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IImage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO_DB implements IImage {

    @Override
    public MyImage createImage(MyImage img) throws Exception {
        String sql = "INSERT INTO Image (orderID, imagePath, comment) VALUES (?,?,?);";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, img.getOrderID());
            stmt.setString(2, img.getImagePath());
            stmt.setString(3, img.getComment());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    img.setImageID(keys.getInt(1));
                }
            }
        }
        return img;
    }

    @Override
    public void updateImage(MyImage img) throws Exception {
        String sql = "UPDATE Image SET imagePath = ?, comment = ? WHERE imageID = ?;";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, img.getImagePath());
            stmt.setString(2, img.getComment());
            stmt.setInt(3, img.getImageID());
            stmt.executeUpdate();
        }
    }
    @Override
    public void updateComment(MyImage img) throws Exception {
        String sql = "UPDATE Image SET comment = ? WHERE imageID = ?;";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, img.getComment());
            stmt.setInt(2, img.getImageID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteImage(int imageID) throws Exception {
        String sql = "DELETE FROM Image WHERE imageID = ?;";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageID);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<MyImage> getImagesForOrder(int orderID) throws Exception {
        String sql = "SELECT imageID, imagePath, comment FROM Image WHERE orderID = ?;";
        List<MyImage> list = new ArrayList<>();
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MyImage img = new MyImage(
                            rs.getInt("imageID"),
                            rs.getString("imagePath"),
                            rs.getString("comment")
                    );
                    img.setOrderID(orderID);
                    list.add(img);
                }
            }
        }
        return list;
    }


}
