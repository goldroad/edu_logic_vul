package com.edu.repository;

import com.edu.entity.File;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileRepository {
    
    @Select("SELECT * FROM files WHERE id = #{id} AND user_id = #{userId} AND is_deleted = 0")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "originalName", column = "original_name"),
        @Result(property = "storedName", column = "stored_name"),
        @Result(property = "filePath", column = "file_path"),
        @Result(property = "fileSize", column = "file_size"),
        @Result(property = "fileType", column = "file_type"),
        @Result(property = "mimeType", column = "mime_type"),
        @Result(property = "uploadTime", column = "upload_time"),
        @Result(property = "downloadCount", column = "download_count"),
        @Result(property = "isDeleted", column = "is_deleted")
    })
    File findByIdAndUserId(Long id, Long userId);
    
    @Select("SELECT * FROM files WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY upload_time DESC")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "originalName", column = "original_name"),
        @Result(property = "storedName", column = "stored_name"),
        @Result(property = "filePath", column = "file_path"),
        @Result(property = "fileSize", column = "file_size"),
        @Result(property = "fileType", column = "file_type"),
        @Result(property = "mimeType", column = "mime_type"),
        @Result(property = "uploadTime", column = "upload_time"),
        @Result(property = "downloadCount", column = "download_count"),
        @Result(property = "isDeleted", column = "is_deleted")
    })
    List<File> findByUserId(Long userId);
    
    @Select("SELECT * FROM files WHERE user_id = #{userId} AND original_name LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = 0 ORDER BY upload_time DESC")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "originalName", column = "original_name"),
        @Result(property = "storedName", column = "stored_name"),
        @Result(property = "filePath", column = "file_path"),
        @Result(property = "fileSize", column = "file_size"),
        @Result(property = "fileType", column = "file_type"),
        @Result(property = "mimeType", column = "mime_type"),
        @Result(property = "uploadTime", column = "upload_time"),
        @Result(property = "downloadCount", column = "download_count"),
        @Result(property = "isDeleted", column = "is_deleted")
    })
    List<File> searchByUserIdAndKeyword(Long userId, String keyword);
    
    @Select("SELECT * FROM files WHERE user_id = #{userId} AND file_type = #{fileType} AND is_deleted = 0 ORDER BY upload_time DESC")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "originalName", column = "original_name"),
        @Result(property = "storedName", column = "stored_name"),
        @Result(property = "filePath", column = "file_path"),
        @Result(property = "fileSize", column = "file_size"),
        @Result(property = "fileType", column = "file_type"),
        @Result(property = "mimeType", column = "mime_type"),
        @Result(property = "uploadTime", column = "upload_time"),
        @Result(property = "downloadCount", column = "download_count"),
        @Result(property = "isDeleted", column = "is_deleted")
    })
    List<File> findByUserIdAndFileType(Long userId, String fileType);
    
    @Select("SELECT COUNT(*) FROM files WHERE user_id = #{userId} AND is_deleted = 0")
    int countByUserId(Long userId);
    
    @Select("SELECT COALESCE(SUM(file_size), 0) FROM files WHERE user_id = #{userId} AND is_deleted = 0")
    long getTotalSizeByUserId(Long userId);
    
    @Select("SELECT COALESCE(SUM(download_count), 0) FROM files WHERE user_id = #{userId} AND is_deleted = 0")
    int getTotalDownloadsByUserId(Long userId);
    
    @Insert("INSERT INTO files(user_id, original_name, stored_name, file_path, file_size, file_type, mime_type, upload_time, download_count, is_deleted) " +
            "VALUES(#{userId}, #{originalName}, #{storedName}, #{filePath}, #{fileSize}, #{fileType}, #{mimeType}, #{uploadTime}, #{downloadCount}, #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(File file);
    
    @Update("UPDATE files SET download_count = download_count + 1 WHERE id = #{id}")
    int incrementDownloadCount(Long id);
    
    @Update("UPDATE files SET is_deleted = 1 WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(Long id, Long userId);
    
    @Delete("DELETE FROM files WHERE id = #{id}")
    int deleteById(Long id);
}