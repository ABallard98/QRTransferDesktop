import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
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
import java.util.Random;

/**
 * This class implements a controller for the main window of the application
 */
public class MainController {

    @FXML
    private ImageView qrImageView; //image view which contains the generated QR code

    @FXML
    private Button selectFileButton; //Button to select and upload a file

    @FXML
    private Button downloadFileButton; //Button to create QR code for file download

    @FXML
    private TextArea qrCodeTextArea; //shows what is encoded in the generated QR code

    @FXML
    private Label serverStatusLabel; //label to display whether server connection is successful

    /**
     * Initializer for main window
     */
    public void initialize(){

        //initialise window
        selectFileButton.setText("Select file");
        downloadFileButton.setText("Download File");
        qrCodeTextArea.setVisible(false);

        //get IP address and port from file
        final String IP_ADDRESS = ServerIpReader.getIpAddress();
        final int PORT = ServerIpReader.getPort();

        try{
            Socket testSocket = new Socket(IP_ADDRESS, PORT);
            //see if socket connection works
            if(testSocket.isConnected()){
                serverStatusLabel.setText("Server online"); //set server status
                serverStatusLabel.setTextFill(Color.web("#32CD32", 0.8));
            } else {
                serverStatusLabel.setTextFill(Color.web("FF0000", 0.8));
            }
        } catch(Exception e){
            serverStatusLabel.setTextFill(Color.web("FF0000", 0.8));
        }

        //set OnClick methods for buttons
        selectFileButton.setOnAction(e -> uploadFile());
        downloadFileButton.setOnAction(e -> generateQRCodeForDownload());

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

            if(fileToTransfer != null){ //if user has selected a file
                loadQrCodeForFile(fileToTransfer); //load QR code for image into image view
                startFileTransfer(fileToTransfer); //upload file to server
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

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

    /**
     * Method to triggers on downloadFileButton on-click. This method
     * generates a QR code to download a file from mobile to this desktop.
     */
    private void generateQRCodeForDownload(){
        try{
            //String to be embedded in QR code
            String qrCodeText = "DOWNLOAD-"+makeRandomCharIdentifier(8);

            qrCodeTextArea.setText(qrCodeText);
            qrCodeTextArea.setVisible(true);

            //generate and load QR code
            BufferedImage downloadQRCode = GenerateQRCode.createQRImageForDownload(qrCodeText);
            Image image = SwingFXUtils.toFXImage(downloadQRCode, null);

            //set image view
            qrImageView.setImage(image);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to generate a random string of chars of n length that as an identifier
     * for the PC waiting to download a file
     * @param numOfChars - length of string generated
     * @return - String of n length consisting of random chars
     */
    private String makeRandomCharIdentifier(int numOfChars){
        String s = "";
        Random rng = new Random();
        for(int i = 0; i < numOfChars; i++){
            s += (char) ('a' + rng.nextInt(26));
        }
        return s;
    }



}
