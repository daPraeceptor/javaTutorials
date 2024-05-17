import javax.swing.*;
import java.awt.*;

public class CaptionTest {

    public static void main (String[] args) {
        JFrame jf = new JFrame() {
            @Override
            public void paint (Graphics g) {

            }
        };
        jf.setTitle("1 Title!");
        jf.setVisible(true);

        SwingUtilities.invokeLater (() -> {
            jf.setTitle("2 New title!");

                });
    }

}
