package com.arka;

import com.arka.gateway.CloudStorageGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
public class S3StorageAdapter implements CloudStorageGateway {
    
    private final S3Client s3Client;

    @Override
    public String upload(String fileName, String bucket, ByteBuffer file) {
        var putObjectRequest = getBuildPutObjet(fileName, bucket);
        var requestBody = RequestBody.fromByteBuffer(file);
        var  putObjectResponse = s3Client.putObject(putObjectRequest,requestBody);
        return putObjectResponse.eTag();
    }

    @Override
    public ByteBuffer download(String fileName, String bucket,String expectedETag) {
        var putObjectRequest =  getBuildGetObjet(fileName, bucket,expectedETag);
        var getObjectResponse = s3Client.getObjectAsBytes(putObjectRequest);
        return getObjectResponse.asByteBuffer();
    }

    private static PutObjectRequest getBuildPutObjet(String fileName, String bucket) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
    }

    private static GetObjectRequest getBuildGetObjet(String fileName, String bucket, String expectedETag) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .ifMatch(expectedETag)
                .build();
    }
}
