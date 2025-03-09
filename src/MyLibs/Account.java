package MyLibs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class Account {
    private BigDecimal balance;
    private int user_id;

    public Account(int userId) {
        this.user_id = userId;
        this.balance = fetchBalanceFromDB();
    }
    
    public static Account fromUser(User user) {
        Account account = new Account(user.getId());
        account.balance = BigDecimal.valueOf(user.getBalance());
        return account;
    }


    public BigDecimal getBalance() {
        return balance;
    }
    
    public int getUserId() {
        return user_id;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            updateBalanceInDB();
        }
    }

    
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            // Amount must be positive
            return;
        }
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            updateBalanceInDB();
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }


    //Fetches user balance
    private BigDecimal fetchBalanceFromDB() {
        BigDecimal dbBalance = BigDecimal.ZERO;
        String query = "SELECT balance FROM users WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dbBalance = rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbBalance;
    }

    //Updates user balance
    private void updateBalanceInDB() {
        String query = "UPDATE users SET balance = ? WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBigDecimal(1, balance);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
    //registers a new user
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
    
    //fetches all users
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
