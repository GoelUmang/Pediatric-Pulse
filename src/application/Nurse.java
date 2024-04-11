package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class Nurse
{
	private String username;
	private String userType;
    
    public Nurse(String username, String userType)
	{
        this.username = username;
        this.userType = userType;
    }
	
	private TextField patientNameField;
    private TextField ageField;
    private TextField heightField;
    private TextField bodyWeightField;
    private TextField bodyTemperatureField;
    private TextField bloodPressureField;
    
    public Scene createScene()
    {
    	Stage primaryStage = new Stage();
    	String nurseName = getPatientFirstName(username);
    	
        // Header setup
        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(20, 10, 10, 10)); // top, right, bottom, left

        Label lblPediatricPulse = new Label("PEDIATRIC PULSE");
        lblPediatricPulse.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        Label lblWelcome = new Label("Welcome, Nurse " + nurseName); // TODO: link with the sql
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(event -> {
            Login login = new Login(primaryStage);
            primaryStage.setScene(login.createScene());
            primaryStage.show();
        });

        HBox leftHeader = new HBox(lblPediatricPulse);
        HBox rightHeader = new HBox(lblWelcome, btnLogout);
        rightHeader.setSpacing(20);

        headerPane.setLeft(leftHeader);
        headerPane.setRight(rightHeader);
        headerPane.setStyle("-fx-background-color: #336699;");

        // Main content layout
        HBox contentBox = new HBox(20); // Spacing between children
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setPadding(new Insets(10, 10, 10, 10)); // Padding around the HBox

        // Left side - Image
        Image image = new Image("file:/Users/sakethkoyi/Downloads/Nurse.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300); // Setting the height of the image
        imageView.setFitWidth(300); // Setting the width of the image
        VBox leftContentBox = new VBox(imageView);
        leftContentBox.setAlignment(Pos.TOP_CENTER);

        // Right side - Patient Info
        VBox rightContentBox = new VBox(10); // Spacing between children
        rightContentBox.setAlignment(Pos.TOP_CENTER);
        
        Label lblPatientName = new Label("Patient information form");
        lblPatientName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        GridPane patientInfoGrid = new GridPane();
        patientInfoGrid.setAlignment(Pos.CENTER_LEFT);
        patientInfoGrid.setHgap(20);
        patientInfoGrid.setVgap(10);

        // Add patient info fields to the grid
        patientNameField = addFormField(patientInfoGrid, "Patient Name:", 3, 2);
        ageField = addFormField(patientInfoGrid, "Age:", 3, 3);
        heightField = addFormField(patientInfoGrid, "Height:", 3, 4);
        bodyWeightField = addFormField(patientInfoGrid, "Body Weight:", 3, 5);
        bodyTemperatureField = addFormField(patientInfoGrid, "Body Temperature:", 3, 6);
        bloodPressureField = addFormField(patientInfoGrid, "Blood Pressure:",3, 7);

        // Buttons for record entry and doctor assignment
        Button enterRecordsBtn = new Button("Save Records");
        Button pastVisitInfo= new Button("Past Medical Records");
        Button message= new Button("Messages");
        
        TextArea txtEnterNotes = new TextArea();
        txtEnterNotes.setPromptText("Enter notes here...");


        HBox buttonBox = new HBox(69,enterRecordsBtn,pastVisitInfo,message);
        rightContentBox.getChildren().addAll(lblPatientName, patientInfoGrid, buttonBox,txtEnterNotes);
        
       
        // Add left and right content to the main content layout
        contentBox.getChildren().addAll(leftContentBox, rightContentBox);

        // Assemble the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerPane);
        mainLayout.setCenter(contentBox);
        
        enterRecordsBtn.setOnAction(e -> {
            saveNurseRecords(
                patientNameField.getText(),
                ageField.getText(),
                heightField.getText(),
                bodyWeightField.getText(),
                bodyTemperatureField.getText(),
                bloodPressureField.getText(),
                txtEnterNotes.getText()  // Pass the notes from the TextArea
            );
        });
        
        
       message.setOnAction(e -> {
    	   ChoiceBox sendMessage = new ChoiceBox(username, userType);
            Stage stage = new Stage();
            stage.setScene(sendMessage.createScene());
            stage.show();
        });
       
       
       pastVisitInfo.setOnAction(event -> {
      	   OldRecords oldRecords = new OldRecords();
           Scene oldRecordsscene = oldRecords.createScene();
           primaryStage.setScene(oldRecordsscene);
           primaryStage.show();
      });

        // Create the scene and place it in the stage
        Scene scene = new Scene(mainLayout, 1400, 700);
        primaryStage.setTitle("Nurse_View");
        primaryStage.setScene(scene);
        return scene;
    }

    private TextField addFormField(GridPane gridPane, String labelText, int colIndex, int rowIndex)
    {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        gridPane.add(label, colIndex, rowIndex);
        gridPane.add(textField, colIndex + 1, rowIndex);
        return textField;
    }
    
    private void saveNurseRecords(String patientName, String age, String height, String bodyWeight, String bodyTemperature, String bloodPressure, String notes)
    {
        String query = "INSERT INTO nurse_records (patient_name, age, height, body_weight, body_temperature, blood_pressure, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query))
        {
            
            pstmt.setString(1, patientName);
            pstmt.setString(2, age);
            pstmt.setString(3, height);
            pstmt.setString(4, bodyWeight);
            pstmt.setString(5, bodyTemperature);
            pstmt.setString(6, bloodPressure);
            pstmt.setString(7, notes);

            pstmt.executeUpdate();
            showAlert("Success", "Record saved successfully", Alert.AlertType.INFORMATION);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            showAlert("Error", "Failed to save record", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

}