import java.io.IOException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        DpsTools dpsTools = new DpsTools();

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
                /*case 5:
                    dpsTools.retrieveMessagesFromSQS();
                    break;
                case 6:
                    dpsTools.submitMessageToSQS();
                    break;
                case 7:
                    dpsTools.submitMessageToSNS();
                    break;
                case 9:
                    // TODO */
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