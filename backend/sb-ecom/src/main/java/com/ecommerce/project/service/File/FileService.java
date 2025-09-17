package com.ecommerce.project.service.File;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
  String uploadImage(MultipartFile file, String path) throws IOException;
}
