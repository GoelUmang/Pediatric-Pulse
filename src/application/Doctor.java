package application;

import java.sql.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Doctor
{
	
	private String username;
	private String userType;
    
    public Doctor(String username, String userType)
	{
        this.username = username;
        this.userType = userType;
    }

    public Scene createScene()
    {
    	Stage primaryStage = new Stage();
    	
    	String doctorName = getPatientFirstName(username);
        // Header setup
        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(10, 20, 10, 20));

        Label lblPediatricPulse = new Label("Pediatric Pulse");
        lblPediatricPulse.setStyle("-fx-text-fill: white; -fx-font-size: 40px;");
        Label lblWelcome = new Label("Welcome, Doctor " + doctorName);
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 26px;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(event -> {
            Login login = new Login(primaryStage);
            primaryStage.setScene(login.createScene());
            primaryStage.show();
        });

        HBox leftHeader = new HBox(lblPediatricPulse);
        HBox rightHeader = new HBox(lblWelcome, btnLogout);
        rightHeader.setSpacing(10);

        headerPane.setLeft(leftHeader);
        headerPane.setRight(rightHeader);
        headerPane.setStyle("-fx-background-color: #0e70ab;");

        // Image section - make sure the image path is correct
        // Replace "doctor.png" with your actual image filename
        Image image = new Image("file:/Users/sakethkoyi/Downloads/WhatsApp Image 2024-04-07 at 18.30.16.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400); // Set the width as needed
        imageView.setFitHeight(400); // Set the height as needed
        imageView.setPreserveRatio(true);
        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER);

        // Basic Measurements Section
        VBox measurementsBox = createSectionBox("Basic Measurements");
       
        

        Label lblPatientName = new Label("PatientName:");
        Label lblAge = new Label("Age:");
        Label lblHeight = new Label("Heght:");
        Label lblBodyWeight = new Label("Body Weight");
        Label lblTemperature = new Label("Body Temperature:");
        Label lblBloodPressure = new Label("Blood Pressure:");
        Label notesLabel = new Label();

        // Empty Labels
        Label lblEmpty1 = new Label();
        Label lblEmpty2 = new Label();
        Label lblEmpty3 = new Label();
        Label lblEmpty4 = new Label();
        Label lblEmpty5 = new Label();
        Label lblEmpty6 = new Label();
        
        displayLatestPatientInfo(lblPatientName, lblAge, lblHeight, lblBodyWeight, lblTemperature, lblBloodPressure, notesLabel);

        // Create HBox containers for each pair of labels
        HBox hbox1 = new HBox(lblPatientName, lblEmpty1);
        HBox hbox2 = new HBox(lblAge, lblEmpty2);
        HBox hbox3 = new HBox(lblHeight, lblEmpty3);
        HBox hbox4 = new HBox(lblBodyWeight, lblEmpty4);
        HBox hbox5 = new HBox(lblTemperature, lblEmpty5);
        HBox hbox6 = new HBox(lblBloodPressure, lblEmpty6);
        
        measurementsBox.getChildren().addAll(lblPatientName, lblEmpty1,
        		lblAge, lblEmpty3, lblHeight,
                lblEmpty4, lblBodyWeight, lblEmpty5, lblTemperature, lblEmpty6,
                lblBloodPressure);


        // Observation Notes Section
        VBox notesBox = createSectionBox("Nurse Notes");
        

        //VBox.setVgrow(notesLabel, Priority.ALWAYS);
        //notesLabel.setMaxHeight(Double.MAX_VALUE);

        notesBox.getChildren().addAll(notesLabel);

        // Content Box with Image, Measurements, and Notes side by side
        HBox contentBox = new HBox(20, imageBox, measurementsBox, notesBox);
        contentBox.setAlignment(Pos.TOP_LEFT);

        // Buttons Section
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        // Create instances of the classes for each button action
        ChoiceBox doctorChoiceBox = new ChoiceBox(username, userType);

        // Create buttons
        Button btnMessages = new Button("Messages");
        Button btnCheckOldRecord = new Button("Check Old Records");
        Button observationNotesButton = new Button("Observation Notes");
        Button btnWritePrescription = new Button("Write Prescription");

        // Set sizes for buttons
        setButtonSize(btnMessages, 250, 70);
        setButtonSize(btnCheckOldRecord, 250, 70);
        setButtonSize(observationNotesButton, 250, 70);
        setButtonSize(btnWritePrescription, 250, 70);

        // Set button actions
        btnMessages.setOnAction(event -> {
            Scene choiceBoxScene = doctorChoiceBox.createScene();
            primaryStage.setScene(choiceBoxScene);
            primaryStage.show();
        });

        btnCheckOldRecord.setOnAction(event -> {
        	 OldRecords oldRecords = new OldRecords();
             Scene oldRecordsscene = oldRecords.createScene();
             primaryStage.setScene(oldRecordsscene);
             primaryStage.show();
        });

        observationNotesButton.setOnAction(event -> {
        	ObservationNotes.ObservationApplication prescriptionApp = new ObservationNotes.ObservationApplication();
            prescriptionApp.start(new Stage());
        });

        btnWritePrescription.setOnAction(event -> {
            // Launch the PrescriptionApplication
            DoctorPrescription.PrescriptionApplication prescriptionApp = new DoctorPrescription.PrescriptionApplication();
            prescriptionApp.start(new Stage());
        });

        // Styling and adding buttons to the box
        styleButton(btnMessages, "#FFD580"); // Light green
        styleButton(btnCheckOldRecord, "#FFD580"); // Light blue
        styleButton(observationNotesButton, "#FFD580"); // Light pink
        styleButton(btnWritePrescription, "#FFD580"); // Lemon chiffon
        
       
        buttonsBox.getChildren().addAll(btnMessages, btnCheckOldRecord, btnWritePrescription, observationNotesButton);
        buttonsBox.setPadding(new Insets(15, 12, 15, 12));

        // Create the main layout
        VBox mainLayout = new VBox(headerPane, contentBox, buttonsBox);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1200, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pediatric Pulse");
        return scene;
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
            System.err.println("Failed to retrieve the latest patient name: " + e.getMessage());
        }

        return patientName;
    }

    
    private void displayLatestPatientInfo(Label lblPatientName, Label lblAge, Label lblHeight, Label lblBodyWeight, Label lblTemperature, Label lblBloodPressure, Label notes) {
        String patientName = getLastPatientName();
        String query = "SELECT * FROM nurse_records WHERE patient_name = ? ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, patientName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lblPatientName.setText("Patient Name: " + rs.getString("patient_name"));
                lblAge.setText("Age: " + rs.getInt("age"));
                lblHeight.setText("Height: " + rs.getInt("height") + " cm");
                lblBodyWeight.setText("Body Weight: " + rs.getInt("body_weight") + " kg");
                lblTemperature.setText("Body Temperature: " + rs.getDouble("body_temperature") + " °C");
                lblBloodPressure.setText("Blood Pressure: " + rs.getString("blood_pressure"));
                notes.setText(rs.getString("notes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving patient information");
        }
    }

    private String getPatientFirstName(String username)
    {
    	String firstName = "";
        String query = "SELECT first_name FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                firstName = rs.getString("first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve the patient's first name");
        }

        return firstName;
    }

    private VBox createSectionBox(String title)
    {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-border-radius: 5px;");
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-weight: bold;");
        box.getChildren().add(lblTitle);
        return box;
    }

    private void setButtonSize(Button button, double width, double height) {
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setMinHeight(height);
        button.setMaxHeight(height);
    }

    private void styleButton(Button button, String color)
    {
        button.setStyle("-fx-background-color: " + color + "; -fx-font-size: 18px;");
    }
}