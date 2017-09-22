package com.logaritmos;

import java.util.ArrayList;

public class Node extends RTree {
 /*TODO
 * 1. Cada nodo interno representa un rectangulo rectangle (MBR)
 * 2. Cada nodo posee entre m y M hijos
 * 3. La raiz posee al menos dos hijos
 * 3.a. La raiz no posee padre
 */
 private ArrayList<Node> children;
 private ChildrenFile childrenFile;


  public Node(ChildrenFile file) {
    super();
    this.children = null;
    this.childrenFile = file;
    this.rectangle = childrenFile.computeMBR();
  }

  //fx getter
  public Node getFather() {return father;}

  public Boolean overlaps(Rectangle aRectangle){
    return this.rectangle.overlaps(aRectangle);
  }
 //fx para setear nodos
  public void setFather(Node n){father=n;}

}
