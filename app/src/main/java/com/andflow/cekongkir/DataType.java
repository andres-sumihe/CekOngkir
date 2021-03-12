package com.andflow.cekongkir;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class DataType implements Serializable {
    @SerializedName("service")
    @Expose
    public String service;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("cost")
    @Expose
    public List<DataCost> cost = null;

    public DataType(String service, String description, List<DataCost> cost){
        this.service = service;
        this.description = description;
        this.cost = cost;
    }
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DataCost> getCost() {
        return cost;
    }

    public void setCost(List<DataCost> cost) {
        this.cost = cost;
    }
}
