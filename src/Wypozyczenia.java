import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Wypozyczenia extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton zamknijButton;
    private JButton wsteczButton;
    private JButton wypożyczButton;
    private JLabel ImieField;
    private JLabel NazwiskoField;
    private JTextField textField1;

    public static User user;

    public static void main(String[] args) throws SQLException {
        Wypozyczenia wypozyczenia = new Wypozyczenia(user);
        wypozyczenia.setVisible(true);
    }

    public Wypozyczenia(User user) throws SQLException {
        Wypozyczenia.user = user;
        setTitle("Dostępny sprzęt");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 800, height = 600;
        this.setSize(width, height);
        setLocationRelativeTo(null);

        ImieField.setText("Zalogowany jako:");
        NazwiskoField.setText(user.getName() + " " + user.getSurname());

        loadData();

        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        wsteczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    Dashboard dashboard = new Dashboard(user);
                    dashboard.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        wypożyczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Proszę wybrać sprzęt do wypożyczenia.");
                    return;
                }

                String cell = table1.getModel().getValueAt(row, 0).toString();
                int sprzet_id = Integer.parseInt(cell);

                int iloscNaStanie = Integer.parseInt(table1.getModel().getValueAt(row, 5).toString());
                int iloscWypozyczona;
                try {
                    iloscWypozyczona = Integer.parseInt(textField1.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Proszę wprowadzić poprawną liczbę.");
                    return;
                }

                if (iloscWypozyczona > iloscNaStanie) {
                    JOptionPane.showMessageDialog(null, "Nie ma wystarczającej ilości sprzętu na stanie.");
                    return;
                }

                String koszt = table1.getModel().getValueAt(row, 4).toString();  // Get the 'koszt' value

                try {
                    rentEquipment(sprzet_id, iloscWypozyczona, koszt);
                    JOptionPane.showMessageDialog(null, "Wypożyczono pomyślnie.");
                    refreshTable();
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas wypożyczania sprzętu.");
                }
            }
        });
    }

    private void loadData() throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "SELECT * FROM sprzet";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            DefaultTableModel model = new DefaultTableModel(new String[]{
                    "Id", "Nazwa", "Typ", "Producent", "Koszt", "ilosc_na_stanie"
            }, 0);
            while (rs.next()) {
                model.addRow(new String[]{
                        rs.getString("sprzet_id"),
                        rs.getString("nazwa"),
                        rs.getString("typ"),
                        rs.getString("producent"),
                        rs.getString("koszt"),
                        rs.getString("ilosc_na_stanie")
                });
            }
            table1.setModel(model);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void rentEquipment(int sprzet_id, int iloscWypozyczona, String koszt) throws SQLException {
        String rentSql = "INSERT INTO wypozyczenia (ID_klienta, sprzet_id, Data_wyp, Data_zwrotu, ilosc_wypozyczona, koszt) " +
                "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), ?, ?)";
        String updateSprzetSql = "UPDATE sprzet SET ilosc_na_stanie = ilosc_na_stanie - ? WHERE sprzet_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement rentStatement = conn.prepareStatement(rentSql);
             PreparedStatement updateSprzetStatement = conn.prepareStatement(updateSprzetSql)) {

            rentStatement.setInt(1, user.getId());
            rentStatement.setInt(2, sprzet_id);
            rentStatement.setInt(3, iloscWypozyczona);
            rentStatement.setString(4, koszt);

            rentStatement.executeUpdate();

            updateSprzetStatement.setInt(1, iloscWypozyczona);
            updateSprzetStatement.setInt(2, sprzet_id);

            updateSprzetStatement.executeUpdate();
        }
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        try {
            loadData();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
