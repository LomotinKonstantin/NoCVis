import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.*;

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

    public void start(int interval_ms) {
        timer = new Timer(interval_ms, stepPerformer);
        timer.start();
    }

    public void start() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                performStep();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        drawGraph(g2);
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    protected void performStep() {
        if (algorithm.isFinished()) {
            path = algorithm.path();
            if (timer != null) {
                timer.stop();
                timer = null;
            }
            removeMouseListener(getMouseListeners()[0]);
        } else {
            path = algorithm.nextStep();
            repaint();
        }
    }

    protected void updateVertices() {
        Dimension size = getSize();
        int center_x = size.width / 2;
        int center_y = size.height / 2;
        // Если виджет не отображается, рисовать не будем
        if (size.width == 0 || size.height == 0)
            return;
        // Устанавливаем радиус циркулянта. Он будет равен 40% от минимального измерения
        int graph_rad = (int) (0.8 * min(center_x, center_y));
        // Устанавливаем радиус вершины
        int node_rad = (int) (graph_rad / nodes_num);
        // Устанавливаем радиус текста
        int text_rad = graph_rad + node_rad * 2;
        for (int i = 0; i < nodes_num; ++i) {
            double sin = sin(-angle * i - PI);
            double cos = cos(-angle * i - PI);
            int node_x = center_x + (int)(graph_rad * sin);
            int node_y = center_y + (int)(graph_rad * cos);
            int text_x = center_x + (int)(text_rad * sin);
            int text_y = center_y + (int)(text_rad * cos);
            nodes[i].setCoords(node_x, node_y, text_x, text_y);
            nodes[i].setRadius(node_rad);
        }
    }

    protected void drawVertices(Graphics2D g2) {
        for (Node n : nodes) n.display(g2);
    }

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

    protected void drawGraph(Graphics2D g2) {
        updateVertices();
        drawConnections(g2);
        drawVertices(g2);
    }

}
