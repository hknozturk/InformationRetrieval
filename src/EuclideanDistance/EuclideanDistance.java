package EuclideanDistance;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.commons.math.linear.SparseRealVector;

import java.awt.*;
import java.util.Map;
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

    public static class DocVector {
        public Map<String, Integer> terms;
        public SparseRealVector vector;

        public DocVector(Map<String, Integer> terms) {
            this.terms = terms;
            this.vector = new OpenMapRealVector(terms.size());
        }

        public void setEntry(String term, int freq) {
            if (terms.containsKey(term)) {
                int pos = terms.get(term);
                vector.setEntry(pos, (double) freq);
            }
        }

        public void normalize() {
            double sum = vector.getL1Norm();
            vector = (SparseRealVector) vector.mapDivide(sum);
        }

        public String toString() {
            RealVectorFormat formatter = new RealVectorFormat();
            return formatter.format(vector);
        }
    }

    public static double getEuclideanDistanceSimilarity(DocVector d1, DocVector d2) {
        return d1.vector.getDistance(d2.vector);
    }
}
