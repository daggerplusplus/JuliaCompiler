import java.util.List;

abstract class Expression {
  interface Visitor<E> {
    E visitAssignExpr(Assign expr);
  }


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



  abstract <E> E accept(Visitor<E> visitor);
}