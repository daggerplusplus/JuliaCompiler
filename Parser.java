import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Parser {
  private final List<Token> tokens;
  private int current = 0;
  
    //these tokens are found at beginning of statements
/*   List<TokenType> newLineTokens = new ArrayList<TokenType>(Arrays.asList(
    TokenType.FUNCTION, TokenType.IDENTIFIER, TokenType.PRINT,
    TokenType.END, TokenType.IF, TokenType.ELSE, TokenType.WHILE
  )); */

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  List<Statement> parse() {        
    List<Statement> statements = new ArrayList<>();    
    while(!end()) statements.add(declaration());        
    return statements;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  Statement declaration() {   
    if(grabAndGo(TokenType.FUNCTION)) return functionDeclaration();
    if(compare(TokenType.IDENTIFIER)) return varDeclaration();        
    return statement();
  }
 ////////////////////////////////////////////////////////////////////////////////////////////

  private Expression expression() {
    return assignment();
  }

////////////////////////////////////////////////////////////////////////////////////////////
  private Expression assignment() {    
    Expression expr = or();
    if(grabAndGo(TokenType.EQUAL)) {      
      Expression value = assignment();      
      if(expr instanceof Expression.Variable) {
        Token name = ((Expression.Variable)expr).name;
        return new Expression.Assign(name, value);
      }
     /*  else if (expr instanceof Expression.Get) {
        Expression.Get get = (Expression.Get) expr;
        return new Expression.Set(get.object, get.name, value);
      }       */
    }
    return expr;
  }
////////////////////////////////////////////////////////////////////////////////////////////
private Expression or() {
  Expression expr = and();
  while(grabAndGo(TokenType.OR)) {
    Token op = previous();
    Expression right = and();
    expr = new Expression.Logical(expr,op,right);
  }
  return expr;
}  
////////////////////////////////////////////////////////////////////////////////////////////
private Expression and() {
  Expression expr = equality();
  while(grabAndGo(TokenType.AND)) {
    Token op = previous();
    Expression right = equality();
    expr = new Expression.Logical(expr,op,right);
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression equality() {
  Expression expr = comparison();
  while(grabAndGo(TokenType.BANG_EQUAL,TokenType.EQUAL_EQUAL)) {
    Token op = previous();
    Expression right = comparison();
    expr = new Expression.Binary(expr,op,right);
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression comparison() {
  Expression expr = term();

  while(grabAndGo(TokenType.GREATER,TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
    Token op = previous();
    Expression right = term();
    expr = new Expression.Binary(expr,op,right);
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression factor() {
  Expression expr = unary();
  while(grabAndGo(TokenType.SLASH,TokenType.STAR,TokenType.SLASH_EQUAL,TokenType.STAR_EQUAL)) {
    Token op = previous();
    Expression right = unary();
    expr = new Expression.Binary(expr,op,right);
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression term() {
  Expression expr = factor();

  while(grabAndGo(TokenType.MINUS, TokenType.PLUS, TokenType.PLUS_EQUAL,TokenType.MINUS_EQUAL)) {
    Token op = previous();
    Expression right = factor();
    expr = new Expression.Binary(expr,op,right);
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression unary() {
  if(grabAndGo(TokenType.PLUS,TokenType.MINUS)) {
    Token op = previous();
    Expression right = unary();
    return new Expression.Unary(op,right);    
  }
  return call();
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression call() {
  Expression expr = primary();  
  while(true) {
    if(grabAndGo(TokenType.LEFT_PAREN)) expr = finishCall(expr);        
    else break;
  }
  return expr;
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression primary() {
  if(grabAndGo(TokenType.NUMBER,TokenType.STRING)) return new Expression.Literal(previous().literal);
  if(grabAndGo(TokenType.FALSE)) return new Expression.Literal(false);
  if(grabAndGo(TokenType.TRUE)) return new Expression.Literal(true);
  if(grabAndGo(TokenType.IDENTIFIER)) return new Expression.Variable(previous());
  return new Expression.Literal(null);  //setup something to throw an error bc we should expect an expression here
}
////////////////////////////////////////////////////////////////////////////////////////////
private Expression finishCall(Expression expr) {
  List<Expression> args = new ArrayList<>();
  Token paren = null;
  while(grabAndGo(TokenType.COMMA)) { if(!compare(TokenType.RIGHT_PAREN)) args.add(expression()); }
  if(grabAndGo(TokenType.RIGHT_PAREN)) paren = look();
  return new Expression.Call(expr,paren,args);
}

////////////////////////////////////////////////////////////////////////////////////////////
  private Statement statement() {
    if(grabAndGo(TokenType.FOR)) return forStatement();
    if(grabAndGo(TokenType.IF)) return ifStatement();
    if(grabAndGo(TokenType.WHILE)) return whileStatement();
    if(grabAndGo(TokenType.PRINT)) return printStatement();
    if(grabAndGo(TokenType.PRINTLN)) return printlnStatement();

    return expressionStatement();
  }
////////////////////////////////////////////////////////////////////////////////////////////
  private Statement expressionStatement() {
    Expression expr = expression();    
    return new Statement.Expr(expr);
  }
////////////////////////////////////////////////////////////////////////////////////////////
 private List<Statement> block() {
   List<Statement> statements = new ArrayList<>();   
   while(!compare(TokenType.END)) statements.add(declaration());
   if(compare(TokenType.END)) moveForward();
   return statements;
 }
 //////////////////////////////////////////////////////////////////////////////////////////// 
  Statement functionDeclaration() {
    //get function name
    Token name = null;
    if (compare(TokenType.IDENTIFIER)) name = look(); moveForward();
    //consume the left paren
    if (compare(TokenType.LEFT_PAREN)) moveForward();
    //check for params
    List<Token> parameters = new ArrayList<>();    
    if(!compare(TokenType.RIGHT_PAREN)) {
      do {
        parameters.add(look());
        moveForward();
      } while(grabAndGo(TokenType.COMMA));
    }
    if(compare(TokenType.RIGHT_PAREN)) moveForward();
    List<Statement> body = new ArrayList<>();    
    body = block();    
    if(compare(TokenType.END)) moveForward();
    return new Statement.Function(name,parameters,body);
  }
////////////////////////////////////////////////////////////////////////////////////////////
Statement varDeclaration() {
  Token name = null;
  if(compare(TokenType.IDENTIFIER)) name = look(); moveForward();  
  Expression init = null;
  if (grabAndGo(TokenType.EQUAL)) init = expression();
  return new Statement.Variable(name,init);
}
////////////////////////////////////////////////////////////////////////////////////////////
  Statement forStatement() {
    Token iterator = null, range = null;
    List<Statement> body = new ArrayList<>();    
    
    //consume identifier (iterator)
    if (compare(TokenType.IDENTIFIER)) iterator = look(); moveForward();

    //consume 'in'
    if(compare(TokenType.IN)) moveForward();

    //consume identifier (range)
    if(compare(TokenType.IDENTIFIER)) range = look(); moveForward();

    //statements
    body = block();

    //consume 'end'
    if(compare(TokenType.END)) moveForward();
    return new Statement.For(iterator,range,body);
  }
////////////////////////////////////////////////////////////////////////////////////////////
  Statement ifStatement() {
    
    Expression condition = expression();

    if(compare(TokenType.ELSEIF)) moveForward();    
    Statement elseifBody = statement();

    if(compare(TokenType.ELSE)) moveForward();    
    Statement elseBody = statement();

    if(compare(TokenType.END)) moveForward();
    return new Statement.If(condition, elseifBody, elseBody);
  }
////////////////////////////////////////////////////////////////////////////////////////////
private Statement whileStatement() {
  
  Expression condition = expression();
  
  Statement body = statement();

  if(compare(TokenType.END)) moveForward();
  return new Statement.While(condition,body);
}
////////////////////////////////////////////////////////////////////////////////////////////
private Statement printStatement() {    
  if(grabAndGo(TokenType.LEFT_PAREN));    
  Expression text = expression();   
  if(grabAndGo(TokenType.RIGHT_PAREN));    
  return new Statement.Print(text);
}
////////////////////////////////////////////////////////////////////////////////////////////
private Statement printlnStatement() {
  grabAndGo(TokenType.LEFT_PAREN);
  Expression text = expression();
  grabAndGo(TokenType.RIGHT_PAREN);
  return new Statement.Println(text);
}
////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////
                                    /* HELPERS */
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
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