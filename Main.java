import java.io.*;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;

class Main {
  private static final Interpreter JuliaInterpreter = new Interpreter();
  public static void main(String[] args) throws IOException {   
    byte[] file1 = Files.readAllBytes(Paths.get("Test1modified.jl"));
    byte[] file2 = Files.readAllBytes(Paths.get("Test2modified.jl"));
    byte[] file3 = Files.readAllBytes(Paths.get("Test3modified.jl"));
    
    String source1 = new String(file1, Charset.defaultCharset());
    String source2 = new String(file2, Charset.defaultCharset());
    String source3 = new String(file3, Charset.defaultCharset());
    
    
    Lexer test1 = new Lexer(source1);   
    Lexer test2 = new Lexer(source2);
    Lexer test3 = new Lexer(source3);
    

    System.out.println("Test 1: ");        
    List<Token> tokens = test1.createTokens();
    Parser Ptest1 = new Parser(tokens);
    List<Statement> stmts1 = Ptest1.parse();
    //for (Token token : tokens) System.out.println(token);
    //for (Statement stmt:stmts1) System.out.println(new Printer().print(stmt));  //print AST  
    JuliaInterpreter.interpret(stmts1); 
    System.out.println(); 
    
    System.out.println("Test 2: ");
    List<Token> tokens2 = test2.createTokens();
    Parser Ptest2 = new Parser(tokens2);
    List<Statement> stmts2 = Ptest2.parse();
    //for (Statement stmt:stmts2) System.out.println(new Printer().print(stmt)); //print AST
    //for (Token token : tokens2) System.out.println(token);
    JuliaInterpreter.interpret(stmts2); 
    System.out.println(); 
    
    System.out.println("Test 3: ");
    List<Token> tokens3 = test3.createTokens();
    Parser Ptest3 = new Parser(tokens3);
    List<Statement> stmts3 = Ptest3.parse();
    //for (Statement stmt:stmts3) System.out.println(new Printer().print(stmt)); //print AST
    //for (Token token : tokens3) System.out.println(token);
    JuliaInterpreter.interpret(stmts3); 
    System.out.println(); 
  }
}