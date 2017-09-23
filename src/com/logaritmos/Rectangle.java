package com.logaritmos;

public class Rectangle {
  public int left;
  public int right;
  public int top;
  public int bottom;

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
