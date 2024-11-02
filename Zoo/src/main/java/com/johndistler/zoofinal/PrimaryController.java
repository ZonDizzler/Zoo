package com.johndistler.zoofinal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;

/**
 *
 * Primary controller of the Zoo app
 *
 * @author John Distler
 */
public class PrimaryController {

    Scanner sc;

    @FXML
    HBox zooParent;

    Zoo zoo;

    @FXML
    HBox overflowParent;

    Overflow overflow;

    @FXML
    /**
     * initalize - initialize the zoo, by importing enclosures and animals from
     * databases
     */
    private void initialize() {

        sc = new Scanner(System.in);
        zoo = new Zoo();
        overflow = new Overflow();

        zooParent.getChildren().add(zoo.getNode());
        overflowParent.getChildren().add(overflow.getNode());

        try {
            importEnclosuresFromDatabase("jdbc:ucanaccess://.//Enclosures.accdb");
            importAnimalsFromDatabase("jdbc:ucanaccess://.//Animals.accdb");

        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * importEnclosuresFromDatabase - imports enclosure objects from database to
     * the zoo
     *
     * @param databaseURL - the url of the database
     * @throws SQLException
     */
    private void importEnclosuresFromDatabase(String databaseURL) throws SQLException {

        Connection conn;
        conn = DriverManager.getConnection(databaseURL);

        String tableName = "Enclosures";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("select * from " + tableName);
        zoo.clearEnclosures();
        while (result.next()) {

            int ID = result.getInt("ID");

            double capacity = result.getDouble("Capacity");

            double minimumSize = result.getDouble("Minimum");

            //System.out.println(id + capacity + minimumSize);
            Enclosure newEnclosure = new Enclosure(capacity, minimumSize);
            zoo.addEnclosure(newEnclosure);

        }
    }

    @FXML
    /**
     * handler for moving animals to zoo
     */
    private void handleMoveOverflowAnimalsToZooAction() {

        ArrayList<Animal> listOfAnimals = new ArrayList();
        Collections.addAll(listOfAnimals, overflow.getAnimals());
        overflow.clearAnimals();

        for (Animal newAnimal : listOfAnimals) {
            addAnimal(newAnimal);
        }
    }

    /**
     * imports Animals from database to zoo
     *
     * @param databaseURL - url of database
     * @throws SQLException - when there is a SQL issue
     */
    private void importAnimalsFromDatabase(String databaseURL) throws SQLException {

        Connection conn;
        conn = DriverManager.getConnection(databaseURL);

        String tableName = "Animals";
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("select * from " + tableName);

        zoo.clearAnimals();

        while (result.next()) {

            int id = result.getInt("ID");

            String species = result.getString("Species");

            char diet = result.getString("Diet").charAt(0);

            double size = result.getDouble("Size");

            try {
                //System.out.println(id + capacity + minimumSize);
                Animal newAnimal = new Animal(species, diet, size);

                if (!zoo.addAnimal(newAnimal)) {
                    overflow.addAnimal(newAnimal);
                }

            } catch (Exception ex) {
                System.out.println("Invalid animal");
            }
        }
    }

    @FXML
    /**
     * closes app
     */
    private void handleCloseAction() throws IOException {
        Platform.exit();
    }

    /**
     * prompts the user for data about an animal
     *
     * @return Animal based off of User input
     */
    private Animal promptForAnimalData() {
        Animal newAnimal = new Animal();
        String species;
        char diet;
        double size;
        species = promptForString("Add Animal", "Enter Species Name");

        while (!newAnimal.setSpecies(species)) {
            System.out.println("Invalid species name");
            species = promptForString("Add Animal", "Enter Species Name");

        }

        diet = promptForString("Add Animal", "Enter Diet").charAt(0);

        while (!newAnimal.setDiet(diet)) {
            System.out.println("Invalid diet");
            diet = promptForString("Add Animal", "Enter Diet").charAt(0);

        }

        size = Double.parseDouble(promptForString("Add Animal", "Enter Size"));

        while (!newAnimal.setSize(size)) {
            System.out.println("Invalid size");
            size = Double.parseDouble(promptForString("Add Animal", "Enter Size"));

        }

        return newAnimal;
    }

    @FXML
    /**
     * handler for removing an animal
     */
    private void handleRemoveAnimalAction() {

        Animal removeMe = new Animal();
        String species;
        char diet;
        double size;
        species = promptForString("Remove Animal", "Enter Species Name");

        while (!removeMe.setSpecies(species)) {
            System.out.println("Invalid species name");
            species = promptForString("Remove Animal", "Enter Species Name");

        }
        Platform.runLater(() -> {
            removeAnimal(removeMe);
        });
    }

    @FXML
    /**
     * handler for removing an enclosure
     */
    public void handleRemoveEnclosureAction() {
        Platform.runLater(() -> {
            removeEnclosure(Integer.parseInt(promptForString("Remove Enclosure", "Enter Enclosure Index")));
        });
    }

    /**
     *
     * prompts the user for data about an Enclosure
     *
     * @return Enclosure based off of User input
     */
    private Enclosure promptForEnclosureData() {
        Enclosure newEnclosure = new Enclosure();

        double capacity;
        double minimumSize;
        capacity = Double.parseDouble(promptForString("Add Enclosure", "Enter Capacity"));

        while (!newEnclosure.setCapacity(capacity)) {
            System.out.println("Invalid capacity");
            capacity = Double.parseDouble(promptForString("Add Enclosure", "Enter Capacity"));

        }

        minimumSize = Double.parseDouble(promptForString("Add Enclosure", "Enter Minimum Size"));

        while (!newEnclosure.setMinimumSize(minimumSize)) {
            System.out.println("Invalid minimum size");
            minimumSize = Double.parseDouble(promptForString("Add Enclosure", "Enter Minimum Size"));

        }

        return newEnclosure;
    }

    @FXML
    /**
     * handler for adding an enclosure
     */
    private void handleAddEnclosureAction() {
        Platform.runLater(() -> {
            Enclosure newEnclosure = promptForEnclosureData();

            zoo.addEnclosure(newEnclosure);

            addEnclosureToDatabase("jdbc:ucanaccess://.//Enclosures.accdb", newEnclosure);
        });

    }

    /**
     * removes enclosure
     *
     * @param index - index of enclosure do be removed
     */
    public void removeEnclosure(int index) {

        if (zoo.removeEnclosure(index)) {

            clearDatabase("jdbc:ucanaccess://.//Enclosures.accdb", "Enclosures");
            ArrayList<Enclosure> listOfEnclosures = new ArrayList();
            Collections.addAll(listOfEnclosures, zoo.getEnclosures());

            listOfEnclosures.forEach(newEnclosure -> {
                addEnclosureToDatabase("jdbc:ucanaccess://.//Enclosures.accdb", newEnclosure);
            });

        }

    }

    /**
     * adds Enclosure to database
     *
     * @param databaseURL - url of database
     * @param newEnclosure - enclosure to be added
     */
    private void addEnclosureToDatabase(String databaseURL, Enclosure newEnclosure) {
        try {
            Connection conn;
            conn = DriverManager.getConnection(databaseURL);
            String sql = "INSERT INTO Enclosures (Capacity, Minimum) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, newEnclosure.getCapacity());
            preparedStatement.setDouble(2, newEnclosure.getMinimumSize());
            int row = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * removes animal from database and zoo
     *
     * @param removeMe - animal to be removed
     */
    public void removeAnimal(Animal removeMe) {
        if (zoo.removeAnimal(removeMe)) {
            clearDatabase("jdbc:ucanaccess://.//Animals.accdb", "Animals");

            ArrayList<Animal> listOfAnimals = new ArrayList();
            Collections.addAll(listOfAnimals, zoo.getAnimals());

            listOfAnimals.forEach(a -> {
                addAnimalToDatabase("jdbc:ucanaccess://.//Animals.accdb", a);
            });
            System.out.println("Animal removed from Zoo");
        } else if (overflow.removeAnimal(removeMe)) {
            System.out.println("Animal removed from Overflow");

        } else {
            System.out.println("Animal not found");
        }

    }

    @FXML
    /**
     * handler for adding an animal
     */
    private void handleAddAnimalAction() {
        Platform.runLater(() -> {
            addAnimal(promptForAnimalData());
        });
    }

    /**
     * add an animal to the zoo
     *
     * @param newAnimal - animal to be added
     */
    public void addAnimal(Animal newAnimal) {
        if (zoo.addAnimal(newAnimal)) {
            addAnimalToDatabase("jdbc:ucanaccess://.//Animals.accdb", newAnimal);

        } else {
            overflow.addAnimal(newAnimal);
        }
    }

    /**
     * add Animal to database
     *
     * @param databaseURL - url of database
     * @param newAnimal - animal to be added
     */
    private void addAnimalToDatabase(String databaseURL, Animal newAnimal) {
        try {
            Connection conn;
            conn = DriverManager.getConnection(databaseURL);
            String sql = "INSERT INTO Animals (Species, Diet, Size) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newAnimal.getSpecies());
            preparedStatement.setString(2, String.valueOf(newAnimal.getDiet()));
            preparedStatement.setDouble(3, newAnimal.getSize());
            int row = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Couldn't add animal");
        }
    }

    @FXML
    /**
     * handler for displaying animals
     */
    private void handleDisplayAnimalsAction() {
        System.out.println(zoo);
    }

    /**
     * prompt for a string
     *
     * @param dialog
     * @param prompt
     * @return
     */
    public String promptForString(String dialog, String prompt) {

        TextInputDialog td = new TextInputDialog(prompt);
        td.setHeaderText(prompt);

        td.showAndWait();

        // set the text of the label
        System.out.println(td.getEditor().getText());
        return td.getEditor().getText();

    }

    @FXML
    /**
     * handler for inporting animals from JSON
     */
    private void handleImportFromJsonAction() {
        Platform.runLater(() -> {

            String fileName = promptForString("Import from JSON file", "Enter JSON filename");

            importFromJson(fileName);
        });
    }

    /**
     * inport animals to zoo from JSON
     *
     * @param fileName - fileName of JSON
     */
    private void importFromJson(String fileName) {

        GsonBuilder builder;
        Gson gson;
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();

        try {
            FileReader fr;

            fr = new FileReader(fileName);

            Animal[] tempArray = gson.fromJson(fr, Animal[].class);

            //clear the previous list, copy the new VideoGames over
            ArrayList<Animal> listOfAnimals = new ArrayList();
            Collections.addAll(listOfAnimals, tempArray);

            listOfAnimals.forEach(a -> {
                addAnimal(a);
            });
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        }

    }

    @FXML
    /**
     * handler for exporting animals to json
     */
    private void handleExportToJsonAction() {
        Platform.runLater(() -> {

            String fileName = promptForString("Export to JSON file", "Enter JSON filename");

            exportToJson(fileName);

        });
    }

    /**
     * export animals to zoo to JSON
     *
     * @param fileName - fileName of JSON
     */
    private void exportToJson(String fileName) {
        GsonBuilder builder;
        Gson gson;

        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        String jsonString = gson.toJson(zoo.getAnimals());

        //String jsonString = gson.toJson(new Animal("Frog"));
        //String jsonString = listOfAnimals.toArray()[0].toString();
        try {
            PrintStream ps;
            ps = new PrintStream(fileName);
            ps.println(jsonString);
            ps.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error!");
        }
    }

    @FXML
    /**
     * handler for clearing animals
     */
    public void handleClearAnimalsAction() {
        Platform.runLater(() -> {
            zoo.clearAnimals();
            overflow.clearAnimals();
            clearDatabase("jdbc:ucanaccess://.//Animals.accdb", "Animals");
        });

    }
    /**
     * clear database
     * 
     * @param databaseURL - url of database to be cleared
     * @param tableName - table to be deleted
     */
    private void clearDatabase(String databaseURL, String tableName) {

        try {
            Connection conn;
            conn = DriverManager.getConnection(databaseURL);
            Statement stmt = conn.createStatement();

            stmt.execute("DELETE FROM " + tableName);
        } catch (SQLException except) {
            System.out.println("Could not delete table");
            except.printStackTrace();
        }

    }

}
