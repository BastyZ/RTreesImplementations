package com.logaritmos;

import java.util.ArrayList;

public class Node extends RTree {
 /*TODO implementar metodos no abstractos
 * 1. Cada nodo interno representa un rectangulo rectangle (MBR)
 * 2. Cada nodo posee entre m y M hijos
 * 3. La raiz posee al menos dos hijos
 * 3.a. La raiz no posee padre
 */
 private ArrayList<Node> children;
 private DiskController diskController;


  public Node(DiskController file) {
    super();
    this.children = null;
    this.diskController = file;
    this.rectangle = diskController.computeMBR();
  }

  //fx getter
  public Node getFather() {return father;}

  public Boolean overlaps(Rectangle aRectangle){
    return this.rectangle.overlaps(aRectangle);
  }
 //fx para setear nodos
  public void setFather(Node n){father=n;}

}
