package online.changfeng.music.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUtils {
	/**
	 * ����url��ȡfile
	 *
	 * @param url
	 * @param suffix
	 *            �ļ���׺��
	 */
	public static File createFileByUrl(String url, String suffix) {
		byte[] byteFile = getImageFromNetByUrl(url);
		if (byteFile != null) {
			File file = getFileFromBytes(byteFile, suffix);
			return file;
		} else {
			return null;
		}
	}

	/**
	 * ���ݵ�ַ������ݵ��ֽ���
	 *
	 * @param strUrl
	 *            �������ӵ�ַ
	 * @return
	 */
	private static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// ͨ����������ȡͼƬ����
			byte[] btImg = readInputStream(inStream);// �õ�ͼƬ�Ķ���������
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������л�ȡ����
	 *
	 * @param inStream
	 *            ������
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	// ������ʱ�ļ�
	private static File getFileFromBytes(byte[] b, String suffix) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = File.createTempFile("pattern", "." + suffix);
			System.out.println("��ʱ�ļ�λ�ã�" + file.getCanonicalPath());
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static MultipartFile createImg(String url) {
		try {
			// Fileת����MutipartFile
			File file = FileUtils.createFileByUrl(url, "jpg");
			FileInputStream inputStream = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
			return multipartFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MultipartFile fileToMultipart(String filePath) {
		try {
			// Fileת����MutipartFile
			File file = new File(filePath);
			FileInputStream inputStream = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(), "png", "image/png", inputStream);
			return multipartFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	public static void main(String[] args) {
//		// WebFileUtils.createFileByUrl("http://122.152.205.72:88/group1/M00/00/01/CpoxxFr7oIaAZ0rOAAC0d3GKDio580.png",
//		// "png");
//		// WebFileUtils.createImg("http://122.152.205.72:88/group1/M00/00/01/CpoxxFr7oIaAZ0rOAAC0d3GKDio580.png");
//	}

	public static boolean base64ToFile(String filePath, String base64Data)  throws Exception {
		String dataPrix = "";
        String data = "";

        if(base64Data == null || "".equals(base64Data)){
            return false;
        }else{
            String [] d = base64Data.split("base64,");
            if(d != null && d.length == 2){
                dataPrix = d[0];
                data = d[1];
            }else{
                return false;
            }
        }

        // ��ΪBASE64Decoder��jar���⣬�˴�ʹ��spring����ṩ�Ĺ��߰�
        byte[] bs = Base64Utils.decodeFromString(data);
        // ʹ��apache�ṩ�Ĺ����������
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(filePath), bs);
        
        return true;
	}
}
