package com.martzatech.vdhg.crmprojectback.application.services;

import com.azure.spring.cloud.core.resource.StorageBlobResource;
import com.azure.storage.blob.BlobServiceClient;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class AzureFileService {

  private static final String AZURE_BLOB_CONTAINER = "public/%s";
  private static final String AZURE_BLOB_URI = "azure-blob://%s";
  private static final String URL_SEPARATOR = "/";
  private static final String DOCK = ".";
  public static final String GENERATING_ARCHIVE = "generating archive";

  private final BlobServiceClient blobServiceClient;

  public String uploadFile(final MultipartFile file, final String path, final String extension) {
    try {
      return uploadFile(file.getBytes(), path, extension);
    } catch (final IOException ioe) {
      throw new BusinessRuleException("Cannot upload file");
    }
  }

  public String uploadFile(final byte[] bytes, final String path, final String extension) {
    try {
      final String name = path + URL_SEPARATOR + UUID.randomUUID() + DOCK + extension;
      final String azureContainer = String.format(AZURE_BLOB_CONTAINER, name);
      final WritableResource resource = new StorageBlobResource(blobServiceClient,
          String.format(AZURE_BLOB_URI, azureContainer));
      try (OutputStream os = resource.getOutputStream()) {
        os.write(bytes);
      }
      log.info(GENERATING_ARCHIVE);
      return blobServiceClient.getAccountUrl() + URL_SEPARATOR + azureContainer;
    } catch (final IOException ioe) {
      throw new BusinessRuleException("Cannot upload file");
    }
  }
}
