import java.io.IOException;
import java.util.Scanner;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;

public class MainApp {

    /* NOTICE: execution of this program requires the necesarray Java and Maven librarioes installed as well as 
     * the AWS SDK Tool Kit and valid IAM credentials to connect and access S3
     */


    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        DpsTools dpsTools = new DpsTools();

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)  // Specify the AWS region
                .build(); 

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Create a bucket");
            System.out.println("2. Upload a file to S3 Bucket");
            System.out.println("3. Download file from S3 bucket");
            System.out.println("4. Delete the file and bucket");
            System.out.println("5. Retrieve messages from SQS");
            System.out.println("6. Submit a message to SQS");
            System.out.println("7. Submit a message to SNS");
            System.out.println("8. Exit");

            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter a name for your bucket, it must adhere to AWS naming rules.");
                    String name = input.next();
                    System.out.println("");
                    System.out.println("Here are the current existing S3 Buckets.");
                    dpsTools.createBucket(name);
                    dpsTools.listBuckets();
                    break;
                case 2:
                    System.out.println("Uploading a txt document to your S3 bucket");
                    dpsTools.uploadObject();
                case 3:
                System.out.println("Downloading a file.");
                    dpsTools.downloadObject();
                    break;
                case 4:
                    System.out.println("Enter the name of the bucket you wish to delete and its contents.");
                    System.out.println("Upon deletion of the bucket, the terminal will end the program.");
                    String name2 = input.next();
                    dpsTools.deleteObject(name2);
                    dpsTools.deleteBucket(name2);
                    System.out.println("Here are your current buckets, post-deletion.");
                    dpsTools.listBuckets();
                case 5:
                    System.out.println("Enter the URL of your desired SQS");
                    String sqs = input.next();
                    System.out.println("Here is the latest message:");
                    System.out.println("");
                    DpsTools.receiveMessages(sqsClient, sqs);
                    break;
                case 6:
                System.out.println("Enter the URL of your desired SQS to send a message to");
                    String url = input.next();
                    DpsTools.sendMessages(sqsClient, url);
               /* case 7: TOOD: Submit a message to SNS
                    dpsTools.submitMessageToSNS();
                    break;
                case 9: TOOD: Testing RDS [TBD]
                */
                case 8:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }
}