import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class GenerateQRCode {

    /**
     * Method that generates the string to be encoded in the QR code
     * @param file - File selected by user
     * @return String to be encoded in the QR code
     */
    public synchronized static String generateQRCode(File file) {
        //todo get information from file parsed and create QR text code
        //String qrCodeText = "80.2.250.205-8007-tosend.png-203535";
        String filename = file.getName().replace("-","");
        long fileSizeBytes = file.length();
        String qrCodeText = "80.2.250.205-8007-"+filename+"-"+fileSizeBytes;

        return qrCodeText;
    }

    /**
     * Method that generates a QR code for the selected file
     * @param qrFile - File to transfer
     * @return BufferedImage of QR code
     */
    public synchronized static BufferedImage createQRImage(File qrFile) {

        //QR Code Image properties
        int size = 500; //size of QR code (pixels)
        String qrCodeText = generateQRCode(qrFile);

        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try{
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
            // Make the BufferedImage that are to hold the QRCode
            int matrixWidth = bitMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            //create the image
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);

            // Paint and save the image
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException e){
            e.printStackTrace();
            return null;
        }
    }

}
