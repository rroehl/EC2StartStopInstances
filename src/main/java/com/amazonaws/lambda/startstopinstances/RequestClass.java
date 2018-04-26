package com.amazonaws.lambda.startstopinstances;

public class RequestClass 
{
	String region;
	String instancename;
	String instancetagname;
	String action;
	String timewaitseconds;
	String topicarn;
    /*
     * 
     	{
     	"region": "us-east-2",
	 	"instancename": "testlinux",
	 	"instancetagname": "Name",
	 	"action": "Start",
	 	"timewaitseconds": "180",
	 	"topicarn": "arn:aws:sns:us-east-2:638139650817:TestTopic"
		}
   
     */
	
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) 
    {
        this.region = region; 
    }
    
    public String getInstanceName() {
        return instancename;
    }
    public void setInstanceName(String instancename) {
        this.instancename = instancename;
    }
    
    public String getInstanceTagName() {
        return instancetagname;
    }
    public void setIInstanceTagName(String instancetagname) {
        this.instancetagname = instancetagname;
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
    public RequestClass(String region, String instancename, String instancetagname, String  action, String timewaitseconds,String topicarn)
    {
        this.region = region;
        this.instancename = instancename;
        this.instancetagname = instancetagname;
        this.action = action;
        this.timewaitseconds = timewaitseconds;
        this.topicarn = topicarn;
        
    }
    public RequestClass() {
    }

}
