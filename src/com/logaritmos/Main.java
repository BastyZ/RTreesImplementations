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

      Random rnd = new Random();

      int n = rnd.nextInt(nRectMax) + nRectMin ; //nro de rectangulos

      int left = rnd.nextInt(maxCord);
      int bottom = rnd.nextInt(maxCord);
      int deltaX = rnd.nextInt(maxDelta);
      int deltaY = rnd.nextInt(maxDelta);

      Rectangle r = new Rectangle(left, left+deltaX,bottom+deltaY,bottom);
      DiskController diskController = new DiskController();
      long address = diskController.memoryAssigner();
      Root tree = new Root(m,M,r,diskController,address);

      ArrayList<Rectangle> toFind = new ArrayList<Rectangle>();

      Date date = new Date();

      LinearSplit lsplit = new LinearSplit();
      GreeneSplit gsplit = new GreeneSplit();

      //Linear Split
      Rectangle rn = null;
      System.out.println("Insersiones tipo "+lsplit.name()+" con M="+M+" y n="+n);
      System.out.println(new Timestamp(date.getTime())+" : Timestamp primera insercion");
      for(int i = 0; i < n; i++){
        left = rnd.nextInt(maxCord);
        bottom = rnd.nextInt(maxCord);
        deltaX = rnd.nextInt(maxDelta);
        deltaY = rnd.nextInt(maxDelta);
        rn = new Rectangle(left,left+deltaX,bottom+deltaY, bottom);
        tree.insert(rn,lsplit);
      }
      date = new Date();
      System.out.println(new Timestamp(date.getTime())+" : Timestamp con "+n+" inserciones.");
      System.out.println("Nro de accesos a disco : "+diskController.callDisk);

      //Greene Split
      rn = null;
      System.out.println("Insersiones tipo "+gsplit.name()+" con M="+M+" y n="+n);
      System.out.println(new Timestamp(date.getTime())+" : Timestamp primera insercion");
      for(int i = 0; i < n; i++){
        left = rnd.nextInt(maxCord);
        bottom = rnd.nextInt(maxCord);
        deltaX = rnd.nextInt(maxDelta);
        deltaY = rnd.nextInt(maxDelta);
        rn = new Rectangle(left,left+deltaX,bottom+deltaY, bottom);
        tree.insert(rn,lsplit);
      }
      date = new Date();
      System.out.println(new Timestamp(date.getTime())+" : Timestamp con "+n+" inserciones.");
      System.out.println("Nro de accesos a disco : "+diskController.callDisk);

      //Busqueda




      System.out.print("Hello fuckers");
    }
}
