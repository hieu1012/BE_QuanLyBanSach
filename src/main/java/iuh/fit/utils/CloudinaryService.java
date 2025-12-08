package iuh.fit.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import iuh.fit.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.upload-folder:quanlybansach/products}")
    private String uploadFolder;

    @Value("${app.upload.allowed-extensions:jpg,jpeg,png,gif,webp}")
    private String allowedExtensions;

    @Value("${app.upload.max-file-size:10485760}")
    private long maxFileSize;

    /**
     * Upload một file lên Cloudinary
     */
    public String uploadFile(MultipartFile file) {
        try {
            validateFile(file);

            // Upload file lên Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", uploadFolder,
                    "resource_type", "auto",
                    "quality", "auto",
                    "fetch_format", "auto"
            ));

            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            
            // Thêm extension vào public_id để URL hoạt động đúng
            return publicId + "." + format;
        } catch (Exception e) {
            throw new ValidationException("Lỗi upload file: " + e.getMessage(), new LinkedHashMap<>());
        }
    }

    /**
     * Upload nhiều files lên Cloudinary
     */
    public List<String> uploadMultipleFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new ValidationException("Vui lòng chọn ít nhất một ảnh!", new LinkedHashMap<>());
        }

        List<String> uploadedPublicIds = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    String publicId = uploadFile(file);
                    uploadedPublicIds.add(publicId);
                } catch (Exception e) {
                    // Log lỗi nhưng tiếp tục upload file tiếp theo
                    System.err.println("Lỗi upload file " + file.getOriginalFilename() + ": " + e.getMessage());
                }
            }
        }

        if (uploadedPublicIds.isEmpty()) {
            throw new ValidationException("Không có file hợp lệ để upload!", new LinkedHashMap<>());
        }

        return uploadedPublicIds;
    }

    /**
     * Xóa file từ Cloudinary theo public_id
     */
    public boolean deleteFile(String publicId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(deleteResult.get("result"));
        } catch (Exception e) {
            System.err.println("Lỗi xóa file " + publicId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa nhiều files từ Cloudinary
     */
    public void deleteMultipleFiles(List<String> publicIds) {
        if (publicIds != null) {
            publicIds.forEach(this::deleteFile);
        }
    }

    /**
     * Lấy URL của ảnh từ public_id với transformation
     */
    public String getImageUrl(String publicId) {
        try {
            return cloudinary.url()
                    .transformation(new Transformation<>()
                            .quality("auto")
                            .fetchFormat("auto"))
                    .generate(publicId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lấy URL của ảnh thumbnail (100x100)
     */
    public String getThumbnailUrl(String publicId) {
        try {
            return cloudinary.url()
                    .transformation(new Transformation<>()
                            .width(100).height(100).crop("fill")
                            .quality("auto")
                            .fetchFormat("auto"))
                    .generate(publicId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lấy URL responsive (dynamic width)
     */
    public String getResponsiveUrl(String publicId, int width) {
        try {
            return cloudinary.url()
                    .transformation(new Transformation<>()
                            .width(width).crop("scale")
                            .quality("auto")
                            .fetchFormat("auto"))
                    .generate(publicId);
        } catch (Exception e) {
            return null;
        }
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
     * Lấy extension của file
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
