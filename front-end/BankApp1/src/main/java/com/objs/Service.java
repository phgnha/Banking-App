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

public class Service {
    private static String DB_URL = "jdbc:sqlserver://nhanguyen.database.windows.net:1433;database=bankjava;user=nhanguyen@nhanguyen;password=@nha16122004;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static String DB_Username = "nhanguyen";
    private static String DB_Password = "@nha16122004";

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

    public static boolean addTransaction(Transaction transaction) {
        try {
            Connection cnt = DriverManager.getConnection(DB_URL, DB_Username, DB_Password);
            PreparedStatement ppst = cnt.prepareStatement("INSERT INTO transactions(user_id, trans_type, trans_amount, trans_date) VALUES(?,?,?,GETDATE())");
            ppst.setInt(1, transaction.getUserId());
            ppst.setString(2, transaction.getTrans_type());
            ppst.setBigDecimal(3, transaction.getTrans_amount());
            ppst.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // public static boolean addTransaction(Transaction transaction) {
    //     try {
    //         JSONObject json = new JSONObject();
    //         json.put("userId", transaction.getUserId());
    //         json.put("trans_type", transaction.getTrans_type());
    //         json.put("trans_amount", transaction.getTrans_amount().toString());
    //         json.put("trans_date", transaction.getTrans_date().toString());
    
    //         URL url = new URL("http://localhost:8081/api/transactions/add");
    //         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //         conn.setRequestMethod("POST");
    //         conn.setRequestProperty("Content-Type", "application/json");
    //         conn.setDoOutput(true);
    
    //         try (OutputStream os = conn.getOutputStream()) {
    //             byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
    //             os.write(input, 0, input.length);
    //         }
    
    //         int responseCode = conn.getResponseCode();
    //         if (responseCode == 200) {
    //             return true;
    //         } else {
    //             return false;
    //         }
    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //         return false;
    //     }
    // }


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
