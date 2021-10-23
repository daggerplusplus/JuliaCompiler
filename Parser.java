import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Parser {
  private final List<Token> tokens;
  private int current = 0;
  
    //these tokens are found at beginning of statements
  List<TokenType> newLineTokens = new ArrayList<TokenType>(Arrays.asList(
    TokenType.FUNCTION, TokenType.IDENTIFIER, TokenType.PRINT,
    TokenType.END, TokenType.IF, TokenType.ELSE, TokenType.WHILE
  ));

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  void parse() {    
    System.out.println("Begin");
    declaration();
    System.out.println("End");
  }


  Statement declaration() {
    System.out.println("<program>->");
    //if TokenType == function, run functiondeclaration()
    if(grabAndGo(TokenType.FUNCTION)) return functionDeclaration();
    //if TokenType == identifier, run vardeclaration()
    
    return statement();
  }

  
  Statement functionDeclaration() {
    //consume the identifier

    //consume the left paren

    //check for params

    //consume end

    return Statement.Function();
  }



  boolean checkNewLine() {
    //if current token is in newLineTokens, return true;
    return false;
  }  



private boolean grabAndGo(TokenType... types) {
  for (TokenType type: types) {
    if(compare(type)) {
      moveForward();
      return true;
    }
  }
  return false;
}

private boolean compare(TokenType type) {
  if (end()) return false;
  return look().type == type;
}

private Token moveForward() {
    if (!end()) current++;
    return previous();
}



private boolean end() { return look().type == TokenType.EOF; }
private Token look() { return tokens.get(current); }
private Token previous() { return tokens.get(current-1);}
private Token next() { return tokens.get(current+1);}
private Token skipnext() {return tokens.get(current+2);}








}