package com.seed.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

/**
 * 文件上传工具类
 * 
 * @author Joey
 */
public class FileUploadUtils {

    /**
     * 默认大小 50MB
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 上传文件
     *
     * @param baseDir      基础目录
     * @param relativePath 相对路径
     * @param fileName     文件名
     * @param file         文件
     * @return 文件完整路径
     * @throws IOException
     */
    public static String uploadFile(String baseDir, String relativePath, String fileName, MultipartFile file)
            throws IOException {
        // 检查文件大小
        assertAllowed(file);

        // 创建目录
        String fullPath = baseDir + File.separator + relativePath;
        Path uploadPath = Paths.get(fullPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    /**
     * 上传FilePart文件
     *
     * @param baseDir      基础目录
     * @param relativePath 相对路径
     * @param fileName     文件名
     * @param filePart     文件部分
     * @return 包含文件完整路径的Mono
     */
    public static Mono<String> uploadFilePart(String baseDir, String relativePath, String fileName, FilePart filePart) {
        // 创建目录
        String fullPath = baseDir + File.separator + relativePath;
        Path uploadPath = Paths.get(fullPath);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(fileName);

            // 使用FilePart的transferTo方法将内容写入目标文件
            return filePart.transferTo(filePath)
                    .then(Mono.just(filePath.toString()));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    /**
     * 检查文件大小和类型
     *
     * @param file 上传的文件
     */
    public static void assertAllowed(MultipartFile file) {
        if (file.getSize() > DEFAULT_MAX_SIZE) {
            throw new IllegalArgumentException("文件大小超过限制，最大允许：" + (DEFAULT_MAX_SIZE / 1024 / 1024) + "MB");
        }
    }

    /**
     * 智能上传FilePart文件（根据配置决定上传到本地还是腾讯云COS）
     *
     * @param baseDir      本地基础目录
     * @param relativePath 相对路径
     * @param fileName     文件名
     * @param filePart     文件部分
     * @return 包含文件访问路径的Mono
     */
    public static Mono<String> smartUploadFilePart(String baseDir, String relativePath, String fileName,
            FilePart filePart) {
        // 检查配置文件中是否有腾讯云COS的配置
        Properties properties = new Properties();
        try (InputStream inputStream = FileUploadUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);

                // 检查是否有腾讯云COS的必要配置
                String secretId = properties.getProperty("tencent.cos.secret-id");
                String secretKey = properties.getProperty("tencent.cos.secret-key");
                String region = properties.getProperty("tencent.cos.region");
                String bucketName = properties.getProperty("tencent.cos.bucket-name");

                // 如果所有必要的配置都存在，则上传到腾讯云COS
                if (secretId != null && !secretId.isEmpty() &&
                        secretKey != null && !secretKey.isEmpty() &&
                        region != null && !region.isEmpty() &&
                        bucketName != null && !bucketName.isEmpty()) {

                    // 获取COS路径前缀，如果没有配置则使用默认值
                    String cosPath = properties.getProperty("tencent.cos.path-prefix", "uploads/");
                    if (!cosPath.endsWith("/")) {
                        cosPath = cosPath + "/";
                    }

                    // 先将FilePart保存到临时文件，然后上传到COS
                    return uploadFilePartToTempAndThenToCos(filePart, bucketName, secretId, secretKey, region,
                            cosPath + relativePath + "/");
                }
            }
        } catch (Exception e) {
            // 如果读取配置或上传到COS出错，则回退到本地上传
            return uploadFilePart(baseDir, relativePath, fileName, filePart);
        }

        // 如果没有COS配置或配置不完整，则上传到本地
        return uploadFilePart(baseDir, relativePath, fileName, filePart);
    }

    /**
     * 将FilePart先保存到临时文件，然后上传到腾讯云COS
     *
     * @param filePart   文件部分
     * @param bucketName 存储桶名称
     * @param secretId   腾讯云SecretId
     * @param secretKey  腾讯云SecretKey
     * @param region     地域信息
     * @param cosPath    COS路径
     * @return 包含文件访问URL的Mono
     */
    private static Mono<String> uploadFilePartToTempAndThenToCos(FilePart filePart, String bucketName, String secretId,
            String secretKey, String region, String cosPath) {
        try {
            // 创建临时文件
            Path tempFile = Files.createTempFile("cos_upload_", ".tmp");

            // 将FilePart内容写入临时文件
            return filePart.transferTo(tempFile)
                    .then(Mono.fromCallable(() -> {
                        try {
                            // 生成唯一文件名
                            String originalFilename = filePart.filename();
                            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

                            // 构建完整的对象键（Key）
                            String key = cosPath + fileName;

                            // 创建 COSClient 实例
                            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
                            ClientConfig clientConfig = new ClientConfig(new Region(region));
                            COSClient cosClient = new COSClient(cred, clientConfig);

                            try {
                                // 获取临时文件大小
                                long fileSize = Files.size(tempFile);

                                // 上传文件
                                ObjectMetadata metadata = new ObjectMetadata();
                                metadata.setContentLength(fileSize);

                                // 尝试设置内容类型
                                String contentType = determineContentType(originalFilename);
                                if (contentType != null) {
                                    metadata.setContentType(contentType);
                                }

                                try (InputStream fileInputStream = new FileInputStream(tempFile.toFile())) {
                                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key,
                                            fileInputStream, metadata);
                                    cosClient.putObject(putObjectRequest);
                                }

                                // 生成文件访问URL
                                return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
                            } finally {
                                cosClient.shutdown();
                                // 删除临时文件
                                Files.deleteIfExists(tempFile);
                            }
                        } catch (Exception e) {
                            // 确保临时文件被删除
                            Files.deleteIfExists(tempFile);
                            throw e;
                        }
                    }));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    /**
     * 根据文件名确定内容类型
     *
     * @param fileName 文件名
     * @return 内容类型
     */
    private static String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            case "mp3":
                return "audio/mpeg";
            case "mp4":
                return "video/mp4";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 上传文件到腾讯云对象存储
     *
     * @param file       文件
     * @param bucketName 存储桶名称
     * @param secretId   腾讯云SecretId
     * @param secretKey  腾讯云SecretKey
     * @param region     地域信息，例如：ap-guangzhou
     * @param cosPath    COS路径，例如：folder/subfolder/
     * @return 文件访问URL
     * @throws Exception
     */
    public static String uploadToCos(MultipartFile file, String bucketName, String secretId, String secretKey,
            String region, String cosPath) throws Exception {
        // 检查文件大小
        assertAllowed(file);

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        // 构建完整的对象键（Key）
        String key = cosPath + fileName;

        // 创建 COSClient 实例
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            // 上传文件
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            cosClient.putObject(putObjectRequest);

            // 生成文件访问URL
            return "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
        } finally {
            cosClient.shutdown();
        }
    }
}