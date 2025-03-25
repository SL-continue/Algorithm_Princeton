import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private int[][] image;
    private int width;
    private int height;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Input picture is null");

        width = picture.width();
        height = picture.height();
        image = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image[x][y] = picture.get(x, y).getRGB();
            }
        }
    }

    private double computeEnergy(int x, int y) {
        if (x == 0 || y == 0 || x + 1 == width || y + 1 == height) return 1000.0;
        return Math.sqrt(gradient(image[x-1][y], image[x+1][y]) + gradient(image[x][y-1], image[x][y+1]));
    }

    private int gradient(int rgbA, int rgbB) {
        int ra = (rgbA >> 16) & 0xFF, rb = (rgbB >> 16) & 0xFF;
        int ga = (rgbA >> 8) & 0xFF, gb = (rgbB >> 8) & 0xFF;
        int ba = rgbA & 0xFF, bb = rgbB & 0xFF;
        return (ra - rb) * (ra - rb) + (ga - gb) * (ga - gb) + (ba - bb) * (ba - bb);
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pic.set(x, y, new Color(image[x][y]));
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException("x, y not in range");
        return computeEnergy(x, y);
    }

    public int[] findSeam(boolean isVertical) {
        int W = isVertical ? width : height;
        int H = isVertical ? height : width;

        double [][] distTo = new double[W][H];
        int [][] edgeTo = new int[W][H];

        for (double[] col : distTo) {
            Arrays.fill(col, Double.POSITIVE_INFINITY);
        }

        double[][] energyGrid = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energyGrid[x][y] = computeEnergy(x, y);
            }
        }

        for (int i = 0; i < W; i++) {
            distTo[i][0] = isVertical ? energyGrid[i][0] : energyGrid[0][i];
        }

        for (int i = 0; i < H - 1; i++) {
            for (int j = 0; j < W; j++) {
                for (int k = -1; k <= 1; k++) {
                    int nextJ = j + k;
                    if (nextJ < 0 || nextJ >= W) continue;

                    double newDist = distTo[j][i] + (isVertical ? energyGrid[nextJ][i + 1] : energyGrid[i + 1][nextJ]);
                    if (newDist < distTo[nextJ][i + 1]) {
                        distTo[nextJ][i + 1] = newDist;
                        edgeTo[nextJ][i + 1] = j;
                    }
                }
            }
        }

        int[] seam = new int[H];
        int endIdx = -1;
        double minDist = Double.POSITIVE_INFINITY;

        for (int i = 0; i < W; i++) {
            if (distTo[i][H - 1] < minDist) {
                minDist = distTo[i][H - 1];
                endIdx = i;
            }
        }

        seam[H - 1] = endIdx;
        for (int i = H - 2; i >= 0; i--) {
            seam[i] = edgeTo[seam[i + 1]][i + 1];
        }

        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(false);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(true);
    }


    private void checkSeam(int[] seam, int range) {
        int cur = seam[0];
        for (int num : seam) {
            if (num < 0 || num >= range || Math.abs(num - cur) > 1) {
                throw new IllegalArgumentException("seam member not in range");
            }
            cur = num;
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Input for horizontalSeam is null");
        if (seam.length != width) throw new IllegalArgumentException("Seam length not match");
        checkSeam(seam, height);
        if (height <= 1) throw new IllegalArgumentException("Image height less or equal to 1");

        height = height - 1;
        int[][] newImage = new int[width][height];
        for (int x = 0; x < width; x++) {
            int newY = 0;
            for (int y = 0; y <= height; y++) {
                if (y == seam[x]) {
                    continue;
                }
                newImage[x][newY] = image[x][y];
                newY++;
            }
        }
        image = newImage;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Input for verticalSeam is null");
        if (seam.length != height) throw new IllegalArgumentException("Seam length not match");
        checkSeam(seam, width);
        if (width <= 1) throw new IllegalArgumentException("Image width less or equal to 1");

        width = width - 1;
        int[][] newImage = new int[width][height];
        for (int y = 0; y < height; y++) {
            int newX = 0;
            for (int x = 0; x <= width; x++) {
                if (x == seam[y]) {
                    continue;
                }
                newImage[newX][y] = image[x][y];
                newX++;
            }
        }
        image = newImage;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
