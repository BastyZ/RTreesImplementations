package com.logaritmos;

import java.util.Random;

public class Test {
  public static void main(String[] args) throws Exception {
    //int M = (int) args[0];
    int B = 4096; //bytes max por pagina
    int M = 200; //cantidad de rectangulos tal que el nodo no pese mas que B

    int maxCord = 500000;
    int maxDelta = 100;

    Random rnd = new Random();

    int m = (M * 40) / 100; // m = 40% M

    int left = rnd.nextInt(maxCord);
    int bottom = rnd.nextInt(maxCord);
    int deltaX = rnd.nextInt(maxDelta);
    int deltaY = rnd.nextInt(maxDelta);

    Rectangle r = new Rectangle(left, left + deltaX, bottom + deltaY, bottom);
    DiskController diskController = new DiskController(M);
    long address = diskController.memoryAssigner();
    Node tree = new Root(m, M, r, diskController, address);

    diskController.saveNode(tree);
    long rootSize =diskController.getNodeSize(address);
    System.out.println("Tamaño de raiz : " + rootSize + " bytes");

    int n=0;
    while (diskController.getNodeSize(address) < B){
      if(n==157) { break;}
      n++;
      Rectangle rn;
      left = rnd.nextInt(maxCord);
      bottom = rnd.nextInt(maxCord);
      deltaX = rnd.nextInt(maxDelta);
      deltaY = rnd.nextInt(maxDelta);
      rn = new Rectangle(left, left + deltaX, bottom + deltaY, bottom);
      tree.insert(rn, new LinearSplit());
      System.out.println("Rectangulos insertados : " + n);
    }

    System.out.println("Tamaño de raiz llena : " + diskController.getNodeSize(address) + " bytes, con "+n+" nodos insertados. Con raiz vacía de "+rootSize+" bytes");
    //Tamaño de raiz llena : 4089 bytes, con 157 nodos insertados. Con raiz vacía de 478 bytes

  }
}
