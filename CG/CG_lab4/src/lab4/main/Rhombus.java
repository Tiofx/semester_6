package lab4.main;

public class Rhombus extends Quad {
    public Rhombus() {
        this(new Vertex3f[]{
                new Vertex3f("B", 0, 1),
                new Vertex3f("C", 1, 2),
                new Vertex3f("D", 2, 1),
                new Vertex3f("A", 1, 0),
        });
    }

    public Rhombus(lab2.main.figures.Vertex3f[] vertices) {
        super(vertices);
    }
}
