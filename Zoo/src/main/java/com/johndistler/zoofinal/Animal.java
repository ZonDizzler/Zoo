/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johndistler.zoofinal;

import com.google.gson.annotations.SerializedName;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * Animal - representation of an Animal
 *
 * @author links
 */
public class Animal implements Comparable<Animal> {

    //species - name of animal, used for identification purposes
    @SerializedName("species")
    private String species;

    //diet - used to determine compatablility with other animals
    @SerializedName("diet")
    private char diet;

    //size - used to determine compatability with other animals and fitting into Enclosures
    @SerializedName("size")
    private double size;

    /**
     *
     * Animal - parameterized constructor
     *
     * @param species - species of animal
     * @param diet - diet of animal
     * @param size - size of animal
     * @throws Exception
     */
    public Animal(String species, char diet, double size) throws Exception {

        if (!setSpecies(species)) {
            throw new Exception();
        }

        if (!setDiet(diet)) {
            throw new Exception();
        }
        if (!setSize(size)) {
            throw new Exception();
        }

    }

    /**
     * Animal - default constructor
     */
    public Animal() {

        setSpecies("Default");
        setDiet('H');
        setSize(50);

    }

    /**
     * getNode - converts the Animal into a node, containing a label and circle
     *
     * @return node representation of an Animal
     */
    public Node getNode() {
        Circle circle = new Circle();
        Label label = new Label();
        StackPane stack = new StackPane();

        //node contains both a circle and a label
        stack.getChildren().add(circle);
        stack.getChildren().add(label);

        //radius of circle, and size of font is determined by animal size
        circle.setRadius(this.calculateRadius(1000 * size));
        label.setText(String.format("%s\n%s", species, String.format("%1$,.2f", size)));
        label.setFont(new Font("Comic Sans MS", (this.calculateRadius(1000 * size)) / 2));
        
        //Carnivores are red, Omnivores blue, Herbivores Green
        switch (diet) {

            case 'C' -> {
                circle.setFill(javafx.scene.paint.Color.RED);
            }
            case 'O' -> {
                circle.setFill(javafx.scene.paint.Color.BLUE);

            }
            case 'H' -> {
                circle.setFill(javafx.scene.paint.Color.GREEN);

            }

        }
        
        return stack;
    }

  
             /**
              * calculate radius of a circle given its area
              * @param area
              * @return 
              */
    private double calculateRadius(double area) {

        double areaOfCircle = area;
        
        //minimum area is 10000 pixels
        if (area < 10000) {
            areaOfCircle = 10000;
        }
        
        //maximum area is 50000 pixels
        if (area > 50000) {
            areaOfCircle = 50000;
        }

        return Math.sqrt(areaOfCircle) / Math.PI;
    }
    /**
     * Acessor  
     * @param species
     * @return true if String species is a valid species name
     * false if String species is an invalid species name
     */
    public boolean setSpecies(String species) {

        if (species.isBlank() || species.isEmpty()) {
            return false;
        }

        this.species = species;
        return true;
    }

    public String getSpecies() {
        return species;
    }

    public boolean setDiet(char diet) {

        if (diet != 'C' && diet != 'O' && diet != 'H') {
            return false;
        }

        this.diet = diet;
        return true;
    }

    public char getDiet() {
        return diet;
    }

    public boolean setSize(double size) {

        this.size = size;
        return true;
    }

    public double getSize() {
        return size;
    }

    public boolean compatableWith(Animal other) {
        switch (diet) {

            case 'C' -> {

                if (other.getSize() <= this.getSize()) {
                    return false;
                }

            }
            //Omnivores an be kept with animals that are at least 50% their size.
            case 'O' -> {

                if ((double) other.getSize() <= (0.5 * (double) this.getSize())) {
                    return false;
                }

            }
            //Herbivores are always compatable

            case 'H' -> {
            }

        }

        return true;
    }

    @Override
    public int compareTo(Animal other) {

        if (diet == other.getDiet()) {

            return Double.compare(size, other.getSize());

        }

        switch (diet) {

            case 'C' -> {
                return 1;
            }

            case 'O' -> {
                if (other.getDiet() == 'H') {
                    return 1;
                }
                return -1;
            }

            case 'H' -> {
                return -1;
            }

        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format("{ \"species\": \"%s\", \"diet\": \"%c\", \"size\": %s}", species, diet, String.format("%1$,.2f", size));
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {

        if (other instanceof Animal) {
            return ((Animal) other).getSpecies().equals(this.getSpecies());
        }
        return false;

    }

}
