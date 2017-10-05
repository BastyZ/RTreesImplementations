package com.logaritmos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {


  public static void main(String[] args) throws Exception {
    int M = 157; //cantidad de rectangulos tal que el nodo no pese mas que B
                  // calculado con Test.java

    int maxCord = 500000;
    int maxDelta= 100;
//PARAMETRO A CAMBIAR :D
    int x = 13;//0; not-> x<9 || x>25

    int k = 10;
    int linearCount = 0;
    int greeneCount = 0;
    int searchCount = 0;

    Random rnd = new Random();
    Scanner s = new Scanner(System.in);
    int m = (M * 40) / 100; // m = 40% M

    /*while (x < 9 || x > 25) {
    *  System.out.println("Ingrese la cantidad de rectangulos (x entre 9 y 25):");
    *  x =( Integer.parseInt(s.nextLine()) ); //nro de rectangulos
    *}*/
    int n = (int) Math.pow(2,x);

    /*while (k <= 1 || k >= n) {
    *  System.out.println("Ingrese el porción de rectangulos a buscar (k entre 1 y n):");
    *  k = Integer.parseInt(s.nextLine());
    *}*/
    int left = rnd.nextInt(maxCord);
    int bottom = rnd.nextInt(maxCord);
    int deltaX = rnd.nextInt(maxDelta);
    int deltaY = rnd.nextInt(maxDelta);

    Rectangle r = new Rectangle(left, left+deltaX,bottom+deltaY,bottom);
    DiskController diskController = new DiskController(M);
    long address = diskController.memoryAssigner();
    Root tree = new Root(m,M,r,diskController,address);

    //Date date = new Date();

    LinearSplit lsplit = new LinearSplit();
    GreeneSplit gsplit = new GreeneSplit();

    //Linear Split
    Rectangle rn;
    System.out.println("Comenzando insersiones tipo "+lsplit.name()+" con M="+M+" y n="+n);
    long iLinearTime = System.nanoTime();
    //System.out.println(new Timestamp(date.getTime())+" : Timestamp primera insercion");
    for(int i = 0; i < n; i++){
      left = rnd.nextInt(maxCord);
      bottom = rnd.nextInt(maxCord);
      deltaX = rnd.nextInt(maxDelta);
      deltaY = rnd.nextInt(maxDelta);
      rn = new Rectangle(left,left+deltaX,bottom+deltaY, bottom);
      tree.insert(rn,lsplit);
    }
    //date = new Date();
    long fLinearTime = System.nanoTime();

    //System.out.println(new Timestamp(date.getTime())+" : Timestamp con "+n+" inserciones.");
    linearCount = diskController.callDisk;
    System.out.println("Nro de accesos a disco : "+ linearCount);

    //Greene Split
    diskController.callDisk=0;
    tree = new Root(m,M,r,diskController,address);
    System.out.println("Comenzando insersiones tipo "+gsplit.name()+" con M="+M+" y n="+n);
    //System.out.println(new Timestamp(date.getTime())+" : Timestamp primera insercion");
    long iGreeneTime = System.nanoTime();
    for(int i = 0; i < n; i++){
      left = rnd.nextInt(maxCord);
      bottom = rnd.nextInt(maxCord);
      deltaX = rnd.nextInt(maxDelta);
      deltaY = rnd.nextInt(maxDelta);
      rn = new Rectangle(left,left+deltaX,bottom+deltaY, bottom);
      tree.insert(rn,lsplit);
    }
    long fGreeneTime = System.nanoTime();

    //date = new Date();
    //System.out.println(new Timestamp(date.getTime())+" : Timestamp con "+n+" inserciones.");
    greeneCount = diskController.callDisk;
    System.out.println("Nro de accesos a disco : "+greeneCount);

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
    //date = new Date();
    long iSearchTime = System.nanoTime();
    //System.out.println(new Timestamp(date.getTime())+" : Timestamp antes de la búsqueda, con n="+n);
    System.out.println("Iniciando búsqueda...");
    int rfound = 0;
    for(Rectangle searched : toFind){
      ArrayList<Rectangle> foundRect = tree.search(searched);
      rfound+= foundRect.size();
    }
    //date = new Date();
    long fSearchTime = System.nanoTime();

    System.out.println("Se buscaron "+toFind.size()+" elementos");
    System.out.println("Se encontraron "+rfound+" elementos");
    searchCount= diskController.callDisk;
    System.out.println("Nro de accesos a disco en búsqueda : "+searchCount);
    //System.out.println(new Timestamp(date.getTime())+" : Timestamp después de la búsqueda, con n="+n);

    /*1.Tiempo de construccion en cada tipo de insercion
        (linearTime, greeneTime) (linearCount, greeneCount)
    * 2.Espacio ocupado y porcentage de llenado de las páginas
    *   (full=4MB*nroNodosCreados)
    * 3.Evaluar desempeño de busqueda
    *   (buscados, encontrados, searchTime, searchCount)*/

    long linearTime = fLinearTime - iLinearTime;
    long greeneTime = fGreeneTime - iGreeneTime;
    float ocupation = diskController.nodeOcupation();
    long searchTIme = fSearchTime - iSearchTime;
    System.out.println("-- RESULTADOS -----------------------------------");
    System.out.println("Nro elementos : "+n);
    System.out.println("1. linear (tiempo(ns) | accesos), greene (tiempo(ns) | accesos)");
    System.out.println(linearTime+"\t"+linearCount+"\t"+greeneTime+"\t"+greeneCount);
    System.out.println("2. Porcentaje de ocupacion (%) : "+ocupation);
    System.out.println("3. rect. (buscados | encontrados) busqueda (tiempo(ns) | acceso) ");
    System.out.println(toFind.size()+"\t"+rfound+"\t"+searchTIme+"\t"+searchCount);
    System.out.println(n+"\t"+linearTime+"\t"+linearCount+"\t"+greeneTime+"\t"+greeneCount+"\t"
        +ocupation+"\t"+toFind.size()+"\t"+rfound+"\t"+searchTIme+"\t"+searchCount);
  }
}
