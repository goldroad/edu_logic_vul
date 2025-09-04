package com.edu.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    
    private static final String UPLOAD_DIR = "uploads/";
    
    /**
     * 读取文件
     */
    public String readFile(String filename) {
        try {
            // 漏洞：直接使用用户输入的文件名，可能导致目录遍历攻击
            Path filePath = Paths.get(filename);
            
            if (Files.exists(filePath)) {
                byte[] bytes = Files.readAllBytes(filePath);
                return new String(bytes);
            } else {
                return "文件不存在";
            }
        } catch (IOException e) {
            return "读取文件失败: " + e.getMessage();
        }
    }
    
    /**
     * 安全文件读取方法
     */
    public String readFileSafe(String filename) {
        try {
            // 验证文件名，防止目录遍历
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                return "非法文件名";
            }
            
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            
            // 确保文件在允许的目录内
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path resolvedPath = filePath.toAbsolutePath().normalize();
            
            if (!resolvedPath.startsWith(uploadPath)) {
                return "访问被拒绝";
            }
            
            if (Files.exists(resolvedPath)) {
                byte[] bytes = Files.readAllBytes(resolvedPath);
                return new String(bytes);
            } else {
                return "文件不存在";
            }
        } catch (IOException e) {
            return "读取文件失败: " + e.getMessage();
        }
    }
    
    /**
     * 获取文件信息
     */
    public String getFileInfo(String filename) {
        try {
            // 漏洞：可以获取任意文件信息
            Path filePath = Paths.get(filename);
            File file = filePath.toFile();
            
            if (file.exists()) {
                StringBuilder info = new StringBuilder();
                info.append("文件名: ").append(file.getName()).append("\n");
                info.append("文件大小: ").append(file.length()).append(" bytes\n");
                info.append("是否为目录: ").append(file.isDirectory()).append("\n");
                info.append("最后修改时间: ").append(new java.util.Date(file.lastModified())).append("\n");
                info.append("绝对路径: ").append(file.getAbsolutePath()).append("\n");
                return info.toString();
            } else {
                return "文件不存在";
            }
        } catch (Exception e) {
            return "获取文件信息失败: " + e.getMessage();
        }
    }
    
    /**
     * 列出目录内容
     */
    public String listDirectory(String dirPath) {
        try {
            // 漏洞：可以列出任意目录内容
            Path path = Paths.get(dirPath);
            File dir = path.toFile();
            
            if (!dir.exists()) {
                return "目录不存在";
            }
            
            if (!dir.isDirectory()) {
                return "不是目录";
            }
            
            File[] files = dir.listFiles();
            if (files == null) {
                return "无法读取目录";
            }
            
            StringBuilder result = new StringBuilder();
            result.append("目录: ").append(dir.getAbsolutePath()).append("\n");
            result.append("包含 ").append(files.length).append(" 个项目:\n\n");
            
            for (File file : files) {
                result.append(file.isDirectory() ? "[DIR]  " : "[FILE] ");
                result.append(file.getName());
                if (file.isFile()) {
                    result.append(" (").append(file.length()).append(" bytes)");
                }
                result.append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "列出目录失败: " + e.getMessage();
        }
    }
}