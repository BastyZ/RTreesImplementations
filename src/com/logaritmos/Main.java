package com.logaritmos;

import com.sun.jmx.snmp.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Main {


  public static void main(String[] args) throws Exception {
    //int M = (int) args[0];
    //int B = 4096; //bytes max por pagina
    int M = 0; //cantidad de rectangulos tal que el nodo no pese mas que B

    int maxCord = 500000;
    int maxDelta= 100;

    Random rnd = new Random();
    Scanner s = new Scanner(System.in);
    while (M < 1 || M > 4096) {
      System.out.println("Ingrese la cantidad de rectangulos (n entre 2^9 y 2^25):");
      M = Integer.parseInt(s.nextLine()); //nro de rectangulos
    }
    int m = (M * 40) / 100; // m = 40% M

    int nRectMin = 2^9;
    int nRectMax = 2^25;
    int n = 0;
    while (n < nRectMin || n > nRectMax) {
      System.out.println("Ingrese la cantidad de rectangulos (n entre 2^9 y 2^25):");
      n = Integer.parseInt(s.nextLine()); //nro de rectangulos
    }
    int k = 0;
    while (k <= 1 || k >= n) {
      System.out.println("Ingrese el porción de rectangulos a buscar (k entre 1 y n):");
      k = Integer.parseInt(s.nextLine());
    }
    int left = rnd.nextInt(maxCord);
    int bottom = rnd.nextInt(maxCord);
    int deltaX = rnd.nextInt(maxDelta);
    int deltaY = rnd.nextInt(maxDelta);

    Rectangle r = new Rectangle(left, left+deltaX,bottom+deltaY,bottom);
    DiskController diskController = new DiskController();
    long address = diskController.memoryAssigner();
    Root tree = new Root(m,M,r,diskController,address);

    Date date = new Date();

    LinearSplit lsplit = new LinearSplit();
    GreeneSplit gsplit = new GreeneSplit();

    //Linear Split
    Rectangle rn;
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
    diskController.callDisk=0;
    tree = new Root(m,M,r,diskController,address);
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
    ArrayList<Rectangle> toFind = new ArrayList<>();
    for(int i = 0; i < n / k; i++){
      left = rnd.nextInt(maxCord);
      bottom = rnd.nextInt(maxCord);
      deltaX = rnd.nextInt(maxDelta);
      deltaY = rnd.nextInt(maxDelta);
      rn = new Rectangle(left,left+deltaX,bottom+deltaY, bottom);
      toFind.add(rn);
    }
    diskController.callDisk=0;
    date = new Date();
    System.out.println(new Timestamp(date.getTime())+" : Timestamp antes de la búsqueda, con n="+n);

    int rfound = 0;
    for(Rectangle searched : toFind){
      ArrayList<Rectangle> foundRect = tree.search(searched);
      rfound+= foundRect.size();
    }
    date = new Date();
    System.out.println("Se buscaron "+toFind.size()+" elementos");
    System.out.println("Se encontraron "+rfound+" elementos");
    System.out.println("Nro de accesos a disco en búsqueda : "+diskController.callDisk);
    System.out.println(new Timestamp(date.getTime())+" : Timestamp después de la búsqueda, con n="+n);
    System.out.print("Bye fuckers");
  }
}
