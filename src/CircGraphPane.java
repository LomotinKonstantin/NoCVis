import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.Math.*;

/**
 *  Виджет для отображения произвольного циркулянта и визуализации алгоритма
 *  поиска пути.
 */
public class CircGraphPane extends JPanel {

    /* C(nodes_num; components) */
    protected int nodes_num;                  // количество вершин
    protected int[] components;               // массив образующих
    protected int[] path;                     // путь, который будет отображаться
    protected double angle;                   // угол между вершинами
    protected Node[] nodes;                   // массив вершин
    protected Algorithm algorithm;            // алгоритм поиска пути
    protected ActionListener stepPerformer;   // шаг вперед
    protected Timer timer;

    public final static double TWO_PI = 6.263;
    private Graphics g;

    /**
     *
     * @param nodes_num Число вершин в графе
     * @param components Образующие циркулянта
     */
    CircGraphPane(int nodes_num, int[] components) {
        super();

        this.nodes_num = nodes_num;
        this.components = components;
        // Угол между вершинами циркулянта (в радианах)
        this.angle = TWO_PI / (double)nodes_num;
        // Инициализация массива узлов
        nodes = new Node[nodes_num];
        for (int i = 0; i < nodes_num; ++i) {
            Node n = new Node();
            n.setNum(i);
            nodes[i] = n;
        }
        path = new int[0];
        stepPerformer = e -> performStep();
        timer = null;
    }

    /**
     * Запустить визуализацию с таймером.
     * @param interval_ms Интервал между шагами алгоритма (в миллисекундах).
     */
    public void start(int interval_ms) {
        timer = new Timer(interval_ms, stepPerformer);
        timer.start();
    }

    /**
     *  Запустить визуализацию. Каждый следующий шаг происходит по щелчку мышью.
     */
    public void start() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                performStep();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        this.g = g;
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        drawGraph(g2);
    }

    /**
     * Установить алгоритм в виджет
     * @param algorithm Объект, реализующий интерфейс Algorithm
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        for (Node n : nodes)
            n.markAs(Node.NONE);
        int start = algorithm.startNum();
        int dest = algorithm.finishNum();
        nodes[start].markAs(Node.START);
        nodes[dest].markAs(Node.DEST);
    }

    /**
     * Получить и отрисовать очередной шаг алгоритма
     */
    protected void performStep() {
        if (algorithm.isFinished()) {
            path = algorithm.path();
            if (timer != null) {
                timer.stop();
                timer = null;
            }
            MouseListener[] ml = getMouseListeners();
            if (ml.length > 0)
                removeMouseListener(getMouseListeners()[0]);
        } else {
            path = algorithm.nextStep();
            repaint();
        }
    }

    /**
     * Рассчитать новое положение вершин
     * @param g2 Графический объект для рисования
     */
    protected void updateVertices(Graphics2D g2) {
        Dimension size = getSize();
        int center_x = size.width / 2;
        int center_y = size.height / 2;
        // Если виджет не отображается, рисовать не будем
        if (size.width == 0 || size.height == 0)
            return;
        // Устанавливаем радиус циркулянта. Он будет равен 40% от минимального измерения
        int graph_rad = (int) (0.8 * min(center_x, center_y));
        // Устанавливаем радиус вершины
        int node_rad = max(graph_rad / nodes_num, 5);
        // Устанавливаем радиус текста
        int text_rad = graph_rad + node_rad / 2 + g2.getFontMetrics().stringWidth("444");
        for (int i = 0; i < nodes_num; ++i) {
            double sin = sin(-angle * i - PI);
            double cos = cos(-angle * i - PI);
            int node_x = center_x + (int)(graph_rad * sin);
            int node_y = center_y + (int)(graph_rad * cos);
            int offset_x = g2.getFontMetrics(g2.getFont()).stringWidth(
                    String.valueOf(nodes[i].getNum())) / 2;
//            int offset_y = g2.getFontMetrics(g2.getFont()).getHeight() / 2;
            int text_x = center_x + (int)(text_rad * sin) - offset_x;
            int text_y = center_y + (int)(text_rad * cos);
            nodes[i].setCoords(node_x, node_y,
                    text_x, text_y);
            nodes[i].setRadius(node_rad);
        }
    }

    /**
     * Отрисовать вершины
     * @param g2 Графический объект для рисования
     */
    protected void drawVertices(Graphics2D g2) {
        for (Node n : nodes) n.display(g2);
    }

    /**
     * Отрисовать грани
     * @param g2 Графический объект для рисования
     */
    protected void drawConnections(Graphics2D g2) {
        for (int node = 0; node < nodes_num; ++node) {
            for (int i : components) {
                int next = (node + i) % nodes_num;
                nodes[node].connect(nodes[next], g2, Node.con_type.COMMON);
            }
        }
        if (algorithm != null) {
            Node.con_type type;
            if (algorithm.isFinished())
                type = Node.con_type.FOUND;
            else
                type = Node.con_type.HIGHLIGHT;
            for (int i = 0; i < path.length - 1; ++i) {
                nodes[path[i]].connect(nodes[path[i + 1]], g2, type);
            }
        }
    }

    /**
     * Отрисовать весь граф
     * @param g2 Графический объект для рисования
     */
    protected void drawGraph(Graphics2D g2) {
        updateVertices(g2);
        drawConnections(g2);
        drawVertices(g2);
    }

}
