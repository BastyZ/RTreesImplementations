package com.logaritmos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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
      throws IOException, ClassNotFoundException{
    if(this.imLeaf){
      this.rectangles.add(r);
      this.children.add(null);
    } else {
      ArrayList<Integer> candidates = new ArrayList<Integer>();
      double areaMin = -1;
      int index = 0;
      for (Rectangle temp : this.rectangles){
        double simArea = myMBR.calculateMBR(r,temp);
        double deltaMBR = simArea - temp.area();
        if(areaMin == -1){
          areaMin = deltaMBR;
          candidates.add(index);
        }else if(deltaMBR == areaMin){
          candidates.add(index);
        }else if(deltaMBR < areaMin){
          areaMin = deltaMBR;
          candidates = resetAndAdd(index);
        }
        index++;
      }

      if(candidates.size() > 1){
        areaMin = -1;
        int candidate = 0;
        for (Integer cIndex : candidates){
          Rectangle temp = rectangles.get(cIndex);
          double area = temp.area();
          if (areaMin == -1){
            areaMin = area;
          } else if (area == areaMin){
            candidate = cIndex;
          } else if (area < areaMin){
            areaMin = area;
            candidate = cIndex;
          }
        }
        candidates = resetAndAdd(candidate);
      }
      int cIndex = candidates.get(0);

      Node thisChild = this.getChild(cIndex);
      Long childAddr = thisChild.insert(r, overflowHandler);
      ArrayList<Rectangle> childrenRect =  this.getChild(cIndex).getRectangles();
      if(childAddr != null){
        this.rectangles.add(myMBR.calculateMBR(childrenRect));
        this.children.add(childAddr);
      }
      this.rectangles.set(cIndex, myMBR.calculateMBR(childrenRect));
    }
  }

}
