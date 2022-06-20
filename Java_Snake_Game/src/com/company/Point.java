package com.company;

import java.awt.*;

public class Point {
    private int x;
    private int y;
    //設定圖片
    //private ImageIcon img;

    public Point(){
        //設定圖片
        //img = new ImageIcon("fruit.png:)
        //img = new ImageIcon(getClass().getResource("point.png"));
        this.x = (int)(Math.floor(Math.random() * Main.colum) * Main.CELL_SIZE);
        this.y = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void drawPoint(Graphics g){
        //設定圖片
        //img.painIcon(null, g, this.x,  this.y);
        g.setColor(Color.white);
        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
    }

    //放Snake原因：避免point放在snake身上
    public void setNewLocation(Snake s){
        int newPointX;
        int newPointY;
        boolean overlapping;
        do{
            newPointX = (int)(Math.floor(Math.random() * Main.colum) * Main.CELL_SIZE);
            newPointY = (int)(Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
            overlapping = check_overlap(newPointX, newPointY, s); //確認是否放在身上
        }while(overlapping);

        //確認完成後設定新point
        this.x = newPointX;
        this.y = newPointY;
    }

    private boolean check_overlap(int x, int y, Snake s){
        for(int i=0; i < s.getSnakeBody().size(); i++){
            if( x == s.getSnakeBody().get(i).x && y == s.getSnakeBody().get(i).y){
                return true; //如果overlap 重新執行do-while
            }
        }
        return false; // 迴圈全跑完沒有return就確認沒有overlap
    }
}

