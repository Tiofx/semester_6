package lab4.main;

import lab2.main.figures.Vertex3f;

public class Trapeze extends Quad {
    public Trapeze() {
        this(new lab4.main.Vertex3f[]{
                new lab4.main.Vertex3f("A", 0, 0),
                new lab4.main.Vertex3f("D", 5, 0),
                new lab4.main.Vertex3f("C", 3, 2),
                new lab4.main.Vertex3f("B", 1, 2)
        });
    }

    public Trapeze(Vertex3f[] vertices) {
        super(vertices);
    }
}
