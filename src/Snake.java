import javax.swing.JFrame;
import java.awt.*;

public class Snake extends JFrame {

    public static int screen_width;
    public static int screen_height;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame main = new Snake();
            main.setVisible(true);
        });

    }

    public Snake() {
        initUI();

    }

    private void initUI() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        screen_width = screenSize.width;
        screen_height = screenSize.height;

        add(new Board());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(true);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
