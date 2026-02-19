package com.example.labassignment2;

public class Boat {
    private String name;
    private String model;
    private int year;
    private double length;
    private String fuelType;
    private double price;

    public Boat(String name, String model, int year, double length, String fuelType, double price) {
        this.name = name;
        this.model = model;
        this.year = year;
        this.length = length;
        this.fuelType = fuelType;
        this.price = price;
    }

    public double getLength() {
        return length;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nModel: " + model + "\nYear: " + year +
                "\nLength: " + length + " feet\nFuel Type: " + fuelType +
                "\nPrice: $" + String.format("%.2f", price) + "\n";
    }
}
