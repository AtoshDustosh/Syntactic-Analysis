package test;

public class HelloWorld {
  public static int value = 2;

  public static void main(String[] args) {
    char ch = '\n';
    String str1 = "\n";
    String str2 = "\\n";
    String str3 = "" + ch;

    System.out.println("str1: " + str1);
    System.out.println("str2: " + str2);
    System.out.println("str3: " + str3);
  }
}
