package main.java.com.bankapp.guis;

import javax.swing.*;

//import org.jfree.data.json.impl.JSONObject;

import main.java.com.objs.Service;
import main.java.com.objs.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.json.JSONObject;

/*
    This gui will allow user to login or launch the register gui
    This extends from the BaseFrame which means we will need to define our own addGuiComponent()
 */
public class LoginGui extends BaseFrame {
    public LoginGui() {
        super("Banking App Login");
    }

    @Override
    protected void addGuiComponents() {
        // tạo màu nền
        getContentPane().setBackground(Color.WHITE);

        // thêm logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/main/java/com/bankapp/guis/icon/LogoImage.jpg"));
        if (logoIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.err.println("Error loading image: /main/java/com/bankapp/guis/icon/LogoImage.jpg");
        }
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(190, 100, 100, 100);
        add(logoLabel);

        // create banking app label
        JLabel bankingAppLabel = new JLabel("PTITBANK PLUS");
        bankingAppLabel.setBounds(0, 30, super.getWidth(), 40);
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bankingAppLabel.setForeground(new Color(183, 28, 28));
        add(bankingAppLabel);

        // create username label
        JLabel usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setBounds(20, 215, getWidth() - 30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        usernameLabel.setForeground(new Color(183, 28, 28));
        add(usernameLabel);

        // create username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 240, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(usernameField);

        // create password label
        JLabel passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setBounds(20, 295, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(183, 28, 28));
        add(passwordLabel);

        // create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 320, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 25));
        add(passwordField);

        // Checkbox để hiển thị mật khẩu
        JCheckBox showPasswordCheckBox = new JCheckBox("Hiển thị mật khẩu");
        showPasswordCheckBox.setBounds(135, 362, 150, 24);
        showPasswordCheckBox.setFont(new Font("Dialog", Font.PLAIN, 14));
        showPasswordCheckBox.setForeground(new Color(183, 28, 28));
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Hiển thị mật khẩu
                } else {
                    passwordField.setEchoChar('•'); // Ẩn mật khẩu
                }
            }
        });
        add(showPasswordCheckBox);

        // Thêm nút quên mật khẩu
        JButton forgotPasswordButton = new JButton("Quên mật khẩu?");
        forgotPasswordButton.setBounds(20, 410, getWidth() - 50, 40);
        forgotPasswordButton.setFont(new Font("Dialog", Font.PLAIN, 16));
        forgotPasswordButton.setForeground(new Color(183, 28, 28));
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ForgotPassword().setVisible(true);
            }
        });
        add(forgotPasswordButton);

        // create login button
        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(20, 460, getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.setBackground(new Color(183, 28, 28));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get username
                String username = usernameField.getText();

                // get password
                String password = String.valueOf(passwordField.getPassword());

                // validate login
                try {
                    URL url = new URL("http://localhost:8081/api/users/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);

                    String urlParameters = "username=" + username + "&password=" + password;
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(postData);
                    }
                    

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        
                        // Convert JSON to User object
                        JSONObject json = new JSONObject(response.toString());
                        User user = new User(
                            json.getInt("id"),
                            json.getString("username"), 
                            json.getString("password"),
                            json.getBigDecimal("currentBalance"),
                            json.getString("cccd"),
                            json.getString("pin"), 
                            json.getString("phoneNumber"),
                            json.getString("stk")
                        );
                        // means valid login

                        // dispose this gui
                        LoginGui.this.dispose();

                        // launch bank app gui
                        BankingAppGui bankingAppGui = new BankingAppGui(user);
                        bankingAppGui.setVisible(true);

                        // show success dialog
                        JOptionPane.showMessageDialog(bankingAppGui, "Đăng nhập thành công!");
                    } else {
                        // invalid login
                        JOptionPane.showMessageDialog(LoginGui.this, "Đăng nhập thất bại...");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(LoginGui.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(loginButton);

        // create register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Chưa có tài khoản, ĐĂNG KÍ</a></html>");
        registerLabel.setBounds(0, 510, getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose of this gui
                LoginGui.this.dispose();

                // launch the register gui
                new RegisterGui().setVisible(true);
            }
        });
        add(registerLabel);
    }
}















