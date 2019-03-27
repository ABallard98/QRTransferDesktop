import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionThread implements Runnable {

    private File fileToTransfer;
    private Socket socket;
    private static final String IP_ADDRESS = "80.2.250.205"; //ip address of server
    private static final int PORT = 8007; //port to connect to

    public ServerConnectionThread(File fileToTransfer){
        this.fileToTransfer = fileToTransfer;
        try {
            this.socket = new Socket(IP_ADDRESS,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(this.fileToTransfer != null) {
            transferFile(fileToTransfer);
        }
    }

    private synchronized void transferFile (File fileToTransfer){
        byte[] byteArray = new byte[(int) fileToTransfer.length()];
        System.out.println("Attempting to send file to server...");
        try{//sending instructions to the server
            socket = new Socket(IP_ADDRESS,PORT); //ensure there is a connection to the server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String filename = fileToTransfer.getName();
            filename = filename.replace("-","")
                    .replace("(","")
                    .replace(")","")
                    .replace(" ","");

            String instructionToSend = "SENDING-"+filename
                +"-"+fileToTransfer.length();

            System.out.println(instructionToSend);
            dos.writeUTF(instructionToSend);

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



