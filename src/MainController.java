import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;

public class MainController {

    @FXML
    private ImageView qrImageView; //image view which contains the generated QR code

    @FXML
    private Button selectFileButton; //Button to select and upload a file

    @FXML
    private TextArea qrCodeTextArea; //shows what is encoded in the generated QR code

    /**
     * Initializer for main window
     */
    public void initialize(){
        selectFileButton.setText("Select file");
        qrCodeTextArea.setVisible(false);

        try{

        } catch(Exception e){

            //Alert the user that connection to server could not be made
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Could not connect to server");
            alert.setHeaderText(null);
            alert.setContentText("Could not establish a connection to server.");
            alert.showAndWait();

            e.printStackTrace();
        }

        selectFileButton.setOnAction(e -> uploadFile());
        //transferFileButton.setOnAction(e -> startFileTransfer());
    }

    /**
     * Method that prompts the user to select a file. Compatible file types are .jpg, .jpeg, .png and .pdf. A QR code
     * is then generated for the selected file which is then loaded into the imageView. The file is then parsed to
     * startFileTransfer and loadQrCodeForFile.
     */
    private void uploadFile(){
        try{
            //set existing QR code to null
            qrImageView.setImage(null);

            //set qrCodeTextArea to false
            qrCodeTextArea.setVisible(false);

            //Create file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File To Transfer");

            //file extension filter - jpg, jpeg, png, pdf
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter(
                            "Available Files", "*.jpg", "*.jpeg", "*.png", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            //open file chooser
            File fileToTransfer = fileChooser.showOpenDialog(new Stage());

            if(fileToTransfer != null){

                loadQrCodeForFile(fileToTransfer); //load QR code for image into image view
                startFileTransfer(fileToTransfer); //upload file to server

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }//end of selectFile

    /**
     * Method that creates and runs a serverSocketThread which takes in the socket and the selected file by the user.
     */
    private void startFileTransfer(File fileToTransfer){
        try{
            //Create and run ServerConnectionThread
            ServerConnectionThread serverConnectionThread = new ServerConnectionThread(fileToTransfer);
            serverConnectionThread.run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that loads the QR code created by the GenerateQRCode class.
     */
    private void loadQrCodeForFile(File fileToTransfer){
        //generated QR code for chosen file
        try{
            //get text to load into QR code
            String qrText = GenerateQRCode.generateQRCode(fileToTransfer);

            //todo delete this text-view for release
            qrCodeTextArea.setText(qrText);
            qrCodeTextArea.setVisible(true);

            //load QR code as an Image object
            BufferedImage newQRCode = GenerateQRCode.createQRImage(fileToTransfer);
            Image image = SwingFXUtils.toFXImage(newQRCode, null);

            //set image
            qrImageView.setImage(image);

        } catch (Exception e){
            Image qrCodeImage = new Image("generatedQRCodes\\qrCode.png");
            qrImageView.setImage(qrCodeImage);
        }
    }

}
