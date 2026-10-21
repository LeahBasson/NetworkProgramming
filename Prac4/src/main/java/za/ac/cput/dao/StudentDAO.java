package za.ac.cput.dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import za.ac.cput.connection.DBConnection;
import za.ac.cput.domain.*;

public class StudentDAO {

    private Connection con;
    private PreparedStatement pstmt;

    public StudentDAO() {
        try {
            this.con = DBConnection.derbyConnection();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    public void insert(Student student) {
        try {

            pstmt = this.con.prepareStatement("INSERT INTO Student (studName, studId, score) VALUES(?,?,?)");

            pstmt.setString(1, student.getStudName());
            pstmt.setString(2, student.getStudId());
            pstmt.setString(3, student.getScore());

            pstmt.executeUpdate();

        } catch (SQLException err) {
            System.out.println("ERROR: " + err);
        }
    }

    public void deleteStudent(String studId) {
        try {
            pstmt = this.con.prepareStatement("DELETE FROM Student WHERE stud_id = ?");

            pstmt.setString(1, studId);

            pstmt.executeUpdate();

        } catch (SQLException err) {
            System.out.println("ERROR: " + err);
        }

    }
    
    public ArrayList<Student> select() {

        ArrayList<Student> subjects = new ArrayList<>(); 

        try {

                String sql = "SELECT * FROM Student";

            pstmt = this.con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(); // stores the results from the select query

            while (rs.next()) { // go through every row, then when there is no more rows the loop ends
                String studName = rs.getString("studName"); // take the value from subCode column.
                String studId = rs.getString("studId");
                String score = rs.getString("score");

                Student student = new Student(studName, studId, score); // creates a subject object

                subjects.add(student); // adds the object to the arraylist
            }

        } catch (SQLException err) { // handles with sql database
            System.out.println("ERROR: " + err);
        }

        return subjects;
    }

}// end of class
