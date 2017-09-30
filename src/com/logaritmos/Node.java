package com.logaritmos;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import org.w3c.dom.css.Rect;

public class Node implements Serializable{

 private static final long serialVersionUID = 4L;
 private int m; //tamano min de paginacion
 private int M; //tamano max de pag, VARIABLE
 private ArrayList<Rectangle> rectangles;
 private ArrayList<Long> children; //IMPORTANTE: indices de rectangles y children deben coincidir
 transient DiskController diskController;
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
   /*
 * 1. Cada nodo interno representa un rectangulo rectangle (MBR)
 * 2. Cada nodo posee entre m y M hijos
 * 3. La raiz posee al menos dos hijos
 * 3.a. La raiz no posee padre
 */
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
    this.diskController.saveNode(this);
   }
 }

 public byte[] serialize()
     throws IOException{
   ByteArrayOutputStream info = new ByteArrayOutputStream();
   ObjectOutputStream node = new ObjectOutputStream(info);
   node.writeObject(this);
   byte[] code = info.toByteArray();
   info.close();
   node.close();
   return code;
 }

 public Node deserialize(byte[] code)
     throws IOException, ClassNotFoundException{
   Node n;
   ByteArrayInputStream info = new ByteArrayInputStream(code);
   ObjectInputStream node = new ObjectInputStream(info);
   n = (Node) node.readObject();
   node.close();
   info.close();
   return n;
 }

  public ArrayList<Rectangle> getRectangles() {
    return this.rectangles;
  }

  public long getMemoryAddress(){
   return this.myAddress;
  }

  private Long getChildAddress(int index){
    return this.children.get(index);
  }

  private Node getChild (int index) throws IOException, ClassNotFoundException{
    return diskController.loadNode(getChildAddress(index));
  }

  private Rectangle getRectangle(int index){
    return this.rectangles.get(index);
  }

  public ArrayList<Rectangle> search(Rectangle r)
      throws IOException, ClassNotFoundException{
    ArrayList<Rectangle> found = new ArrayList<Rectangle>();
    if (this.imLeaf){
      for(Rectangle temp : this.rectangles){
        if(temp.intersects(r)){
          found.add(temp);
        }
      }
    } else {
      int index = 0;
      for (Rectangle temp : this.rectangles){
        if (temp.intersects(r)){
          ArrayList<Rectangle> inception = this.getChild(index).search(r);
          found.addAll(inception);
        }
        index++;
      }
    }
    return found;
  }

  private ArrayList<Integer> resetAndAdd(int i){
    ArrayList<Integer> array = new ArrayList<Integer>();
    array.add(i);
    return array;
  }

  public Long insert(Rectangle r, ISplit overflowHandler)
      throws Exception {
    if (this.imLeaf) {
      //Si el nodo es una hoja se agrega r a rectangles y un null a children
      this.rectangles.add(r);
      this.children.add(null);
    } else {
      //Si estamos en un nodo interno, hay que decidir por donde bajar para esto
      ArrayList<Integer> candidates = new ArrayList<Integer>();
      double areaMin = -1;
      int index = 0;
      for (Rectangle temp : this.rectangles) {
        double simArea = Rectangle.calculateMBR(r, temp).area();
        double deltaMBR = simArea - temp.area();
        if (areaMin == -1) {
          areaMin = deltaMBR;
          candidates.add(index);
        } else if (deltaMBR == areaMin) {
          candidates.add(index);
        } else if (deltaMBR < areaMin) {
          areaMin = deltaMBR;
          candidates = resetAndAdd(index);
        }
        index++;
      }
      //elejimos al MEJOR candidato
      if (candidates.size() > 1) {
        areaMin = -1;
        int candidate = 0;
        for (Integer cIndex : candidates) {
          Rectangle temp = rectangles.get(cIndex);
          double area = temp.area();
          if (areaMin == -1) {
            areaMin = area;
          } else if (area == areaMin) {
            candidate = cIndex;
          } else if (area < areaMin) {
            areaMin = area;
            candidate = cIndex;
          }
        }
        candidates = resetAndAdd(candidate);
      }
      int cIndex = candidates.get(0);
      //insertamos en rectangulo elegido
      Node thisChild = this.getChild(cIndex);
      Long childAddr = thisChild.insert(r, overflowHandler);
      ArrayList<Rectangle> childrenRect = this.getChild(cIndex).getRectangles();
      if (childAddr != null) {
        addChild(Rectangle.calculateMBR(childrenRect),childAddr);
      }
      this.rectangles.set(cIndex, Rectangle.calculateMBR(childrenRect));
    }
    if (this.rectangles.size() <= this.M) {
      //si no hubo overflow, guarda y retorna null
      diskController.saveNode(this);
      return null;
    } else {
      //si hay overflow hay que hacer split
      ArrayList<Integer> splitted = this.split(overflowHandler);

      ArrayList<Rectangle> r1 = new ArrayList<Rectangle>();
      ArrayList<Long> c1 = new ArrayList<Long>();
      int a1 = splitted.get(0).intValue();
      ArrayList<Rectangle> r2 = new ArrayList<Rectangle>();
      ArrayList<Long> c2 = new ArrayList<Long>();
      int a2 = splitted.get(1).intValue();

      c1.add(this.children.get(a1));
      r1.add(this.rectangles.get(a1));
      c2.add(this.children.get(a2));
      r2.add(this.rectangles.get(a2));

      this.children.remove(c1.get(0));
      this.children.remove(c2.get(0));
      this.rectangles.remove(r1.get(0));
      this.rectangles.remove(r2.get(0));

      int maxIndex = this.rectangles.size();
      Random rnd = new Random();
      while(maxIndex>0){
        int index = rnd.nextInt(this.children.size());
        Long child = this.children.remove(index);
        Rectangle rect = this.rectangles.remove(index);
        if ((c1.size() + children.size()) < this.m ){
          c1.add(child);
          r1.add(rect);
        } else if ((c2.size() + children.size()) < this.m){
          c2.add(child);
          r2.add(rect);
        } else {
          double area1 = Rectangle.calculateMBR(rect,r1).area() - Rectangle.calculateMBR(r1).area();
          double area2 = Rectangle.calculateMBR(rect,r2).area() - Rectangle.calculateMBR(r2).area();
          if(area1 < area2) {
            c1.add(child);
            r1.add(rect);
          } else {
            c2.add(child);
            r2.add(rect);
          }
        }
        maxIndex--;
      }
      //si el nodo es raiz
      if(this.imRoot){
        Long addr1 = this.diskController.memoryAssigner();
        Long addr2 = this.diskController.memoryAssigner();
        Node n1 = new Node(this.m,this.M,r1,c1,this.diskController,addr1,this.imLeaf);
        Node n2 = new Node(this.m,this.M,r2,c2,this.diskController,addr2,this.imLeaf);

        this.rectangles = new ArrayList<Rectangle>();
        this.children = new ArrayList<Long>();
        this.addChild(Rectangle.calculateMBR(r1),addr1);
        this.addChild(Rectangle.calculateMBR(r2),addr2);
        this.imLeaf = false;

        diskController.saveNode(this);
        diskController.saveNode(n1);
        diskController.saveNode(n2);
        return null;
      }
      //actualizo el nodo
      this.rectangles = r1;
      this.children = c1;
      Long addrBro = this.diskController.memoryAssigner();
      Node bro = new Node(this.m,this.M,r2,c2,this.diskController,addrBro,this.imLeaf);
      diskController.saveNode(this);
      diskController.saveNode(bro);
      return addrBro;
    }
  }

    private void addChild(Rectangle r, Long addr){
    this.rectangles.add(r);
    this.children.add(addr);
  }

  //Overwrite
  private ArrayList<Integer> split(ISplit overflowHandler) {
    return this.split(overflowHandler);
  }

  //Overwrite
  public ArrayList<Integer> split(LinearSplit l){
    int index = 0;
    int top = 0;
    int bottom = 0;
    int left = 0;
    int right = 0;
    int minTop, maxBottom, maxLeft, minRight;
    //iteramos sobre rectangles
    for (Rectangle temp : this.rectangles){
      if (index == 0){
        minTop = temp.getTop();
        maxBottom = temp.getBottom();
        maxLeft = temp.getLeft();
        minRight = temp.getRight();
      } else {
        if(isMin(temp.getRight(),minRight)){
          right = index;
          minRight = temp.getRight();
        } if (isMin(maxLeft,temp.getLeft())){
          left = index;
          maxLeft = temp.getLeft();
        } if (isMin(temp.getTop(),minTop)) {
          top = index;
          minTop = temp.getTop();
        } if (isMin(maxBottom,temp.getBottom())){
          bottom = index;
          maxBottom = temp.getBottom();
        }
      }
      index++;
    }
    //Actualizo Rectangulo myMBR como los rectangulos de rectangles
    this.myMBR = Rectangle.calculateMBR(rectangles);
    //Se crea un ArrayList para guardar los indices de los rectangulos más lejanos, luego
    ArrayList<Integer> farTrees = new ArrayList<Integer>();
    float propX = (minRight - maxLeft) / this.myMBR.getWidth();
    float propY = (minTop - maxBottom) / this.myMBR.getHeight();
    /*propX es mayor o igual a propY : se agrega el indice del menor derecho y mayor izquierdo
    * sino : se agrega el indice del menor superior y el máximo inferior*/
    if (propX >= propY){
      farTrees.add(right);
      farTrees.add(left);
    } else {
      farTrees.add(top);
      farTrees.add(bottom);
    }
    return farTrees;
  }

  private boolean isMin(int min,int max){
    return min < max;
  }

  //Overwrite
  public ArrayList<Integer> split(GreeneSplit g){
    return null;

  }

}
