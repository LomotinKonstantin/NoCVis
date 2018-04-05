import javax.swing.*;
import java.awt.*;

/**
 * Класс, демонстрирующий работу визуализатора
 */
public class Demo extends JFrame{

    Demo() {
        super();
        int[] comp = {1, 37};
        int num = 80;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        CircGraphPane cgp = new CircGraphPane(num, comp);
        cgp.setAlgorithm(new DemoAlgorithm(num, comp, 0, 59));
        cgp.start(50);
        add(cgp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(600, 600));
    }

    public static void main(String[] argv) {
        Demo d = new Demo();
        d.setVisible(true);
    }
}
