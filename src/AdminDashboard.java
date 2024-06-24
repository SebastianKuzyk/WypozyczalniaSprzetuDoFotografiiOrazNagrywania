import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class AdminDashboard extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton zamknijButton;
    private JLabel ImieField;
    private JLabel NazwiskoField;
    private JButton listaSprzetuButton;
    private JButton wylogujButton;
    private JTable sprzet;

    public static void main(String[] args) throws SQLException {
        AdminDashboard admindashboard = new AdminDashboard();
        admindashboard.setVisible(true);
    }

    public AdminDashboard() throws SQLException {
        setTitle("Admin");
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 900, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        sprzet = new JTable();

        Connection connection = Database.getConnection();
        String sql = "SELECT * FROM klienci";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rst = pst.executeQuery();
            rst.next();

            ImieField.setText("Zalogowano: ");
            NazwiskoField.setText("Admin");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        listaSprzetuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdminWypozyczenia adminWypozyczenia = null;
                try {
                    adminWypozyczenia = new AdminWypozyczenia();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                adminWypozyczenia.setVisible(true);
            }
        });

        try {
            String query = "SELECT k.id, k.name, k.surname, k.mail, w.sprzet_id, w.data_wyp, w.data_zwrotu, s.koszt, w.ilosc_wypozyczona " +
                    "FROM klienci k " +
                    "LEFT JOIN wypozyczenia w ON k.id = w.ID_klienta " +
                    "LEFT JOIN sprzet s ON w.sprzet_id = s.sprzet_id";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new String[]{
                    "ID",
                    "Imię i nazwisko",
                    "Mail",
                    "ID sprzętu",
                    "Data wypożyczenia",
                    "Data zwrotu",
                    "Koszt",
                    "Ilość wypożyczona"
            }, 0);

            while (rs.next()) {
                int userId = rs.getInt("id");
                String userName = rs.getString("name") + " " + rs.getString("surname");
                String userMail = rs.getString("mail");
                int idSprz = rs.getInt("sprzet_id");
                String dataWyp = rs.getString("data_wyp");
                String dataZwrotu = rs.getString("data_zwrotu");
                double koszt = rs.getDouble("koszt");
                int ilosc_wypozyczona = rs.getInt("ilosc_wypozyczona");

                model.addRow(new Object[]{
                        userId,
                        userName,
                        userMail,
                        idSprz,
                        dataWyp,
                        dataZwrotu,
                        koszt,
                        ilosc_wypozyczona
                });
            }
            table1.setModel(model);

            // Setting tooltips for the column headers
            setTableColumnToolTips(table1);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Menu Menu = new Menu();
                Menu.setVisible(true);
            }
        });
    }

    private void setTableColumnToolTips(JTable table) {
        String[] columnToolTips = {
                "ID",
                "Imię i nazwisko",
                "Mail",
                "ID sprzętu",
                "Data wypożyczenia",
                "Data zwrotu",
                "Koszt",
                "Ilość wypożyczona"
        };

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new TableCellRenderer() {
            private final TableCellRenderer renderer = header.getDefaultRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(columnToolTips[column]);
                }
                return c;
            }
        });
    }
}
