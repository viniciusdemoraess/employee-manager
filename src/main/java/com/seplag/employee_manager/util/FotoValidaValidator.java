package com.seplag.employee_manager.util;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FotoValidaValidator implements ConstraintValidator<FotoValida, MultipartFile> {

  private List<String> validMimes =
      Arrays.asList(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);

  private boolean isValidMimeType(String contentType) {
    return validMimes.contains(MediaType.parseMediaType(contentType).toString());
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return true;
    }

    if (!isValidMimeType(file.getContentType())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Tipo de arquivo inválido")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
