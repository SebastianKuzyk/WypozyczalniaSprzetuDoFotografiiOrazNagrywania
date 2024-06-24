import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class LoginForm extends JFrame {
    private JPanel JPanel1;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton wsteczButton;
    public static User user;

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);

        User user = loginForm.user;
        if (user != null) {
            System.out.println("Udana autoryzacja użytkownika: " + user.name + " " + user.surname);
        } else {
            System.out.println("Logowanie przerwane!");
        }
    }

    LoginForm() {
        setTitle("Logowanie");
        this.setContentPane(JPanel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        try {
            setIconImage(ImageIO.read(new File("src/icon.png")));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Wystąpił błąd przy wczytywaniu icon.png.");
        }

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (isAdminCredentials(mail, password)) {
                    dispose();
                    try {
                        AdminDashboard adminDashboard = new AdminDashboard();
                        adminDashboard.setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    user = getAuthenticateUser(mail, password);

                    if (user != null) {
                        dispose();
                        try {
                            System.out.println("Zalogowano jako: " + user.name + " " + user.surname);
                            Dashboard dashboard = new Dashboard(user);
                            dashboard.setVisible(true);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginForm.this,
                                "Niepoprawny adres email lub hasło",
                                "Spróbuj ponownie",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        wsteczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Menu menu = new Menu();
                menu.setVisible(true);
            }
        });

        getRootPane().setDefaultButton(loginButton);
    }

    private boolean isAdminCredentials(String mail, String password) {
        Admin admin = new Admin();
        return mail.equals(admin.default_mail) && password.equals(admin.default_password);
    }

    private User getAuthenticateUser(String mail, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/projekt";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM klienci WHERE mail=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, mail);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("mail"),
                        resultSet.getString("password"),
                        resultSet.getInt("id")
                );
            }
            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

class Admin {
    String default_name = "admin";
    String default_surname = "admin";
    String default_mail = "admin";
    String default_password = "admin";

    Admin() {
    }
}
