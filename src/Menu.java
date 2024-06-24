import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame{
    private JPanel JPanel1;
    private JButton logowanieButton;
    private JButton rejestracjaButton;
    private JButton wyjścieButton;

    public static void main(String[] args) {
        Menu Menu = new Menu();
        Menu.setVisible(true);
    }

    public Menu(){
        super("Wypożyczalnia");
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 800, height = 400;
        this.setSize(width, height);
        setLocationRelativeTo(null);

        try {
            setIconImage(ImageIO.read(new File("src/icon.png")));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Wystąpił błąd przy wczytywaniu icon.png.");
        }

        wyjścieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        logowanieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
        rejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                RegisterForm register = new RegisterForm();
                register.setVisible(true);
            }
        });
    }


}
