package com.amazonaws.lambda.startstopinstances;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.regions.Regions;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;


public class LambdaFunctionHandler implements RequestHandler<RequestClass, ResponseClass> 
{
	private String topicArn;
	private String region;
	private String instance_id;
	private String action;
	
	private void  PublishAlert(String msg, String topicArn, String region,Context context)
	{	
		try
		{		//Check to see if SNS topic ARN provided
			if( topicArn != null && !topicArn.isEmpty())
			{
				AmazonSNS snsClient = AmazonSNSClient.builder()                          
													.withRegion(region)
													.build(); 
				//publish to an SNS topic
				PublishRequest publishRequest = new PublishRequest(topicArn, msg);
				PublishResult publishResult = snsClient.publish(publishRequest);
				//Log to console  MessageId of message published to SNS topic
				context.getLogger().log("MessageId - " + publishResult.getMessageId());
			}
		}
		catch(AmazonServiceException ase) 
		{
			context.getLogger().log("Target Topic ARN: " + topicArn);
			context.getLogger().log("Caught Exception: " + ase.getMessage());
			context.getLogger().log("Reponse Status Code: " + ase.getStatusCode());
			context.getLogger().log("Error Code: " + ase.getErrorCode());
			context.getLogger().log("Request ID: " + ase.getRequestId());
		}
		return;
	}
	private int GetInstanceState( AmazonEC2 ec2 ) 
	{
	   int ret_val = -1;
		try
		{
		   DescribeInstancesRequest describeInstanceRequest = 
		    			new DescribeInstancesRequest().withInstanceIds(instance_id);
		    
		   DescribeInstancesResult describeInstanceResult = 
		    		                           ec2.describeInstances(describeInstanceRequest);
		   InstanceState state = 
		    		describeInstanceResult.getReservations().get(0).getInstances().get(0).getState();		
		 ret_val = state.getCode();
		}
			
		catch (AmazonServiceException ase) 
		{
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}
			
		return ret_val;
	}
	
	private void CheckInstanceFinalStatusStatus( AmazonEC2 ec2, String sleep_seconds, Context context)
	{
		Integer instance_state;
		int time_millisec;
		String sz_state;
		String sz_status;
		
		try 
		{
			//Wait for the instance to start or stop
			time_millisec = Integer.parseInt(sleep_seconds) * 1000;	
			Thread.sleep(time_millisec);
		
			switch((instance_state =  GetInstanceState( ec2 )))
			{
				case 0 : {sz_state = "pending";} break;
				case 16 : {sz_state = "running";} break;			
				case 32 : {sz_state = "shutting down";} break;
				case 48 : {sz_state = "terminated";} break;
				case 64 : {sz_state = "stopping";} break;
				case 80 : {sz_state = "stopped";} break;
				default:{sz_state = "unknown state";} break;
			}
			if(this.action.equalsIgnoreCase("start"))   // Instance was directed to start
			{
				if ( instance_state == 16 ) // Instance should be in the started state
				{
					sz_status = "The " + this.instance_id + " was sucessfully started.\n";
				}
				else
				{
				//Alert Instance was not successfully started
					sz_status = "******* The " + this.instance_id + " instance was NOT sucessfully started in " + sleep_seconds + " seconds. It is in the "+ sz_state + " state!\n";
					
					PublishAlert(sz_status, topicArn,  region, context);
				}
			}
			else
			{
				if(this.action.equalsIgnoreCase("stop")) // Instance was directed to stop
				{
					if ( instance_state == 80 ) // Instance should be in the stopped state
					{
						sz_status = "The " + this.instance_id + " was sucessfully stopped.\n";
					}
					else
					{
						//Alert Instance was not successfully started
						sz_status = "******* The " + this.instance_id + " instance was NOT sucessfully stopped in " + sleep_seconds + " seconds. It is in the "+ sz_state + " state!\n";
						PublishAlert(sz_status, topicArn,  region, context);
					}
				}
				else
				{
					sz_status = "The " + this.instance_id + "  is in the "+ sz_state + " state.\n";
				}
		
			}
			context.getLogger().log(sz_status);
		} 
		catch (AmazonServiceException ase) 
		{
			context.getLogger().log("Caught Exception: " + ase.getMessage());
			context.getLogger().log("Reponse Status Code: " + ase.getStatusCode());
			context.getLogger().log("Error Code: " + ase.getErrorCode());
			context.getLogger().log("Request ID: " + ase.getRequestId());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}
 
	public ResponseClass handleRequest(RequestClass request, Context context) 
	{
		Integer instance_state;
		try
		{		
			context.getLogger().log("--------------------------------------------------------------------------------------------------------\n");
			context.getLogger().log("Input Data -Instance ID: " + request.instance +" -Region: " + request.region + " -Action: " +request.action + " -Wait time: " + request.timewaitseconds+ " sec -SNS Topic ARN: "+ request.topicarn + " \n");
			
			// Verify parameters
			if( request.region != null && request.instance != null && request.action != null
			 && !request.region.isEmpty() && !request.instance.isEmpty() && !request.action.isEmpty()
					)
			{
				this.region = request.region;
				this.topicArn = request.topicarn;
				this.instance_id = request.instance;
				this.action = request.action;
				
				AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
	        		 	.withRegion(Regions.fromName(request.region))
	        		 	.build(); 
	        
		         //Check instance state 
				instance_state = GetInstanceState(ec2);
				//Start Instance
				if(this.action.equalsIgnoreCase("start"))
				{
			        if(instance_state == 80) // 80 == stopped or 64 == stopping
				    {
				        StartInstancesRequest start_instance_request = new StartInstancesRequest()
				            .withInstanceIds(this.instance_id);
				
				        ec2.startInstances(start_instance_request);
				        
				        context.getLogger().log("Successfully starting instance " +  this.instance_id + "\n");

				        CheckInstanceFinalStatusStatus( ec2, request.timewaitseconds,context);
				     }
				     else
				     {
				        	context.getLogger().log("Instance " +  this.instance_id + "was already running... state = " + instance_state.toString() + "\n");
				     }
				}
				//Stop Instance
				if(request.action.equalsIgnoreCase("stop"))
				{
			        //Check instance state
			        if( instance_state == 16) // 16 == running or 0 == pending state 
				    {
				        StopInstancesRequest stop_instance_request = new StopInstancesRequest()
				            .withInstanceIds(this.instance_id);
				
				        ec2.stopInstances(stop_instance_request);
				        
				        context.getLogger().log("Successfully stopping instance " +  this.instance_id + "\n");
	
				        CheckInstanceFinalStatusStatus( ec2, request.timewaitseconds, context);
				     }
				     else
				     {
				        	context.getLogger().log("Instance " +  this.instance_id + " was already not running... state = " + instance_state.toString() + "\n");
				     }
				}
			}
			else
			{
				//Missing input data
				context.getLogger().log("Missing Input Data: -Instance ID: " + request.instance +" -Region: " + request.region + " -Action: " +request.action + " \n");
			}
		}
	   catch (AmazonServiceException ase) 
		{
		   context.getLogger().log("Caught Exception: " + ase.getMessage());
		   context.getLogger().log("Reponse Status Code: " + ase.getStatusCode());
		   context.getLogger().log("Error Code: " + ase.getErrorCode());
		   context.getLogger().log("Request ID: " + ase.getRequestId());
		   ase.printStackTrace();
		}
    context.getLogger().log("--------------------------------------------------------------------------------------------------------\n");
	return new ResponseClass( this.region, this.instance_id );
	}


}
