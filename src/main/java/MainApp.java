import java.io.IOException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        DpsTools dpsTools = new DpsTools();

        while (true) {
            System.out.println("Plinease choose an option:");
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
                    //String name = input.next();
                    dpsTools.createBucket();
                    break;
                case 2:
                
                    dpsTools.uploadObject();
                case 3:
                    dpsTools.downloadObject();
                    break;
                case 4:
                    dpsTools.deleteObject();
                    dpsTools.deleteBucket();
                /*case 5:
                    dpsTools.retrieveMessagesFromSQS();
                    break;
                case 6:
                    dpsTools.submitMessageToSQS();
                    break;
                case 7:
                    dpsTools.submitMessageToSNS();
                    break;*/
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