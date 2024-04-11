package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;

public class Login
{
	private Stage primaryStage;
	
	// Constructor to accept the primary stage
    public Login(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
	
    public Scene createScene()
    {
        primaryStage.setTitle("Pediatric Pulse");
       
        
        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #A9A9A9;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(40));
        
        // Login Page
        Label loginPageLabel = new Label("       Login");
        loginPageLabel.setAlignment(Pos.CENTER);
        loginPageLabel.setStyle("-fx-font-size: 34px; -fx-font-weight: bold;");
        GridPane.setConstraints(loginPageLabel, 1, 0);

        // User type combo box
        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Doctor", "Nurse", "Patient");
        userTypeCombo.setPromptText("User Type");
        GridPane.setConstraints(userTypeCombo, 1, 1); // column=1, row=0

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane.setConstraints(emailLabel, 1, 2);
        
        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        GridPane.setConstraints(emailInput, 1, 3); // column=1, row=1

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane.setConstraints(passwordLabel, 1, 4);
        
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        GridPane.setConstraints(passwordInput, 1, 5); // column=1, row=2

        // Date picker
        Label dobLabel = new Label("Date of birth");
        dobLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        GridPane.setConstraints(dobLabel, 1, 6);
        
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("MM/DD/YYYY");
        GridPane.setConstraints(dobPicker, 1, 7); // column=1, row=3

        // Log in and Sign up buttons
        Button logInButton = new Button("Log In");
        Button signUpButton = new Button("Sign Up");
        HBox buttonBox = new HBox(12, logInButton, signUpButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(buttonBox, 1, 8); // column=1, row=4
        
        /*// 'Forgot Password' hyperlink
        Hyperlink forgotPasswordLink = new Hyperlink("             Forgot Password?             ");
        forgotPasswordLink.setStyle("-fx-font-size: 14px;");
        GridPane.setConstraints(forgotPasswordLink, 1, 9);*/
        
        // Button actions
        
        // login
        logInButton.setOnAction(event -> {
            String userType = userTypeCombo.getValue();
            String username = emailInput.getText();
            String password = passwordInput.getText();
            LocalDate dob = dobPicker.getValue();
            
            if (username.isEmpty() || password.isEmpty() || dob == null || userType == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out all fields.");
                alert.showAndWait();
                return;
            }

            boolean loginSuccessful = handleLogin(username, password, dob, userType);

            if (loginSuccessful)
            {
            	UserSession.getInstance(username, userType);
            	
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username + "!");
                alert.showAndWait();
                
                switch (userType)
                {
                case "Doctor":
                	Doctor doctor = new Doctor(username, userType);
                    Scene doctorScene = doctor.createScene();
                    primaryStage.setScene(doctorScene);
                    primaryStage.show();
                    break;
                case "Nurse":
                	Nurse nurse = new Nurse(username, userType);
                    Scene nurseScene = nurse.createScene();
                    primaryStage.setScene(nurseScene);
                    primaryStage.show();
                    break;
                case "Patient":
                    Patient patient = new Patient(username, userType);
                    primaryStage.setScene(patient.createScene());
                    primaryStage.show();
                    break;
                default:
                    System.out.println("Invalid user type.");
                    break;
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid credentials. Please try again.");
                alert.showAndWait();
            }
        });
        
        // sign-up
        signUpButton.setOnAction(event -> {
            Signup signup = new Signup(primaryStage);
            Scene signupScene = signup.createScene();
            primaryStage.setScene(signupScene);
            primaryStage.show();
        });


        // Adding all nodes to the gridPane
        gridPane.getChildren().addAll(loginPageLabel, userTypeCombo, emailLabel, emailInput, passwordLabel, passwordInput, dobLabel, dobPicker, buttonBox);
        
        // Company Label
        Label companyName = new Label("Pediatric Pulse");
        companyName.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(companyName, 35.0); // Adjust this value to set the vertical position
        AnchorPane.setLeftAnchor(companyName, 600.0);
        
        // AnchorPane layout
        AnchorPane rootPane = new AnchorPane();
        rootPane.setStyle("-fx-background-color: #D3D3D3;");
        rootPane.getChildren().addAll(companyName, gridPane); // place the company name and the GridPane on the AnchorPane

        // Use AnchorPane to position GridPane
        AnchorPane.setTopAnchor(gridPane,130.0);
        AnchorPane.setRightAnchor(gridPane, 540.0);

        // return scene
        return new Scene(rootPane, 1400, 700);
    }
    
    public boolean handleLogin(String username, String password, LocalDate dob, String role)
    {
    	String dobStr = dob.format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Directly comparing the password, which is not secure and not recommended for production
        String query = "SELECT * FROM users WHERE username = ? AND dob = ? AND role = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query))
        {

            pstmt.setString(1, username);
            pstmt.setString(2, dobStr);
            pstmt.setString(3, role);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    // For demonstration purposes, we're doing a simple comparison
                    // In production, use a secure method for password verification
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}