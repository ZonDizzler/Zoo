/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johndistler.zoofinal;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * 
 * Enclosure - representation of a zoo enclosure, containing various animals
 * 
 * @author John Distler
 */
public class Enclosure {

    ArrayList<Animal> animals;

    double capacity;

    double minimumSize;

    Label label = new Label();

    HBox node = new HBox();

    /**
     * Default constructor
     */
    public Enclosure() {
        animals = new ArrayList<>();
        capacity = 1000;
        minimumSize = 10;

        label.setText(getLabelText());

        node.getChildren().add(label);

    }
    /**
     * parameterized constructor
     * @param capacity
     * @param minimumSize 
     */
    public Enclosure(double capacity, double minimumSize) {

        this.minimumSize = minimumSize;
        animals = new ArrayList<>();
        this.capacity = capacity;

        label.setText(getLabelText());
        node.getChildren().add(label);
    }
    /**
     * 
     * @return String with information about the enclosure
     */
    public String getLabelText() {
        return String.format("%s / %s used capacity\nMinimum size: %s",
                String.format("%1$,.2f", getUsedCapacity()),
                String.format("%1$,.2f", capacity),
                String.format("%1$,.2f", minimumSize));
    }
    /**
     * 
     * 
     * @param newAnimal - Animal to add to enclosure
     * @return True if Animal was sucessfully added
     * False if animal was not added
     */
    public boolean addAnimal(Animal newAnimal) {
        
        //check if there is capacity left for new Animal
        if (getUsedCapacity() + newAnimal.getSize() > capacity) {
            return false;
        }
        
        if (newAnimal.getSize() < minimumSize){
            return false;
        }
        
        //check if Animal is compatable with other Animals in enclosure
        for (Animal otherAnimal : animals) {

            if (!otherAnimal.compatableWith(newAnimal) || !newAnimal.compatableWith(otherAnimal)) {
                return false;
            }

        }
        
        animals.add(newAnimal);

        Node animalNode = newAnimal.getNode();

        node.getChildren().add(animalNode);

        FadeTransition fade = new FadeTransition(Duration.millis(500), animalNode);

        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);

        fade.play();

        label.setText(getLabelText());
        return true;
    }
    /**
     * 
     * @param newAnimal - Animal to be removed from enclosure
     * @return True if Animal was sucessfully removed
     * False if animal was not found
     */
    public boolean removeAnimal(Animal removeMe) {

        for (Animal current : animals) {

            if (current.equals(removeMe)) {

                animals.remove(current);

                //remove all nodes, before re-adding them them
                while (node.getChildren().size() > 0) {
                    node.getChildren().remove(0);
                }

                node.getChildren().add(label);

                //re-add all nodes
                animals.forEach(a -> {
                    node.getChildren().add(a.getNode());
                });

                //update label
                label.setText(getLabelText());

                return true;

            }

        }
        return false;
    }
    /**
     * 
     * @return used capacity of enclosure
     */
    public double getUsedCapacity() {
        return animals.stream().map(currentAnimal -> currentAnimal.getSize()).reduce(0.0, Double::sum);
    }
    /**
     * 
     * 
     * @return total capacity of enclosure
     */
    public double getCapacity() {
        return capacity;
    }
    /**
     * 
     * @param capacity - new capacity
     * @return true if new value is valid
     * false if invalid
     */
    public boolean setCapacity(double capacity) {

        if (capacity <= 0) {
            return false;
        }

        this.capacity = capacity;
        return true;
    }
    
    /**
     * 
     * @return minimumSize in enclosure
     */
    public double getMinimumSize() {
        return minimumSize;
    }
    
    /**
     * 
     * @param minimumSize - new minimum size
     * @return true if new size is valid
     */
    public boolean setMinimumSize(double minimumSize) {

        if (minimumSize < 0) {
            return false;
        }

        this.minimumSize = minimumSize;
        return true;
    }
    /**
     * 
     * @return Node representation of enclosure
     */
    public Node getNode() {
        return node;

    }
    /**
     * 
     * @return true if enclosure is empty
     */
    public boolean isEmpty() {
        return animals.isEmpty();
    }
    /**
     * clears enclosure of animals
     */
    public void clear() {
        animals.clear();

        while (node.getChildren().size() > 0) {
            node.getChildren().remove(0);
        }

        node.getChildren().add(label);
        label.setText(getLabelText());

    }
    /**
     * 
     * @return Array containing all animals in enclosure
     */
    public Animal[] getAnimals() {
        ArrayList<Animal> listOfAnimals = new ArrayList();

        for (Animal currentAnimal : animals) {
            listOfAnimals.add(currentAnimal);
        }
        Object[] objectArray = listOfAnimals.toArray();
        Animal[] animalArray = Arrays.copyOf(objectArray, objectArray.length, Animal[].class);

        return animalArray;
    }
}
