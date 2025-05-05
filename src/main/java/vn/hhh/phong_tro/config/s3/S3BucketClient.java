package vn.hhh.phong_tro.config.s3;// ✅ Phiên bản mới sử dụng AWS SDK v2
// Import cần thiết
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3BucketClient {
    private final S3Client s3Client;
    private final Region region;


    public String putObject(String bucketName, MultipartFile file, boolean publicObject) {
        log.info("Uploading file {} to Amazon S3", file.getOriginalFilename());

        String key = Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "_");
        ZoneId zoneId = ZoneId.of("UTC+7");
        LocalDate date = ZonedDateTime.now(zoneId).toLocalDate();

        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType());

            if (publicObject) {
                requestBuilder.acl(ObjectCannedACL.PUBLIC_READ);
            }

            s3Client.putObject(requestBuilder.build(), RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), key);
            return fileUrl;
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new RuntimeException("File upload failed");
        }
    }
    public void createS3Bucket(String bucketName, boolean publicBucket) {
        if (bucketExists(bucketName)) {
            log.info("Bucket name already in use. Try another name.");
            return;
        }

        CreateBucketRequest.Builder requestBuilder = CreateBucketRequest.builder().bucket(bucketName);

        if (!publicBucket) {
            requestBuilder.acl(BucketCannedACL.PRIVATE);
        }

        s3Client.createBucket(requestBuilder.build());
        log.info("Bucket created: {}", bucketName);
    }

    public boolean bucketExists(String bucketName) {
        ListBucketsResponse listBuckets = s3Client.listBuckets();
        return listBuckets.buckets().stream()
                .anyMatch(bucket -> bucket.name().equals(bucketName));
    }

    public List<Bucket> listBuckets() {
        return s3Client.listBuckets().buckets();
    }

    public void deleteBucket(String bucketName) {
        try {
            s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }



    public void downloadObject(String bucketName, String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            File outputFile = new File("." + File.separator + objectKey);
            s3Client.getObject(getObjectRequest, Paths.get(outputFile.getPath()));

            log.info("Downloaded file: {}", objectKey);
        } catch (S3Exception e) {
            log.error("Download failed: {}", e.getMessage());
        }
    }

    public List<String> listObjects(String bucketName) {
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build());
        return listResponse.contents().stream().map(S3Object::key).collect(Collectors.toList());
    }

    public void deleteObject(String bucketName, String objectKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build());
    }

    public void deleteMultipleObjects(String bucketName, List<String> keys) {
        List<ObjectIdentifier> objects = keys.stream()
                .map(k -> ObjectIdentifier.builder().key(k).build())
                .collect(Collectors.toList());

        Delete delete = Delete.builder().objects(objects).build();

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(delete)
                .build();

        s3Client.deleteObjects(deleteRequest);
    }

    public void moveObject(String bucketSource, String objectKey, String bucketTarget) {
        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .copySource(bucketSource + "/" + objectKey)
                .destinationBucket(bucketTarget)
                .destinationKey(objectKey)
                .build();

        s3Client.copyObject(copyRequest);
        deleteObject(bucketSource, objectKey);
    }
}
