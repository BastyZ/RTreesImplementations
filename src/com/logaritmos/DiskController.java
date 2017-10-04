package com.logaritmos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

public class DiskController {
  private int PageSize; //tamaño buffer
  private HashMap<Long,Node> nodes; //direccion y nodos guardados
  private HashMap<Long,Boolean> stateOfNodes; //boolean de si el nodo fue modificado o no
  private LinkedList<Long> lastOnes; //direccion de los ultimos nodos accesados
  private int usedNodes; //nro de nodos guardados
  private long createdNodes; //nro nodos creados
  public int callDisk; //nro llamadas a disco

  public DiskController(int bufferSize){
    PageSize=bufferSize;//M
    nodes = new HashMap<Long,Node>();
    stateOfNodes = new HashMap<Long, Boolean>();
    lastOnes = new LinkedList<Long>();
    usedNodes = 0;
    callDisk = 0;
    createdNodes = 1;
  }

  public void saveNode(Node n) throws IOException{
    long addr = n.getMemoryAddress();
    if (nodes.containsKey(addr)){
      nodes.put(addr, n);
      stateOfNodes.put(addr,true);
      this.setLast(addr);
    } else if (usedNodes < PageSize){
      nodes.put(addr,n);
      stateOfNodes.put(addr,true);
      this.usedNodes++;
      return;
    } else {
      long lastAddr = lastOnes.pollLast();
      Node temp = nodes.get(lastAddr);
      writeFile(temp, lastAddr);
      stateOfNodes.remove(lastAddr);
      nodes.remove(lastAddr);
      nodes.put(addr,n);
      lastOnes.addFirst(addr);
      stateOfNodes.put(addr,true);
    }
  }

  public Node loadNode(long addr)
      throws IOException, ClassNotFoundException{
    if (nodes.containsKey(addr)){
      this.setLast(addr);
      return nodes.get(addr);
    } else {
      if ( PageSize == usedNodes){
        long lastAddr = lastOnes.pollLast();
        Node lastNode = nodes.get(lastAddr);
        if (stateOfNodes.get(lastAddr)){
          writeFile(lastNode, lastAddr);
        }
        nodes.remove(lastAddr);
        stateOfNodes.remove(lastAddr);
      }

      Node temp = readFile(addr);
      lastOnes.addFirst(addr);
      stateOfNodes.put(addr,false);
      nodes.put(addr,temp);
      return temp;
    }

  }

  private void setLast(long addr){
    lastOnes.remove(addr);
    lastOnes.addFirst(addr);
  }

  public long memoryAssigner(){
    this.createdNodes++;
    return this.createdNodes;
  }

  private Node readFile(long addr)
      throws IOException, ClassNotFoundException{
    ObjectInputStream node = new ObjectInputStream(new FileInputStream(addr+".node"));
    Node n = (Node) node.readObject();
    node.close();
    this.callDisk++;
    n.diskController = this;
    return n;
  }

  private void writeFile(Node n, long addr) throws IOException{
    ObjectOutputStream info = new ObjectOutputStream(new FileOutputStream(addr+".node"));
    info.writeObject(n);
    info.close();
    this.callDisk++;
  }

}
