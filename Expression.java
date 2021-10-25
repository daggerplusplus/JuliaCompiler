import java.util.List;

abstract class Expression {
  interface Visitor<E> {
    E visitAssignExpr(Assign expr);
    E visitVariableExpr(Variable expr);
    E visitBinaryExpr(Binary expr);
    E visitUnaryExpr(Unary expr);
    E visitLogicalExpr(Logical expr);
    E visitLiteralExpr(Literal expr);
    E visitCallExpr(Call expr);
    E visitGetExpr(Get expr);
    E visitSetExpr(Set expr);
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Assign extends Expression{
    Assign(Token name, Expression value) {
      this.name = name;
      this.value = value;
    }

    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitAssignExpr(this);
    }
    final Token name;
    final Expression value;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Variable extends Expression{
    Variable(Token name) {
      this.name = name;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitVariableExpr(this);
    }
    final Token name;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Binary extends Expression{
    Binary(Expression left, Token operator, Expression right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitBinaryExpr(this);
    }
    final Expression left;
    final Token operator;
    final Expression right;
  }
////////////////////////////////////////////////////////////////////////////////////////////
  static class Unary extends Expression{
    Unary(Token operator, Expression expr) {
      this.operator = operator;
      this.expr = expr;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitUnaryExpr(this);
    }
    final Token operator;
    final Expression expr;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  static class Logical extends Expression{
    Logical(Expression expr,Token operator, Expression right){
      this.expr = expr;
      this.operator = operator;
      this.right = right;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitLogicalExpr(this);
    }
    final Expression expr;
    final Token operator;
    final Expression right;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  static class Literal extends Expression{
    Literal(Object value) {
      this.value = value;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitLiteralExpr(this);
    }
    final Object value;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////
  static class Call extends Expression{
    Call(Expression expr, Token paren, List<Expression> args) {
      this.expr = expr;
      this.paren = paren;
      this.args = args;
    }
    @Override
    <E> E accept(Visitor<E> visitor) {
      return visitor.visitCallExpr(this);
    }
    final Expression expr;
    final Token paren;
    final List<Expression> args;
  }
////////////////////////////////////////////////////////////////////////////////////////////
static class Get extends Expression{
  Get(Expression object, Token name) {
    this.object = object;
    this.name = name;
  }
  @Override
  <E> E accept(Visitor<E> visitor) {
    return visitor.visitGetExpr(this);
  }
  final Expression object;
  final Token name;
}
////////////////////////////////////////////////////////////////////////////////////////////
static class Set extends Expression{
  Set(Expression object, Token name, Expression value) {
    this.object = object;
    this.name = name;
    this.value = value;
  }
  @Override
  <E> E accept(Visitor<E> visitor) {
    return visitor.visitSetExpr(this);
  }
  final Expression object;
  final Token name;
  final Expression value;
}
  
abstract <E> E accept(Visitor<E> visitor);
}