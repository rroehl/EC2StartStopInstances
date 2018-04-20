# AWS Schedule ECS Start Stop Instances Lambda Function

The product will allow the scheduling of the starting and stopping EC2 instances. The repo contains a Java Lambda function and CloudFormation JSON scripts to configure the Lambda function and schedule the starting and stopping of EC2 instances.

The LambdaFunctionHandler class has five inputted parameters that are passed to it from the AWS scheduled event via the RequestClass:
    a.)instance - The instance ID to which the action will be applied ("i-004aa8aetd9736e2e").
    b.)region - The AWS region in which it is installed ("us-east-2").
    c.) action - Either "Stop" or "Start" action applied to the instance.
    d.) timewaitseconds - The time in seconds between the action being applied to the instance and when theinstance is tested to ensure that action is accomplished ( -- optional).
    e.) topicarn - The SNS ARN topic to which the notification is sent in the event the action is not accomplished in the defined period ("arn:aws:sns:us-east-2:638158650817:EmailTopic" -- optional).
The handleRequest function will will either stop or start the instance, wait for a predefined period, and then check the the instance state. If this state is not set as defined by the action, a notification is sent to the SNS topic. The function will log to CloudWatch logs info and error messages, and will return the region and the instance ID via the ResponseClass (just for testing).

The "stopstartEC2" CloudFormation script will configure the Lambda function and the IAM role and policy. The StopStartEC2InstancePolicy policy will permit the function to:
    a.) Create CloudWatch Group, Streams, and write to the logs.
    b.) Query EC2 instance.
    c.) Publish notifications to SNS topics
    d.) Stop and start EC2 instance which must have the "LambdaStartStopControl" tag set to "true".
It will export Lambda function ARN and the Lambda function name for the CloudFormation script that schedules the actions.

The "schedulerulestartstopEC2" CloudFromation script will configure the CloudWatch Event Rule to send an event to Lambda function. It imports the LamdaFunction name and ARN from the "stopstartEC2" CloudFormation script, configures the schedule that the event occurs and configures the input parameters to be passed to the Lambda Java function. Also it configures the permissions so that it can invoke the Lambda function with the event.

## Getting Started

The Eclipse IDE for Java development has a nice AWS Lambda plugin and the AWS toolkit.  

### Prerequisites

The "stopstartEC2" CloudFormation script will require:
    a.) An S3 bucket must be created and the Zip Java class file is stored there. 
    b.) Rights to create IAM roles and policies, and the Lambda function.
The "schedulerulestartstopEC2" CloudFromation script will require:
    a.) An SNS topic with an associated email address to receive notifications.
    b.) The "stopstartEC2" CloudFormation script needs to have been executed successfully.
     c.) An EC2 instance with a tag named "LambdaStartStopControl" set to "true".

### Installing

Ensure that the Java class zip files is in the S3 bucket and then run the "stopstartEC2" CloudForamtion script via console or AWS CLI. It will prompt for:

    a.) IAM role name that will be created amd used by the Lambda function - The CloudFormation created IAM role allows Lambda fn to start and stop instances and send notifications to SNS. It will be created.
    b.) Lambda function name - The name of the Lambda function.
    c.) Lambda function timeout - he function execution time (in seconds) after which Lambda terminates the function. It has to be greater that the longest instance startup or stopping time (300 sec. is the max.).
    d.) Lambda function memory usage size - The amount of memory, in MB, that is allocated to your Lambda function.
    e.) S3 Bucket code repo name - The name of the Amazon S3 bucket where the Lambda .zip file that contains your deployment package is stored.
    f.)  Java class zip filename - Please ensure that the zip of the Lambda code is on the S3 bucket. It has to be manually copied to the S3 bucket.
    g.) Instance tag value for the Lambda function - The EC2 instance must have this tag name of "LambdaStartStopControl" and value of "true" in order that the function can stop and start it.
    h.) Your name.
    
   
Next execute  "schedulerulestartstopEC2" CloudFromation script. It will prompt for:

      a.) The name of the stack used to create stop/start Lambda function - The stack name which was used to create the start stop Lambda function. It is needed since information is pulled from the Lambda function creation stack.
      b.) The name of the schedule event rule name - The schedule event name (must be unique).
      c.)The EC2 instance id - The EC2 instance id that the action will be applied to.
      d.) The start or stop action - Start or stop the EC2 instance.
      e.) The Cron expression for the schedule (UTC only) - The format is (min hrs day-of-mnth mnth day-of-wk) and the time is in UTC time.  See doc at https://docs.aws.amazon.com/AmazonCloudWatch/latest/events/ScheduledEvents.html.
      f.) The time  (sec.) to wait before checking the instance state - This is the time between starting and stopping the instance and checking if it is in the correct state. The value is limited by the maximum time of 5 minutes (300 sec.) that Lambda function can execute. 
      g.) SNS notification ARN - The SNS topic ARN is used by the Lambda function to send a notification if after the wait period the instance state is not in the desired stop or start state.
       h.) Your name.

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* There many great sites with information that help build this liitle project. Thanks to all of them.


