import java.util.Scanner;
import java.io.*;


class Main {
  

  public static void main(String[] args) throws IOException {
    Scanner input = new Scanner(System.in);     
    Lexer test1 = new Lexer("Test1.jl");    
    Lexer test2 = new Lexer("Test2.jl");
    Lexer test3 = new Lexer("Test3.jl");

    System.out.println("Test 1: ");
    test1.readFile();
    test1.createTokens();
    System.out.println();


    System.out.println("Test 2: ");
    test2.readFile();
    test2.createTokens();
    System.out.println();


    System.out.println("Test 3: ");
    test3.readFile();
    test3.createTokens();
    System.out.println();
   
    input.close();     
  }
}