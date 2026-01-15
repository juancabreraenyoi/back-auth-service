package com.arka.gateway;

import java.nio.ByteBuffer;

public interface CloudStorageGateway {

    String upload(String fileName, String bucket, ByteBuffer file);
    ByteBuffer download(String fileName, String bucket,String expectedETag);
}
