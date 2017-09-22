package com.logaritmos;

public class Node {
 /*TODO
 * 1. Cada nodo interno representa un rectangulo (=nodo)
 * 2. nodos con datos = hojas
 * 3. cada nodo lmacena r rectángulos (m <= r <= M)
 * 3.a. la raiz almacena al menos dos rectángulos
 * 4. Todas las  hojas están a la misma altura*/
 private Node father;
 private Node left;
 private Node center;
 private Node right;

 private boolean Leaf;
 private int value; //valores como enteros (?)

 //fx para extraer nodos
  public Node getFather() {return father;}
 public Node getLeft(){ return left;}
 public Node getCenter(){return center;}
 public Node getRight(){return right;}
 //fx para setear nodos
  public void setFather(Node n){father=n;}
 public void setLeft(Node n) {left= n;}
 public void setCenter(Node n) {center= n;}
 public void setRight(Node n) {right= n;}

 public boolean isFull(){
   return !(isLEmpty() && isCEmpty() && isREmpty());
 }

 private boolean isLEmpty(){
   return this.left == null;
 }
 private boolean isCEmpty(){
   return this.center == null;
 }
 private boolean isREmpty(){
   return this.right==null;
 }

  /*TODO
  fxs para hojas*/
  public boolean isLeaf(){return this.Leaf;}
  public void setValue(int n){
    if (this.isLeaf()){
      value=n;
    } else{
      //TODO manejo de errores
    }
  }
}
