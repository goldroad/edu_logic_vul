package com.edu.controller;

import com.edu.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    /**
     * 读取文件
     */
    @GetMapping("/read")
    public Map<String, Object> readFile(@RequestParam String filename) {
        Map<String, Object> response = new HashMap<>();
        
        // 文件读取处理
        String content = fileService.readFile(filename);
        
        response.put("success", true);
        response.put("filename", filename);
        response.put("content", content);
        
        return response;
    }
    
    /**
     * 安全文件读取
     */
    @GetMapping("/read-safe")
    public Map<String, Object> readFileSafe(@RequestParam String filename) {
        Map<String, Object> response = new HashMap<>();
        
        String content = fileService.readFileSafe(filename);
        
        response.put("success", true);
        response.put("filename", filename);
        response.put("content", content);
        
        return response;
    }
    
    /**
     * 获取文件信息 - 任意文件信息泄露
     */
    @GetMapping("/info")
    public Map<String, Object> getFileInfo(@RequestParam String filename) {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：可以获取任意文件信息
        String info = fileService.getFileInfo(filename);
        
        response.put("success", true);
        response.put("filename", filename);
        response.put("info", info);
        
        return response;
    }
    
    /**
     * 列出目录内容 - 目录遍历漏洞
     */
    @GetMapping("/list")
    public Map<String, Object> listDirectory(@RequestParam String path) {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：可以列出任意目录内容
        String content = fileService.listDirectory(path);
        
        response.put("success", true);
        response.put("path", path);
        response.put("content", content);
        
        return response;
    }
    
    /**
     * 下载文件 - 任意文件下载漏洞
     */
    @GetMapping("/download")
    public Map<String, Object> downloadFile(@RequestParam String filename) {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：任意文件下载
        String content = fileService.readFile(filename);
        
        response.put("success", true);
        response.put("filename", filename);
        response.put("content", content);
        response.put("downloadUrl", "/api/file/read?filename=" + filename);
        
        return response;
    }
    
    /**
     * 读取系统配置文件 - 敏感信息泄露
     */
    @GetMapping("/config")
    public Map<String, Object> readConfig(@RequestParam(defaultValue = "application.yml") String configFile) {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：可以读取敏感配置文件
        String[] commonConfigFiles = {
            "application.yml",
            "application.properties", 
            "src/main/resources/application.yml",
            "src/main/resources/application.properties",
            "../../../etc/passwd",
            "C:\\Windows\\System32\\drivers\\etc\\hosts",
            "/etc/passwd",
            "/etc/shadow"
        };
        
        Map<String, String> configs = new HashMap<>();
        for (String file : commonConfigFiles) {
            String content = fileService.readFile(file);
            if (!content.contains("文件不存在") && !content.contains("读取文件失败")) {
                configs.put(file, content);
            }
        }
        
        // 也读取用户指定的配置文件
        String userConfig = fileService.readFile(configFile);
        configs.put(configFile, userConfig);
        
        response.put("success", true);
        response.put("configs", configs);
        
        return response;
    }
}