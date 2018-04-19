
/**
 * Write a description of class GameServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer
{
    private ServerSocket theServer;

    private int MAXPLAYERS = 2;
    private int numPlayers;

    private String p1Msg; //Message from p1
    private String p2Msg; //Message from p2
    private String map;

    private TalkToClientThread p1Thread;
    private TalkToClientThread p2Thread;
    /**
     * Constructor for objects of class GameServer
     */
    public GameServer()
    {
        p1Msg = "";
        p2Msg = "";
        numPlayers = 0;
        generateNewMap();
        try {
            theServer = new ServerSocket(8888);
            System.out.println("===== THE SERVER IS NOW ACCEPTING CONNECTIONS =====");
        } catch (IOException ex) {
            System.out.println("Error in GameServer constructor.");
        }
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void acceptConnections() {
        try {
            while (numPlayers < MAXPLAYERS) {
                Socket s = theServer.accept();

                numPlayers++;

                if(numPlayers == 1) {
                    p1Thread = new TalkToClientThread(s, numPlayers);
                } else {
                    p2Thread = new TalkToClientThread(s, numPlayers);
                    Thread t1 = new Thread(p1Thread);
                    Thread t2 = new Thread(p2Thread);
                    t1.start();
                    t2.start();
                }
            }    
        } catch (IOException ex) {
            System.out.println("Error in acceptConnections() method.");  
        }
    }

    public void generateNewMap(){
        String newMap = "MAP-";
        Random r = new Random();

        for(int i = 0; i<15; i++){
            for(int j = 0; j<15; j++){
                if(i == 0 || i == 14)
                    newMap += ("tile " + i + " " + j + " " + 1);
                else if(j == 0 || j == 14)
                    newMap += ("tile " + i + " " + j + " " + 1);
                else if((i%2) == 0 && (j%2) == 0)
                    newMap += ("tile " + i + " " + j + " " + 1);
                else if(((i>1 && i<4)||(i>10 && i<13)) && 
                ((j>1 && j<4)||(j>10 && j<13)))
                    newMap += ("tile " + i + " " + j + " " + 2);
                else if(((i==4 || i==10) && j<4) || ((i==4 || i==10) && j>10) ||
                ((j==4 || j==10) && i<4) || ((j==4 || j==10) && i>10))
                    newMap += ("tile " + i + " " + j + " " + 2);
                else if (!(((i>0 && i<5) || (i>9 && i<14)) && 
                    ((j>0 && j<5) || (j>9 && j<14)))){
                    int spawn = r.nextInt(9);
                    if(spawn>0)
                        newMap += ("tile " + i + " " + j + " " + 2);
                    else
                        newMap += ("tile " + i + " " + j + " " + 0);
                }else
                    newMap += ("tile " + i + " " + j + " " + 0);
                newMap+="-";
            }
        }

        map = newMap;
    }

    class TalkToClientThread implements Runnable{
        private int playerID;
        private Socket theSocket;
        private Scanner dataIn;
        private PrintWriter dataOut;

        public TalkToClientThread(Socket s, int pid){
            theSocket = s;
            playerID = pid;
            try {  
                dataIn = new Scanner(theSocket.getInputStream());
                dataOut = new PrintWriter(theSocket.getOutputStream());;

                dataOut.println(playerID);
                dataOut.flush();
            } catch (IOException ex) {    
                System.out.println("Error in TalkToClientThread constructor.");   
            }
        }

        public void run(){
            dataOut.println(map);
            dataOut.flush();
            readWriteToClient();
        }

        public void readWriteToClient(){
            try{
                while(true){
                    if(playerID == 1) {

                        dataOut.println(p2Msg);
                        dataOut.flush();
                        if(dataIn.hasNextLine()){
                            p1Msg = dataIn.nextLine();
                        }
                    } else {

                        dataOut.println(p1Msg);
                        dataOut.flush();
                        if(dataIn.hasNextLine()){
                            p2Msg = dataIn.nextLine(); 
                        }
                    }

                    Thread.sleep(10);
                }
            }catch(InterruptedException ie){}
        }
    }

    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
