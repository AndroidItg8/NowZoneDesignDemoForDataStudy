package itg8.com.nowzonedesigndemo.sleep.model;

public class SleepVectorModel {
    private long timestamp;
    private double vector;
    private double zScore;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getVector() {
        return vector;
    }

    public void setVector(double vector) {
        this.vector = vector;
    }

    public double getzScore() {
        return zScore;
    }

    public void setzScore(double zScore) {
        this.zScore = zScore;
    }
}
