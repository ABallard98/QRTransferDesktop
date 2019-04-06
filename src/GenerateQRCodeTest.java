import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GenerateQRCodeTest {

    @org.junit.jupiter.api.Test
    void generateQRCode() {

        //Given
        File testPdfFile = new File ("testFiles\\testPdf.pdf");
        File testImageFile = new File("testFiles\\testImage.png");

        //Expected Results
        String pdfExpectedResult = "80.2.250.205-8007-testPdf.pdf-0";
        String imageExpectedResult = "80.2.250.205-8007-testImage.png-0";

        //Actual Results
        String pdfFileQrCode = GenerateQRCode.generateQRCode(testPdfFile);
        String imageFileQrCode = GenerateQRCode.generateQRCode(testImageFile);

        //Assert Equals
        assertEquals(pdfFileQrCode,pdfExpectedResult);
        assertEquals(imageFileQrCode,imageExpectedResult);
    }

}