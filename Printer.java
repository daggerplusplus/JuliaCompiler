import java.util.List;

class Printer implements Expression.Visitor<String>, Statement.Visitor<String> {

    String print(Expression expr) {
        return expr.accept(this);
    }
    String print(Statement stmt) {
        return stmt.accept(this);
    }
    @Override
    public String visitBlockStmt(Statement.Block stmt) {
        StringBuilder bl = new StringBuilder();
        for (Statement st:stmt.stmts) {
            bl.append(st.accept(this));
        }
        return bl.toString();
    }

    @Override
    public String visitExprStmt(Statement.Expr stmt) {
        return enclose("EXPRESSION",stmt.expression);
    }

    @Override
    public String visitIfStmt(Statement.If stmt) {
        if(stmt.elsebody == null) {
            return encloseChange("IF","CONDITION",stmt.condition,stmt.elseifbody);
        }
        return encloseChange("ELSE-IF","CONDITION", stmt.condition,stmt.elseifbody,"[ELSE",stmt.elsebody,"]");
    }

    @Override
    public String visitPrintStmt(Statement.Print stmt) {
        return enclose("PRINT", stmt.text);
    }
    @Override
    public String visitPrintlnStmt(Statement.Println stmt) {
        return enclose("PRINTLN", stmt.text);
    }

    @Override
    public String visitWhileStmt(Statement.While stmt) {
        return encloseChange("WHILE",stmt.condition,stmt.body);
    }

    @Override
    public String visitForStmt(Statement.For stmt) {
        return encloseChange("FOR",stmt.iterator,stmt.range,stmt.body);
    }

    @Override
    public String visitFunctionStmt(Statement.Function stmt) {
        return encloseChange("FunctionDeclaration",stmt.name,"[Params",stmt.params,"]","[FunctionBody",stmt.body,"]");
    }
    @Override
    public String visitVariableStmt(Statement.Variable stmt) {
        StringBuilder builder = new StringBuilder();
        if (stmt.initializer == null) return encloseChange("VariableDeclaration",builder.toString(),stmt.name);
        return encloseChange("VariableDeclaration",builder.toString(),"[ASSIGNMENT",stmt.name,stmt.initializer,"]");
    }
    @Override
    public String visitAssignExpr(Expression.Assign expr) {
        return encloseChange("Assignment",expr.name.lexeme, expr.value);
    }
    @Override
    public String visitVariableExpr(Expression.Variable expr) {
        return expr.name.lexeme;        
    }
    @Override
    public String visitBinaryExpr(Expression.Binary expr) {
        return enclose(expr.operator.lexeme,expr.left,expr.right);
    }
    @Override
    public String visitUnaryExpr(Expression.Unary expr) {
        return enclose(expr.operator.lexeme,expr.expr);
    }
    @Override
    public String visitLogicalExpr(Expression.Logical expr) {        
        return enclose(expr.operator.lexeme,expr.expr,expr.right);
    }
    @Override
    public String visitLiteralExpr(Expression.Literal expr) {
        if(expr.value==null) return "NULL";
        return expr.value.toString();
    }
    @Override
    public String visitCallExpr(Expression.Call expr) {
        return encloseChange("CALL", expr.expr,expr.args);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////

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