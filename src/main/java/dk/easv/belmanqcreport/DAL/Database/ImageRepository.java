package dk.easv.belmanqcreport.DAL.Database;

import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.DAL.DBConnection;
import dk.easv.belmanqcreport.DAL.Interface.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository implements IRepository<MyImage> {

    private final DBConnection dbConnection;

    public ImageRepository() throws Exception {
        this.dbConnection = new DBConnection();
    }

    @Override
    public MyImage add(MyImage img) throws SQLException {
        String sql = "INSERT INTO Image (orderID, imagePath, comment) VALUES (?,?,?);";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, img.getOrderItemID());
            stmt.setString(2, img.getImagePath());
            stmt.setString(3, img.getComment());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    img.setImageID(keys.getInt(1));
                } else {
                    throw new Exception("No ID returned after insert.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return img;
    }

    @Override
    public MyImage update(MyImage img)  {
        String sql = "UPDATE Image SET imagePath = ?, comment = ? WHERE imageID = ?;";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, img.getImagePath());
            stmt.setString(2, img.getComment());
            stmt.setInt(3, img.getImageID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    @Override
    public void delete(MyImage img) {
        String sql = "DELETE FROM Image WHERE imageID = ?;";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, img.getImageID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MyImage> getAll() {
        // Return all images, regardless of orderID
        String sql = "SELECT imageID, orderID, imagePath, comment FROM Image;";
        List<MyImage> list = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MyImage img = new MyImage(
                        rs.getInt("imageID"),
                        rs.getString("imagePath"),
                        rs.getString("comment")
                );
                img.setOrderItemID(rs.getInt("orderID"));
                list.add(img);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<MyImage> getImagesByOrderId(int orderID) throws Exception {
        String sql = "SELECT imageID, imagePath, comment FROM Image WHERE orderID = ?;";
        List<MyImage> list = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MyImage img = new MyImage(
                            rs.getInt("imageID"),
                            rs.getString("imagePath"),
                            rs.getString("comment")
                    );
                    img.setOrderItemID(orderID);
                    list.add(img);
                }
            }
        }
        return list;
    }

    public void updateComment(MyImage img) throws Exception {
        String sql = "UPDATE Image SET comment = ? WHERE imageID = ?;";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, img.getComment());
            stmt.setInt(2, img.getImageID());
            stmt.executeUpdate();
        }
    }





}
