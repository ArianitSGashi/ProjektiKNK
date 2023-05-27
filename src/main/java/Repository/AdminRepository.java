package Repository;

import Models.AbsenceData;
import Models.getData;
import Models.studentData;
import Repository.Interfaces.AdminInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import service.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AdminRepository implements AdminInterface {
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private void closeDatabaseResources() {
        try {
            if (result != null) {
                result.close();
            }
            if (prepare != null) {
                prepare.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int homeDisplayTotalEnrolledStudents() throws SQLException {
        String sql = "SELECT COUNT(id) FROM student";
        connect = ConnectionUtil.getConnection();
        int countEnrolled = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                countEnrolled = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return countEnrolled;
    }

    @Override
    public int homeDisplayFemaleEnrolled() throws SQLException {
        String sql = "SELECT COUNT(id) FROM student WHERE gender = 'Female' and status = 'Enrolled'";
        connect = ConnectionUtil.getConnection();
        int countFemale = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                countFemale = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return countFemale;
    }

    @Override
    public int homeDisplayMaleEnrolled() throws SQLException {
        String sql = "SELECT COUNT(id) FROM student WHERE gender = 'Male' and status = 'Enrolled'";
        connect = ConnectionUtil.getConnection();
        int countMale = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                countMale = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return countMale;
    }

    @Override
    public XYChart.Series<String, Integer> homeDisplayTotalEnrolledChart() throws SQLException {
        String sql = "SELECT date, COUNT(id) FROM student WHERE status = 'Enrolled' GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 5";
        connect = ConnectionUtil.getConnection();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return chart;
    }

    @Override
    public XYChart.Series<String, Integer> homeDisplayFemaleEnrolledChart() throws SQLException {
        String sql = "SELECT date, COUNT(id) FROM student WHERE status = 'Enrolled' and gender = 'Female' GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 5";
        connect = ConnectionUtil.getConnection();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return chart;
    }

    @Override
    public XYChart.Series<String, Integer> homeDisplayEnrolledMaleChart() throws SQLException {
        String sql = "SELECT date, COUNT(id) FROM student WHERE status = 'Enrolled' and gender = 'Male' GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 5";
        connect = ConnectionUtil.getConnection();
        XYChart.Series<String, Integer> chart = new XYChart.Series<>();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }

        return chart;
    }

    public void StudentsAdd(studentData sData) throws SQLException {
        String insertData = "INSERT INTO student "
                + "(year,course,firstName,lastName,gender,birth,status,image,date) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        connect = ConnectionUtil.getConnection();

        try {
            prepare = connect.prepareStatement(insertData);
            prepare.setString(1, sData.getYear());
            prepare.setString(2, sData.getCourse());
            prepare.setString(3, sData.getFirstName());
            prepare.setString(4, sData.getLastName());
            prepare.setString(5, sData.getGender());
            prepare.setDate(6, sData.getBirth());
            prepare.setString(7, sData.getStatus());
            prepare.setString(8, sData.getImage());
            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
            prepare.setDate(9, sqlDate);

            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    public void addStudentsUpdate(studentData sData) {
        String updateData = "UPDATE student SET "
                + "year = ?, "
                + "course = ?, "
                + "firstName = ?, "
                + "lastName = ?, "
                + "gender = ?, "
                + "birth = ?, "
                + "status = ?, "
                + "image = ? "
                + "WHERE id = ?";

        connect = ConnectionUtil.getConnection();

        try {
            prepare = connect.prepareStatement(updateData);
            prepare.setString(1, sData.getYear());
            prepare.setString(2, sData.getCourse());
            prepare.setString(3, sData.getFirstName());
            prepare.setString(4, sData.getLastName());
            prepare.setString(5, sData.getGender());
            prepare.setDate(6, sData.getBirth());
            prepare.setString(7, sData.getStatus());
            prepare.setString(8, sData.getImage());
            prepare.setInt(9, sData.getId());
            prepare.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }


    public void addStudentsDelete(studentData sData) {
        String deleteData = "DELETE FROM student WHERE id = ?";

        connect = ConnectionUtil.getConnection();

        try {
            PreparedStatement statement = connect.prepareStatement(deleteData);
            statement.setInt(1, sData.getId()); // Set the value of a_id parameter
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    public ObservableList<studentData> addStudentsListData() {
        ObservableList<studentData> listStudents = FXCollections.observableArrayList();

        String students = "SELECT * FROM student";

        try {
            studentData studentD;
            connect=ConnectionUtil.getConnection();
            prepare = connect.prepareStatement(students);
            result = prepare.executeQuery();

            while (result.next()) {
                studentD = new studentData(result.getInt("id"),
                        result.getString("year"),
                        result.getString("course"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getDate("birth"),
                        result.getString("status"),
                        result.getString("image"));

                listStudents.add(studentD);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listStudents;
    }



}