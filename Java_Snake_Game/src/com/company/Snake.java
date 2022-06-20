package com.company;

import java.awt.*;
import java.util.ArrayList;

public class Snake {

    private ArrayList<Node> snakeBody;

    public Snake(){
        snakeBody = new ArrayList();
        snakeBody.add(new Node(80, 0));
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));
    }

    public ArrayList<Node> getSnakeBody(){
        return snakeBody;
    }

    //畫蛇的身體
    public void drawSnake(Graphics g){
        for(int i=0; i<snakeBody.size(); i++){
            if(i==0){
                g.setColor(Color.GREEN);
            }else{
                g.setColor(Color.ORANGE);
            }

            //如果蛇超出邊界設定頭會從另一邊出現
            Node n = snakeBody.get(i);
            if(n.x >= Main.width){
                n.x = 0;
            }
            if(n.x < 0){
                n.x = Main.width - Main.CELL_SIZE;//如果沒減去CELL_SIZE第一個點會在視窗外
            }
            if(n.y >= Main.height){
                n.y = 0;
            }
            if(n.y < 0){
                n.y = Main.height - Main.CELL_SIZE;//如果沒減去CELL_SIZE第一個點會在視窗外
            }
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }

}
