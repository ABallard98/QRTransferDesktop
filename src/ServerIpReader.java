import java.io.File;
import java.util.Scanner;

public class ServerIpReader {

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
