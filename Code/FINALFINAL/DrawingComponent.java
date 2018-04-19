
/**
 * Creates and draws all of the components for the GUI
 * 
 * @author Tomy Callanta & Kemp Po
 * @version 5/9/2016
 */
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.geom.*;
import java.net.*;

public class DrawingComponent extends JComponent
{
    private MyHierarchyListener mh;
    private Tile[][] board;
    private BufferedImage sb, bg, menu, pause, pointer, lose, win;
    private Character[] player;
    private Animation animation;
    private int pauseInput;
    private Timer timer;
    private int time;
    private int playerID;
    private String map;
    private boolean isConnected;
    private TalkToServerThread ttst;
    private String explosion;
    private boolean youWin;

    private enum STATE{
        MENU,
        GAME,
        PAUSE
    };

    private STATE State = STATE.MENU;
    /**
     * Constructor for objects of class DrawingComponent
     */
    public DrawingComponent()
    {
        isConnected = false;
        mh = new MyHierarchyListener();
        this.addHierarchyListener(mh);
        board = new Tile[15][15];
        addBG();
        animation = new Animation(this);
        player = new Character[2];
        setPlayers();
        pauseInput = 0;
        explosion = "MAP-";
        youWin = false;
    }

    /**
     * Draws all the components of the GUI
     * 
     * @param  g where the components are drawn on
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        if(State == STATE.MENU){
            g2d.drawImage(menu, 0, 0, null);

            if(!isConnected){
                g2d.setFont(new Font("Impact", Font.BOLD, 65));
                g2d.setColor(Color.RED);
                g2d.drawString("Connect to Server", 25,350);
                g2d.drawString("-spacebar-", 115, 425);
            }
        }else if(State == STATE.GAME){
            pauseInput = 0;
            g2d.drawImage(sb, 0, 0, null);
            g2d.drawImage(bg, 0, 60, null);

            for(int i = 0; i<15; i++){
                for(int j = 0; j<15; j++){
                    if(board[i][j] instanceof BombFire){
                        BombFire bf = (BombFire) board[i][j];
                        if(bf.maxTime()){
                            if(bf.ownFlame())
                                destroy(i,j);
                            board[i][j] = null;
                            explosion += ("tile " + i + " " + j + " " + 0 + "-");
                        }
                    }

                    if(board[i][j] instanceof Bomb){
                        Bomb b = (Bomb) board[i][j];
                        if(b.maxTime()){
                            addBombFire(i,j);
                            if(player[playerID-1] != null)
                                player[playerID-1].reload();
                        }
                    }

                    if(board[i][j] instanceof Item){
                        Item item = (Item) board[i][j];
                        if(item.maxTime() == true ){
                            board[i][j] = null;
                            explosion += ("tile " + i + " " + j + " " + 0 + "-");
                        }
                    }
                }
            }

            for(int i = 0; i<15; i++){
                for(int j = 0; j<15; j++){
                    if(board[i][j] instanceof Bomb || board[i][j] instanceof Breakable ||
                    board[i][j] instanceof BombFire || board[i][j] instanceof Item){
                        board[i][j].draw(g2d);
                    }
                }
            }

            for(Character c : player){
                if(c!=null)
                    c.draw(g2d);
            }

            if(player[playerID-1] != null){ 
                int currentRow = (player[playerID-1].getY()+20 - 60)/40;
                int currentColumn = (player[playerID-1].getX())/40;

                if(board[currentRow][currentColumn] instanceof Item){
                    Item step = (Item) board[currentRow][currentColumn];
                    player[playerID-1].useItem(step.getType());
                    board[currentRow][currentColumn] = null;
                    explosion += ("tile " + currentRow + " " + currentColumn + " " + 0 + "-");
                }else if(board[currentRow][currentColumn] instanceof BombFire){
                    player[playerID-1] = null; 
                    explosion += ("death " + playerID + "-");
                }

                if(youWin){
                    g2d.setColor(new Color(0,0,0,0.5f));
                    g2d.fill(new Rectangle2D.Double(0,0,600,660));
                    g2d.drawImage(win,(600 - win.getWidth())/2, (660-win.getHeight())/2, null);
                    gameDone();
                }
            }else{
                g2d.setColor(new Color(0,0,0,0.5f));
                g2d.fill(new Rectangle2D.Double(0,0,600,660));
                g2d.drawImage(lose,(600 - lose.getWidth())/2, (660-lose.getHeight())/2, null);
                gameDone();
            }

            if(!explosion.equals("MAP-"))
                ttst.sendState(explosion);
            explosion = "MAP-";
        }else if(State == STATE.PAUSE){
            g2d.drawImage(pause, 0,0, null);
            if(pauseInput == 0)
                g2d.drawImage(pointer, 200,305, null);
            else if(pauseInput == 1)
                g2d.drawImage(pointer, 200, 435, null);
        }
    }

    public void destroy(int i, int j){
        Random rand = new Random();
        if(board[i-1][j] instanceof Breakable){
            int r = rand.nextInt(2);
            if(r == 0){
                Tile a = board[i-1][j];
                board[i-1][j] = new Item(a.getX(), a.getY());
                Item item = (Item) board[i-1][j];
                item.defineType();
                explosion += ("tile " + (i-1) + " " + j + " " + (40+item.getType()) + "-");
            }else{
                board[i-1][j] = null; 
                explosion += ("tile " + (i-1) + " " + j +" " + 0 + "-");
            } 
        }
        if(board[i+1][j] instanceof Breakable){
            int r = rand.nextInt(2);
            if(r == 0){
                Tile a = board[i+1][j];
                board[i+1][j] = new Item(a.getX(), a.getY());
                Item item = (Item) board[i+1][j];
                item.defineType();
                explosion += ("tile " + (i+1) + " " + j + " " + (40+item.getType()) + "-");
            }else{
                board[i+1][j] = null; 
                explosion += ("tile " + (i+1) + " " + j +" " + 0 + "-");
            } 
        }
        if(board[i][j-1] instanceof Breakable){
            int r = rand.nextInt(2);
            if(r == 0){
                Tile a = board[i][j-1];
                board[i][j-1] = new Item(a.getX(), a.getY());
                Item item = (Item) board[i][j-1];
                item.defineType();
                explosion += ("tile " + i + " " + (j-1) + " " + (40+item.getType()) + "-");
            }else{
                board[i][j-1] = null; 
                explosion += ("tile " + i + " " + (j-1) +" " + 0 + "-");
            } 
        }
        if(board[i][j+1] instanceof Breakable){
            int r = rand.nextInt(2);
            if(r == 0){
                Tile a = board[i][j+1];
                board[i][j+1] = new Item(a.getX(), a.getY());
                Item item = (Item) board[i][j+1];
                item.defineType();
                explosion += ("tile " + i + " " + (j+1) + " " + (40+item.getType()) + "-");
            }else{
                board[i][j+1] = null; 
                explosion += ("tile " + i + " " + (j+1) +" " + 0 + "-");
            } 
        }
    }

    /**
     * Accesses the jpg file for the background
     */
    public void addBG(){
        try{
            sb = ImageIO.read(new File("StatusBar.jpg"));
            bg = ImageIO.read(new File("bg2.jpg"));
            menu = ImageIO.read(new File("TitleScreen.jpg"));
            pause = ImageIO.read(new File("PauseScreen.jpg"));
            pointer = ImageIO.read(new File("Pointer.png"));
            lose = ImageIO.read(new File("LoseMessage.png"));
            win = ImageIO.read(new File("WinMessage.png"));
        }catch(IOException e){}
    }

