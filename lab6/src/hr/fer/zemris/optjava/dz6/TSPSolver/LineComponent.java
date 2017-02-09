package hr.fer.zemris.optjava.dz6.TSPSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

class LineComponent extends JComponent {

    ArrayList<Line2D.Double> lines;
    ArrayList<Point2D> points;
    Random random;
    private int factor;

    LineComponent(int width, int height, int factor) {
        super();
        setPreferredSize(new Dimension(width,height));
        lines = new ArrayList<Line2D.Double>();
        points= new ArrayList<Point2D>();
        random = new Random();
        this.factor = factor;
    }

    public void addPoint(double x, double y){
        points.add(new Point2D.Double(x, y));
    }

    public void addLine(double x1, double y1, double x2, double y2) {
        Line2D.Double line = new Line2D.Double(
                x1,
                y1,
                x2,
                y2
            );
        lines.add(line);
        repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        Dimension d = getPreferredSize();
        g.setColor(Color.black);
        for (Line2D.Double line : lines) {
            g.drawLine(
                (int)line.getX1()/factor,
                (int)line.getY1()/factor,
                (int)line.getX2()/factor,
                (int)line.getY2()/factor
                );
        }

        for(Point2D point2D : points){
            int x  = (int) (point2D.getX()/factor);
            int y  = (int) (point2D.getY()/factor);
            g.drawLine(x, y, x, y);
        }
    }
}

