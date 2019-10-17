import java.util.Scanner;

public class Main {
    static char sym;
    static String nowString;
    static int index;
    static void getsym(){
        sym=nowString.charAt(index++);
    }
    static boolean A(){
        if(sym=='('){
            getsym();
            if(!B()){
                return false;
            }
            if(sym!=')'){
                return false;
            }
        }
        else if(sym=='d'){
            getsym();
            if(!B()){
                return false;
            }
            if(sym!='e'){
                return false;
            }
        }
        else{
            return false;
        }
        return true;
    }
    static boolean B(){
        if(sym=='c'){
            getsym();
            while (sym=='c'){
                getsym();
            }
        }
        else{
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);

        int T=scanner.nextInt();
        scanner.nextLine();
        while ((T--)>0){
            nowString=scanner.nextLine()+"\n";
            index=0;
            getsym();
            if(A()&&index==nowString.length()-1){
                System.out.println("T");
            }
            else{
                System.out.println("F");
            }
        }
    }
}
