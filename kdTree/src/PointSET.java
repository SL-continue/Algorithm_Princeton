import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    public         PointSET() {                              // construct an empty set of points
        points = new SET<Point2D>();
    }

    private void nullcheck(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("input is null");
        }
    }

    public           boolean isEmpty() {                      // is the set empty?
        return points.isEmpty();
    }
    public               int size() {                        // number of points in the set
        return points.size();
    }
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        nullcheck(p);
        points.add(p);
    }

    public           boolean contains(Point2D p) {           // does the set contain point p?
        nullcheck(p);
        return points.contains(p);
    }

    public              void draw() {                         // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        nullcheck(rect);
        ArrayList<Point2D> inRange = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                inRange.add(p);
            }
        }
        return inRange;
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        nullcheck(p);
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D near = null;
        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (minDistance > distance) {
                minDistance = distance;
                near = point;
            }
        }
        return near;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
        return;
    }
}
