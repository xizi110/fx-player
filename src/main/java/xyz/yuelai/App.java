package xyz.yuelai;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xyz.yuelai.view.MainView;

/**
 * Hello world!
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView view = View.createView(MainView.class);
        Scene scene = new Scene(view.getRoot());
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
