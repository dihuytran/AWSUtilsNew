import java.io.*;
import java.util.List;

// S3 related imports
import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

// SQS imports
import software.amazon.awssdk.services.sqs.SqsClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry;

public class DpsTools {
    private AmazonS3 s3;
    
    public DpsTools() {

        AWSCredentials credentials = null;
        
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. " +
                "Please make sure that your credentials file is at the correct " +
                "location (~/.aws/credentials), and is in valid format.", e);
        }

        s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();

       /* sqs = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();
            */

    }

    // ALL CODE REGARDING S3 STARTS HERE

    private String bucketName;
    String key = "MyObjectKey";
    
    // creating a bucket with user inputted name
    public void createBucket(String newBuckName) {
        bucketName = newBuckName;
        s3.createBucket(bucketName);
    }
    
    
    public void listBuckets() {
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
    }
    
    public void uploadObject() throws IOException {
        
        s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile())); 
    }
    
    public void downloadObject() throws IOException {
        S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
        displayTextInputStream(object.getObjectContent());
    }
    
    public void listObjects(String bucketName, String prefix) {
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
            .withBucketName(bucketName)
            .withPrefix(prefix));
        
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                "(size = " + objectSummary.getSize() + ")");
        }
    }
    
    public void deleteObject(String newBuckName1) {
        s3.deleteObject(newBuckName1, key);
    }

    public void deleteBucket(String newBuckName2) {
        s3.deleteBucket(newBuckName2);   
    }
    
    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("    " + line);
        }
    }
    
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("test file for S3 Java");
        writer.close();
        
        return file;
    }

    //ALL CODE REGARDING SNS / SQS STARTS HERE

    static AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    public static void receiveMessages(SqsClient sqsClient, String queueUrl) {

        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
                    .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            messages.forEach(message -> System.out.println(message.body()));
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    
     public static void sendMessage(SqsClient sqsClient, String queueUrl) {
        System.out.println("\nSend message");
        try {
            // snippet-start:[sqs.java2.sqs_example.send_message]
            sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("Hello world!")
                .delaySeconds(10)
                .build());
            // snippet-end:[sqs.java2.sqs_example.send_message]

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
    
}