import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)throws IOException {
        GetSym getSym=new GetSym();
        do{
            getSym.getsym();
            if(getSym.symbol==20||getSym.symbol==21||getSym.symbol==-1){
                System.out.println(getSym.symbol+" "+getSym.token.toString());
            }
            else if(getSym.symbol==-2){
                return;
            }
            else if(getSym.symbol== GetSym.Symbol.NOTESY.getNumVal()){

            }
            else{
                System.out.println(getSym.symbol);
            }
        }while (getSym.symbol!=-1&&getSym.symbol!=-2);
    }
}
class GetSym{
    //BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream((new File("in.txt")))));
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
        NOTESY(33);
        private int numVal;
        Symbol(int i) {
            this.numVal=i;
        }

        public int getNumVal() {
            return numVal;
        }
    }
    void getchar()throws IOException {
        bufferedReader.mark(10);
        nowChar=(char)bufferedReader.read();
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
    void catToken(){
        token.append(nowChar);
    }
    void retract()throws IOException{
        bufferedReader.reset();
    }
    int reserver(){
        if(token.toString().equals("BEGIN")){
            return Symbol.BEGINSY.getNumVal();
        }
        else if(token.toString().equals("END")){
            return Symbol.ENDSY.getNumVal();
        }
        else if(token.toString().equals("IF")){
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
    int getsym()throws IOException{
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
