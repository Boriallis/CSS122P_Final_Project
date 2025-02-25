package MyLibs;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotDBAccess {
    public List<Lot> getAllLots(){
        List<Lot> lots = new ArrayList<>();
        
        try{
            Connection connect = DBConnect.getConnection();
            Statement statement = connect.createStatement(); 
            ResultSet resultSet = statement.executeQuery("SELECT * FROM lots");
            
            while (resultSet.next()){
                Lot lot = new Lot(
                resultSet.getInt("id"),
                resultSet.getString("location"),
                resultSet.getDouble("size"),
                resultSet.getDouble("price"),
                resultSet.getBoolean("status")
                );
                lots.add(lot);
            }
        }
        
        catch (Exception e){
            System.out.println(e);
        }
        
        return lots;
    }
    
    public static List<String> getLotsSortedByPrice() {
        List<String> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY price DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lots.add("Lot " + rs.getInt("id") + " - Price: " + rs.getDouble("price") + " - Size: " + rs.getDouble("size"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }

    public static List<String> getLotsSortedBySize() {
        List<String> lots = new ArrayList<>();
        String query = "SELECT * FROM lots ORDER BY size DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lots.add("Lot " + rs.getInt("id") + " - Price: " + rs.getDouble("price") + " - Size: " + rs.getDouble("size"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lots;
    }
    
    public void updateLotStatus(int id, boolean status){
        try (Connection connect = DBConnect.getConnection();
             PreparedStatement ps = connect.prepareStatement("SELECT * FROM lots")) {

            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
