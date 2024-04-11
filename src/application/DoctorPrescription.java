package application;

import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DoctorPrescription {

    public static class PrescriptionApplication extends Application {
        
        private TextArea prescriptionTextArea;

        @Override
        public void start(Stage primaryStage) {
            VBox root = new VBox(20);
            root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            prescriptionTextArea = new TextArea();
            prescriptionTextArea.setPromptText("Write prescription here...");

            Button saveButton = new Button("Save Prescription");
            saveButton.setOnAction(event -> {
                String patientName = getLastPatientName();
                String prescription = prescriptionTextArea.getText();
                saveOrUpdateRecord(patientName, prescription, null);
            });

            root.getChildren().addAll(new Label("Prescription:"), prescriptionTextArea, saveButton);

            Scene scene = new Scene(root, 800, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Prescription");
            primaryStage.show();
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

        private void saveOrUpdateRecord(String patientName, String prescription, String observationNotes) {
            String latestRecordQuery = "SELECT id, prescription, observation_notes FROM patient_records WHERE patient_name = ? ORDER BY created_at DESC LIMIT 1";

            try (Connection conn = DatabaseConnector.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(latestRecordQuery)) {
                
                stmt.setString(1, patientName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String existingPrescription = rs.getString("prescription");
                    String existingObservationNotes = rs.getString("observation_notes");

                    // Check if the latest record for this patient has either prescription or observation notes as NULL
                    if ((existingPrescription == null && prescription != null) || (existingObservationNotes == null && observationNotes != null)) {
                        // Update the existing record
                        updateRecord(conn, rs.getInt("id"), prescription, observationNotes);
                    } else {
                        // Insert a new record if both fields are non-NULL in the existing record
                        insertRecord(conn, patientName, prescription, observationNotes);
                    }
                } else {
                    // No existing record for this patient, so insert a new one
                    insertRecord(conn, patientName, prescription, observationNotes);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to save or update record");
            }
        }

        private void updateRecord(Connection conn, int recordId, String prescription, String observationNotes) throws SQLException {
            if (prescription != null && observationNotes == null) {
                String updateQuery = "UPDATE patient_records SET prescription = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, prescription);
                    updateStmt.setInt(2, recordId);
                    updateStmt.executeUpdate();
                }
            } else if (observationNotes != null && prescription == null) {
                String updateQuery = "UPDATE patient_records SET observation_notes = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, observationNotes);
                    updateStmt.setInt(2, recordId);
                    updateStmt.executeUpdate();
                }
            }
            System.out.println("Record updated successfully");
        }


        private void insertRecord(Connection conn, String patientName, String prescription, String observationNotes) throws SQLException {
            String insertQuery = "INSERT INTO patient_records (patient_name, prescription, observation_notes) VALUES (?, ?, ?)";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, patientName);
                insertStmt.setString(2, prescription);
                insertStmt.setString(3, observationNotes);

                insertStmt.executeUpdate();
                System.out.println("New record inserted successfully");
            }
        }

    }
}

