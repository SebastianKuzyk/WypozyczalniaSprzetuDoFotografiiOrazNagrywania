import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class Dashboard extends JFrame {
    private JPanel panel1;
    private JButton listaSprzetuButton;
    private JButton zamknijButton;
    private JTable table1;
    private JLabel ImieField;
    private JButton zwróćButton;
    private JLabel NazwiskoField;
    private JButton wylogujButton;

    public static User user;

    public Dashboard(User user) throws SQLException {
        Dashboard.user = user;
        setTitle("Wypożyczalnia");

        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        try {
            setIconImage(ImageIO.read(new File("src/icon.png")));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Wystąpił błąd przy wczytywaniu icon.png.");
        }

        Connection connection = Database.getConnection();
        try {
            String sql = "SELECT * FROM klienci WHERE id=" + user.getId();
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rst = pst.executeQuery();
            rst.next();

            DefaultTableModel model = new DefaultTableModel(new String[]{
                    "Nazwa",
                    "Typ",
                    "Producent",
                    "Data wypożyczenia",
                    "Data zwrotu",
                    "Koszt",
                    "Ilość wypożyczona"
            }, 0);
            ImieField.setText("Zalogowany jako:");
            NazwiskoField.setText(user.getName() + " " + user.getSurname());

            table1.setModel(model);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        listaSprzetuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                Wypozyczenia wypozyczenia = null;
                try {
                    wypozyczenia = new Wypozyczenia(user);
                    wypozyczenia.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        zwróćButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();

                if (selectedRow != -1) {
                    String nazwa = table1.getValueAt(selectedRow, 0).toString().trim();
                    int iloscWypozyczona = Integer.parseInt(table1.getValueAt(selectedRow, 6).toString().trim());
                    double koszt = Double.parseDouble(table1.getValueAt(selectedRow, 5).toString().trim());

                    double totalCost = iloscWypozyczona * koszt;

                    int result = JOptionPane.showConfirmDialog(null,
                            "Czy chcesz zwrócić wypożyczony sprzęt: " + nazwa + "?\nŁączny koszt: " + totalCost, "Potwierdzenie", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            String selectnazwaSql = "SELECT * FROM sprzet WHERE LOWER(nazwa) = LOWER(?)";
                            PreparedStatement selectSprzetStatement = connection.prepareStatement(selectnazwaSql);
                            selectSprzetStatement.setString(1, nazwa);
                            ResultSet nazwaResultSet = selectSprzetStatement.executeQuery();

                            if (nazwaResultSet.next()) {
                                int sprzetId = nazwaResultSet.getInt("sprzet_id");

                                String deleteSql = "DELETE FROM wypozyczenia WHERE ID_klienta = ? AND sprzet_id = ?";
                                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                                deleteStatement.setInt(1, user.getId());
                                deleteStatement.setInt(2, sprzetId);
                                deleteStatement.executeUpdate();

                                refreshTable();

                                JOptionPane.showMessageDialog(null, nazwa + " została zwrócona.\nŁączny koszt: " + totalCost);
                            } else {
                                JOptionPane.showMessageDialog(null, "Nie znaleziono: " + nazwa, "Błąd", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Wystąpił błąd.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nie wybrano.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Wylogowano.");
                Menu menu = new Menu();
                menu.setVisible(true);
            }
        });

        showUserRent();

        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Wyłączono program.");
            }
        });
    }

    private void showUserRent() {
        String sql = "SELECT s.nazwa, s.typ, s.producent, w.Data_wyp, w.Data_zwrotu, w.koszt, w.ilosc_wypozyczona FROM wypozyczenia w " +
                "JOIN sprzet s ON w.sprzet_id = s.sprzet_id WHERE w.ID_klienta = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, user.getId());
            ResultSet rst = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{
                    "Nazwa",
                    "Typ",
                    "Producent",
                    "Data wypożyczenia",
                    "Data zwrotu",
                    "Koszt",
                    "Ilość wypożyczona"
            }, 0);

            while (rst.next()) {
                String dataZwrotu = rst.getString("Data_zwrotu");

                Object[] row = {
                        rst.getString("nazwa"),
                        rst.getString("typ"),
                        rst.getString("producent"),
                        rst.getString("Data_wyp"),
                        dataZwrotu,
                        rst.getDouble("koszt"),
                        rst.getInt("ilosc_wypozyczona")
                };

                if (dataZwrotu != null) {
                    LocalDate returnDate = LocalDate.parse(dataZwrotu);
                    if (returnDate.isBefore(LocalDate.now())) {
                        row[4] = "<html><font color='red'>" + dataZwrotu + "</font></html>";
                    }
                }

                model.addRow(row);
            }
            table1.setModel(model);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        showUserRent();
    }

    public static void main(String[] args) throws SQLException {
        Dashboard dashboard = new Dashboard(user);
        dashboard.setVisible(true);
    }
}
