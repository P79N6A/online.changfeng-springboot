package online.changfeng.music.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class QRCodeUtils {
	
	public void createQRCode(String filePath, String content) {
		int width=300;      		//ͼƬ�Ŀ���
        int height=300;     		//ͼƬ�ĸ߶�
        String format="png";    	//ͼƬ�ĸ�ʽ
//        String content="���Ӱ��";     //����

        /**
         * �����ά��Ĳ���
         */
        HashMap hints=new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //ָ���ַ�����Ϊ��utf-8��
        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);  //ָ����ά��ľ����ȼ�Ϊ�м�
        hints.put(EncodeHintType.MARGIN, 2);    //����ͼƬ�ı߾�

        /**
         * ���ɶ�ά��
         */
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            Path file=new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public String getContentFromQRCode(String filePath) {
		MultiFormatReader formatReader=new MultiFormatReader();
        File file=new File(filePath);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer
                                    (new BufferedImageLuminanceSource(image)));
            HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //ָ���ַ�����Ϊ��utf-8��
            Result result=formatReader.decode(binaryBitmap,hints);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}