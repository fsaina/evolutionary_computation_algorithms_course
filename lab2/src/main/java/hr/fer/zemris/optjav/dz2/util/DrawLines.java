package hr.fer.zemris.optjav.dz2.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;


class DrawLines extends JComponent {

    ArrayList<Line2D.Double> lines;
    Random random;

    public DrawLines(int width, int height) {
        setPreferredSize(new Dimension(width,height));
        lines = new ArrayList<Line2D.Double>();
        random = new Random();
    }

    public void addLine(Line2D.Double line) {
        lines.add(line);
        repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        Dimension d = getPreferredSize();
        g.setColor(Color.black);
        for (Line2D.Double line : lines) {

            int size = getWidth()/2;

            int factor = 10;

            g.drawLine(
                size + (int)line.getX1() * factor,
                (size +(int)line.getY1() * factor),
                size +(int)line.getX2() * factor,
                    (size +(int)line.getY2() * factor)
                );
        }

        g.drawLine(getWidth()/2, 0,
                getWidth()/2, getHeight());

        g.drawLine(0, getHeight()/2,
                getWidth(), getHeight()/2);
    }
}