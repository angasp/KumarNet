package repository;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class AwsUploadRepository {
    static Dotenv dotenv = Dotenv.configure().load();
    static String accessKey = dotenv.get("AWS_ACCESS_KEY");
    static String secretKey= dotenv.get("AWS_SECRET_KEY");


    public String uploadImgToAws(UploadedFile uploadedFile) {
        try {
            String bucketName = "kumarnetbuket";
            String fileName = uploadedFile.getFilename();
            Region region = Region.US_EAST_1;
            StaticCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
            S3Client s3Client = S3Client.builder().credentialsProvider(credentials).region(region).build();
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(fileName).build();
            s3Client.putObject(request, RequestBody.fromInputStream(uploadedFile.getContent(), uploadedFile.getSize()));
            GetUrlRequest requestUrl = GetUrlRequest.builder().bucket(bucketName).key(fileName).build();
            return s3Client.utilities().getUrl(requestUrl).toExternalForm();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
