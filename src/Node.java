import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Класс, хранящий информацию об узле-вершине
 */
public class Node {

    private int x, y, text_x, text_y;                   // координаты вершины и текста номера
    private int num;                                    // номер
    private Color color;                                // цвет
    private int radius;                                 // радиус вершины
    private Ellipse2D.Double ellipse;                   // объект для отображения
    public enum con_type {COMMON, HIGHLIGHT, FOUND}     // тип соединения

    public static final int NONE = 0,                   // тип вершины
                            START = 1,
                            DEST = 2;

    Node() {
        markAs(NONE);
        ellipse = new Ellipse2D.Double();
    }

    /**
     * @return номер вершины
     */
    public int getNum() {
        return num;
    }

    /**
     * Установить координаты текста и вершины
     * @param x координата x вершины
     * @param y координата y вершины
     * @param text_x координата x номера
     * @param text_y координата y номера
     */
    public void setCoords(int x, int y, int text_x, int text_y) {
        this.x = x;
        this.y = y;
        this.text_x = text_x;
        this.text_y = text_y;
    }

    /**
     * Установить номер вершины
     * @param num номер вершины
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Установить радиус вершины
     * @param radius радиус вершины
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Пометить вершину как старт (красный), цель (синий) или как обычную вершину (черный)
     * @param type
     */
    public void markAs(int type) {
        switch (type) {
            case NONE:
                color = new Color(0xFF000000, true);
                break;
            case START:
                color = new Color(0xFFFF0000, true);
                break;
            case DEST:
                color = new Color(0xFF0000FF, true);
                break;
        }
    }

    /**
     * Отрисовать вершину
     * @param g2 графический объект для рисования
     */
    public void display(Graphics2D g2) {
        ellipse.setFrame(x - radius/2, y - radius/2, radius, radius);
        g2.setPaint(color);
        g2.fill(ellipse);
        g2.draw(ellipse);
        g2.setPaint(Color.BLACK);
        g2.drawString(String.valueOf(num), text_x, text_y);
    }

    /**
     * Переопределение метода для сравнения узлов
     * @param obj объект для сравнения
     * @return true, если номера узлов равны, иначе false
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Node))
            return false;
        return this.num == ((Node)obj).num;
    }

    /**
     * Отрисовать соединение между узлами
     * @param n узел, с которым нужно отрисовать связь
     * @param g2 графический объект для рисования
     * @param type тип связи (обычный, подсвеченный, найденный путь)
     */
    public void connect(Node n, Graphics2D g2, con_type type) {
        switch (type) {
            case COMMON:
                g2.setPaint(Color.GRAY);
                g2.setStroke(new BasicStroke(2));
                break;
            case HIGHLIGHT:
                g2.setPaint(Color.ORANGE);
                g2.setStroke(new BasicStroke(3));
                break;
            case FOUND:
                g2.setPaint(Color.GREEN);
                g2.setStroke(new BasicStroke(3));
        }
        g2.drawLine(x, y, n.x, n.y);
    }
}
