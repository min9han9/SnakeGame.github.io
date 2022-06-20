package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel implements KeyListener {

    //視窗大小400*400
    public static int width = 400;
    public static int height = 400;

    public static final int CELL_SIZE = 20; //身體大小20
    public static int row = height / CELL_SIZE;
    public static int colum = width / CELL_SIZE;
    private Snake snake;
    private Point point;
    private Timer timer;
    private int speed = 100; //每0.1秒執行一次
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highest_score;
    String desktop = System.getProperty("user.home") + "/Desktop/";
    String myFile = desktop + "filename.txt";

    public Main(){
        highest_score_record();
        reset();
        addKeyListener(this);
    }

    private void setTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                repaint();
            }
        }, 0, speed);
    }

    private void reset(){
        score = 0;
        if(snake != null){
            snake.getSnakeBody().clear(); //清空
        }
        //reset 設定
        direction = "Right";
        addKeyListener(this);
        allowKeyPress = true;
        snake = new Snake();
        point = new Point();
        setTimer();
    }

    //在視窗畫出蛇
    @Override
    public void paintComponent(Graphics g){
        //檢查蛇有沒有咬到自己身體
        ArrayList<Node> snakeBody = snake.getSnakeBody();
        Node head = snakeBody.get(0);
        for(int i = 1; i < snake.getSnakeBody().size(); i++){
            if(snakeBody.get(i).x == head.x && snakeBody.get(i).y == head.y){
                //先讀取分數，第一次玩一定是最高分
                write_highest_score_file(score);

                //咬到身體停止遊戲
                allowKeyPress = false;
                timer.cancel();
                timer.purge();
                int response = JOptionPane.showOptionDialog(this, "Game Over! Your Score:" + score + "\nThe highest score:" + highest_score + "\nWant to start over?"  , "Snake Game", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);

                //選擇是否關閉或重新遊戲
                switch(response){
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }


        g.fillRect(0, 0, width, height);
        point.drawPoint(g); //先畫出point再畫出蛇可以避免瑕疵
        snake.drawSnake(g);

        //remove snake tail and put in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;

        //right, x += cell_size
        //left, x-= cell_size
        //up, y+= cell_size
        //down, y -= cell_size

        if(direction.equals("Left")){
            snakeX -= CELL_SIZE;
        }else if(direction.equals("Up")){
            snakeY -= CELL_SIZE;
        }else if(direction.equals("Right")){
            snakeX += CELL_SIZE;
        }else if(direction.equals("Down")){
            snakeY += CELL_SIZE;
        }

        //頭往前放
        Node newHead = new Node(snakeX, snakeY);

        //在remove尾巴前，先確認是不是有吃到point
        if(snake.getSnakeBody().get(0).x == point.getX() && snake.getSnakeBody().get(0).y == point.getY())
        {
            //設置新的point
            //畫出新point
            //分數++
            point.setNewLocation(snake);
            point.drawPoint(g);
            score++;
        }else{
            snake.getSnakeBody().remove(snake.getSnakeBody().size()-1);
        }

        snake.getSnakeBody().add(0, newHead);
        allowKeyPress =true; //重新畫完蛇之後就可以允許上下左右
        requestFocusInWindow();
    }


    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }

    public static void main(String[] args){
        JFrame window = new JFrame("snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setVisible(true);
        window.setResizable(false); //不能調整視窗大小
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(allowKeyPress) {
            //上下左右轉
            if (e.getKeyCode() == 37 && !direction.equals("Right")) {
                direction = "Left";
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction = "Right";
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";
            }
            allowKeyPress = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void highest_score_record(){
        try{
            File myObj = new File(myFile);
            Scanner myRecord = new Scanner(myObj);
            highest_score = myRecord.nextInt();
            myRecord.close();
        }catch(FileNotFoundException e){
            highest_score = 0;
            try{
                File myObj = new File(myFile);
                if(myObj.createNewFile()){
                    System.out.println("File created:" + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write(0);
            }catch(IOException err){
                System.out.println("An error occurred");
                err.printStackTrace(); //印出錯誤的原因
            }
        }
    }

    public void write_highest_score_file(int score){
        try{
            FileWriter myWriter = new FileWriter(myFile);
            if(score > highest_score){
                myWriter.write("" + score);
                highest_score = score;
            }else{
                myWriter.write("" + highest_score);
            }
            myWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}