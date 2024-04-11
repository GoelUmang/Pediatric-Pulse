package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OldRecords {

    public Scene createScene()
    {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Patient Medical Records");

        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);

        String patientName = getLastPatientName();
        Label titleLabel = new Label("Medical Records for " + patientName);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        root.getChildren().add(titleLabel);

        // Fetch and display records for the last treated patient
        try {
            VBox recordsBox = fetchAndDisplayRecords(patientName);
            ScrollPane scrollPane = new ScrollPane(recordsBox);
            root.getChildren().add(scrollPane);
        } catch (SQLException e) {
            e.printStackTrace();
            root.getChildren().add(new Label("Failed to load medical records."));
        }

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        return scene;
    }

    private VBox fetchAndDisplayRecords(String patientName) throws SQLException
    {
        VBox recordsBox = new VBox(10);
        recordsBox.setPadding(new Insets(15));

        String query = "SELECT created_at, prescription, observation_notes FROM patient_records WHERE patient_name = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, patientName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getTimestamp("created_at").toString();
                String prescription = rs.getString("prescription");
                String observationNotes = rs.getString("observation_notes");

                Label dateLabel = new Label("Date: " + date);
                Label prescriptionLabel = new Label("Prescription: " + (prescription != null ? prescription : "No prescription"));
                Label notesLabel = new Label("Observation notes: " + (observationNotes != null ? observationNotes : "No observation notes"));

                VBox recordBox = new VBox(5, dateLabel, prescriptionLabel, notesLabel);
                recordBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #A9A9A9;");

                recordsBox.getChildren().add(recordBox);
            }
        }

        return recordsBox;
    }

    private String getLastPatientName() {
        String patientName = "";
        String query = "SELECT patient_name FROM nurse_records ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                patientName = rs.getString("patient_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve the latest patient name");
        }

        return patientName;
    }
}