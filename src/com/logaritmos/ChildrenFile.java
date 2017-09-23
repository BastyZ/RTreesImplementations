package com.logaritmos;

import static java.lang.Integer.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ChildrenFile {
  private File path;

  public Rectangle computeMBR() throws IOException {
    Rectangle mbr = new Rectangle();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] list = line.split(",");
        mbr.multiMax(parseInt(list[0]), parseInt(list[1]), parseInt(list[2]), parseInt(list[3]));
      }

      br.close();
    }
    /*
    TODO calcular el MBR a artir de los hijos
     */
    return null;
  }
}
