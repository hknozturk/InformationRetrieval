package EuclideanDistance;

import java.awt.*;

/**
 * Euclidean Distance calculation.
 * This is basically distance between two vectors.
 * It is not the best way to do scoring because most relevant documents to the search query might have high frequencies
 * and this will make longer vector then the query. Better approach is to calculate angles between vectors to see similarity
 * between query and the documents.
 **/
public class EuclideanDistance {
    public double calculateDistance(Point vector1, Point vector2) {
        double deltaX = vector1.x - vector2.x;
        double deltaY = vector1.y - vector2.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        System.out.println("Distance between two vectors are: " + distance);
        return distance;
    }
}
