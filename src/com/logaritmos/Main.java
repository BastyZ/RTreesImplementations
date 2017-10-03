package com.logaritmos;

import com.sun.jmx.snmp.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
      //int M = (int) args[0];
      //int B = 4096; //bytes max por pagina
      int M = 150; //cantidad de rectangulos tal que el nodo no pese mas que B
      int m = 60; //

      int maxCord = 500000;
      int maxDelta= 100;

      int nRectMin = 2^9;
      int nRectMax = 2^25;
      int n ; //nro de rectangulos


      Random rnd = new Random();

      int left = rnd.nextInt(maxCord);
      int bottom = rnd.nextInt(maxCord);
      int deltaX = rnd.nextInt(maxDelta); //TODO
      int deltaY = rnd.nextInt(maxDelta); //TODO

      Rectangle r = new Rectangle(left, left+deltaX,bottom,bottom+deltaY);
      DiskController diskController = new DiskController();
      long address = diskController.memoryAssigner();
      Root tree = new Root(m,M,r,diskController,address);

      Rectangle rn = null;
      ArrayList<Rectangle> toFind = new ArrayList<Rectangle>();

      Date date = new Date();
      int k =; //TODO nro de inserciones

      LinearSplit lsplit = new LinearSplit();
      GreeneSplit gsplit = new GreeneSplit();

      System.out.println("Insersiones tipo "+lsplit.name()+" con M="+M+" y k="+k);
      System.out.println(new Timestamp(date.getTime())+" : Timestamp primera insercion");

      for(int i = 0; i < k; i++){
        left = rnd.nextInt()*maxRect;
        bottom = rnd.nextInt()*maxRect;
        deltaX = ;//TODO copiar el de arriba
        deltaY = ;//TODO that
        rn = new Rectangle()
      }


      System.out.print("Hello fuckers");
    }
}
