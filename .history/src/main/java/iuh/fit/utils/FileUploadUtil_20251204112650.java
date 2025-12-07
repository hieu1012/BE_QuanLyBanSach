package iuh.fit.utils;

import iuh.fit.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FileUploadUtil {

    @Value("${app.upload.dir:uploads/products/}")
    private String uploadDir;

    @Value("${app.upload.allowed-extensions:jpg,jpeg,png,gif,webp}")
    private String allowedExtensions;

    @Value("${app.upload.max-file-size:10485760}")
    private long maxFileSize;

    /**
     * Upload một file duy nhất
     */
    public String uploadFile(MultipartFile file) throws IOException {
        validateFile(file);
        String fileName = generateFileName(file);
        Path uploadPath = Paths.get(uploadDir);
        
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        
        return fileName;
    }

    /**
     * Upload nhiều files cùng lúc
     */
    public List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            throw new ValidationException("Vui lòng chọn ít nhất một ảnh!", new LinkedHashMap<>());
        }

        List<String> uploadedFileNames = new ArrayList<>();
        Path uploadPath = Paths.get(uploadDir);
        
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                validateFile(file);
                String fileName = generateFileName(file);
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                uploadedFileNames.add(fileName);
            }
        }

        if (uploadedFileNames.isEmpty()) {
            throw new ValidationException("Không có file hợp lệ để upload!", new LinkedHashMap<>());
        }

        return uploadedFileNames;
    }

    /**
     * Xóa file
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Xóa nhiều files
     */
    public void deleteMultipleFiles(List<String> fileNames) {
        if (fileNames != null) {
            fileNames.forEach(this::deleteFile);
        }
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public boolean fileExists(String fileName) {
        return Files.exists(Paths.get(uploadDir).resolve(fileName));
    }

    /**
     * Lấy URL của file
     */
    public String getFileUrl(String fileName) {
        return "/uploads/products/" + fileName;
    }

    /**
     * Validate file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File không hợp lệ!", new LinkedHashMap<>());
        }

        // Kiểm tra kích thước
        if (file.getSize() > maxFileSize) {
            Map<String, Object> errors = new LinkedHashMap<>();
            errors.put("fileSize", "Kích thước file vượt quá giới hạn " + (maxFileSize / 1024 / 1024) + "MB");
            throw new ValidationException("File quá lớn!", errors);
        }

        // Kiểm tra extension
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationException("Tên file không hợp lệ!", new LinkedHashMap<>());
        }

        String fileExtension = getFileExtension(fileName).toLowerCase();
        List<String> allowedExts = Arrays.asList(allowedExtensions.split(","));

        if (!allowedExts.contains(fileExtension)) {
            Map<String, Object> errors = new LinkedHashMap<>();
            errors.put("fileType", "Định dạng file không được phép. Chỉ chấp nhận: " + allowedExtensions);
            throw new ValidationException("Loại file không hợp lệ!", errors);
        }
    }

    /**
     * Tạo tên file duy nhất
     */
    private String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String timestamp = System.currentTimeMillis() + "";
        String randomStr = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + randomStr + "." + extension;
    }

    /**
     * Lấy extension của file
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
