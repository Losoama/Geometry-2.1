public class Line {
    private Point point0;
    private Point point1;

    public Line() {
        this.point0 = new Point(0, 0);
        this.point1 = new Point(0, 0);
    }

    public Line(int x1, int y1) {
        this.point0 = new Point(0, 0);
        this.point1 = new Point(x1, y1);
    }

    public Line(String s1, String s2, String s3, String s4)
            throws IllegalArgumentException {
        if (s1 == null || s1 == "" || s2 == null || s2 == "" || s3 == null || s3 == "" || s4 == null || s4 == "") {
            throw new IllegalArgumentException("Строка не должна быть пустой");
        } else try {
            this.point0 = new Point(Integer.parseInt(s1), Integer.parseInt(s2));
            this.point1 = new Point(Integer.parseInt(s3), Integer.parseInt(s4));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неправильное содержание строки");
        }
    }

    public Line(Point p) {
        this.point0 = new Point(0, 0);
        this.point1 = new Point(p.getX(), p.getY());
    }

    public Line(int x0, int y0, int x1, int y1) {
        this.point0 = new Point(x0, y0);
        this.point1 = new Point(x1, y1);
    }

    public Line(Point p0, Point p1) {
        this.point0 = new Point(p0.getX(), p0.getY());
        this.point1 = new Point(p1.getX(), p1.getY());
    }

    public void setX0(int x0) {
        this.point0.setX(x0);
    }

    public void setY0(int y0) {
        this.point0.setY(y0);
    }

    public void setX1(int x1) {
        this.point1.setX(x1);
    }

    public void setY1(int y1) {
        this.point1.setY(y1);
    }

    public void setXY0(int x0, int y0) {
        this.point0.setXY(x0, y0);
    }

    public void setXY1(int x1, int y1) {
        this.point1.setXY(x1, y1);
    }

    public void setXY(int x0, int y0, int x1, int y1) {
        this.point0.setXY(x0, y0);
        this.point1.setXY(x1, y1);
    }

    public void setXY(Line line) {
        this.point0.setXY(line.getPoint0());
        this.point1.setXY(line.getPoint1());
    }

    public void setXY(Point p0, Point p1) {
        this.point0.setXY(p0);
        this.point1.setXY(p1);
    }

    public Point getPoint0() {
        return this.point0;
    }

    public Point getPoint1() {
        return this.point1;
    }

    public Line copyLine() {
        return new Line(
                this.getPoint0().getX(),
                this.getPoint0().getY(),
                this.getPoint1().getX(),
                this.getPoint1().getY());
    }

    public String isCrossed(Line line) {
        int f1 = Integer.signum(Vector.mulVectors(new Vector(this), new Vector(this.point1, line.point0)));
        int f2 = Integer.signum(Vector.mulVectors(new Vector(this), new Vector(this.point1, line.point1)));
        int f3 = Integer.signum(Vector.mulVectors(new Vector(line), new Vector(line.point1, this.point0)));
        int f4 = Integer.signum(Vector.mulVectors(new Vector(line), new Vector(line.point1, this.point1)));
        if (f1 * f2 < 0 && f3 * f4 < 0) {
            return "\nОтрезки пересекаются";
        } else if (((f1 * f2 == 0 && f3 * f4 < 0) || (f3 * f4 == 0 && f1 * f2 < 0) ||
                (f1 * f2 == 0 && f3 * f4 == 0)) && (f1 != f2)) {
            return "\nОтрезки пересекаются";
        } else if (f1 == 0 && f2 == 0 && f3 == 0 && f4 == 0) {
            System.out.println(Math.max(
                    Math.max(this.point0.getX(), this.point1.getX()) -
                            Math.min(line.point0.getX(), line.point1.getX()),
                    Math.min(this.point0.getX(), this.point1.getX()) -
                            Math.max(line.point0.getX(), line.point1.getX())));
            System.out.println(Math.abs(this.point0.getX() - this.point1.getX()) +
                    Math.abs(line.point0.getX() - line.point1.getX()));
            if (maxAbs(
                    Math.max(this.point0.getX(), this.point1.getX()) -
                            Math.min(line.point0.getX(), line.point1.getX()),
                    Math.min(this.point0.getX(), this.point1.getX()) -
                            Math.max(line.point0.getX(), line.point1.getX()))
                    <=
                    Math.abs(this.point0.getX() - this.point1.getX()) +
                            Math.abs(line.point0.getX() - line.point1.getX())) {
                return "\nОтрезки пересекаются";
            } else {
                return "\nОтрезки не пересекаются";
            }
        }

        return "\nОтрезки не пересекаются";
    }

    private static int maxAbs(int a, int b) {
        if (Math.abs(a) > Math.abs(b)) {
            return Math.abs(a);
        }
        return Math.abs(b);
    }

    private static int minAbs(int a, int b) {
        if (Math.abs(a) > Math.abs(b)) {
            return Math.abs(b);
        }
        return Math.abs(a);
    }

    @Override
    public String toString() {
        return "(" + this.getPoint0().getX() + ", " + this.getPoint0().getY() + "); " +
                "(" + this.getPoint1().getX() + ", " + this.getPoint1().getY() + ")\n" +
                (new Vector(this));
    }
}
