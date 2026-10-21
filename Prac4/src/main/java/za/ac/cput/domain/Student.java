package za.ac.cput.domain;

import java.io.Serializable;

public class Student implements Serializable {

    private String stud_name;
    private String stud_id;
    private String score;

    public Student(String stud_name, String stud_id, String score) {
        this.stud_name = stud_name;
        this.stud_id = stud_id;
        this.score = score;
    }

    public void setStud_Name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getStud_Name() {
        return stud_name;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
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
        return "Student{" + "stud_name=" + stud_name + ", stud_id=" + stud_id + ", score=" + score + '}';
    }

}// end of class
