package za.ac.cput.domain;

import java.io.Serializable;

public class Student implements Serializable {

    private String studName;
    private String studId;
    private String score;

    public Student(String studName, String studId, String score) {
        this.studName = studName;
        this.studId = studId;
        this.score = score;
    }

    public void setStudName(String stud_name) {
        this.studName = studName;
    }

    public String getStudName() {
        return studName;
    }

    public String getStudId() {
        return studId;
    }

    public void setStudId(String stud_id) {
        this.studId = stud_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    // used to fill textfield.
    @Override
    public String toString() {
        return "Student{" + "stud_name=" + studName + ", stud_id=" + studId + ", score=" + score + '}';
    }

}// end of class
