package com.ecommerce.project.service.File;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  String uploadImage(MultipartFile file, String path) throws IOException;
}
