import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DodajSprzet extends JFrame{
    private JButton wsteczButton;
    private JButton dodajSprzętButton;
    private JTextField textField4;
    private JTextField textField3;
    private JTextField textField2;
    private JTextField textField1;
    private JButton wyjścieButton;
    private JPanel AddSprzet;
    private JTextField textField5;

    public Sprzet sprzet;

    public static void main(String[] args) throws SQLException {
        DodajSprzet dodajSprzet = new DodajSprzet();
        dodajSprzet.setVisible(true);
        Sprzet sprzet   = dodajSprzet.sprzet  ;
        if (sprzet != null){
            System.out.println("Dodano: " + sprzet.nazwa);
        }else{
            System.out.println("Przerwano dodawanie.");
        }
    }

    public DodajSprzet() throws SQLException {
        setTitle("Dodaj");
        this.setContentPane(this.AddSprzet);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        dodajSprzętButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DodajSprzet();
                dispose();

                AdminWypozyczenia AdminWypozyczenia = null;
                try {
                    AdminWypozyczenia = new AdminWypozyczenia();
                    AdminWypozyczenia.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        wyjścieButton.addActionListener(new ActionListener() {
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
                    AdminWypozyczenia adminWypozyczenia = new AdminWypozyczenia();
                    adminWypozyczenia.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void DodajSprzet(){
        String nazwa = textField1.getText();
        String typ = textField2.getText();
        String producent = textField3.getText();
        float koszt = Float.parseFloat(textField5.getText().trim());
        int ilosc_na_stanie = Integer.parseInt(textField4.getText().trim());

        if (nazwa.isEmpty() || typ.isEmpty() || producent.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Uzupełnij wszystkie pola," +
                            "Spróbuj ponownie");
            return;
        }

        sprzet = addSprzetToDatabase(nazwa, typ, producent, koszt, ilosc_na_stanie);
        if (sprzet != null){
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,
                    "Niepowodzenie w dodawaniu," +
                            "Spróbuj ponownie");
        }

    }

    private Sprzet addSprzetToDatabase(String nazwa, String typ,String producent,float koszt, int ilosc_na_stanie){
        Sprzet sprzet = null;

        try{
            Connection connection = Database.getConnection();
            String sql = "INSERT INTO sprzet (nazwa, typ, producent,koszt, ilosc_na_stanie) VALUES (?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nazwa);
            ps.setString(2, typ);
            ps.setString(3, producent);
            ps.setString(4, String.valueOf(koszt));
            ps.setString(5, String.valueOf(ilosc_na_stanie));

            int addedRows = ps.executeUpdate();
            if (addedRows > 0){
                sprzet = new Sprzet();
                sprzet.nazwa = nazwa;
                sprzet.typ = typ;
                sprzet.producent = producent;
                sprzet.koszt = koszt;
                sprzet.ilosc_na_stanie = ilosc_na_stanie;
            }
            System.out.println("Dodano ksiażkę:" +
                    "\nNazwa: " + nazwa +
                    "\nTyp: " + typ +
                    "\nProducnet: " + producent +
                    "\nKoszt: " + koszt +
                    "\nilosc_na_stanie: " + ilosc_na_stanie
            );
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return sprzet;
    }

}




