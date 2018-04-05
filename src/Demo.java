import javax.swing.*;
import java.awt.*;

/**
 * Класс, демонстрирующий работу визуализатора
 */
public class Demo extends JFrame{

    private static CircGraphPane cgp;

    /* Параметры демонстрации */
////////////////////////////////////////////////////
    /* Параметры графа */
    private static int NUMBER_OF_NODES = 41;        // Число вершин в графе
    private static int[] COMPONENTS = {3, 17};   // Образующие

    /* Параметры запуска */
    private static int START = 0;                   // Номер стартовой вершины
    private static int DEST = 11;                   // Номер конечной вершины
    private static int INTERVAL_MS = 400;           // Количество миллисекунд на шаг
////////////////////////////////////////////////////

    Demo() {
        super();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        cgp = new CircGraphPane(NUMBER_OF_NODES, COMPONENTS);
        cgp.setAlgorithm(new DemoAlgorithm(NUMBER_OF_NODES, COMPONENTS, START, DEST));
        add(cgp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(600, 600));
    }

    public static void main(String[] argv) {
        Demo d = new Demo();
        d.setVisible(true);

        /* Запуск визуализации */
        /* Раскомментировать одну строку из двух! */
/////////////////////////////////////////////////////////
        cgp.start(INTERVAL_MS); // на каждый шаг отводится INTERVAL_MS миллисекунд
//        cgp.start();            // шаги переключаются по щелчку
/////////////////////////////////////////////////////////
    }
}
