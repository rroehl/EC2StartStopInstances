package com.amazonaws.lambda.startstopinstances;

public class RequestClass 
{
	String region;
	String name;
	String tag;
	String action;
	String timewaitseconds;
	String topicarn;
    /*
     * 
     	{
     	"region": "us-east-2",
	 	"name": "testlinux",
	 	"tag": "Name",
	 	"action": "Start",
	 	"timewaitseconds": "180",
	 	"topicarn": "arn:aws:sns:us-east-2:638139650817:TestTopic"
		}
   
     */
	
    public String getRegion() 
    {
        return region;
    }
    public void setRegion(String region) 
    {
        this.region = region; 
    }
    
    public String getName() 
    {
        return name;
    }
    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String getTag() 
    {
        return tag;
    }
    
    public void setTag(String tag) 
    {
        this.tag = tag;
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
    public RequestClass(String region, String name, String tag, String  action, String timewaitseconds,String topicarn)
    {
        this.region = region;
        this.name = name;
        this.tag = tag;
        this.action = action;
        this.timewaitseconds = timewaitseconds;
        this.topicarn = topicarn;
        
    }
    public RequestClass() {
    }

}
