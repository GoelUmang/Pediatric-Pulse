package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class nurse extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Header setup
        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(10, 20, 10, 20));

        Label lblPediatricPulse = new Label("Pediatric Pulse");
        lblPediatricPulse.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        Label lblWelcome = new Label("Welcome, Doctor Steven Dowry");
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        Button btnLogout = new Button("Logout");

        HBox leftHeader = new HBox(lblPediatricPulse);
        HBox rightHeader = new HBox(lblWelcome, btnLogout);
        rightHeader.setSpacing(10);

        headerPane.setLeft(leftHeader);
        headerPane.setRight(rightHeader);
        headerPane.setStyle("-fx-background-color: #DB7093;");
        // Create the GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Welcome label
        Label welcomeLabel = new Label("Welcome, Nurse");
        grid.add(welcomeLabel, 0, 0, 2, 1);

        // Patient information fields
        Label patientNameLabel = new Label("Patient Name:");
        grid.add(patientNameLabel, 0, 1);

        TextField patientNameTextField = new TextField();
        grid.add(patientNameTextField, 1, 1);

        Label ageLabel = new Label("Age:");
        grid.add(ageLabel, 0, 2);

        TextField ageTextField = new TextField();
        grid.add(ageTextField, 1, 2);

        Label sexLabel = new Label("Sex:");
        grid.add(sexLabel, 0, 3);

        TextField sexTextField = new TextField();
        grid.add(sexTextField, 1, 3);

        Label weightLabel = new Label("Body Weight:");
        grid.add(weightLabel, 0, 4);

        TextField weightTextField = new TextField();
        grid.add(weightTextField, 1, 4);

        Label temperatureLabel = new Label("Body Temperature:");
        grid.add(temperatureLabel, 0, 5);

        TextField temperatureTextField = new TextField();
        grid.add(temperatureTextField, 1, 5);

        Label bloodPressureLabel = new Label("Blood Pressure:");
        grid.add(bloodPressureLabel, 0, 6);

        TextField bloodPressureTextField = new TextField();
        grid.add(bloodPressureTextField, 1, 6);

        // Enter Records Button
        Button enterRecordsBtn = new Button("Enter Records");
        enterRecordsBtn.setOnAction(e -> {
            // Handle record entry
        });
        grid.add(enterRecordsBtn, 1, 7);

        // Assign Doctor Button (assuming the pop-up and other views are already created)
        Button assignDoctorBtn = new Button("Assign Doctor");
        assignDoctorBtn.setOnAction(e -> {
            // Handle doctor assignment
        });
        grid.add(assignDoctorBtn, 1, 8);

        // Create the scene and place it in the stage
        Scene scene = new Scene(grid, 900, 875);
        primaryStage.setTitle("Nurse View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
