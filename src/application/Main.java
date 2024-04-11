package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
	    primaryStage.setTitle("Pediatric Pulse");

	    Login login = new Login(primaryStage);
	    Scene loginScene = login.createScene();
	    primaryStage.setScene(loginScene);
	    primaryStage.show();
	}


    public static void main(String[] args)
    {
        launch(args);
    }
}