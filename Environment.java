import java.util.*;

import javax.management.RuntimeErrorException;

class Environment {
    Environment e;
    private Map<String,Object> values = new HashMap<>();
    
    Environment(Environment e) {
        this.e = e;
    }

    Environment() {
        e = null;
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (e != null) return e.get(name);

        throw new RuntimeErrorException(null, "Undefined variable " + name.lexeme);
    }

    void assign(Token name, Object value) {
        if(values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (e != null) {
            e.assign(name,value);
            return;
        }


        throw new RuntimeErrorException(null, "Undefined variable " + name.lexeme);    
    }

    void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme,value);
    }

    Environment ancestor(int distance) {
        Environment en = this;
        for (int i=0; i<distance; i++)
            en = en.e;
        return en;
    }

    void define(String name, Object value) {
        values.put(name,value);
    }

    


}