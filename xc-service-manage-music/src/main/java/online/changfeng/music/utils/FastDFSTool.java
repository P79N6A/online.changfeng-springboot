package online.changfeng.music.utils;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * FastDFS������
 * 
 * @author Administrator
 */
public class FastDFSTool {
	/**
	 * �ϴ��ļ���FastDFS
	 * 
	 * @param bs
	 *            �ļ��ֽ�����
	 * @param filename
	 *            �ļ���
	 * @return �ϴ��ɹ��󣬴����fastdfs�е��ļ�λ�ü��ļ���
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static String uploadFile(byte[] bs, String filename)
			throws FileNotFoundException, IOException, Exception {
		// ���classpath���ļ��ľ���·��
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
       // ��ʼ���ͻ���
		ClientGlobal.init(resource.getClassLoader()
				.getResource("fdfs_client.conf").getPath());

		// �����ϴ�ͻ���
		TrackerClient trackerClient = new TrackerClient();
		// ͨ���ϴ�ͻ���ȡ�����ӻ���ϴ��������
		TrackerServer connection = trackerClient.getConnection();
		// С�ܿͻ���
		StorageClient1 storageClient1 = new StorageClient1(connection, null);
		// ����ļ�������չ��
		String extension = FilenameUtils.getExtension(filename);

		// ͨ��С�ܿͻ��˿�ʼ�ϴ��ļ��������ش����fastdfs�е��ļ�λ�ü��ļ���
		// ���磺 group1/M00/00/00/wKg4ZViGbUOAWYBOAAFcP6dp0kY652.jpg
		String upload_file1 = storageClient1.upload_file1(bs, extension, null);

		return upload_file1;
	}
}