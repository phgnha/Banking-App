/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.bankapp.guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.java.com.objs.Service;
import main.java.com.objs.Transaction;
import main.java.com.objs.User;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Moment
 */
public class BankingAppDialog extends JDialog implements ActionListener
{
    private JLabel enterUserLabel;
    private JTextField enterUserField;
    private JLabel balanceLabel;
    private JLabel enterAmountLabel;
    private JTextField enterAmountField ;
    private JButton ActionButton;
    private User user;
    private main.java.com.bankapp.guis.BankingAppGui bankingAppGui;
    private JPanel pastTransactionPanel;
    private List<Transaction> pastTransactionList;
    private JLabel OTPLabel;
    private JTextField OTPField;
    public BankingAppDialog(main.java.com.bankapp.guis.BankingAppGui bankingAppGui, User user)
    {
        this.setSize(500,600);
        this.setModal(true);
        this.setLocationRelativeTo(bankingAppGui);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.bankingAppGui=bankingAppGui;
        this.user=user;
    }
    public void addComponent(String trans_type)
    {
        //Xu ly cac button ngoai tru "Dang Xuat"
        if(trans_type.equals("Số dư hiện tại"))
        {
            //this.addBalanceInfo();
        }
        else
        {
            if(trans_type.equals("Nạp tiền")||trans_type.equals("Rút tiền"))
            {
                this.addActionButton(trans_type);
                this.addCurrentBalanceandAmount();
                this.addOtpField();
            }
            else
            {
                if(trans_type.equals("Chuyển tiền"))
                {
                    this.addActionButton(trans_type);
                    this.addCurrentBalanceandAmount();
                    this.addOtpField();
                    this.addUserField();
                }
                else
                {
                    this.addPastTransactionComponents();
                }
            }
            //this.addActionButton(trans_type);
        }
    }
    public void addOtpField() {
    // Tạo label OTPLabel và field OTPField
    OTPLabel = new JLabel("Vui lòng nhập mã OTP:");
    OTPField = new JTextField(10); // Độ dài của trường OTP có thể điều chỉnh
    
    // Đặt vị trí và kích thước cho OTPLabel và OTPField
    OTPLabel.setBounds(0, 280, getWidth() - 20, 20); // Đặt tọa độ và kích thước cho OTPLabel
    OTPLabel.setHorizontalAlignment(SwingConstants.CENTER);
    OTPLabel.setFont(new Font("Dialog", Font.BOLD, 16));
    OTPField.setBounds(15, 320, getWidth() - 50, 40); // Đặt tọa độ và kích thước cho OTPField
    OTPField.setHorizontalAlignment(SwingConstants.CENTER);

    // Thêm các thành phần vào panel hoặc frame
    this.add(OTPLabel);
    this.add(OTPField);

    // Đảm bảo bạn đã set layout của `this` là null để sử dụng setBounds
    this.setLayout(null);
}
    public void addCurrentBalanceandAmount()
    {   
        // balance label
        balanceLabel = new JLabel("Số dư: " + BalanceFormat()+" VND");
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);
        
