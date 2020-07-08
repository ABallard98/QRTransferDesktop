import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.Socket;

/**
 * This class implements a controller for the main window of the application
 */
public class MainController {

    @FXML
    private ImageView qrImageView; //image view which contains the generated QR code

    @FXML
    private Button selectFileButton; //Button to select and upload a file

    @FXML
    private TextArea qrCodeTextArea; //shows what is encoded in the generated QR code

    @FXML
    private Label serverStatusLabel; //label to display whether server connection is successful

    /**
     * Initializer for main window
     */
    public void initialize(){
        selectFileButton.setText("Select file");
        qrCodeTextArea.setVisible(false);

        //get IP address from file
        String ipAddress = ServerIpReader.getIpAddress();
        int port = ServerIpReader.getPort();

        try{
            Socket testSocket = new Socket(ipAddress, port);
            if(testSocket.isConnected()){
                serverStatusLabel.setText("Server online");
                serverStatusLabel.setTextFill(Color.web("#32CD32", 0.8));
            } else {
                serverStatusLabel.setTextFill(Color.web("FF0000", 0.8));
            }
        } catch(Exception e){
            serverStatusLabel.setTextFill(Color.web("FF0000", 0.8));
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
                            "Available Files",
                            "*.jpg", "*.jpeg", "*.png", "*.pdf", "*.mp4", "*.apk");
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
