package org.laba3;

public class Point {
    private double X;
    private double Y;
    private double R;
    private int result;
    private int id = 1;

    public Point(double X, double Y, double R){
        this.X = ((double)Math.round(X*10000))/10000;
        this.Y = ((double)Math.round(Y*10000))/10000;
        this.R = ((double)Math.round(R*10000))/10000;
        result = calculate(this.X, this.Y, this.R);
    }

    public boolean isDrawable(){
        return X * X <= 36 && Y * Y <= 36;
    }

    public String getColor(){
        switch (result) {
            case 1: return "'#00FF00'";
            default: return "'#FF0000'";
        }
    }

    public String getColor(int col){
        switch (col) {
            case 1: return "'#00FF00'";
            default: return "'#FF0000'";
        }
    }

    public String redrawFunction(){
        if(isDrawable()) {
            String col = this.getColor();
            return "drawPoint(" + Double.toString(X) + ", " + Double.toString(Y) + ", " + col + ");";
        }
        return "";
    }

    public String redrawFunction(double R){
        if(isDrawable()) {
            String col = this.getColor(calculate(X, Y, R));
            return "drawPoint(" + Double.toString(X) + ", " + Double.toString(Y) + ", " + col + ");";
        }
        return "";
    }

    public String getMatch(){
        switch (result){
            case 1: return "Попал";
            default: return "Не попал";
        }
    }

    public int calculate(double x, double y, double r){
        if (
                (x >= 0 && y <= 0 && x * x + y * y <= r * r / 4) ||
                (x <= 0 && y <= 0 && x >= -r / 2 && y >= -r) ||
                (x <= 0 && y >= 0 && y <= 2 * ((r /2 ) + x) && x >= -r / 2)
        ) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public double getX() {
        return this.X;
    }

    public void setX(double x) {
        this.X = x;
        result = calculate(x, Y, R);
    }

    public double getY() {
        return this.Y;
    }

    public void setY(double y) {
        this.Y = y;
        result = calculate(X, y, R);
    }

    public double getR() {
        return this.R;
    }

    public void setR(double r) {
        this.R = r;
        result = calculate(X, Y, r);
    }

    public int getResult() {
        return this.result;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
