package com.logaritmos;

public class LinearSplit implements ISplit {

  @Override
  public String name() {
    return "Linear Split";
  }

  @Override
  public Long splittingMethod(Node n) throws Exception {
    return n.linearSplit();
  }
}
