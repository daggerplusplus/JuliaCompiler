import java.util.*;

public class Resolver implements Expression.Visitor<Void>, Statement.Visitor<Void>{
    private final Interpreter intrptr;
    private final Stack<Map<String,Boolean>> scopes = new Stack<>();
    private FunctionType currentFun = FunctionType.NONE;
    

    Resolver(Interpreter intrptr) {
        this.intrptr = intrptr;
    }

    void resolve(List<Statement> stmts) {
        for (Statement stmt:stmts) {
            resolve(stmt);
        }
    }

    
    
    @Override
    public Void visitExprStmt(Statement.Expr stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitBlockStmt(Statement.Block stmt) {
        beginScope();
        resolve(stmt.stmts);
        endScope();
        return null;
    }

    @Override
    public Void visitIfStmt(Statement.If stmt) {
        resolve(stmt.condition);
        resolve(stmt.elseifbody);
        if (stmt.elsebody != null) resolve(stmt.elsebody);
        return null;
    }

    @Override
    public Void visitPrintStmt(Statement.Print stmt) {
        resolve(stmt.text);
        return null;
    }
    @Override
    public Void visitPrintlnStmt(Statement.Println stmt) {
        resolve(stmt.text);
        return null;
    }
    @Override
    public Void visitVariableStmt(Statement.Variable stmt) {
        declare(stmt.name);
        if (stmt.initializer != null) resolve(stmt.initializer);
        define(stmt.name);
        return null;
    }
    @Override
    public Void visitWhileStmt(Statement.While stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }
    @Override
    public Void visitAssignExpr(Expression.Assign expr) {
        resolve(expr.value);
        resolveLocal(expr, expr.name);
        return null;
    }
    @Override
    public Void visitBinaryExpr(Expression.Binary expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }
    @Override
    public Void visitCallExpr(Expression.Call expr) {
        resolve(expr.expr);

        for (Expression arg:expr.args) {
            resolve(arg);
        }
        return null;
    }
    @Override
    public Void visitLiteralExpr(Expression.Literal expr) {
        return null;
    }
    @Override
    public Void visitLogicalExpr(Expression.Logical expr) {
        resolve(expr.expr);
        resolve(expr.right);
        return null;
    }
    @Override
    public Void visitUnaryExpr(Expression.Unary expr) {
        resolve(expr.expr);
        return null;
    }
    @Override
    public Void visitVariableExpr(Expression.Variable expr) {
        if (!scopes.isEmpty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
            System.out.println("Can't read local variable");
        }
        resolveLocal(expr, expr.name);
        return null;
    }
    @Override
    public Void visitForStmt(Statement.For stmt) {
        return null;
    }
    @Override
    public Void visitFunctionStmt(Statement.Function stmt) {
        declare(stmt.name);
        define(stmt.name);
        resolveFunction(stmt, FunctionType.FUNCTION);
        return null;
    }


    

    


    private void resolve(Statement stmt) {
        stmt.accept(this);
    }
    private void resolve(Expression expr) {
        expr.accept(this);
    }
    private void resolveLocal(Expression expr, Token name) {
        for (int i = scopes.size() -1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme)) {
                intrptr.resolve(expr,scopes.size() - 1 - i);
                return;
            }
        }
    }
    private void resolveFunction(Statement.Function fun, FunctionType type) {
        FunctionType enclosingFun = currentFun;
        currentFun = type;

        beginScope();
        for (Token param:fun.params) {
            declare(param);
            define(param);
        }
        resolve(fun.body);
        endScope();
        currentFun = enclosingFun;
    }

    private void beginScope() {
        scopes.push(new HashMap<String, Boolean>());
    }
    private void endScope() {
        scopes.pop();
    }
    private enum FunctionType {
        NONE,FUNCTION,INITIALIZER
    }


    private void define(Token name) {
        if (scopes.isEmpty()) return;
        scopes.peek().put(name.lexeme, true);
    }

    private void declare(Token name) {
        if (scopes.isEmpty()) return;
        Map<String,Boolean> scope = scopes.peek();
        if (scope.containsKey(name.lexeme)) {
            System.out.println("Already a variable with this name");
        }
    }
}
