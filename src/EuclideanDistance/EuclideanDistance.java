package EuclideanDistance;

import java.awt.*;
import java.util.Vector;

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

    public double calculateDistance(Double[] queryVector, Double[] documentVector) {
        double distance = 0;
        if (queryVector.length != documentVector.length) {
            System.out.println("ERROR: vectors don't have equal dimensions");

            return -1;
        } else {
            for (int i = 0; i < queryVector.length; i++) {
                distance += queryVector[i] - documentVector[i];
            }

            distance = Math.sqrt(distance);
        }

        return distance;
    }
}