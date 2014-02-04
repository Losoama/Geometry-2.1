public class Vector {
    private Line line;
    private Point point0;
    private Point point1;
    private int x;
    private int y;
    private double size;

    public Vector() {
        this(new Line());
    }

    public Vector(Line line) {
        this.line = line;
        this.point0 = line.getPoint0();
        this.point1 = line.getPoint1();
        this.x = this.point1.getX() - this.point0.getX();
        this.y = this.point1.getY() - this.point0.getY();
        this.size = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector(Point p0, Point p1) {
        this.line = new Line(p0, p1);
        this.point0 = p0;
        this.point1 = p1;
        this.x = this.point1.getX() - this.point0.getX();
        this.y = this.point1.getY() - this.point0.getY();
        this.size = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public static Vector addVectors(Vector v1, Vector v2)
            throws IllegalArgumentException {
        if (!v1.point1.equals(v2)) {
            throw new IllegalArgumentException("Конец первого вектора должен совпадать с началом второго вектора!");
        }
        return new Vector(new Line(v1.point0, v2.point1));
    }

    public static int mulVectors(Vector v1, Vector v2) {
        return (v1.point1.getX() - v1.point0.getX()) * (v2.point1.getY() - v2.point0.getY()) -
                (v1.point1.getY() - v1.point0.getY()) * (v2.point1.getX() - v2.point0.getX());
    }

    @Override
    public String toString() {
        return "Координата вектора: (" + this.x + ", " + this.y + ")\n" +
                "Длина вектора: " + this.size + "\n";
    }

    public double getSize(){
        return this.size;
    }
}
