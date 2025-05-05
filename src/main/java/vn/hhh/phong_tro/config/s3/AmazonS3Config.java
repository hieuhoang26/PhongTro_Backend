package vn.hhh.phong_tro.config.s3;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j(topic = "AMAZON-S3-CONFIG")
public class AmazonS3Config {

    @Value("${amazon.s3.region}")
    String region;
    @Value("${amazon.s3.accessKey}")
    String accessKey;
    @Value("${amazon.s3.secretKey}")
    String secretKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    @Bean
    public Region awsRegion() {
        return Region.of(region); // Tạo bean Region
    }
}
