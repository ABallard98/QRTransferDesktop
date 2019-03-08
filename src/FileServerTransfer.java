import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerTransfer {

    private ServerSocket serverSocket;
    private String ipAddress;
    private int port;
    private File fileToTransfer;

    public FileServerTransfer(String ipAdress, int port, File selectedFile){
        try{
            this.ipAddress = ipAddress;
            this.port = port;
            //this.serverSocket = new ServerSocket(port);
            this.fileToTransfer = selectedFile;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startServer (){
        try{
            ServerSocketThread serverSocketThread = new ServerSocketThread(fileToTransfer);
            serverSocketThread.run();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
