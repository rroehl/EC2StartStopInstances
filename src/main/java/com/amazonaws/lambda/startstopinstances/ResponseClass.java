package com.amazonaws.lambda.startstopinstances;

public class ResponseClass {

    String instance;
    String region;

    public String getInstance() {
        return instance;
    }
    public void setInstance(String instance_id) {
        this.instance = instance_id;
    }
    
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    
    public ResponseClass(String region, String instance_id) 
    {
        this.instance = instance_id;
        this.region = region;
    }
    
    public ResponseClass() {
    }

}

