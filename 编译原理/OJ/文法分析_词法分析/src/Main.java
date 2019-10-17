import java.io.*;

public class Main {
    public static void main(String[] args) {
        getsym();
        if(statement()){
            System.out.println('T');
        }
        else {
            System.out.println('F');
        }
    }
    static int sym;
    static GetSym getSym;

    static {
        try {
            getSym = new GetSym();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void getsym(){
        getSym.getsym();
        sym=getSym.symbol;
    }
    static boolean statement(){
        if(sym==GetSym.Symbol.IDSY.getNumVal()){
            if(!variable()){
                return false;
            }
            if(sym!=GetSym.Symbol.ASSIGNSY.getNumVal()){
                return false;
            }
            getsym();
            if(!expression()){
                return false;
            }
        }
        else if(sym==GetSym.Symbol.IFSY.getNumVal()){
            getsym();
            if(!expression()){
               return false;
            }
            if(sym!=GetSym.Symbol.THENSY.getNumVal()){
                return false;
            }
            getsym();
            if(!statement()){
                return false;
            }
            if(sym==GetSym.Symbol.ELSE.getNumVal()){
                getsym();
                if(!statement()){
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }
    static boolean variable(){
        if(sym==GetSym.Symbol.IDSY.getNumVal()){
            getsym();
            if(sym==GetSym.Symbol.LBRACK.getNumVal()){
                getsym();
                if(!expression()){
                    return false;
                }
                if(sym!=GetSym.Symbol.RBRACK.getNumVal()){
                    return false;
                }
                getsym();
            }
        }
        else {
            return false;
        }
        return true;
    }
    static boolean expression(){
        if(sym == GetSym.Symbol.IDSY.getNumVal()||sym==GetSym.Symbol.LPARSY.getNumVal()){
            if(!term()){
                return false;
            }
            while (sym==GetSym.Symbol.PLUSSY.getNumVal()){
                getsym();
                if(!term()){
                    return false;
                }
            }
        }
        else{
            return false;
        }
        return true;
    }
    static boolean term(){
        if(sym==GetSym.Symbol.IDSY.getNumVal()||sym==GetSym.Symbol.LPARSY.getNumVal()){
            if(!factor()){
                return false;
            }
            while (sym==GetSym.Symbol.STARTSY.getNumVal()){
                getsym();
                if(!factor()){
                    return false;
                }
            }
        }
        else {
            return false;
        }
        return true;
    }
    static boolean factor(){
        if(sym==GetSym.Symbol.IDSY.getNumVal()){
            if(!variable()){
                return false;
            }
        }
        else if(sym==GetSym.Symbol.LPARSY.getNumVal()){
            getsym();
            if(!expression()){
                return false;
            }
            if(sym!=GetSym.Symbol.RPATSY.getNumVal()){
                return false;
            }
            getsym();
        }
        else {
            return false;
        }
        return true;
    }

}
class GetSym{
    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
    //BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream((new File("in.txt")))));
    char nowChar=' ';
    StringBuilder token=new StringBuilder();
    int num;
    int symbol;

    GetSym() throws FileNotFoundException {
    }

    enum Symbol{
        BEGINSY(1),
        ENDSY(2),
        IFSY(3),
        THENSY(4),
        ELSE(5),
        IDSY(20),
        INTSY(21),
        PLUSSY(22),
        MINUSSY(23),
        STARTSY(24),
        DIVISY(25),
        LPARSY(26),
        RPATSY(27),
        COMMASY(28),
        SEMISY(29),
        COLONSY(30),
        ASSIGNSY(31),
        EQUSY(32),
        NOTESY(33),
        LBRACK(34),
        RBRACK(35);
        private int numVal;
        Symbol(int i) {
            this.numVal=i;
        }

        public int getNumVal() {
            return numVal;
        }
    }
    void getchar(){
        try {
            bufferedReader.mark(10);
            nowChar=(char)bufferedReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void clearToken(){
        token=new StringBuilder();
    }
    boolean isSpace(){
        return nowChar==' ';
    }
    boolean isNewLine(){
        return nowChar=='\n'||nowChar=='\r';
    }
    boolean isTab(){
        return nowChar=='\t';
    }
    boolean isLetter(){
        return Character.isLetter(nowChar);
    }
    boolean isDigit(){
        return Character.isDigit(nowChar);
    }
    boolean isColon(){
        return nowChar==':';
    }
    boolean isComma(){
        return nowChar==',';
    }
    boolean isSemi(){
        return nowChar==';';
    }
    boolean isEqu(){
        return nowChar=='=';
    }
    boolean isPlus(){
        return nowChar=='+';
    }
    boolean isMinus(){
        return nowChar=='-';
    }
    boolean isDivi(){
        return nowChar=='/';
    }
    boolean isStar(){
        return nowChar=='*';
    }
    boolean isLpar(){
        return nowChar=='(';
    }
    boolean isRpar(){
        return nowChar==')';
    }
    boolean isLbrack(){
        return nowChar=='[';
    }
    boolean isRbrack(){
        return nowChar==']';
    }
    void catToken(){
        token.append(nowChar);
    }
    void retract(){
        try {
            bufferedReader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int reserver(){
//        if(token.toString().equals("BEGIN")){
//            return Symbol.BEGINSY.getNumVal();
//        }
//        else if(token.toString().equals("END")){
//            return Symbol.ENDSY.getNumVal();
//        }
        if(token.toString().equals("IF")){
            return Symbol.IFSY.getNumVal();
        }
        else if(token.toString().equals("THEN")){
            return Symbol.THENSY.getNumVal();
        }
        else if(token.toString().equals("ELSE")){
            return Symbol.ELSE.getNumVal();
        }
        return 0;
    }
    int transNum(){
        token = new StringBuilder(token.toString().replaceFirst("^0*", ""));
        if(token.toString().length()<=10&& Long.parseLong(token.toString())<=2147483647){
            num=Integer.parseInt(token.toString());
            return num;
        }
        else{
            token=new StringBuilder("OF");
            return -1;
        }
    }
    void error(){
        if(nowChar==0xffff){
            symbol=-2;
            return;
        }
        symbol=-1;
        catToken();
    }
    int getsym(){
        getchar();
        clearToken();
        while (isSpace()||isNewLine()||isTab()){
            getchar();
        }
        if(isLetter()){
            while (isLetter()||isDigit()){
                catToken();
                getchar();
            }
            retract();
            int resultValue=reserver();

            if(resultValue==0){
                symbol=Symbol.IDSY.getNumVal();
            }

            else {
                symbol=resultValue;
            }
        }
        else if(isDigit()){
            while (isDigit()){
                catToken();
                getchar();
            }
            retract();
            num=transNum();
            symbol=Symbol.INTSY.getNumVal();
        }
        else if(isColon()){
            getchar();
            if(isEqu()){
                symbol=Symbol.ASSIGNSY.getNumVal();
            }
            else{
                retract();
                symbol=Symbol.COLONSY.getNumVal();
            }
        }
        else if(isPlus()){
            symbol=Symbol.PLUSSY.getNumVal();
        }
        else if(isMinus()){
            symbol=Symbol.MINUSSY.getNumVal();
        }
        else if(isStar()){
            symbol=Symbol.STARTSY.getNumVal();
        }
        else if(isLpar()){
            symbol=Symbol.LPARSY.getNumVal();
        }
        else if(isRpar()){
            symbol=Symbol.RPATSY.getNumVal();
        }
        else if(isLbrack()){
            symbol=Symbol.LBRACK.getNumVal();
        }
        else if(isRbrack()){
            symbol=Symbol.RBRACK.getNumVal();
        }
        else if(isComma()){
            symbol=Symbol.COMMASY.getNumVal();
        }
        else if(isSemi()){
            symbol=Symbol.SEMISY.getNumVal();
        }
        else if(isEqu()){
            symbol=Symbol.EQUSY.getNumVal();
        }
        else if(isDivi()){
            getchar();
            if(isStar()){
                symbol=Symbol.NOTESY.getNumVal();
                do{
                    do{
                        getchar();
                        if(nowChar==0xffff){
                            token=new StringBuilder("incomplete comment");
                            symbol=-1;
                            return 0;
                        }
                    }while (!isStar());
                    do{
                        getchar();
                        if(nowChar==0xffff){
                            token=new StringBuilder("incomplete comment");
                            symbol=-1;
                            return 0;
                        }
                        if(isDivi()){
                            return 0;
                        }
                    }while (isStar());
                }while (!isStar());
            }
            retract();
            symbol=Symbol.DIVISY.getNumVal();
        }
        else{
            error();
        }
        return 0;
    }
}

