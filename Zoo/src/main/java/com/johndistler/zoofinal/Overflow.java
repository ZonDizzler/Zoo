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
 * Representation of an overflow of animals that do not fit in a zoo
 * 
 * @author John Distler
 */
public class Overflow {

    ArrayList<Animal> animals;

    Label label = new Label();

    HBox node = new HBox();

    public Overflow() {
        animals = new ArrayList<>();

        label.setText(getLabelText());
        node.getChildren().add(label);

    }

    public String getLabelText() {
        return String.format("Overflow Animals: %d", animals.size());
    }

    public void addAnimal(Animal newAnimal) {

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

    }

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

    public Node getNode() {
        return node;

    }

    public Animal[] getAnimals() {
        ArrayList<Animal> listOfAnimals = new ArrayList();

        animals.forEach(currentAnimal -> {
            listOfAnimals.add(currentAnimal);
        });
        Object[] objectArray = listOfAnimals.toArray();
        Animal[] animalArray = Arrays.copyOf(objectArray, objectArray.length, Animal[].class);

        return animalArray;
    }

    public void clearAnimals() {
        animals.clear();

        while (node.getChildren().size() > 0) {
            node.getChildren().remove(0);
        }

        node.getChildren().add(label);
        label.setText(getLabelText());

    }

}
