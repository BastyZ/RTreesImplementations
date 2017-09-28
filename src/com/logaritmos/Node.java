package com.logaritmos;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{
 /*TODO implementar metodos no abstractos
 * 1. Cada nodo interno representa un rectangulo rectangle (MBR)
 * 2. Cada nodo posee entre m y M hijos
 * 3. La raiz posee al menos dos hijos
 * 3.a. La raiz no posee padre
 */
 private int m; //tamano min de paginacion
 private int M; //tamano max de pag, VARIABLE
 private ArrayList<Rectangle> rectangles;
 private ArrayList<Long> children; //IMPORTANTE: indices de rectangles y children deben coincidir
 private DiskController diskController;
 /*info propia del nodo*/
 private long myAddress;
 private Rectangle myMBR;
 private boolean imLeaf;
 private boolean imRoot;

 public Node (int m, int M, ArrayList<Rectangle> r, ArrayList<Long> c,
     DiskController d, long addr, boolean leaf) throws Exception{
   this(m,M,r,c,d,addr,leaf,false);
 }

 protected Node(int m, int M, ArrayList<Rectangle> r, ArrayList<Long> c,
     DiskController d, long addr, boolean leaf, boolean root) throws Exception{
   if(r.size()>M || (!root && r.size()<m)){
     throw new Exception();
   }else{
    this.m = m;
    this.M = M;
    this.rectangles = r;
    this.children = c;
    this.diskController = d;
    this.myAddress = addr;
    this.imLeaf = leaf;
    this.imRoot = root;
     //TODO incompleto
    //this.diskController.saveNode(this);
   }
 }

}
