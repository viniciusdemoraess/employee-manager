package com.seplag.employee_manager.util;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FotoValidaListaValidator implements ConstraintValidator<FotoValida, List<MultipartFile>> {

    private final List<String> validMimes = Arrays.asList(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) return true;

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                if (!validMimes.contains(file.getContentType())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Tipo de arquivo inválido na lista de arquivos")
                        .addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}
