package com.amazonaws.lambda.startstopinstances;

public class ResponseClass {
	
    String region;
    String name;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    
    public ResponseClass(String region, String name) 
    {
        this.name = name;
        this.region = region;
    }
    
    public ResponseClass() {
    }

}

