import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class Lexer {
  static HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
  private Scanner reader;
  private int position;
  private String input = "";
  private String holdNextLine;
  private static ArrayList<String> inputList;
  private final String codePath; 
  private String currentString = "";


  Lexer(String path) {
        codePath = path;        
        position = -1;
        populateTable();
  }

  String getPath() {
    return codePath;
  }

  private Object getKeyFromValue(Object value) {
    for (Object o : map.keySet()) {
      if (map.get(o).contains(value)) {
        return o;
      }
    }
    return null;
  }

  private boolean containsVal(HashMap<String,ArrayList<String>> map,String value) {
    for (Object o : map.keySet()) {
      if (map.get(o).contains(value)) {
        return true;
      }
    }
    return false;
  }

  protected void populateTable() {
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
      map.get("ASSIGNMENT").add("="); map.get("ASSIGNMENT").add("+=");map.get("ASSIGNMENT").add("-="); 
      map.get("ASSIGNMENT").add("*=");map.get("ASSIGNMENT").add("/="); 

    map.put("COMPARISON", new ArrayList<String>());
      map.get("COMPARISON").add("==");map.get("COMPARISON").add("!=");map.get("COMPARISON").add("<");
      map.get("COMPARISON").add("<=");map.get("COMPARISON").add(">");map.get("COMPARISON").add(">=");
      map.get("COMPARISON").add("~=");

    map.put("WHITESPACE", new ArrayList<String>());
      map.get("WHITESPACE").add(" ");

    map.put("LPAREN", new ArrayList<String>());
      map.get("LPAREN").add("(");
    map.put("RPAREN", new ArrayList<String>());
      map.get("RPAREN").add(")");

    map.put("IDENTIFIER", new ArrayList<String>());
      map.get("IDENTIFIER").add("");

  }

  protected void readFile() throws IOException{
        try {
            FileReader fr = new FileReader(getPath());
            BufferedReader br = new BufferedReader(fr);
            String line;            
            reader = new Scanner(new File(getPath()));
            inputList = new ArrayList<String>();

            while ((line = br.readLine()) != null)
            {
              if(line.trim().length() > 0) {
                if (line.startsWith("//"))
                  continue;
                holdNextLine = line.replaceAll("[\\p{Punct}]", " $0 ")
                                   .trim()
                                   .replaceAll(" +", " ");                
                input += holdNextLine + " ";                
                }
            }
            br.close();
            inputList = new ArrayList<>(Arrays.asList(input.split(" "))); 
        } catch (FileNotFoundException e) {
            System.out.print("File not found: " + e.getMessage());
        }
    }
  
    protected void createTokens() throws IOException {
      getNextString();  //get first line      
  
      while (position < inputList.size()) {        
        String lexeme = inputList.get(position);
        
        String nextLexeme = "";
        if (position < inputList.size()-1)
          nextLexeme = inputList.get(position+1); //look out for IndexOutOfBounds        

        String pattern = "[\\d]+";
        if (Pattern.matches(pattern,lexeme)) {
          System.out.print("Lexeme: "
          + lexeme
          + "\t\t Token: LITERAL\n");
        }
        boolean dupe = false;  

        if (lexeme.equals("+")
          || lexeme.equals("-")
          || lexeme.equals("*")
          || lexeme.equals("/")
          && nextLexeme.equals("="))  {
          System.out.print("Lexeme: "
            + lexeme + nextLexeme
            + "\t\t Token: "
            + getKeyFromValue(lexeme+nextLexeme)
            + "\n");
          dupe = true;
          getNextString();      //skip next lexeme to avoid a repeat of "="
        }      
        
        if (containsVal(map, lexeme) && !dupe) {
          System.out.print("Lexeme: " 
            + lexeme
            + "\t\t Token: " 
            + getKeyFromValue(lexeme)
            + "\n");
          dupe = true;        
        }   
        
        pattern = ("[a-zA-Z]+");
        if (Pattern.matches(pattern,lexeme)         
         && nextLexeme.equals("(")) 
         {  
            System.out.print("Lexeme: " 
            + lexeme            
            + "\t\t Token: FUNCTION\n");
            dupe = true;
         }
  
              
        if (Pattern.matches(pattern,lexeme) && !dupe) {
            System.out.print("Lexeme: "
            + lexeme
            + "\t\t Token: IDENTIFIER\n");          
          }      
        
        
        if (lexeme.matches("=|!|<|>|~") 
        && nextLexeme.equals("=")
        && !dupe) {
          System.out.print("Lexeme: "
          + lexeme + nextLexeme
          + "\t\t Token: "
          + getKeyFromValue(lexeme+nextLexeme)
          + "\n");
          dupe = true; 
          getNextString();  //skip next lexeme to avoid a repeat of "="
        }
              
        getNextString();            
      }
    }


   protected String getNextString() {
        // extracts a single line of the source as a string value
        position += 1;
        if (position < inputList.size())
            currentString = inputList.get(position);
        return currentString;
    }

}