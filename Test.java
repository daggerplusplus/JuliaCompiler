import java.util.List;

public class Test implements Expression.Visitor<String>, Statement.Visitor<String>{
    String print(Expression expr) {
        return expr.accept(this);
    }
    String print(Statement stmt) {
        return stmt.accept(this);
    }
    @Override
    public String visitBlockStmt(Statement.Block stmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitAssignExpr(Expression.Assign expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitBinaryExpr(Expression.Binary expr) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String visitCallExpr(Expression.Call expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitExprStmt(Statement.Expr stmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitForStmt(Statement.For stmt) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String visitFunctionStmt(Statement.Function stmt) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String visitIfStmt(Statement.If stmt) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String visitLiteralExpr(Expression.Literal expr) {
        StringBuilder literal = new StringBuilder();
        literal.append(expr.value);
        return literal.toString();
    }
    @Override
    public String visitLogicalExpr(Expression.Logical expr) {
        StringBuilder log = new StringBuilder();

        return log.toString();
    }
    @Override
    public String visitPrintStmt(Statement.Print stmt) {
        StringBuilder print = new StringBuilder();
        print.append("");    
        return print.toString();
    }
    @Override
    public String visitPrintlnStmt(Statement.Println stmt) {
        StringBuilder print = new StringBuilder();
        print.append("\n" + stmt.text);
        return print.toString();
    }
    
    @Override
    public String visitUnaryExpr(Expression.Unary expr) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String visitVariableExpr(Expression.Variable expr) {
        StringBuilder variable = new StringBuilder();
        variable.append(expr.name.lexeme);
        return variable.toString();         
    }
    @Override
    public String visitVariableStmt(Statement.Variable stmt) {
        StringBuilder var = new StringBuilder();
        var.append(expand(stmt.initializer));    
        return var.toString();
    }
    @Override
    public String visitWhileStmt(Statement.While stmt) {
        // TODO Auto-generated method stub
        return null;
    } 



    //////////////////////////////////////////////////////////////////


    private String expand(Expression... exprs) {
        StringBuilder builder = new StringBuilder();
        for (Expression expr : exprs) {
          // builder.append(" ");
          builder.append(expr.accept(this));
        }
        return builder.toString();
      }
    private String expandstmt(Statement... stmts) {
        StringBuilder builder = new StringBuilder();
        for (Statement stmt : stmts) {
          // builder.append(" ");
          builder.append(stmt.accept(this));
        }
        return builder.toString();
      }

    private String enclose(String operator, Expression... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(operator);
        for (Expression expr:exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));            
        }
        builder.append("]");
        return builder.toString();
    }
    private String encloseChange(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(name);
        change(builder,parts);
        builder.append("]");
        return builder.toString();
    }
    private void change(StringBuilder builder, Object... parts) {
        for (Object part:parts) {
            builder.append(" ");
            if (part instanceof Expression) builder.append(((Expression)part).accept(this));
            else if (part instanceof Statement) builder.append(((Statement)part).accept(this));
            else if (part instanceof Token) builder.append(((Token)part).lexeme);
            else if (part instanceof List) change(builder,((List)part).toArray());            
            else builder.append(part);
        }
    }
    
}
