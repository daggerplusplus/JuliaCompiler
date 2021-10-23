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
  private int start = 0;
  private int current = 0;
  private final List<Token> tokens = new ArrayList<>();
  private static final Map<String, TokenType> keywords;
   static {
    keywords = new HashMap<>();            
      keywords.put("begin",       TokenType.BEGIN);
      keywords.put("end",         TokenType.END);
      keywords.put("else",        TokenType.ELSE);
      keywords.put("false",       TokenType.FALSE);
      keywords.put("for",         TokenType.FOR);      
      keywords.put("if",          TokenType.IF);
      keywords.put("function",    TokenType.FUNCTION);
      keywords.put("baremodule",  TokenType.BAREMODULE);
      keywords.put("global",      TokenType.GLOBAL);      
      keywords.put("print",       TokenType.PRINT);
      keywords.put("return",      TokenType.RETURN);
      keywords.put("super",       TokenType.SUPER);
      keywords.put("this",        TokenType.THIS);
      keywords.put("true",        TokenType.TRUE);
      keywords.put("var",         TokenType.VAR);
      keywords.put("while",       TokenType.WHILE);            
      keywords.put("break",       TokenType.BREAK);      
      keywords.put("catch",       TokenType.CATCH);
      keywords.put("continue",    TokenType.CONTINUE);
      keywords.put("const",       TokenType.CONST);
      keywords.put("finally",     TokenType.FINALLY);
      keywords.put("try",         TokenType.TRY);      
      keywords.put("do",          TokenType.DO);       
      keywords.put("import",      TokenType.IMPORT);
      keywords.put("struct",      TokenType.STRUCT);
      keywords.put("let",         TokenType.LET);
      keywords.put("module",      TokenType.MODULE);
      keywords.put("export",      TokenType.EXPORT);
      keywords.put("local",       TokenType.LOCAL);
      keywords.put("quote",       TokenType.QUOTE);
      keywords.put("true",        TokenType.TRUE);
      keywords.put("in",          TokenType.IN);
      keywords.put("elseif",      TokenType.ELSEIF);
      keywords.put("macro",       TokenType.MACRO);
      keywords.put("isa",         TokenType.ISA);
  } 
 

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
  
      List<Token> createTokens() throws IOException {
            //getNextString();  //get first line      
  
      while (!isAtEnd()) {
        start = current;
        char c = forward();        
        switch (c) {
        case '(': addToken(TokenType.LEFT_PAREN); break;
        case ')': addToken(TokenType.RIGHT_PAREN); break;
        case ',': addToken(TokenType.COMMA); break;
        case '.': addToken(TokenType.DOT); break;                        
        case '!':
          addToken(compare('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
          break;
        case '=':
          addToken(compare('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
          break;
        case '<':
          addToken(compare('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
          break;
        case '>':
          addToken(compare('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
          break;
        case '+':
          addToken(compare('=') ? TokenType.PLUS_EQUAL :
          TokenType.PLUS);
          break;
        case '-':
          addToken(compare('=') ? TokenType.MINUS_EQUAL :
          TokenType.MINUS);
          break;
        case '*':
          addToken(compare('=') ? TokenType.STAR_EQUAL :
          TokenType.STAR);
          break;
        case '/':
          if (compare('/')) {            
            while (current() != '\n' && !isAtEnd()) forward();
          }
          else if (compare('='))
            addToken(TokenType.SLASH_EQUAL);          
          else {
            addToken(TokenType.SLASH);
          }
          break;
        
        case ' ':
        case '\r':
        case '\t':
        case '\n':
          break;
        
        case '"': string(); break;   
        default:
          if (isDigit(c)) {
            number();
          } else if (isAlpha(c)) {
            identifier();
          }
          else {
            //error(line, "Unexpected character.");
          }
          break;
        } //end switch
          
      }// end while
        tokens.add(new Token(TokenType.EOF, "",null));
        return tokens;
  } //end createTokens

        /* String lexeme = inputList.get(position);
        
        String nextLexeme = "";
        if (position < inputList.size()-1)
          nextLexeme = inputList.get(position+1); //look out for IndexOutOfBounds        

        String pattern = "[\\d]+";
        if (Pattern.matches(pattern,lexeme)) {
          System.out.print("Lexeme: "
          + lexeme
          + "\t\t Token: LITERAL\n");
          addToken(TokenType.NUMBER);
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
            addToken(TokenType.FUNCTION);
         }
  
              
        if (Pattern.matches(pattern,lexeme) && !dupe) {
            System.out.print("Lexeme: "
            + lexeme
            + "\t\t Token: IDENTIFIER\n");          
            addToken(TokenType.IDENTIFIER);
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
      } */


  /*   protected List<Token> createTokens() throws IOException {
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
    } */


   protected String getNextString() {
        // extracts a single line of the source as a string value
        position += 1;
        current++;
        if (position < inputList.size())
            currentString = inputList.get(position);
        return currentString;
    }

    private void addToken(TokenType type) {
    addToken(type, null);
    }

  private void addToken(TokenType type, Object literal) {
    String text = codePath.substring(start, current);
    tokens.add(new Token(type, text, literal));
  }     
      private boolean isAtEnd() {
    return current >= codePath.length();
  }

    private char forward() {
      return codePath.charAt(current++);
  }

  private boolean compare(char c) {
    if (isAtEnd()) return false;
    if (codePath.charAt(current) != c) return false;

    current++;
    return true;
  }

  private char current() {
    if (isAtEnd()) return '\0';
    return codePath.charAt(current);
  }



       private void identifier() {
      while (isAlphaNumeric(current())) forward();      
      String text = codePath.substring(start, current);
      TokenType type = keywords.get(text);
     
      if (type == null) type = TokenType.IDENTIFIER;      
      addToken(type);          
  }   

    private void number() {
    while (isDigit(current())) forward();

    // Look for a fractional part.
    if (current() == '.' && isDigit(next())) {
      // Consume the "."
      forward();

      while (isDigit(current())) forward();   }

    addToken(TokenType.NUMBER,
        Double.parseDouble(codePath.substring(start, current)));
  }

      private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  } 

    private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  
   private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
  }

      private char next() {
    if (current + 1 >= codePath.length()) return '\0';
    return codePath.charAt(current + 1);
  } 
   private void string() {
    while (current() != '"' && !isAtEnd()) {      
      forward();
    }
    if (isAtEnd()) {
      //Main.error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    forward();

    // Trim the surrounding quotes.
    String value = codePath.substring(start + 1, current - 1);
    addToken(TokenType.STRING, value);
  }
}