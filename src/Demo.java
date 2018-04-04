import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame{

    Demo() {
        super();
        int[] comp = {1, 34};
        setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        add(new CircGraphPane(100, comp));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(600, 600));
    }

    public static void main(String[] argv) {
        Demo d = new Demo();
        d.setVisible(true);
    }
}
