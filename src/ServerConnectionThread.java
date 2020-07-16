import java.io.*;
import java.net.Socket;

/**
 * This class is used to implement a thread that sends a file to the server.
 */
public class ServerConnectionThread extends Thread {

    private File fileToTransfer; //file to transfer
    private Socket socket; //socket

    /**
     * Constructor for ServerConnectionThread
     * @param fileToTransfer - File to transfer
     */
    public ServerConnectionThread(File fileToTransfer){
        this.fileToTransfer = fileToTransfer;
        try {

            final String IP_ADDRESS = ServerIpReader.getIpAddress();
            final int PORT = ServerIpReader.getPort();

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
     * Method to send file to server. File is sent by being broken down into bytes, and these bytes are sent
     * to the server, for it to be re-built.
     * @param fileToTransfer - file to be sent
     */
    private synchronized void transferFile (File fileToTransfer){

        byte[] byteArray = new byte[(int) fileToTransfer.length()]; //array for bytes of file

        System.out.println("Attempting to send file to server...");

        final String IP_ADDRESS = ServerIpReader.getIpAddress();
        final int PORT = ServerIpReader.getPort();

        try{
            socket = new Socket(IP_ADDRESS,PORT); //ensure there is a connection to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String filename = fileToTransfer.getName();

            //formatting filename
            filename = filename.replace("-","")
                    .replace("(","")
                    .replace(")","")
                    .replace(" ","");

            //instructions to be sent to the server
            String instructionToSend = "SENDING-"+filename
                +"-"+fileToTransfer.length();

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



