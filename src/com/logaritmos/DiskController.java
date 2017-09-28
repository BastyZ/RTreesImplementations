package com.logaritmos;

import java.io.IOError;
import java.io.IOException;
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

  public DiskController(){
    PageSize=1;
    nodes = new HashMap<Long,Node>();
    stateOfNodes = new HashMap<Long, Boolean>();
    lastOnes = new LinkedList<Long>();
    usedNodes = 0;
    callDisk = 0;
    createdNodes = 1;
  }

  public void saveNode(Node n)
      throws IOException{
    //TODO: issue #23
    long addr = n.getMemoryAddress();
    nodes.put(addr, n);
    stateOfNodes.put(addr,true);
    this.setLast(addr);
    if(!nodes.containsKey(addr)){
      usedNodes++;
    }
    return;
  }

  public Node loadNode(long addr)
      throws IOException, ClassNotFoundException{
    this.setLast(addr);
    return nodes.get(addr);
  }

  private void setLast(long addr){
    lastOnes.remove(addr);
    lastOnes.addFirst(addr);
  }
}
