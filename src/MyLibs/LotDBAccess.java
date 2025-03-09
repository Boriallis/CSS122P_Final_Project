package MyLibs;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotDBAccess {
    //Lists all of the lots
    public List<Lot> getAllLots(){
        List<Lot> lots = new ArrayList<>();
        
        try{
            Connection connect = DBConnect.getConnection();
            Statement statement = connect.createStatement(); 
            ResultSet resultSet = statement.executeQuery("SELECT * FROM lots");
            
            while (resultSet.next()){
                Lot lot = new Lot(
                resultSet.getInt("lot_id"),
                resultSet.getString("location"),
                resultSet.getDouble("size"),
                resultSet.getDouble("price"),
                resultSet.getString("status"),
                resultSet.getInt("owner_id")
                );
                lots.add(lot);
            }
        }
        
        catch (Exception e){
            System.out.println(e);
        }
        
        return lots;
    }
    
    //Sorts everything by price, highest to lowest
    public List<Lot> getLotsSortedByPrice() {
        List<Lot> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY price DESC";  // highest to lowest
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int lotId = rs.getInt("lot_id");
                String location = rs.getString("location");
                double price = rs.getDouble("price");
                double size = rs.getDouble("size");
                String status = rs.getString("status");
                int tempOwner = rs.getInt("owner_id");
                Integer ownerId = rs.wasNull() ? null : tempOwner;
                lots.add(new Lot(lotId, location, price, size, status, ownerId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }

    //Sorts everything by size, lowest to highest
    public List<Lot> getLotsSortedBySize() {
        List<Lot> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY size DESC";  // highest to lowest
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int lotId = rs.getInt("lot_id");
                String location = rs.getString("location");
                double price = rs.getDouble("price");
                double size = rs.getDouble("size");
                String status = rs.getString("status");
                int tempOwner = rs.getInt("owner_id");
                Integer ownerId = rs.wasNull() ? null : tempOwner;
                lots.add(new Lot(lotId, location, price, size, status, ownerId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }
    
    //sorts by size
    public static List<String> getLotsBySizeRange(double minSize, double maxSize) {
    List<String> lots = new ArrayList<>();
    String query = "SELECT * FROM lots WHERE size BETWEEN ? AND ? ORDER BY size ASC";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setDouble(1, minSize);
        stmt.setDouble(2, maxSize);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String lotInfo = String.format(
                    "Lot %d - Location: %s - Price: %.2f - Size: %.2f - Status: %s",
                    rs.getInt("lot_id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getDouble("size"),
                    rs.getString("status")
                );
                lots.add(lotInfo);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lots;
    }
    
    //sorts by block
    public static List<String> getLotsByBlock(int blockNumber) {
    List<String> lots = new ArrayList<>();
    String query = "SELECT * FROM lots WHERE location = ? ORDER BY lot_id ASC";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        // If your database 'location' column literally stores "Block 1", "Block 2", etc.
        stmt.setString(1, "Block " + blockNumber);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String lotInfo = String.format(
                    "Lot %d - Location: %s - Price: %.2f - Size: %.2f - Status: %s",
                    rs.getInt("lot_id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getDouble("size"),
                    rs.getString("status")
                );
                lots.add(lotInfo);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lots;
    }
    
    //search function
    public static List<Lot> searchLots(String block, double minSize, double maxSize, double minPrice, double maxPrice) {
        List<Lot> results = new ArrayList<>();
        String query = "SELECT * FROM lots WHERE location LIKE ? AND size BETWEEN ? AND ? AND price BETWEEN ? AND ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, block + "%");
            stmt.setDouble(2, minSize);
            stmt.setDouble(3, maxSize);
            stmt.setDouble(4, minPrice);
            stmt.setDouble(5, maxPrice);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lot lot = new Lot(
                    rs.getInt("lot_id"),
                    rs.getString("location"),
                    rs.getDouble("price"),
                    rs.getDouble("size"),
                    rs.getString("status"),
                    rs.getInt("owner_id")
                );
                results.add(lot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    
    //Updates lot status, sold or available
    public void updateLotStatus(int id, String status) {
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement ps = connect.prepareStatement("UPDATE lots SET status = ? WHERE id = ?")) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //updates when lot is bought
    public void updateLotStatusAndOwner(int lotId, String newStatus, Integer newOwnerId) {
        String query = "UPDATE lots SET status = ?, owner_id = ? WHERE lot_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

             ps.setString(1, newStatus);
             if(newOwnerId == null) {
                 ps.setNull(2, java.sql.Types.INTEGER);
             } else {
                 ps.setInt(2, newOwnerId);
             }
             ps.setInt(3, lotId);
             ps.executeUpdate();
        } catch (SQLException e) {
             e.printStackTrace();
        }
    }

}
