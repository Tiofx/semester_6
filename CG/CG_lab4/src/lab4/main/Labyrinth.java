package lab4.main;

import lab4.util.Complex;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Labyrinth extends Quad {
    protected final List<lab2.main.figures.Vertex3f> list = new ArrayList<>();
    float xMin, xMax, yMin, yMax;

    public Labyrinth() {
        super(new Vertex3f[]{
                new Vertex3f("A", 0, 0),
                new Vertex3f("B", 0, 1),
                new Vertex3f("C", 1, 1),
                new Vertex3f("D", 1, 0),
        });
        setColor(Color.black);
    }


    public boolean hit(Complex figure) {
        list.clear();
        figure.getVertexes(list);

        return list.stream().filter(vertex3f ->
                vertex3f.x > xMin && vertex3f.x < xMax &&
                        vertex3f.y > yMin && vertex3f.y < yMax)
                .count() >= list.size() / 2;
    }

    public void updateBounds() {
        xMin = (float) Arrays.stream(getVertices())
                .mapToDouble(lab2.main.figures.Vertex3f::getX)
                .min()
                .getAsDouble();

        yMin = (float) Arrays.stream(getVertices())
                .mapToDouble(lab2.main.figures.Vertex3f::getY)
                .min()
                .getAsDouble();

        xMax = (float) Arrays.stream(getVertices())
                .mapToDouble(lab2.main.figures.Vertex3f::getX)
                .max()
                .getAsDouble();

        yMax = (float) Arrays.stream(getVertices())
                .mapToDouble(lab2.main.figures.Vertex3f::getY)
                .max()
                .getAsDouble();
    }
}
