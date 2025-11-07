import javax.swing.*;
import gui.LoginPage;
import controller.Controller;

public class Main {
    public static void main(String[] args) {
        Controller c = new Controller();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            LoginPage login = new LoginPage(c);
            frame.setContentPane(login.mainPanel());
            frame.pack();
            frame.setSize(500, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}