    public void gameDone(){
        time = 0;
        ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    time++;
                    if(time >= 60) {
                        State = STATE.MENU;
                        ttst.sendState("MENU");
                        timer.stop();
                    }               
                }
            };

        timer = new Timer(50,al);
        timer.start();
    }

    public void setPlayers(){
        player[0] = new Character(40,100);
        player[1] = new Character(520,100);
    }

    public void changeState(String s){
        switch(s){
            case "MENU":
            State = STATE.MENU;
            break;
            case "GAME":
            State = STATE.GAME;
            break;
            case "PAUSE":
            State = STATE.PAUSE;
            break;
        }
    }

    public void processMap(String code){
        String[] newTiles = code.split("-");
        for(String s : newTiles){
            if(!(s.equals("MAP"))){
                String[] msg = s.split(" ");
                if(msg[0].equals("tile")){
                    int i = Integer.parseInt(msg[1]);
                    int j = Integer.parseInt(msg[2]);
                    changeTile(i, j, msg[3]);
                }else if(msg[0].equals("move")){
                    int id = Integer.parseInt(msg[1]);
                    moveOtherChar(id, msg[2], msg[3], msg[4]);
                }else{
                    int id = Integer.parseInt(msg[1]);
                    player[id-1] = null;
                    youWin = true;
                }
            }
        }
    }

    public void changeTile(int i, int j, String kind){
        switch(kind){
            case "0":
            board[i][j] = null;
            break;
            case "1":
            board[i][j] = new Unbreakable(j*40, i*40 + 60);
            break;
            case "2":
            board[i][j] = new Breakable(j*40, i*40 + 60);
            break;
            case "3":
            board[i][j] = new Bomb(j*40, i*40 + 60);
            break;

            case "40":
            board[i][j] = new Item(j*40, i*40 + 60);
            Item a = (Item) board[i][j];
            a.setType(0);
            break;

            case "41":
            board[i][j] = new Item(j*40, i*40 + 60);
            Item b = (Item) board[i][j];
            b.setType(1);
            break;

            case "42":
            board[i][j] = new Item(j*40, i*40 + 60);
            Item c = (Item) board[i][j];
            c.setType(2);
            break;

            case "43":
            board[i][j] = new Item(j*40, i*40 + 60);
            Item d = (Item) board[i][j];
            d.setType(3);
            break;

            case "5":
            board[i][j] = new BombFire(j*40, i*40 + 60,0,true);
            break;

            case "6":
            board[i][j] = new BombFire(j*40, i*40 + 60,1,true);
            break;

            case "7":
            board[i][j] = new BombFire(j*40, i*40 + 60,2,true);
            break;
        }
    }

    public void moveOtherChar(int id, String dir, String x, String y){
        player[id-1].faceDirection(dir);

        int newX = Integer.parseInt(x);
        int newY = Integer.parseInt(y);
        player[id-1].setXY(newX, newY);
    }

    public void addBombFire(int i, int j){
        boolean left = true;
        boolean right = true;
        boolean down  = true;
        boolean up = true;

        int x = j * 40;
        int y = i * 40 + 60;
        board[i][j] = new BombFire(x,y,0);
        explosion += ("tile " + i + " " + j + " " + 5 + "-");

        int d = 1;
        while(down){
            if(d >= player[playerID-1].getRange()){
                down = false;
            }
            else if(board[i+d][j] != null){
                down = false;
            }
            else{
                int tempY = (i + d) * 40 + 60;
                int tempX = (j) * 40;
                board[i+d][j] = new BombFire(tempX, tempY, 1);
                explosion += ("tile " + (i+d) + " " + j + " " + 6 + "-");
            }
            d++;   
        }
        int u = 1;
        while(up){
            if(u >= player[playerID-1].getRange()){
                up = false;
            }
            else if(board[i-u][j] != null){
                up = false;
            }

            else{
                int tempY = (i - u) * 40 + 60;
                int tempX = (j) * 40;
                board[i-u][j] = new BombFire(tempX, tempY, 1);
                explosion += ("tile " + (i-u) + " " + j + " " + 6 + "-");
            }
            u++;
        }
        int r = 1;
        while(right){
            if(r >= player[playerID-1].getRange()){
                right = false;
            }
            else if(board[i][j+r] != null){
                right = false;
            }
            else{
                int tempY = (i) * 40 + 60;
                int tempX = (j + r) * 40;
                board[i][j + r] = new BombFire(tempX, tempY, 2);
                explosion += ("tile " + i + " " + (j+r) + " " + 7 + "-");
            }
            r++;   
        }
        int l = 1;
        while(left){
            if(l >= player[playerID-1].getRange()){
                left = false;
            }
            else if(board[i][j-l] != null){
                left = false;
            }
            else{
                int tempY = (i) * 40 + 60;
                int tempX = (j - l) * 40;
                board[i][j - l] = new BombFire(tempX, tempY, 2);
                explosion += ("tile " + i + " " + (j-l) + " " + 7 + "-");
            }
            l++;   
        }
    }

    public void moveChar(int dir, int newX, int newY ){ 
        explosion += ("move " + playerID + " " + dir + " " + newX + " " + newY + "-"); 
    }

    public void connectToServer(){
        Socket s;
        try{
            s = new Socket("localhost", 8888);

            ttst = new TalkToServerThread(s);
            Thread t = new Thread(ttst);
            t.start();
        }catch (IOException ex) {
            System.out.println("Error in connectToServer() method.");
        }
    }

    class MyHierarchyListener implements HierarchyListener{
        private DrawingComponent dc = DrawingComponent.this;
        private KeyInput ki = new KeyInput();
        public void hierarchyChanged(HierarchyEvent he){
            if(SwingUtilities.getRoot(dc) != null){
                dc.removeHierarchyListener(mh);
                JFrame f = (JFrame) SwingUtilities.getRoot(dc);
                f.getContentPane().addKeyListener(ki);
                f.getContentPane().setFocusable(true);

                Thread constantUpdate = new Thread(animation);
                constantUpdate.setPriority(Thread.MAX_PRIORITY);
                constantUpdate.start();
            }
        }
    }

    class KeyInput implements KeyListener{
        public void keyPressed(KeyEvent ke){
            int keyCode = ke.getKeyCode();
            int column = 0;
            int row = 0;
            if(State == STATE.MENU){
                switch(keyCode){
                    case KeyEvent.VK_ENTER:
                    if(isConnected){
                        setPlayers();
                        processMap(map);
                        State = STATE.GAME;
                        ttst.sendState("GAME");
                    }
                    break;

                    case KeyEvent.VK_SPACE:
                    if(!isConnected){
                        DrawingComponent.this.connectToServer();
                        isConnected = true;
                    }
                    break;
                }
            }else if(State == STATE.GAME){
                int p = playerID - 1;
                if(player[p] != null){
                    int currentRow = (player[p].getY()+20 - 60)/40;
                    int currentColumn = (player[p].getX())/40;
                    switch(keyCode){
                        case KeyEvent.VK_W:
                        row = currentRow - 1;
                        column = currentColumn + 1;
                        if(row<0)
                            row = 0; 
                        if(player[p].canMove(board[row][currentColumn], board[row][column],
                            board[currentRow][currentColumn],0)){
                            player[p].moveUp();
                            moveChar(1,player[p].getX(),player[p].getY());
                        }
                        break;

                        case KeyEvent.VK_S:
                        row = currentRow + 1;
                        column = currentColumn + 1;
                        if(row>14)
                            row = 14; 
                        if(player[p].canMove(board[row][currentColumn], board[row][column],
                            board[currentRow][currentColumn],1)){
                            player[p].moveDown();
                            moveChar(0,player[p].getX(),player[p].getY());
                        }
                        break;

                        case KeyEvent.VK_A:
                        row = currentRow + 1;
                        column = currentColumn - 1;
                        if(column < 0)
                            column = 0; 
                        if(player[p].canMove(board[currentRow][column], board[row][column],
                            board[currentRow][currentColumn],2)){
                            player[p].moveLeft();
                            moveChar(2,player[p].getX(),player[p].getY());
                        }
                        break;

                        case KeyEvent.VK_D:
                        row = currentRow + 1;
                        column = currentColumn + 1;
                        if(column > 14)
                            column = 14; 
                        if(player[p].canMove(board[currentRow][column], board[row][column],
                            board[currentRow][currentColumn],3)){
                            player[p].moveRight();
                            moveChar(3,player[p].getX(),player[p].getY());
                        }
                        break;

                        case KeyEvent.VK_SPACE:
                        if(player[p].getDropped() < player[p].getMaxBomb() &&
                        board[currentRow][currentColumn] == null &&
                        (board[currentRow+1][currentColumn] == null ||
                            board[currentRow-1][currentColumn] == null ||
                            board[currentRow][currentColumn+1] == null ||
                            board[currentRow][currentColumn-1] == null)){
                            board[currentRow][currentColumn] = new Bomb(currentColumn*40, currentRow*40+60);
                            player[p].drop();
                            Bomb b = (Bomb) board[currentRow][currentColumn]; 
                            b.startTimer();
                            explosion += ("tile " + currentRow + " " + currentColumn + " " + 3 + "-");
                        }
                        break;

                        case KeyEvent.VK_ENTER:
                        State = STATE.PAUSE;
                        ttst.sendState("PAUSE");
                        break;
                    }
                }
            }else if(State == STATE.PAUSE){
                switch(keyCode){
                    case KeyEvent.VK_W:
                    if(pauseInput >0){
                        pauseInput--;
                    }
                    break;

                    case KeyEvent.VK_S:
                    if(pauseInput < 1){
                        pauseInput++;
                    }
                    break;

                    case KeyEvent.VK_ENTER:
                    if(pauseInput == 0){
                        State = STATE.GAME;
                        ttst.sendState("GAME");
                    }else if(pauseInput == 1){
                        State = STATE.MENU;
                        ttst.sendState("MENU");
                        processMap(map);
                        setPlayers();
                    }
                    break;
                }
            }
        }

        public void keyReleased(KeyEvent ke){}

        public void keyTyped(KeyEvent ke){}
    }

    class TalkToServerThread implements Runnable{
        private Socket theSocket;
        private Scanner dataIn;
        private PrintWriter dataOut;
        private String output;

        public TalkToServerThread(Socket s){
            theSocket = s;
            output = "";
            try{
                dataIn = new Scanner(theSocket.getInputStream());
                dataOut = new PrintWriter(theSocket.getOutputStream());
            } catch (IOException ex) {
                System.out.println("Error in TalkToServerThread constructor.");
            }
        }

        public void run(){
            playerID = Integer.parseInt(dataIn.nextLine());
            map = dataIn.nextLine();
            processMap(map);
            try{
                while(true){
                    dataOut.println(output);
                    dataOut.flush();
                    output = "";

                    String command = "";
                    if(dataIn.hasNextLine()){
                        command = dataIn.nextLine();
                    }

                    if(command.equals(""))
                        continue;
                    else if(command.equals("GAME") || command.equals("PAUSE") || command.equals("MENU")){
                        changeState(command);
                    }
                    else{
                        processMap(command);
                    }    
                    Thread.sleep(10);
                }
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }

        public void sendState(String s){
            output = s;
        }
    }
}
