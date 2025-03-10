package main.java.com.bankapp.guis;
import javax.swing.*;

import main.java.com.objs.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;
import java.nio.charset.StandardCharsets;

import org.json.*;

public class ForgotPassword extends BaseFrame {
    public ForgotPassword() {
        super("Quên Mật Khẩu");
    }

    @Override
    protected void addGuiComponents() {
        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = new JLabel("Khôi phục mật khẩu");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(183, 28, 28));
        titleLabel.setBounds(0, 20, getWidth(), 50);
        add(titleLabel);

        // CCCD Label and Field
        JLabel cccdLabel = new JLabel("CCCD:");
        cccdLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        cccdLabel.setForeground(new Color(183, 28, 28));
        cccdLabel.setBounds(20, 100, 150, 30);
        add(cccdLabel);

        JTextField cccdField = new JTextField();
        cccdField.setFont(new Font("Dialog", Font.PLAIN, 18));
        cccdField.setBounds(20, 130, getWidth() - 50, 35);
        add(cccdField);

        // Phone Label and Field
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        phoneLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        phoneLabel.setForeground(new Color(183, 28, 28));
        phoneLabel.setBounds(20, 180, 150, 30);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Dialog", Font.PLAIN, 18));
        phoneField.setBounds(20, 210, getWidth() - 50, 35);
        add(phoneField);

        // New Password Label and Field
        JLabel newPasswordLabel = new JLabel("Mật khẩu mới:");
        newPasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        newPasswordLabel.setForeground(new Color(183, 28, 28));
        newPasswordLabel.setBounds(20, 260, 150, 30);
        add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Dialog", Font.PLAIN, 18));
        newPasswordField.setBounds(20, 290, getWidth() - 50, 35);
        add(newPasswordField);

        // Submit Button
        JButton submitButton = new JButton("Gửi yêu cầu khôi phục");
        submitButton.setFont(new Font("Dialog", Font.BOLD, 18));
        submitButton.setBackground(new Color(183, 28, 28));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(20, 360, getWidth() - 50, 40);
        add(submitButton);

        // Back to Login Button
        JButton backButton = new JButton("Quay về màn hình đăng nhập");
        backButton.setFont(new Font("Dialog", Font.BOLD, 18));
        backButton.setBackground(new Color(183, 28, 28));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(20, 420, getWidth() - 50, 40);
        add(backButton);

        // ActionListener for submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CCCD = cccdField.getText();
                String phoneNumber = phoneField.getText();
                String newPassword = new String(newPasswordField.getPassword());
                try {
                    String urlParameters = "cccd=" + URLEncoder.encode(CCCD, "UTF-8") + 
                                   "&phoneNumber=" + URLEncoder.encode(phoneNumber, "UTF-8") + 
                                   "&newPassword=" + URLEncoder.encode(newPassword, "UTF-8");

                    URL url = new URL("http://localhost:8081/api/users/reset-password");
                    HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);
                    
                    try(OutputStream os = conn.getOutputStream()){
                        byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8); 
                        os.write(input,0,input.length);
                    }
                    int code= conn.getResponseCode();
                    if(code == 200){
                        JOptionPane.showMessageDialog(ForgotPassword.this,"Doi mat khau thanh cong" );
                        dispose();
                        new LoginGui().setVisible(true);
                    }
                    else{
                        JOptionPane.showMessageDialog(ForgotPassword.this,"Doi mat khau that bai" );
                    }


                    // if(MyJDBC.resetPassword(CCCD, phoneNumber, newPassword))
                    // {
                        // JOptionPane.showMessageDialog(ForgotPassword.this,"Doi mat khau thanh cong" );
                        // dispose();
                        // new LoginGui().setVisible(true);
                    // }
                    // else
                    // {
                    //     JOptionPane.showMessageDialog(ForgotPassword.this,"Doi mat khau that bai" );
                    // }
                    
                } catch (Exception ex) {
                    Logger.getLogger(ForgotPassword.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // ActionListener for backButton to return to login screen
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to the login screen
                new LoginGui().setVisible(true); // Assuming you have a LoginGui class
                dispose(); // Close the current ForgotPassword window
            }
        });
    }
}
