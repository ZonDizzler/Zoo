package com.johndistler.zoofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App - Zoo App
 * 
 * Software representation of a Zoo
 * 
 * The Zoo has a number of Enclosures, which hold animals.
 * The user can add enclosures with a particular capacity and minimum size
 * The capacity determines the number of Animals the enclosure can hold
 * The minimum size is the smallest an Animal can be while in an enclosure
 * 
 * The user can also remove enclosures, based on its position in the zoo, starting with 0
 * The program will only allow the user to remove enclosures that hold no Animals
 * If one wishes to remove an enclosure with Animals, one must first remove the Animals in the enclosure
 * 
 * The user can also add Animals to the Zoo, with a particular Species name, diet, and size
 * As well, the user can import Animals from a JSON file.
 * It is possible to add duplicate animals to the zoo. This represents multiple of the same Animal
 * The program automatically determines which enclosure the Animal can fit in.
 * If the Animal fits in none of the Enclosures, the Animal is put into Overflow
 * 
 * When there are Animals in overflow, the user can opt to add one or more enclosures to the zoo that would fit them
 * The user can, move overflow animals to zoo, This attempts to place the animals in overflow into the zoo proper.
 * If there are no enclosures where an overflow Animal can fit, it remains in overflow
 * If there is an enclosure where an overflow animal can fit, it is moved to the enclosure
 * 
 * The user can export a JSON file representing the Animals. 
 * 
 * 
 * The user can remove animals from the Zoo, based on the animal's species name (case sensitive)
 * 
 * @author - John Distler (DistJC@farmingdale.edu)
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}