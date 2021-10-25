import java.util.List;

abstract class Statement{
  interface Visitor<S> {
    S visitExprStmt(Expr stmt);
    S visitFunctionStmt(Function stmt);
    S visitVariableStmt(Variable stmt);
    S visitForStmt(For stmt);
    S visitPrintStmt(Print stmt);
    S visitPrintlnStmt(Println stmt);
    S visitWhileStmt(While stmt);
    S visitIfStmt(If stmt);
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Expr extends Statement {
    Expr(Expression expression) {
      this.expression = expression;
    }

    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitExprStmt(this);
    }
    final Expression expression;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Function extends Statement{
    Function(Token name,List<Token> params, List<Statement> body) {
      this.name = name;      
      this.params = params;
      this.body = body;
    }

    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitFunctionStmt(this);
    }

    final Token name;    
    final List<Token> params;
    final List<Statement> body;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class For extends Statement{
    For(Token iterator, Token range, List<Statement> body) {
      this.iterator = iterator;
      this.range = range;
      this.body = body;
    }

    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitForStmt(this);
    }

    final Token iterator;
    final Token range;
    final List<Statement> body;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class If extends Statement{
    If(Expression condition, Statement elseifbody, Statement elsebody) {
      this.condition = condition;
      this.elseifbody = elseifbody;
      this.elsebody = elsebody;
    }
    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitIfStmt(this);
    }
    final Expression condition;
    final Statement elseifbody;
    final Statement elsebody;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class While extends Statement{
    While(Expression condition, Statement body){
      this.condition = condition;
      this.body = body;
    }
    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitWhileStmt(this);
    }
    final Expression condition;
    final Statement body;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Print extends Statement{
    Print(Expression text){
      this.text = text;
    }
    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitPrintStmt(this);
    }
    final Expression text;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  static class Println extends Statement{
    Println(Expression text){
      this.text = text;
    }
    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitPrintlnStmt(this);      
    }
    final Expression text;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  static class Variable extends Statement{
    Variable(Token name, Expression initializer) {
      this.name = name;
      this.initializer = initializer;
    }
    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitVariableStmt(this);
    }
    final Token name;
    final Expression initializer;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  
  abstract <S> S accept(Visitor<S> visitor);
}