import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private final Point2D point;
        private Node left, right;
        private final RectHV rect;
        private final boolean comparingX;

        public Node(Point2D p, boolean compare, double xmin, double ymin, double xmax, double ymax) {
            this.point = p;
            this.comparingX = compare;
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
            this.left = null;
            this.right = null;
        }

        public int compare(Point2D p) {
            return comparingX ? Double.compare(p.x(), point.x()) : Double.compare(p.y(), point.y());
        }

        public boolean insert(Point2D p) {
            if (point.equals(p)) return false;
            int cmp = compare(p);
            if (cmp <= 0) {
                if (left == null) {
                    left = new Node(p, !comparingX, rect.xmin(), rect.ymin(), comparingX ? point.x() : rect.xmax(), comparingX ? rect.ymax() : point.y());
                    return true;
                }
                return left.insert(p);
            } else {
                if (right == null) {
                    right = new Node(p, !comparingX, comparingX ? point.x() : rect.xmin(), comparingX ? rect.ymin() : point.y(), rect.xmax(), rect.ymax());
                    return true;
                }
                return right.insert(p);
            }
        }


        public boolean contains(Point2D p) {
            if (point.equals(p)) return true;
            int cmp = compare(p);
            if (cmp <= 0) {
                return left != null && left.contains(p);
            } else {
                return right != null && right.contains(p);
            }
        }

        public ArrayList<Point2D> range(RectHV rectangle) {
            if (!rect.intersects(rectangle)) return new ArrayList<>();

            ArrayList<Point2D> points = new ArrayList<>();
            if (rectangle.contains(point)) {
                points.add(point);
            }

            if (left != null)
                points.addAll(left.range(rectangle));
            if (right != null)
                points.addAll(right.range(rectangle));

            return points;
        }

        private Point2D nearest(Point2D p, Point2D best, double bestDist) {
            if (rect.distanceSquaredTo(p) >= bestDist) return best;  // Prune search

            double currDist = point.distanceSquaredTo(p);
            if (currDist < bestDist) {
                best = point;
                bestDist = currDist;
            }

            // Determine which subtree is closer
            Node first, second;
            if ((comparingX && p.x() <= point.x()) || (!comparingX && p.y() <= point.y())) {
                first = left;
                second = right;
            } else {
                first = right;
                second = left;
            }

            // Recursively check the closer subtree first
            if (first != null) {
                best = first.nearest(p, best, bestDist);
                bestDist = best.distanceSquaredTo(p);  // Update best distance
            }

            // Only check the farther subtree if it might contain a closer point
            if (second != null && second.rect.distanceSquaredTo(p) < bestDist) {
                best = second.nearest(p, best, bestDist);
            }

            return best;
        }

        public Point2D nearest(Point2D p) {
            return nearest(p, point, point.distanceSquaredTo(p));
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
            if (comparingX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }
            if (left != null) left.draw();
            if (right != null) right.draw();
        }
    }

    private Node root;
    private int size;
    public KdTree() {
        root = null;
        size = 0;
    }

    private void nullcheck(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("input is null");
        }
    }

    public boolean isEmpty() {  // is the set empty?
        return size == 0;
    }

    public int size() {  // number of points in the set
        return size;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        nullcheck(p);
        if (contains(p)) return;
        if (isEmpty()) {
            root = new Node(p, true, 0.0, 0.0, 1.0, 1.0);
            size++;
        } else if (root.insert(p)) {
            size++;
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        nullcheck(p);
        return !isEmpty() && root.contains(p);
    }

    public void draw() {                         // draw all points to standard draw
        root.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        nullcheck(rect);
        return (isEmpty()) ? null : root.range(rect);
    }

    public Point2D nearest(Point2D p) {
        nullcheck(p);
        if (root == null) return null;
        return root.nearest(p);
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        StdOut.println(tree.nearest(new Point2D(0.23, 0.89)));
    }

}
