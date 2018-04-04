import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Node {

    private int x, y, text_x, text_y;
    private int num;
    private Color color;
    private int radius;
    private Ellipse2D.Double ellipse;

    public static final int NONE = 0,
                            START = 1,
                            DEST = 2;

    Node() {
        markAs(NONE);
        ellipse = new Ellipse2D.Double();
    }

    public void setCoords(int x, int y, int text_x, int text_y) {
        this.x = x;
        this.y = y;
        this.text_x = text_x;
        this.text_y = text_y;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void markAs(int type) {
        switch (type) {
            case NONE:
                color = new Color(0xFF888888, true);
                break;
            case START:
                color = new Color(0xFFFF0000, true);
                break;
            case DEST:
                color = new Color(0xFF0000FF, true);
                break;
        }
    }

    public void display(Graphics2D g2) {
        ellipse.setFrame(x - radius/2, y - radius/2, radius, radius);
        g2.setPaint(color);
        g2.fill(ellipse);
        g2.draw(ellipse);
        g2.setPaint(Color.BLACK);
        g2.drawString(String.valueOf(num), text_x, text_y);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Node))
            return false;
        return this.num == ((Node)obj).num;
    }

    public void connect(Node n, Graphics2D g2) {
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x, y, n.x, n.y);
    }
}
