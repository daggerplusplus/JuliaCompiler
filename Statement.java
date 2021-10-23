import java.util.List;

abstract class Statement{
  interface Visitor<S> {
    S visitFunctionStmt(Function stmt);
  }

  static class Function extends Statement{
    Function(Token name, List<Token> paramstype, List<Token> params, List<Statement> body) {
      this.name = name;
      this.paramstype = paramstype;
      this.params = params;
      this.body = body;
    }

    @Override
    <S> S accept(Visitor<S> visitor) {
      return visitor.visitFunctionStmt(this);
    }

    final Token name;
    final List<Token> paramstype;
    final List<Token> params;
    final List<Statement> body;
  }






  abstract <S> S accept(Visitor<S> visitor);
}