         // enter amount label
        enterAmountLabel = new JLabel("Nhập số tiền:");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        // enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
        
    }
    public void addActionButton(String ActionType)
    {
        ActionButton = new JButton(ActionType);
        ActionButton.setBounds(15, 500, getWidth() - 50, 40);
        ActionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        ActionButton.setHorizontalAlignment(SwingConstants.CENTER);
        ActionButton.addActionListener(this);
        //ActionButton.setContentAreaFilled(false);
        ActionButton.setBackground(Color.decode("#bd193d"));
        ActionButton.setForeground(Color.WHITE);
        add(ActionButton);
    }

    public void addUserField()
    {
        //Enter User Label
        enterUserLabel = new JLabel("Tên tài khoản người nhận:");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);
        
        //Enter User Field;
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }
    public void addPastTransactionComponents()
    {
        // container where we will store each transaction
        pastTransactionPanel = new JPanel();

        // make layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // add scrollability to the container
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // displays the vertical scroll only when it is required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 80);

        // perform db call to retrieve all of the past transaction and store into array list
        pastTransactionList = Service.getPastTransactions((long)user.getId());

        // iterate through the list and add to the gui
        for(int i = 0; i <pastTransactionList.size() ; i++){
            // store current transaction
            Transaction pastTransaction = pastTransactionList.get(i);

            // create a container to store an individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // create transaction type label
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTrans_type());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTrans_amount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTrans_date()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // add to the container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST); // place this on the west side
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST); // place this on the east side
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH); // place this on the south side
            // give a white background to each container
            pastTransactionContainer.setBackground(Color.WHITE);

            // give a black border to each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // add transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // add to the dialog
        add(scrollPane);
    }
    public void handleTransaction(String trans_type,float amount)
    {
        Transaction transaction;
        if(trans_type.equals("Nạp tiền"))
        {
            //add money to current balance
            user.setcurrentBalance(user.getCurrentBalance().add(new BigDecimal(amount)));
            transaction =new Transaction(user.getId(),trans_type,new BigDecimal(amount),null);
            
            
        }
        else
        {
            //case Withdraw so we subtract money
            user.setcurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));
            //set the amount negative to show sign of withdraw
            transaction =new Transaction(user.getId(),trans_type,new BigDecimal(-amount),null);
        }
         if(Service.addTransaction(transaction)&&Service.updateBalance(user))
        //if(MyJDBC.updateBalance(user))
        {
            JOptionPane.showMessageDialog(this, trans_type+" thành công");
            resetFieldandUpdate();
        }
        else
        {
            JOptionPane.showMessageDialog(this, trans_type+" thất bại");
        }
    }
    public void resetFieldandUpdate()
    {
        //reset amount and user field
        enterAmountField.setText("");
        if(enterUserField!=null)
        {
            enterUserField.setText("");
        }
        
        //update balance
        balanceLabel.setText("Số dư: " + BalanceFormat()+"VND");
        bankingAppGui.getCurrentBalanceField().setText("Số dư: " + BalanceFormat()+"VND");
    }
    public void handleTransfer(User user,String transferUser,BigDecimal amount)
    {
        if(Service.transfer((long) user.getId(), transferUser, amount))
        {
            JOptionPane.showMessageDialog(this, "Giao dịch thành công");
            resetFieldandUpdate();
        }
        else
        {
             JOptionPane.showMessageDialog(this, "Giao dịch thất bại");
        }
        
    }
    // Định dạng số tiền
    public String BalanceFormat(){
        BigInteger balance= user.getCurrentBalance().toBigInteger();
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        String formattedBalance = numberFormat.format(balance);
        return formattedBalance;
    }

    @Override public void actionPerformed(ActionEvent e)
    {
//        System.out.println("Nhay vao day");
        String buttonPressed=e.getActionCommand();
        float amount=Float.parseFloat(enterAmountField.getText());
        String OTP=this.OTPField.getText();
//        System.out.println(OTP);
//        System.out.println(user.getId());
//        System.out.println(user.getOTP());
        if(user.getOTP().equals(OTP))
        {
        if(buttonPressed.equals("Nạp tiền"))
        {
            handleTransaction(buttonPressed,amount);
            resetFieldandUpdate();
        }
        else
        {
            //check if the current balance less than amount value
            int cmp=user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if(cmp==-1)
            {
                JOptionPane.showMessageDialog(this, "Error: Số tiền trong tài khoản không đủ");
                return;
            }
            if(buttonPressed.equals("Rút tiền"))
            {
                handleTransaction(buttonPressed,amount);
            }
            else
            {
                String transferUser=enterUserField.getText();
                handleTransfer(user,transferUser,BigDecimal.valueOf(amount));
            }
            
            
        }
        }
        else
        {
            JOptionPane.showMessageDialog(this,"MA OTP KHONG CHINH XAC");
        }
    }
    
}
