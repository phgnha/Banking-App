package main.java.com.objs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

import javax.swing.JOptionPane;

public class MyJDBC {
    private static String DB_URL = "jdbc:sqlserver://nhanguyen.database.windows.net:1433;database=bankjava;user=nhanguyen@nhanguyen;password=@nha16122004;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static String DB_Username = "nhanguyen";
    private static String DB_Password = "@nha16122004";

    public static void register(String username, String password, String CCCD, String OTP, String phoneNumber, String STK) throws SQLException {
        if (!checkUser(username, CCCD, phoneNumber)) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_Username, DB_Password)) {
                String sql = "INSERT INTO users (username, password, balance, CCCD, OTP, phoneNumber, STK) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.setBigDecimal(3, BigDecimal.ZERO);
                    pstmt.setString(4, CCCD);
                    pstmt.setString(5, OTP);
                    pstmt.setString(6, phoneNumber);
                    pstmt.setString(7, STK);
                    pstmt.executeUpdate();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "User already exists!");
        }
    }

    public static boolean checkUser(String username, String CCCD, String phoneNumber) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_Username, DB_Password)) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR CCCD = ? OR phoneNumber = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, CCCD);
                pstmt.setString(3, phoneNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        }
        return false;
    }

    // Các phương thức CRUD khác...

    // public static User validLogin(String username, String password) throws SQLException {
    //     Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
    //     PreparedStatement ppst = cnt.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
    //     ppst.setString(1, username);
    //     ppst.setString(2, password);
    //     ResultSet rs = ppst.executeQuery();
    //     if (rs.next()) {
    //         int id = rs.getInt("id");
    //         String CCCD = rs.getString("CCCD");
    //         String OTP = rs.getString("OTP");
    //         String phoneNumber = rs.getString("phoneNumber");
    //         BigDecimal currentBalance = rs.getBigDecimal("balance");
    //         String STK = rs.getString("STK");
    //         return new User(id, username, password, currentBalance, CCCD, OTP, phoneNumber, STK);
    //     } else {
    //         return null;
    //     }
    // }

    public static boolean resetPassword(String CCCD, String phoneNumber, String newPassword) throws SQLException {
        Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
        PreparedStatement ppst = cnt.prepareStatement("SELECT * FROM users WHERE CCCD=? AND phoneNumber=?");
        ppst.setString(1, CCCD);
        ppst.setString(2, phoneNumber);
        ResultSet rs = ppst.executeQuery();
        if (!rs.next()) {
            return false;
        }
        int id = rs.getInt("id");
        PreparedStatement ppst1 = cnt.prepareStatement("UPDATE users SET password=? WHERE id=?");
        ppst1.setString(1, newPassword);
        ppst1.setInt(2, id);
        ppst1.executeUpdate();
        return true;
    }

    // public static ArrayList<Transaction> getPastTransaction(User user) {
    //     ArrayList<Transaction> pastList = new ArrayList<>();
    //     try {
    //         Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
    //         PreparedStatement ppst = cnt.prepareStatement("SELECT * FROM transactions WHERE user_id=? ORDER BY trans_date DESC");
    //         ppst.setInt(1, user.getId());
    //         ResultSet rs = ppst.executeQuery();
    //         while (rs.next()) {
    //             Transaction transaction = new Transaction(user.getId(), rs.getString("trans_type"), rs.getBigDecimal("trans_amount"), rs.getDate("trans_date"));
    //             pastList.add(transaction);
    //         }
    //     } catch (SQLException e) {

    //     }
    //     return pastList;
    // }
    public static List<Transaction> getPastTransactions(Long userId) {
    List<Transaction> transactions = new ArrayList<>();
    try {
        URL url = new URL("http://localhost:8081/api/transactions/history/" + userId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Transaction transaction = new Transaction(
                    0,
                    json.getString("trans_type"),
                    json.getBigDecimal("trans_amount"),
                    Date.valueOf(json.getString("trans_date"))
                );
                transactions.add(transaction);
            }
        } else {
            System.err.println("Failed to get transactions: " + responseCode);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return transactions;
}

    // public static boolean addTransaction(Transaction transaction) {
    //     try {
    //         Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
    //         PreparedStatement ppst = cnt.prepareStatement("INSERT INTO transactions(user_id, trans_type, trans_amount, trans_date) VALUES(?,?,?,GETDATE())");
    //         ppst.setInt(1, transaction.getUserId());
    //         ppst.setString(2, transaction.getTrans_type());
    //         ppst.setBigDecimal(3, transaction.getTrans_amount());
    //         ppst.executeUpdate();

    //         return true;

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return false;
    // }


    public static boolean addTransaction(Transaction transaction) {
        try {
            JSONObject json = new JSONObject();
            json.put("userId", transaction.getUserId());
            json.put("trans_type", transaction.getTrans_type());
            json.put("trans_amount", transaction.getTrans_amount().toString());
            json.put("trans_date", transaction.getTrans_date().toString());
    
            URL url = new URL("http://localhost:8081/api/transactions/add");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
    
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
    
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public static boolean getBalance(User user){
        try{
            Connection cnt=DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
            PreparedStatement ppst=cnt.prepareStatement("SELECT balance FROM users WHERE id=?");
            ppst.setInt(1, user.getId());
            ResultSet rs = ppst.executeQuery();
            if(rs.next()){
                user.setcurrentBalance(rs.getBigDecimal("balance"));
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // public static boolean getBalance(User user){
    //     try{
    //         URL url = new URL(String.format("http://localhost:8081/api/users/%d/balance",user.getId()));
    //         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //         conn.setRequestMethod("GET");
    //         conn.setRequestProperty("Accept", "application/json");
    //         int code=conn.getResponseCode();
    //         if(code == 200){
    //             BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //             StringBuilder response = new StringBuilder();
    //             String line;  
    //             while ((line = reader.readLine()) != null) {
    //                 response.append(line);
    //             }
    //             JSONObject json = new JSONObject(response.toString());
    //             BigDecimal balance= json.getBigDecimal("balance");
    //             user.setcurrentBalance(balance);
    //             return true;
    //         }

    //     } catch (Exception e){
    //         e.printStackTrace();
    //     }
    //     return false;
    // }


    // public static boolean updateBalance(User user) {
    //     try {
    //         Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
    //         PreparedStatement ppst = cnt.prepareStatement("UPDATE users Set balance=? WHERE id = ?");
    //         ppst.setBigDecimal(1, user.getCurrentBalance());
    //         ppst.setInt(2, user.getId());
    //         ppst.executeUpdate();

    //         return true;
    //     } catch (SQLException e) {}

    //     return false;
    // }


    public static boolean updateBalance(User user) {
        try {
            String urlParameters = "newBalance=" + URLEncoder.encode(user.getCurrentBalance().toString(), "UTF-8");

            URL url = new URL("http://localhost:8081/api/users/" + user.getId() + "/update-balance");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return true;
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean transfer(Long userId, String toUsername, BigDecimal amount) {
    try {
        String urlParameters = "toUsername=" + URLEncoder.encode(toUsername, "UTF-8") + 
                               "&amount=" + URLEncoder.encode(amount.toString(), "UTF-8");

        URL url = new URL("http://localhost:8081/api/users/" + userId + "/transfer");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return true;
        } else {
            return false;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

    // public static boolean transfer(User user, String transferUsername, float amount) {
    //     try {
    //         Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
    //         PreparedStatement ppst = cnt.prepareStatement("SELECT * FROM users WHERE username=?");
    //         ppst.setString(1, transferUsername);
    //         ResultSet rs = ppst.executeQuery();
    //         while (rs.next()) {
    //             int userId = rs.getInt("id");
    //             String password = rs.getString("password");
    //             BigDecimal balance = rs.getBigDecimal("balance");
    //             String CCCD = rs.getString("CCCD");
    //             String phoneNumber = rs.getString("phoneNumber");
    //             String STK = rs.getString("STK");
    //             String OTP = rs.getString("OTP");
    //             User transferUser = new User(userId, transferUsername, password, balance, CCCD, OTP, phoneNumber, STK);
    //             Transaction sentTrans = new Transaction(user.getId(), "Transfer", BigDecimal.valueOf(-amount), null);
    //             Transaction reTrans = new Transaction(userId, "Transfer", BigDecimal.valueOf(amount), null);
    //             //update receiver
    //             transferUser.setcurrentBalance(transferUser.getCurrentBalance().add(BigDecimal.valueOf(amount)));
    //             updateBalance(transferUser);
    //             //update sendUser
    //             user.setcurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(amount)));
    //             updateBalance(user);
    //             //add these Transaction to Database
    //             addTransaction(sentTrans);
    //             addTransaction(reTrans);
    //             return true;
    //         }
    //     } catch (SQLException e) {

    //     }
    //     return false;
    // }

    public static boolean checkOTP(User user, String OTP) {
        try {
            int userId = user.getId();
            Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
            PreparedStatement ppst = cnt.prepareStatement("SELECT * from users WHERE id=?");
            ppst.setInt(1, userId);
            ResultSet rs = ppst.executeQuery();
            if (rs.next()) {
                String otpUser = rs.getString("ÖTP");
                if (OTP.equals(otpUser)) {
                    return true;
                } else {

                    return false;
                }
            }
        } catch (SQLException e) {

        }
        return false;
    }

}
