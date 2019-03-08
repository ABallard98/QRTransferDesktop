import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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
        deletePreviouslyMadeQr();
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
            deletePreviouslyMadeQr();
            qrImageView.setImage(null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if(selectedFile != null){
                this.fileToTransfer = selectedFile;

                //generated QR code for chosen file
                qrCodeTextArea.setText(GenerateQRCode.generateQRCode(fileToTransfer));

                Thread.sleep(1000); //wait a bit for QR code to be generated

                //load QR code
                Image qrCodeImage = new Image("generatedQRCodes\\"+fileToTransfer.getName());
                qrImageView.setImage(qrCodeImage);

                return fileToTransfer;
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean deletePreviouslyMadeQr(){
        try{
            File qrFile = new File("src\\generatedQR.png");
            qrFile.delete();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
