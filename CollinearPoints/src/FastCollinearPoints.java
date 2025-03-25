import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException("points array is null");

        int length = points.length;
        for (int i = 0; i < length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null point found in input");
            }
        }
        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        for (int i = 0; i < length; i++) {
            if (i > 0 && pointsCopy[i].compareTo(pointsCopy[i - 1]) == 0) {
                throw new IllegalArgumentException("Duplicate points detected");
            }
        }
        ArrayList<LineSegment> lsa = new ArrayList<>();
        for (int i = 0; i < length; i ++) {
            Point[] copy = pointsCopy.clone();
            Point origin = copy[i];
            Arrays.sort(copy, origin.slopeOrder());

            int start = 1, end = 1;
            while (end < length) {
                while (end < length && Double.compare(origin.slopeTo(copy[start]), origin.slopeTo(copy[end])) == 0) {
                    end ++;
                }
                if (end - start >= 3) {
                    if (origin.compareTo(copy[start]) < 0) {
                        lsa.add(new LineSegment(origin, copy[end - 1]));
                    }
                }
                start = end;
            }
        }
        segments = lsa.toArray(new LineSegment[0]);

    }
    public int numberOfSegments() {        // the number of line segments
        return segments.length;
    }
    public LineSegment[] segments() {                // the line segments
        return segments.clone();
    }
}
