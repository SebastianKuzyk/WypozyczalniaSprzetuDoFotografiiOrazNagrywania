import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminWypozyczenia extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton zamknijButton;
    private JButton wsteczButton;
    private JButton dodajButton;
    private JButton usuńButton;

    public static User user;

    public static void main(String[] args) throws SQLException {
        AdminWypozyczenia adminWypozyczenia = new AdminWypozyczenia();
        adminWypozyczenia.setVisible(true);
    }

    Connection connection = Database.getConnection();

    public AdminWypozyczenia() throws SQLException {
        setTitle("Wypożyczalnia");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        updateTable();

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
                AdminDashboard adminDashboard = null;
                try {
                    adminDashboard = new AdminDashboard();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                adminDashboard.setVisible(true);
            }
        });

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    DodajSprzet dodajSprzet = new DodajSprzet();
                    dodajSprzet.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        usuńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row != -1) {
                    String cell = table1.getModel().getValueAt(row, 0).toString();
                    String sql = "DELETE FROM sprzet WHERE sprzet_id=" + cell;
                    try {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.execute();
                        updateTable();
                        JOptionPane.showMessageDialog(null, "Usunięto");
                    } catch (SQLException ex) {
                        System.out.println("Error: " + ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nie wybrano żadnego wiersza", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void centerTextInColumns(JTable table1) {
        TableColumnModel columns = table1.getColumnModel();
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < columns.getColumnCount(); i++) {
            columns.getColumn(i).setCellRenderer(renderer);
        }
    }

    public void updateTable() {
        String sql = "SELECT * FROM sprzet";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{
                    "sprzet_id",
                    "Nazwa",
                    "Typ",
                    "Producent",
                    "Koszt",
                    "ilosc_na_stanie"
            }, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("sprzet_id"),
                        rs.getString("nazwa"),
                        rs.getString("typ"),
                        rs.getString("producent"),
                        rs.getDouble("koszt"),
                        rs.getInt("ilosc_na_stanie")
                });
            }

            table1.setModel(model);
            centerTextInColumns(table1);

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
