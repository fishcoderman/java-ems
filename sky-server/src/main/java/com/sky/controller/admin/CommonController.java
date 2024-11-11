package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @PostMapping("/upload")
  @ApiOperation("文件上传")
  public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return Result.error("上传的文件不能为空");
    }

    try {
      // 确保上传目录存在
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // 获取原始文件名
      String originalFilename = file.getOriginalFilename();

      // 手动获取扩展名
      int dotIndex = originalFilename.lastIndexOf('.');
      String extension = (dotIndex == -1) ? "" : originalFilename.substring(dotIndex);

      // 获取文件名（不包括扩展名）
      String baseName = (dotIndex == -1) ? originalFilename : originalFilename.substring(0, dotIndex);

      // 生成UUID
      String uuid = UUID.randomUUID().toString();

      // 构建新文件名
      String newFileName = baseName + "-" + uuid + extension;

      // 保存文件到指定目录
      Path filePath = uploadPath.resolve(newFileName);
      Files.copy(file.getInputStream(), filePath);

      // 生成文件的URL路径
      String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
          .path("/uploads/")
          .path(newFileName)
          .toUriString();

      return Result.success(fileUrl);
    } catch (IOException e) {
      e.printStackTrace();
      return Result.error("文件上传失败");
    }
  }
}