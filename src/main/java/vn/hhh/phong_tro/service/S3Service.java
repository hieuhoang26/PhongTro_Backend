package vn.hhh.phong_tro.service;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.config.s3.S3BucketClient;

import java.util.List;

@Service
@Slf4j(topic = "S3-SERVICE")
@RequiredArgsConstructor
public class S3Service {

    private final S3BucketClient s3BucketClient;
    private final String bucketName = "phongtrodatn";

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be blank");
        }

        log.info("Uploading file to bucket: {}", bucketName);
        return s3BucketClient.putObject(bucketName, file, true); // true = public
    }
    public void delete(String keys){
        if (keys == null) {
            throw new IllegalArgumentException("key cannot be blank");
        }
        s3BucketClient.deleteObject(bucketName,keys);
    }
    public String extractKeyFromUrl(String url) {
        // Giả sử URL là: https://your-bucket.s3.amazonaws.com/folder/filename.jpg
        // Trả về: folder/filename.jpg
        return url.substring(url.indexOf(".amazonaws.com/") + 15);
    }
}
