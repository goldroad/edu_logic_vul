package com.edu.controller;

import com.edu.entity.File;
import com.edu.entity.User;
import com.edu.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file,
                                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        try {
            File uploadedFile = fileService.uploadFile(file, user.getId());
            
            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("file", createFileResponse(uploadedFile));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取用户文件列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getUserFiles(@RequestParam(required = false) String type,
                                                           @RequestParam(required = false) String search,
                                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        try {
            List<File> files;
            
            if (search != null && !search.trim().isEmpty()) {
                files = fileService.searchUserFiles(user.getId(), search.trim());
            } else if (type != null && !type.trim().isEmpty() && !"全部类型".equals(type)) {
                files = fileService.getUserFilesByType(user.getId(), type);
            } else {
                files = fileService.getUserFiles(user.getId());
            }
            
            // 获取文件统计信息
            FileService.FileStats stats = fileService.getUserFileStats(user.getId());
            
            response.put("success", true);
            response.put("files", files.stream().map(this::createFileResponse).toArray());
            response.put("stats", createStatsResponse(stats));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取文件列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Optional<File> fileOpt = fileService.getFileById(fileId, user.getId());
            if (!fileOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            File file = fileOpt.get();
            byte[] fileContent = fileService.downloadFile(fileId, user.getId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            
            // 设置文件名，处理中文编码
            String encodedFileName;
            try {
                encodedFileName = URLEncoder.encode(file.getOriginalName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                encodedFileName = file.getOriginalName();
            }
            
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentLength(fileContent.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable Long fileId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        try {
            boolean deleted = fileService.deleteFile(fileId, user.getId());
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "文件删除成功");
            } else {
                response.put("success", false);
                response.put("message", "文件不存在或无权删除");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "文件删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取文件统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getFileStats(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        try {
            FileService.FileStats stats = fileService.getUserFileStats(user.getId());
            
            response.put("success", true);
            response.put("stats", createStatsResponse(stats));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取统计信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 创建文件响应对象
     */
    private Map<String, Object> createFileResponse(File file) {
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("id", file.getId());
        fileMap.put("originalName", file.getOriginalName());
        fileMap.put("fileSize", file.getFileSize());
        fileMap.put("formattedSize", file.getFormattedFileSize());
        fileMap.put("fileType", file.getFileType());
        fileMap.put("mimeType", file.getMimeType());
        fileMap.put("uploadTime", file.getUploadTime());
        fileMap.put("downloadCount", file.getDownloadCount());
        fileMap.put("extension", file.getFileExtension());
        return fileMap;
    }
    
    /**
     * 创建统计信息响应对象
     */
    private Map<String, Object> createStatsResponse(FileService.FileStats stats) {
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("fileCount", stats.getFileCount());
        statsMap.put("totalSize", stats.getTotalSize());
        statsMap.put("formattedSize", stats.getFormattedSize());
        statsMap.put("downloadCount", stats.getDownloadCount());
        return statsMap;
    }
}