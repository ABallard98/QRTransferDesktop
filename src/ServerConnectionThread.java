import java.io.*;
import java.net.Socket;

/**
 * This class is used to implement a thread that sends a file to the server.
 */
public class ServerConnectionThread extends Thread {

    private File fileToTransfer; //file to transfer
    private Socket socket; //socket
    private static final String IP_ADDRESS = "86.157.154.4"; //ip address of server
    private static final int PORT = 8007; //port to connect to

    /**
     * Constructor for ServerConnectionThread
     * @param fileToTransfer - File to transfer
     */
    public ServerConnectionThread(File fileToTransfer){
        this.fileToTransfer = fileToTransfer;
        try {
            this.socket = new Socket(IP_ADDRESS,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run method that ensures that the file in the ServerConnectionThread is not null and then
     * calls the transferFile method.
     */
    @Override
    public void run() {
        if(this.fileToTransfer != null) {
            transferFile(fileToTransfer);
        }
    }

    /**
     *
     * @param fileToTransfer
     */
    private synchronized void transferFile (File fileToTransfer){
        byte[] byteArray = new byte[(int) fileToTransfer.length()]; //array for bytes of file
        System.out.println("Attempting to send file to server...");

        try{
            socket = new Socket(IP_ADDRESS,PORT); //ensure there is a connection to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String filename = fileToTransfer.getName();
            //formatting filename
            filename = filename.replace("-","")
                    .replace("(","")
                    .replace(")","")
                    .replace(" ","");

            String instructionToSend = "SENDING-"+filename
                +"-"+fileToTransfer.length();

            //System.out.println(instructionToSend);
            dos.writeUTF(instructionToSend); //sending instructions to the server

            InputStream in = new FileInputStream(fileToTransfer);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            long fileLength = fileToTransfer.length();

            if(fileLength > Integer.MAX_VALUE){
                System.out.println("File too large...");
            } else {
                int count;
                while ((count = in.read(byteArray)) > 0) {
                    out.write(byteArray, 0, count);
                }
                in.close();
                out.flush();
                out.close();
                System.out.println("Sent.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



