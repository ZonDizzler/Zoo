/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johndistler.zoofinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * 
 * Enclosure - representation of a zoo, containing various enclosures
 * 
 * @author John Distler
 */
public class Zoo {

    ArrayList<Enclosure> enclosures;
    VBox node;
    Label label = new Label();
    /**
     * default constructor
     */
    public Zoo() {
        node = new VBox();
        enclosures = new ArrayList();
        node.getChildren().add(label);
        label.setText(getLabelString());

    }
    /**
     * Add enclosure to zoo
     * 
     * @param capacity - capacity of new Enclosure
     * @param minimumSize - minimum size of new Enclosure
     */
    public void addEnclosure(double capacity, double minimumSize) {
        Enclosure newEnclosure = new Enclosure(capacity, minimumSize);
        addEnclosure(newEnclosure);

    }
    /**
     * Add enclosure to zoo
     * 
     * @param e - enclosure to add
     */
    public void addEnclosure(Enclosure e) {

        enclosures.add(e);
        
        Node enclosureNode = e.getNode();
        
        node.getChildren().add(enclosureNode);

        FadeTransition fade = new FadeTransition(Duration.millis(500), enclosureNode);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);

        fade.play();
        label.setText(getLabelString());

    }
    /**
     * 
     * @param index - index of enclosure to remove
     * @return true if enclosure is removed
     * false if enclosure doesn't exist or cannot be removed
     */
    public boolean removeEnclosure(int index) {
        if (index > enclosures.size() || index < 0) {
            System.out.println("Enclosure index " + index + " does not exist.");
            return false;
        }

        if (!enclosures.get(index).isEmpty()) {
            System.out.println("Enclosure index " + index + " is not empty, cannot remove.");
            return false;
        }

        enclosures.remove(index);
        node.getChildren().clear();
        label.setText(getLabelString());
        node.getChildren().add(label);
        enclosures.forEach(e -> {
            node.getChildren().add(e.getNode());
        });
        return true;

    }
    /**
     * 
     * @return String containing data about the zoo
     */
    public String getLabelString() {
        return ("# of enclosures: " + enclosures.size());
    }
    /**
     * 
     * @param newAnimal - animal to be added
     * @return true if animal is added to an enclosure
     * false if animal is not added
     */
    public boolean addAnimal(Animal newAnimal) {

        for (Enclosure e : enclosures) {

            if (e.addAnimal(newAnimal)) {
                return true;
            }

        }
        return false;

    }
    /**
     * clearAnimals - remove all animals in the zoo
     */
    public void clearAnimals() {
        enclosures.forEach(e -> {
            e.clear();
        });
    }
    /**
     * clearEnclosures - remove all enclosures in Zoo
     */
    public void clearEnclosures() {

        enclosures.clear();
        node.getChildren().clear();
        label.setText(getLabelString());
    }
    /**
     * 
     * @param removeMe - animal to be removed
     * @return true if animal is found and removed
     * false if animal is not found or not removed
     */
    public boolean removeAnimal(Animal removeMe) {

        if (enclosures.stream().anyMatch(e -> (e.removeAnimal(removeMe)))) {
            return true;
        }
        return false;
    }
    /**
     * 
     * @return node representation of the zoo
     */
    public Node getNode() {
        return node;
    }
    /**
     * 
     * @return Array containing all the animals of the zoo
     */
    public Animal[] getAnimals() {
        ArrayList<Animal> listOfAnimals = new ArrayList();

        for (Enclosure e : enclosures) {
            Collections.addAll(listOfAnimals, e.getAnimals());

        }

        Object[] objectArray = listOfAnimals.toArray();
        Animal[] animalArray = Arrays.copyOf(objectArray, objectArray.length, Animal[].class);

        return animalArray;

    }
    /**
     * 
     * @return Array containing all the enclosures of the zoo
     */
    public Enclosure[] getEnclosures() {

        Object[] objectArray = enclosures.toArray();
        Enclosure[] enclosureArray = Arrays.copyOf(objectArray, objectArray.length, Enclosure[].class);

        return enclosureArray;

    }
    /**
     * 
     * 
     * @return String representation of the zoo
     */
    @Override
    public String toString() {

        ArrayList<Animal> listOfAnimals = new ArrayList();
        Collections.addAll(listOfAnimals, getAnimals());

        return listOfAnimals.toString();

    }

}
