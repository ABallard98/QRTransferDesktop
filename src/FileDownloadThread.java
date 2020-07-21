import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

/**
 * This class is used to download a file from the server on to the desktop device
 */
public class FileDownloadThread extends Thread {

    private Socket socket; //socket with connection to server

    /**
     * Constructor for FileDownloadThread, takes socket as argument
     *
     */
    public FileDownloadThread() {
        try{
            this.socket = new Socket(ServerIpReader.getIpAddress(), ServerIpReader.getPort());
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Method to start the thread running and start the downloadFile method
     */
    @Override
    public void run(){
        if(this.socket.isConnected()){
            downloadFile();
        }
    }

    /**
     * Method to download a file from the server on to the desktop device
     */
    private void downloadFile(){
        try{
            //sending instructions to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            //todo change this
            //dos.writeUTF("DOWNLOAD-"+filename+"-"+byteSize);

            //input stream to receive file information and bytes
            //todo have the server sending the meta-data of the file before the bytes of file
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            //new file
            //todo change this to a customized download location
            File newFile = new File("tempFile.txt");

            FileOutputStream fos = new FileOutputStream(newFile);

            //copy file to internal storage of application
            int fileSize = dis.readInt();
            byte[] buffer = new byte[4096];
            int read = 0;
            int totalRead = 0;
            int remaining = fileSize;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                fos.write(buffer, 0, read);
            }

            System.out.println("read " + totalRead + " bytes.");
            //close input and output streams
            fos.close();
            dis.close();

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
