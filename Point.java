public class Point {
    private int x;
    private int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public boolean isContained(Point p) {
        if ((Math.pow(p.getX() - this.getX(),2) + Math.pow(p.getY() - this.getY(),2)<20)) {
            return true;
        }
        return false;
    }

    public void copyPoint(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    public boolean equals(Object point, int S) {
        if (point.getClass().getName() != "Point") {
            return false;
        }
        return (Math.pow(this.getX() - ((Point) point).getX(),2) + Math.pow(this.getY() - ((Point) point).getY(),2)<S);
    }

    @Override
    public String toString(){
        return "("+this.getX()+", "+this.getY()+")";
    }
}
