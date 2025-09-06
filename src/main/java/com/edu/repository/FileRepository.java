package com.edu.repository;

import com.edu.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
    /**
     * 根据用户ID查找未删除的文件
     */
    List<File> findByUserIdAndIsDeletedFalseOrderByUploadTimeDesc(Long userId);
    
    /**
     * 根据用户ID分页查找未删除的文件
     */
    Page<File> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和文件名搜索文件
     */
    @Query("SELECT f FROM File f WHERE f.userId = :userId AND f.isDeleted = false " +
           "AND f.originalName LIKE %:fileName% ORDER BY f.uploadTime DESC")
    List<File> findByUserIdAndFileNameContaining(@Param("userId") Long userId, 
                                                @Param("fileName") String fileName);
    
    /**
     * 根据用户ID和文件类型查找文件
     */
    List<File> findByUserIdAndFileTypeAndIsDeletedFalseOrderByUploadTimeDesc(Long userId, String fileType);
    
    /**
     * 根据用户ID统计文件数量
     */
    @Query("SELECT COUNT(f) FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    Long countByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID统计文件总大小
     */
    @Query("SELECT COALESCE(SUM(f.fileSize), 0) FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    Long sumFileSizeByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID统计下载次数
     */
    @Query("SELECT COALESCE(SUM(f.downloadCount), 0) FROM File f WHERE f.userId = :userId AND f.isDeleted = false")
    Long sumDownloadCountByUserId(@Param("userId") Long userId);
    
    /**
     * 根据ID和用户ID查找文件（确保用户只能访问自己的文件）
     */
    Optional<File> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);
    
    /**
     * 根据存储名称查找文件
     */
    Optional<File> findByStoredNameAndIsDeletedFalse(String storedName);
}