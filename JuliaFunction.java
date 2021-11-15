import java.util.List;
public class JuliaFunction implements JuliaCallable {
    private final Statement.Function decl;
    private final Environment env;
    private final boolean initializer;

    JuliaFunction(Statement.Function decl, Environment env, boolean initializer){
        this.decl = decl;
        this.env = env;
        this.initializer = initializer;
    }

    @Override
    public int numArgs() {
        return decl.params.size();
    }

    @Override
    public Object call(Interpreter intrptr, List<Object> args) {
        Environment environment = new Environment(env);
        for (int i = 0; i<decl.params.size(); i++)
        {
            environment.define(decl.params.get(i).lexeme,
            args.get(i));
        }
        intrptr.executeBlock(decl.body, environment);
        if (initializer) return env.getAt(0, "this");
        return null;
    }
}
