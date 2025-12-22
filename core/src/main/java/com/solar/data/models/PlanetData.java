package com.solar.data.models;

public class PlanetData {
    //Libgx read faster
    //Data Transfer Object:
    //public since its just a data class n have no logic to protected

    public String id;
    public String name;
    public float gravity; //may be private
    public float radius;
    public String texturePath;
    public String description;

    public PlanetData(){}
}
