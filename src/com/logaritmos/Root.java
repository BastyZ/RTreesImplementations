package com.logaritmos;

import java.util.ArrayList;
import java.util.Arrays;

public class Root extends Node {
  private static final long serialVersionUID = 4L;

  public Root(int m, int M, Rectangle r, DiskController d, long addr)
      throws Exception {
    super(m, M, new ArrayList<Rectangle>(Arrays.asList(r)), new ArrayList<Long>(Arrays.asList((long)0)), d, addr,true, true);
  }
}
