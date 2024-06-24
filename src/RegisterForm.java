import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegisterForm extends JFrame{
    private JPanel JPanel1;
    private JTextField firstNameField;
    private JPasswordField passwordField;
    private JButton wsteczButton;
    private JButton registerButton;
    private JPasswordField passwordField1;
    private JTextField lastNameField;
    private JTextField emailField;

    public static void main(String[] args) {
        RegisterForm myForm = new RegisterForm();
        myForm.setVisible(true);
        User user = myForm.user;
        if (user != null){
            System.out.println("Udana rejestracja użytkownika: " + user.name);
        }else{
            System.out.println("Rejestracja przerwana.");
        }
    }

    public RegisterForm(){
        setTitle("Rejestracja");
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        try {
            setIconImage(ImageIO.read(new File("src/icon.png")));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Wystąpił błąd przy wczytywaniu icon.png.");
        }

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        wsteczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Menu Menu = new Menu();
                Menu.setVisible(true);
            }
        });
        getRootPane().setDefaultButton(registerButton);
    }

    private void registerUser() {
        String name = firstNameField.getText();
        String surname = lastNameField.getText();
        String mail = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String confirmpassword = String.valueOf(passwordField1.getPassword());

        if (name.isEmpty() || surname.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Uzupełnij wszystkie pola",
                    "Spróbuj ponownie",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmpassword)){
            JOptionPane.showMessageDialog(this,
                    "Uzupełnij wszystkie pola",
                    "Spróbuj ponownie",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, surname, mail, password);
        if (user != null){
            dispose();
            System.out.println("Poprawnie zarejestrowano użytkownika.");
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Niepowodzenie w rejestracji użytkownika",
                    "Spróbuj ponownie",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;
    private User addUserToDatabase(String name, String surname, String mail, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/projekt";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO klienci (name, surname, mail, password) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, mail);
            preparedStatement.setString(4, password);

            //insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.surname = surname;
                user.mail = mail;
                user.password = password;
            }
            //close connection
            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }
}
