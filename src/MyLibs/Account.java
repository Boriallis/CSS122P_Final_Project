package MyLibs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private double balance;
    private int userid;

    public Account(int userId) {
        this.userid = userid;
        this.balance = fetchBalanceFromDB();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            updateBalanceInDB();
        }
    }

    //Fetches user balance
    private double fetchBalanceFromDB() {
        double balance = 0.0;
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM users WHERE id = ?")) {
            stmt.setInt(1, userid);
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
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET balance = ? WHERE id = ?")) {
            stmt.setDouble(1, balance);
            stmt.setInt(2, userid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
