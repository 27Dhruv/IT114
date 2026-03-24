package com.example.project1;

import java.time.Year;

public class House {

    private static final double TAX_EXCLUSION_THRESHOLD = 10000.00;
    public static final String NEW_CONSTRUCTION = "new construction";
    public static final int MANSION_THRESHOLD = 7000;

    private String streetAddress;
    private String city;
    private String lotNumber;
    private double price;
    private double propertyTaxes;
    private int squareFootage;
    private int yearBuilt;

    public House(String streetAddress, String city, String lotNumber,
                 double price, double propertyTaxes, int squareFootage, int yearBuilt) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.lotNumber = lotNumber;
        this.price = price;
        this.propertyTaxes = propertyTaxes;
        this.squareFootage = squareFootage;
        this.yearBuilt = yearBuilt;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public double getPrice() {
        return price;
    }

    public double getPropertyTaxes() {
        return propertyTaxes;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public int getAge() {
        return Year.now().getValue() - yearBuilt;
    }

    public double getTaxExclusion() {
        if (propertyTaxes <= TAX_EXCLUSION_THRESHOLD) {
            return 0.0;
        }
        return propertyTaxes - TAX_EXCLUSION_THRESHOLD;
    }

    public String toDetailsString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Street Address: ").append(streetAddress).append("\n");
        sb.append("City: ").append(city).append("\n");
        sb.append("Price: $").append(String.format("%.2f", price)).append("\n");
        sb.append("Property Taxes: $").append(String.format("%.2f", propertyTaxes)).append("\n");
        sb.append("Square Footage: ").append(squareFootage).append("\n");

        if (getTaxExclusion() > 0) {
            sb.append("Tax Exclusion: $")
                    .append(String.format("%.2f", getTaxExclusion()))
                    .append("\n");
        }

        if (getAge() == 0) {
            sb.append("Age: ").append(NEW_CONSTRUCTION);
        } else {
            sb.append("Age: ").append(getAge()).append(" years");
        }

        return sb.toString();
    }
}