package online.changfeng.music.utils;//package com.qianxu.utils;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.github.tobato.fastdfs.domain.StorePath;
//import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
//import com.github.tobato.fastdfs.service.FastFileStorageClient;
//
////@Component
//public class FastDFSClient {
//
//	@Autowired
//	private FastFileStorageClient storageClient;
//
////	@Autowired
////	private AppConfig appConfig; // 项目参数配置
//
//	/**
//	 * 上传文件
//	 *
//	 * @param file
//	 *            文件对象
//	 * @return 文件访问地址
//	 * @throws IOException
//	 */
//	public String uploadFile(MultipartFile file) throws IOException {
//		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
//				FilenameUtils.getExtension(file.getOriginalFilename()), null);
//
//		return storePath.getPath();
//	}
//
//	public String uploadFile2(MultipartFile file) throws IOException {
//		StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
//				FilenameUtils.getExtension(file.getOriginalFilename()), null);
//
//		return storePath.getPath();
//	}
//
//	public String uploadQRCode(MultipartFile file) throws IOException {
//		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
//				"png", null);
//
//		return storePath.getPath();
//	}
//
//	public String uploadFace(MultipartFile file) throws IOException {
//		StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
//				"png", null);
//
//		return storePath.getPath();
//	}
//
//	public String uploadBase64(MultipartFile file) throws IOException {
//		StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
//				"png", null);
//
//		return storePath.getPath();
//	}
//
//	/**
//	 * 将一段字符串生成�?个文件上�?
//	 *
//	 * @param content
//	 *            文件内容
//	 * @param fileExtension
//	 * @return
//	 */
//	public String uploadFile(String content, String fileExtension) {
//		byte[] buff = content.getBytes(Charset.forName("UTF-8"));
//		ByteArrayInputStream stream = new ByteArrayInputStream(buff);
//		StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
//		return storePath.getPath();
//	}
//
//	// 封装图片完整URL地址
////	private String getResAccessUrl(StorePath storePath) {
////		String fileUrl = AppConstants.HTTP_PRODOCOL + appConfig.getResHost() + ":" + appConfig.getFdfsStoragePort()
////				+ "/" + storePath.getFullPath();
////		return fileUrl;
////	}
//
//	/**
//	 * 删除文件
//	 *
//	 * @param fileUrl
//	 *            文件访问地址
//	 * @return
//	 */
//	public void deleteFile(String fileUrl) {
//		if (StringUtils.isEmpty(fileUrl)) {
//			return;
//		}
//		try {
//			StorePath storePath = StorePath.praseFromUrl(fileUrl);
//			storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
//		} catch (FdfsUnsupportStorePathException e) {
//			e.getMessage();
//		}
//	}
//}
