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
                resultSet.getString("status")
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
    public static List<String> getLotsSortedByPrice() {
        List<String> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY price DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lots.add(rs.getInt("lot_id") + " - Location: " + rs.getString("location") + " - Price: " + rs.getDouble("price") + " - Size: " + rs.getDouble("size") + " - Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }

    //Sorts everything by size, lowest to highest
    public static List<String> getLotsSortedBySize() {
        List<String> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY size DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lots.add("Lot " + rs.getInt("lot_id") + " - Price: " + rs.getDouble("price") + " - Size: " + rs.getDouble("size"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }
    
    //Updates lot status, sold or available
    //For future ref, This can be called with updateLotStatus(5, "Sold"); (For ex)
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
}
