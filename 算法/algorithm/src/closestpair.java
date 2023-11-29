import java.util.Arrays;
import java.util.Scanner;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class closestpair {

    private static double euclideanDistance(Point p1, Point p2) {
        return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
    }

    private static double bruteForce(Point[] points, int start, int end) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                minDistance = Math.min(minDistance, euclideanDistance(points[i], points[j]));
            }
        }
        return minDistance;
    }

    private static double closestPair(Point[] points, int start, int end) {
        if (end - start <= 3) {
            return bruteForce(points, start, end);
        }

        int mid = (start + end) / 2;
        double leftMin = closestPair(points, start, mid);
        double rightMin = closestPair(points, mid, end);
        double minDistance = Math.min(leftMin, rightMin);

        // Find points in the strip
        Point[] strip = new Point[end - start];
        int stripSize = 0;
        int midX = points[mid].x;

        for (int i = start; i < end; i++) {
            if (Math.abs(points[i].x - midX) < minDistance) {
                strip[stripSize++] = points[i];
            }
        }

        // Sort points in the strip by y coordinate
        Arrays.sort(strip, 0, stripSize, (p1, p2) -> Integer.compare(p1.y, p2.y));

        // Check points in the strip
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && strip[j].y - strip[i].y < minDistance; j++) {
                minDistance = Math.min(minDistance, euclideanDistance(strip[i], strip[j]));
            }
        }

        return minDistance;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read input
        int n = scanner.nextInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points[i] = new Point(x, y);
        }

        // Sort points by x coordinate
        Arrays.sort(points, (p1, p2) -> Integer.compare(p1.x, p2.x));

        // Find closest pair
        double result = closestPair(points, 0, n);

        System.out.println(result);

        scanner.close();
    }
}
