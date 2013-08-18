package com.baselet.diagram.draw.geom;


/**
 * should be removed after every Point has been replaced by PointDouble
 */
public class Point {

	public int x;
	public int y;
	
	public Point() {
	}
	
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public void move(int diffX, int diffY) {
		this.x += diffX;
		this.y += diffY;
	}

    public double distance(Point o) {
	double distX = o.getX() - this.getX();
	double distY = o.getY() - this.getY();
	return Math.sqrt(distX * distX + distY * distY);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Point other = (Point) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public PointDouble toPointDouble() {
		return new PointDouble(x, y);
	}
	
}