import java.util.List;
public interface JuliaCallable {
    int numArgs();
    Object call(Interpreter intrptr, List<Object> args);
}
