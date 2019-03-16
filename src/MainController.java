import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class MainController {

    @FXML
    private Button transferFileButton;

    @FXML
    private ImageView qrImageView;

    @FXML
    private Button selectFileButton;

    @FXML
    private TextArea qrCodeTextArea;

    private File fileToTransfer;

    public void initialize(){
        transferFileButton.setText("Transfer file");
        selectFileButton.setText("Select file :D");

        selectFileButton.setOnAction(e -> selectFile());
        transferFileButton.setOnAction(e -> startFileTransfer());
    }

    private void startFileTransfer(){
        try{
            if(fileToTransfer == null){
                System.out.println("Selected file is null");
            } else {
                System.out.println("Starting file transfer process...");
                FileServerTransfer server = new FileServerTransfer("80.2.250.205",8007, fileToTransfer);
                server.startServer();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private File selectFile(){
        try{
            //set existing QR code to null
            qrImageView.setImage(null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File To Transfer");

            //file extension filter - jpg, jpeg, png, pdf
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Available Files", "*.jpg", "*.jpeg", "*.png", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if(selectedFile != null){

                this.fileToTransfer = selectedFile; //chosen file to transfer

                //generated QR code for chosen file
                String qrText = GenerateQRCode.generateQRCode(fileToTransfer);
                qrCodeTextArea.setText(qrText);


                //load QR code
                try{
                    BufferedImage newQRCode = GenerateQRCode.createQRImage(fileToTransfer);
                    Image image = SwingFXUtils.toFXImage(newQRCode, null);
                    qrImageView.setImage(image);
                } catch (Exception e){
                    Image qrCodeImage = new Image("generatedQRCodes\\qrCode.png");
                    qrImageView.setImage(qrCodeImage);
                }
                return fileToTransfer;
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
