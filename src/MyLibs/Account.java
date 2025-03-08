package MyLibs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private double balance;
    private int user_id;

    public Account(int userId) {
        this.user_id = userId;
        this.balance = fetchBalanceFromDB();
    }
    
    public static Account fromUser(User user) {
        // You might want to instantiate an Account using the user's id
        // and then set its balance directly based on the User data.
        Account account = new Account(user.getId());
        account.balance = user.getBalance();  // Override the DB-fetch if appropriate.
        return account;
    }

    public double getBalance() {
        return balance;
    }
    
    public int getUserId() {
        return user_id;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            updateBalanceInDB();
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            updateBalanceInDB();
        } else {
            // Optionally, you can throw an exception or log an error if there's insufficient balance.
            System.out.println("Withdrawal failed: insufficient balance or invalid amount.");
        }
    }


    //Fetches user balance
    private double fetchBalanceFromDB() {
        double balance = 0.0;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM users WHERE user_id = ?")) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    //Updates user balance
    private void updateBalanceInDB() {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET balance = ? WHERE user_id = ?")) {
            stmt.setDouble(1, balance);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean registerUser(String username, String password, double balance) {
        String query = "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setDouble(3, balance);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

             while (rs.next()) {
                 int user_id = rs.getInt("user_id");  // Adjust column names if needed.
                 String username = rs.getString("username");
                 String password = rs.getString("password");
                 double balance = rs.getDouble("balance");

                 users.add(new User(user_id, username, password, balance));
             }
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return users;
    }

}
