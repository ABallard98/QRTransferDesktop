import java.io.File;
import java.util.Scanner;

/**
 * This class is used to collect data from the "ServerIp.txt" file which contains the
 * server's IP address and port for the socket to connect to.
 */
public class ServerIpReader {

    /**
     * Method to collect IP address provided from text file
     * @return String - IP address
     */
    public static String getIpAddress(){
        String toReturn = "";
        try{
            Scanner in = new Scanner(new File("ServerIP.txt"));
            if(in.hasNext()){
                toReturn = in.next();
            }
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Method to collect port provided from text file
     * @return int - Port
     */
    public static int getPort(){
        int toReturn = 0;
        try{
            Scanner in = new Scanner(new File("ServerIP.txt"));
            if(in.hasNext()){
                in.nextLine(); //skip ip address
                toReturn = in.nextInt();
            }
            in.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }



}
