import java.util.*;

public class SymbolTable {
  static HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
   

 public SymbolTable() {
   populateTable();
 }

  public HashMap<String, ArrayList<String>> getMap() {
    return map;
  }
  public Object getKeyFromValue(HashMap<String,ArrayList<String>> hm, Object value) {
    for (Object o : hm.keySet()) {
      if (hm.get(o).contains(value)) {
        return o;
      }
    }
    return null;
  }
  public Object getValue(Object value) {
    for (Object o : map.keySet()) {
      if (map.containsValue(value)) {
        return o;
      }
    }

    return null;
  }

  public Object getKey(Object value) {
    for (Object o : map.keySet()) {
      if (map.get(o).contains(value)) {
        return o;
      }
    }
    return null;
  }
  
  private void populateTable() {
    map.put("KEYWORD",new ArrayList<String>());
      map.get("KEYWORD").add("baremodule");map.get("KEYWORD").add("begin");map.get("KEYWORD").add("break");
      map.get("KEYWORD").add("catch");map.get("KEYWORD").add("const");map.get("KEYWORD").add("continue");
      map.get("KEYWORD").add("do");map.get("KEYWORD").add("else");map.get("KEYWORD").add("elseif");
      map.get("KEYWORD").add("end");map.get("KEYWORD").add("export");map.get("KEYWORD").add("false");
      map.get("KEYWORD").add("finally");map.get("KEYWORD").add("for");map.get("KEYWORD").add("function");
      map.get("KEYWORD").add("global");map.get("KEYWORD").add("if");map.get("KEYWORD").add("import");
      map.get("KEYWORD").add("let");map.get("KEYWORD").add("local");map.get("KEYWORD").add("macro");
      map.get("KEYWORD").add("module");map.get("KEYWORD").add("quote");map.get("KEYWORD").add("return");
      map.get("KEYWORD").add("struct");map.get("KEYWORD").add("true");map.get("KEYWORD").add("try");
      map.get("KEYWORD").add("using");map.get("KEYWORD").add("while"); map.get("KEYWORD").add("abstract type"); map.get("KEYWORD").add("mutable struct");map.get("KEYWORD").add("primitive type");
      map.get("KEYWORD").add("where"); map.get("KEYWORD").add("in"); map.get("KEYWORD").add("isa");

    map.put("ARITHMETIC", new ArrayList<String>());
      map.get("ARITHMETIC").add("+"); map.get("ARITHMETIC").add("-");
      map.get("ARITHMETIC").add("*"); map.get("ARITHMETIC").add("/");

     map.put("ASSIGNMENT", new ArrayList<String>());
      map.get("ASSIGNMENT").add("=");

    map.put("WHITESPACE", new ArrayList<String>());
      map.get("WHITESPACE").add(" ");

    map.put("LPAREN", new ArrayList<String>());
      map.get("LPAREN").add("(");
    map.put("RPAREN", new ArrayList<String>());
      map.get("RPAREN").add(")");

    map.put("IDENTIFIER", new ArrayList<String>());
      map.get("IDENTIFIER").add("");

  }
  
}