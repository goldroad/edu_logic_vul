package com.edu.service;

import com.edu.entity.File;
import com.edu.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    
    private static final String UPLOAD_DIR = "files/";
    
    @Autowired
    private FileRepository fileRepository;
    
    /**
     * 初始化文件存储目录
     */
    private void initUploadDir() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建文件存储目录", e);
        }
    }
    
    /**
     * 上传文件
     */
    public File uploadFile(MultipartFile multipartFile, Long userId) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        
        initUploadDir();
        
        try {
            String originalName = multipartFile.getOriginalFilename();
            String fileExtension = getFileExtension(originalName);
            String storedName = UUID.randomUUID().toString() + "." + fileExtension;
            
            Path filePath = Paths.get(UPLOAD_DIR, storedName);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 创建文件记录
            File file = new File();
            file.setUserId(userId);
            file.setOriginalName(originalName);
            file.setStoredName(storedName);
            file.setFilePath(filePath.toString());
            file.setFileSize(multipartFile.getSize());
            file.setFileType(getFileType(fileExtension));
            file.setMimeType(multipartFile.getContentType());
            file.setUploadTime(LocalDateTime.now());
            file.setDownloadCount(0);
            file.setIsDeleted(false);
            
            return fileRepository.save(file);
            
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据用户ID获取文件列表
     */
    public List<File> getUserFiles(Long userId) {
        return fileRepository.findByUserIdAndIsDeletedFalseOrderByUploadTimeDesc(userId);
    }
    
    /**
     * 分页获取用户文件列表
     */
    public Page<File> getUserFiles(Long userId, Pageable pageable) {
        return fileRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
    }
    
    /**
     * 根据文件名搜索用户文件
     */
    public List<File> searchUserFiles(Long userId, String fileName) {
        return fileRepository.findByUserIdAndFileNameContaining(userId, fileName);
    }
    
    /**
     * 根据文件类型获取用户文件
     */
    public List<File> getUserFilesByType(Long userId, String fileType) {
        return fileRepository.findByUserIdAndFileTypeAndIsDeletedFalseOrderByUploadTimeDesc(userId, fileType);
    }
    
    /**
     * 根据ID获取文件（确保用户只能访问自己的文件）
     */
    public Optional<File> getFileById(Long fileId, Long userId) {
        return fileRepository.findByIdAndUserIdAndIsDeletedFalse(fileId, userId);
    }
    
    /**
     * 下载文件
     */
    public byte[] downloadFile(Long fileId, Long userId) {
        Optional<File> fileOpt = getFileById(fileId, userId);
        if (!fileOpt.isPresent()) {
            throw new RuntimeException("文件不存在或无权访问");
        }
        
        File file = fileOpt.get();
        try {
            Path filePath = Paths.get(file.getFilePath());
            if (!Files.exists(filePath)) {
                throw new RuntimeException("文件不存在");
            }
            
            // 增加下载次数
            file.setDownloadCount(file.getDownloadCount() + 1);
            fileRepository.save(file);
            
            return Files.readAllBytes(filePath);
            
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除文件
     */
    public boolean deleteFile(Long fileId, Long userId) {
        Optional<File> fileOpt = getFileById(fileId, userId);
        if (!fileOpt.isPresent()) {
            return false;
        }
        
        File file = fileOpt.get();
        
        try {
            // 标记为已删除
            file.setIsDeleted(true);
            fileRepository.save(file);
            
            // 删除物理文件
            Path filePath = Paths.get(file.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            return true;
            
        } catch (IOException e) {
            // 即使物理文件删除失败，也标记为已删除
            return true;
        }
    }
    
    /**
     * 获取用户文件统计信息
     */
    public FileStats getUserFileStats(Long userId) {
        Long fileCount = fileRepository.countByUserId(userId);
        Long totalSize = fileRepository.sumFileSizeByUserId(userId);
        Long downloadCount = fileRepository.sumDownloadCountByUserId(userId);
        
        return new FileStats(fileCount, totalSize, downloadCount);
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    
    /**
     * 根据扩展名获取文件类型
     */
    private String getFileType(String extension) {
        if (extension == null) return "其他";
        
        switch (extension.toLowerCase()) {
            case "pdf":
                return "PDF";
            case "doc":
            case "docx":
                return "文档";
            case "ppt":
            case "pptx":
                return "演示文稿";
            case "xls":
            case "xlsx":
                return "表格";
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return "图片";
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
                return "视频";
            case "mp3":
            case "wav":
            case "flac":
                return "音频";
            case "zip":
            case "rar":
            case "7z":
                return "压缩包";
            case "txt":
                return "文本";
            case "java":
            case "js":
            case "html":
            case "css":
            case "py":
            case "cpp":
                return "代码";
            default:
                return "其他";
        }
    }
    
    /**
     * 文件统计信息类
     */
    public static class FileStats {
        private Long fileCount;
        private Long totalSize;
        private Long downloadCount;
        
        public FileStats(Long fileCount, Long totalSize, Long downloadCount) {
            this.fileCount = fileCount != null ? fileCount : 0L;
            this.totalSize = totalSize != null ? totalSize : 0L;
            this.downloadCount = downloadCount != null ? downloadCount : 0L;
        }
        
        public Long getFileCount() { return fileCount; }
        public Long getTotalSize() { return totalSize; }
        public Long getDownloadCount() { return downloadCount; }
        
        public String getFormattedSize() {
            if (totalSize == null || totalSize == 0) return "0 B";
            
            long size = totalSize;
            String[] units = {"B", "KB", "MB", "GB"};
            int unitIndex = 0;
            
            while (size >= 1024 && unitIndex < units.length - 1) {
                size /= 1024;
                unitIndex++;
            }
            
            return String.format("%.1f %s", (double) size, units[unitIndex]);
        }
    }
}