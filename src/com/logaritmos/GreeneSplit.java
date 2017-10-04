package com.logaritmos;

public class GreeneSplit implements ISplit {

  @Override
  public String name() {
    return "Greene's Split";
  }

  @Override
  public Long splittingMethod(Node n) throws Exception {
    return n.greeneSplit();
  }
}
