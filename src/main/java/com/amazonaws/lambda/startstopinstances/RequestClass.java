package com.amazonaws.lambda.startstopinstances;

public class RequestClass 
{
	String region;
	String instance;
	String action;
	String timewaitseconds;
	String topicarn;
	
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) 
    {
        this.region = region; 
    }
    
    public String getInstance() {
        return instance;
    }
    public void setInstance(String instance) {
        this.instance = instance;
    }
    
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    } 
    public String getTimewaitseconds() {
        return timewaitseconds;
    }
    public void setTimewaitseconds(String timewaitseconds) 
    {
        this.timewaitseconds = timewaitseconds; 
    }
  
    public String getTopicarn() {
        return topicarn;
    }
    public void setTopicarn(String topicarn) 
    {
        this.topicarn = topicarn; 
    }
    public RequestClass(String region, String instance, String  action, String timewaitseconds,String topicarn)
    {
        this.region = region;
        this.instance = instance;
        this.action = action;
        this.timewaitseconds = timewaitseconds;
        this.topicarn = topicarn;
    }
    public RequestClass() {
    }

}
