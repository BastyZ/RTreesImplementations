package com.logaritmos;

import java.io.Serializable;

public class Rectangle implements Serializable {
  private static final long serialVersionUID = 5L;
  public int left;
  public int right;
  public int top;
  public int bottom;

  public Rectangle(int l,int r, int t, int b){
    this.setXaxis(l,r);
    this.setYaxis(t,b);
  }

  private void setXaxis(int a, int b){
    //el eje crece de izquierda a derecha
    this.left = getMin(a,b);
    this.right = getMax(a,b);
  }

  private void setYaxis(int a, int b){
    //el eje Y crece de abajo hacia arriba
    this.top = getMax(a,b);
    this.bottom = getMin(a,b);
  }

  private int getMax(int a, int b){
    if (a<=b){
      return a;
    }else{
      return b;
    }
  }

  private int getMin(int a, int b){
    if (a<b){
      return a;
    }else{
      return b;
    }
  }

  public boolean overlaps(Rectangle aRectangle) {
    return (this.inVertical(aRectangle) && this.inHorizontal(aRectangle));
  }
  private boolean inVertical(Rectangle aRectangle){
   return (this.inTop(aRectangle.top) || this.inBottom(aRectangle.bottom));
  }
  private boolean inHorizontal(Rectangle aRectangle){
    return (this.inLeft(aRectangle.left) || this.inRight(aRectangle.right));
  }
  private boolean contains(int number, int min ,int max){
    return number>=min && number<=max;
  }
  private boolean inTop(int aTop){
    return contains(aTop,this.bottom,this.top);
  }
  private boolean inBottom(int aBottom){
    return contains(aBottom,this.bottom,this.top);
  }
  private boolean inLeft(int aLeft){
    return contains(aLeft,this.left,this.right);
  }
  private boolean inRight(int aRight){
    return contains(aRight,this.left,this.right);
  }

  public void multiMax(int i, int i1, int i2, int i3) {
  }
}
