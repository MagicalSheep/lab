import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private double scoreOne;
    private double scoreTwo;
    private double scoreThree;
    private double score;
    private double averageScore;

    public Student(int id, String name, double scoreOne, double scoreTwo, double scoreThree) {
        this.id = id;
        this.name = name;
        this.scoreOne = scoreOne;
        this.scoreTwo = scoreTwo;
        this.scoreThree = scoreThree;
        this.score = scoreOne + scoreTwo + scoreThree;
        this.averageScore = score / 3;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public double getScoreOne() {
        return scoreOne;
    }

    public double getScoreThree() {
        return scoreThree;
    }

    public double getScoreTwo() {
        return scoreTwo;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setScoreOne(double scoreOne) {
        this.scoreOne = scoreOne;
    }

    public void setScoreThree(double scoreThree) {
        this.scoreThree = scoreThree;
    }

    public void setScoreTwo(double scoreTwo) {
        this.scoreTwo = scoreTwo;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", name: " + name +
                ", score1: " + scoreOne + ", score2: " + scoreTwo +
                ", score3: " + scoreThree + ", sum: " + score +
                ", average: " + averageScore + "}";
    }
}
