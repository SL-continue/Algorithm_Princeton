import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
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
        for (int a = 0; a < length - 3; a++) {
            for (int b = a + 1; b < length - 2; b++) {
                for (int c = b + 1; c < length - 1; c++) {
                    for (int d = c + 1; d < length; d++) {
                        Point pA = pointsCopy[a], pB = pointsCopy[b], pC = pointsCopy[c], pD = pointsCopy[d];

                        if (Double.compare(pA.slopeTo(pB), pA.slopeTo(pC)) == 0 &&
                                Double.compare(pA.slopeTo(pB), pA.slopeTo(pD)) == 0) {
                            lsa.add(new LineSegment(pA, pD));
                        }
                    }
                }
            }
        }

        segments = lsa.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {        // the number of line segments
        return segments.length;
    }
    public LineSegment[] segments() {
        return segments.clone();
    }                // the line segments
}
