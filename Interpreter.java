import java.util.*;

class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {

    Environment globs = new Environment();
    Environment env = globs;

    private final Map<Expression,Integer> locals = new HashMap<>();

    void interpret(List<Statement> stmts)
    {
       for (Statement stmt:stmts) execute(stmt);        
    }

    private Object evaluate(Expression expr) {
        return expr.accept(this);
    }

    private void execute(Statement stmt) {
        stmt.accept(this);
    }


    void executeBlock(List<Statement> stmts, Environment env) {
        Environment prev = this.env;

        for (Statement stmt:stmts) execute(stmt);
    }


/*     @Override
    public Void visitBlockStmt(Statement.Block stmt) {

    } */

    @Override
    public Void visitExprStmt(Statement.Expr stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Statement.Function stmt) {
        //JuliaFunction function = new JuliaFunction(stmt,env,false);
        //env.define(stmt.name.lexeme,function);
        return null;
    }

     @Override
    public Void visitIfStmt(Statement.If stmt) {
        if (isTrue(evaluate(stmt.condition))) execute(stmt.elseifbody);
        else if (stmt.elsebody != null) execute(stmt.elsebody);
        return null;
    } 
    
    @Override
    public Void visitPrintStmt(Statement.Print stmt) {
        Object value = evaluate(stmt.text);
        System.out.print(value.toString());
        return null;
    }
    @Override
    public Void visitPrintlnStmt(Statement.Println stmt) {
        Object value = evaluate(stmt.text);
        System.out.println(value.toString());
        return null;
    }

    @Override
    public Object visitVariableExpr(Expression.Variable expr) {
        return lookUpVar(expr.name, expr);
    }

    @Override
    public Void visitVariableStmt(Statement.Variable stmt) {
        Object value = null;
        if (stmt.initializer != null) value = evaluate(stmt.initializer);

        env.define(stmt.name.lexeme,value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Statement.While stmt) {
        while (isTrue(evaluate(stmt.condition))) execute(stmt.body);
        return null;
    }

    @Override
    public Void visitForStmt(Statement.For stmt) {
        return null;  //TODO
    }

    @Override
    public Object visitBinaryExpr(Expression.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG_EQUAL: return !isEqual(left,right);
            case EQUAL_EQUAL: return isEqual(left,right);
            case GREATER: checkMultipleOperands(expr.operator, left, right);
                return (int)left > (int)right;
            case GREATER_EQUAL: checkMultipleOperands(expr.operator, left, right);
                return (int) left >= (int) right;
            case LESS: checkMultipleOperands(expr.operator, left, right);
                return (int) left < (int) right;
            case LESS_EQUAL: checkMultipleOperands(expr.operator, left, right);
                return (int) left <= (int) right;
            case PLUS: checkMultipleOperands(expr.operator, left, right);
                return (int) left + (int) right;
            case MINUS: checkMultipleOperands(expr.operator, left, right);
                return (int) left - (int) right;
            case STAR: checkMultipleOperands(expr.operator, left, right);
                return (int) left * (int) right;
            case SLASH: checkMultipleOperands(expr.operator, left, right);
                return (int) left / (int) right;
        }
        return null;
    }

    @Override
    public Object visitCallExpr(Expression.Call expr) {
        Object call = evaluate((expr.expr));

        List<Object> args = new ArrayList<>();
        for (Expression arg: expr.args) args.add(evaluate(arg));

        //JuliaCallable function = (JuliaCallable) call;
        
        return null;
        //return function.call(this,args);
    }

    @Override
    public Object visitLogicalExpr(Expression.Logical expr) {
        Object left = evaluate(expr.expr);
        if(expr.operator.type == TokenType.OR) {
            if (isTrue(left)) return left;            
        } else {
            if (!isTrue(left)) return left;
        }
        return evaluate(expr.right);
    }

    @Override
    public Object visitLiteralExpr(Expression.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitAssignExpr(Expression.Assign expr) {
        Object value = evaluate(expr.value);
        Integer distance = locals.get(expr);
        if (distance != null) env.assignAt(distance,expr.name,value);
        else globs.assign(expr.name,value);
        return value;
    }

    @Override
    public Object visitUnaryExpr(Expression.Unary expr) {
        Object right = evaluate(expr.expr);

        switch (expr.operator.type) {
            case PLUS: checkOperand(expr.operator,right); return +(int)right;
            case MINUS: checkOperand(expr.operator,right); return -(int)right;
        }
        return null;
    }

    private void checkOperand(Token operator, Object operand) {
        if (operand instanceof Integer) return;
        throw new RuntimeException("Operand must be a number");
    }

    private void checkMultipleOperands(Token operator, Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) return;
        throw new RuntimeException("Operands must be numbers");
    }



    private Object lookUpVar(Token name, Expression expr) {
        return globs.get(name);
    }

    private boolean isTrue(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (boolean) obj;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }
}