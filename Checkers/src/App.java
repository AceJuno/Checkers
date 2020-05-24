import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class App extends Application {
    /*public static void main(String[] args) {
        launch(args);
    }*/



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");
        StackPane root = new StackPane();
        root.setPrefWidth(800);
        root.setPrefHeight(800);
        Canvas canvas = new Graphics();


        root.getChildren().add(canvas);
        canvas.widthProperty().bind(
                root.widthProperty());
        canvas.heightProperty().bind(
                root.heightProperty());
        Scene scene = new Scene(root, 1000, 1000);
        canvas.minWidth(800);
        canvas.minHeight(800);
        Image im = new Image("file:img/wood1.jpg");
        BackgroundSize sizeBack = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true);
        root.setBackground(new Background((new BackgroundImage(im, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, sizeBack))));

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(866);
        primaryStage.setMinHeight(889);
        primaryStage.show();
    }

}
