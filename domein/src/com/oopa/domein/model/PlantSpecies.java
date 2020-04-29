package com.oopa.domein.model;

import com.oopa.interfaces.model.IPlantSpecies;

public class PlantSpecies implements IPlantSpecies {

    private int id;
    private String name;
    private int minHumidity;
    private int maxHumidity;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public int getMinHumidity() {
        return minHumidity;
    }

    @Override
    public void setMinHumidity(int minHumidity) {

    }

    @Override
    public int getMaxHumidity() {
        return maxHumidity;
    }

    @Override
    public void setMaxHumidity(int maxHumidity) {

    }
}