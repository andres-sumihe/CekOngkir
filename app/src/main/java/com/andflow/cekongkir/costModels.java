package com.andflow.cekongkir;

public class costModels {
    int  weight, no_id;
    String origin_id, destination_id, courier, origin_name, destination_name;

    public costModels(){

    }

    public costModels(String origin_id, String destination_id, int weight, String courier, String origin_name, String destination_name){
        this.origin_id = origin_id;
        this.destination_id = destination_id;
        this.weight = weight;
        this.courier = courier;
        this.origin_name =  origin_name;
        this.destination_name = destination_name;
    }

    public int getNo_id(){
        return no_id;
    }

    public void setNo_id(int no_id){
        this.no_id = no_id;
    }

    public String getOrigin_id(){
        return origin_id;
    }

    public void setOrigin_id(String origin_id){
        this.origin_id = origin_id;
    }

    public String getDestination_id(){
        return destination_id;
    }

    public void setDestination_id(String destination_id){
        this.destination_id = destination_id;
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getCourier(){
        return courier;
    }

    public void setCourier(String courier){
        this.courier = courier;
    }

    public String getOrigin_name(){
        return origin_name;
    }

    public void setOrigin_name(String origin_name){
        this.origin_name = origin_name;
    }

    public String getDestination_name(){
        return destination_name;
    }

    public void setDestination_name(String destination_name){
        this.destination_name = destination_name;
    }
}
