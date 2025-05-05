//package vn.hhh.phong_tro.config.s3;
//
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//
//@Service
//@Slf4j
//public class S3HealthCheckService {
//
//    private final S3Client s3Client;
//
//
//    public S3HealthCheckService(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    @PostConstruct
//    public void testConnection() {
//        try {
//            ListBucketsResponse bucketsResponse = s3Client.listBuckets();
//            bucketsResponse.buckets().forEach(b -> log.info("Bucket: {}", b.name()));
//        } catch (S3Exception e) {
//            log.error("S3 connection failed: {}", e.awsErrorDetails().errorMessage());
//        }
//    }
//}
