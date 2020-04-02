package test;

import java.util.HashSet;
import java.util.Set;

public class HelloWorld {
  public static int value = 2;

  public static void main(String[] args) {
    double dData1 = 02.3323E2;
    System.out.printf("dData1: %f\n", dData1);
    Set<String> strSet = new HashSet<String>();
    strSet.add("int");
    strSet.add("&");
    strSet.add("\"");

    System.out.println("\"int\" in set?: " + strSet.contains("int"));
    System.out.println("\"&\" in set?: " + strSet.contains("&"));
    System.out.println("\"\"\" in set?: " + strSet.contains("\""));

  }
}
