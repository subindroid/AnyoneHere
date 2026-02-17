package dto;

import java.io.Serializable;

public class Spot implements Serializable {

    private int spotId;       // spot_id
    private String spotName;     // spot_name
    private double spotLatitude;         // latitude
    private double spotLongitude;         // longitude
    private double radiusM;       // radius_m
    private String spotDescription;  // spot_description
    private String spotImage;
    private String spotCategory;


    public Spot() {
        super();
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public double getSpotLatitude() {
        return spotLatitude;
    }

    public void setSpotLatitude(double spotLatitude) {
        this.spotLatitude = spotLatitude;
    }

    public double getSpotLongitude() {
        return spotLongitude;
    }

    public void setSpotLongitude(double spotLongitude) {
        this.spotLongitude = spotLongitude;
    }

    public double getRadiusM() {
        return radiusM;
    }

    public void setRadiusM(double radiusM) {
        this.radiusM = radiusM;
    }

    public String getSpotDescription() {
        return spotDescription;
    }

    public void setSpotDescription(String spotDescription) {
        this.spotDescription = spotDescription;
    }

    public String getSpotImage() {
        return spotImage;
    }

    public void setSpotImage(String spotImage) {
        this.spotImage = spotImage;
    }

    public String getSpotCategory() {
        return spotCategory;
    }

    public void setSpotCategory(String spotCategory) {
        this.spotCategory = spotCategory;
    }

    public Spot(int spotId, String spotName) {
        this.spotId = spotId;
        this.spotName = spotName;
    }

    public Spot(int spotId, String spotName, double spotLatitude,
                double spotLongitude, double radiusM, String spotDescription) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.spotLatitude = spotLatitude;
        this.spotLongitude = spotLongitude;
        this.radiusM = radiusM;
        this.spotDescription = spotDescription;


    }



}
