import java.util.*;

public class Main {
    static Map<Character,Map<Character,Integer>> priority;
    static List<Character> vn=new ArrayList<>();
    static List<Character> vt =new ArrayList<>();
    static List<Grammar> grammarList=new ArrayList<>();
    static List<String> newGrammar=new LinkedList<>();
    static Map<Character,Set<Character>> firstvt=new HashMap<>();
    static Map<Character,Set<Character>> lastvt=new HashMap<>();
    static void initVt(){
        for(char c:vn){
            firstvt.put(c,new HashSet<>());
            lastvt.put(c,new HashSet<>());
        }
    }
    static void initPriority(){
        priority=new HashMap<>();
        for(char c:vt){
            priority.put(c,new HashMap<>());
        }
        for(Grammar grammar:grammarList){
            for(int i=0;i<grammar.right.length()-1;i++){
                char xi=grammar.right.charAt(i);
                char xip1=grammar.right.charAt(i+1);
                if(vt.contains(xi)&&vt.contains(xip1)){
                    priority.get(xi).put(xip1,3);
                }
                if(i<grammar.right.length()-2){
                    char xip2=grammar.right.charAt(i+2);
                    if(vt.contains(xi)&&vn.contains(xip1)&&vt.contains(xip2)){
                        priority.get(xi).put(xip2,3);
                    }
                }
                if(vt.contains(xi)&&vn.contains(xip1)){
                    for(char b:firstvt.get(xip1)){
                        priority.get(xi).put(b,1);
                    }
                }
                if(vn.contains(xi)&&vt.contains(xip1)){
                    for(char a:lastvt.get(xi)){
                        priority.get(a).put(xip1,2);
                    }
                }
            }
        }
        priority.put('#',new HashMap<>());
        for(char c:vt){
            priority.get('#').put(c,1);
            priority.get(c).put('#',2);
        }
        priority.get('#').put('#',3);
    }
    static void initNewGrammar(){
        for(Grammar grammar:grammarList){
            StringBuilder result=new StringBuilder();
            for(int i=0;i<grammar.right.length();i++){
                if(vn.contains(grammar.right.charAt(i))){
                    result.append("&");
                }
                else{
                    result.append(grammar.right.charAt(i));
                }
            }
            if(!result.toString().equals("&")){
                newGrammar.add(result.toString());
            }
        }
    }
    static Stack<VnVt> S=new Stack<>();
    static void insert(char U,char b){
        if(!firstvt.get(U).contains(b)){
            firstvt.get(U).add(b);
            S.push(new VnVt(U,b));
        }
    }
    static void findFirst(){
        for(Grammar grammar:grammarList){
            if(vt.contains(grammar.right.charAt(0))){
                insert(grammar.left,grammar.right.charAt(0));
            }
            else if(grammar.right.length()>=2&&vt.contains(grammar.right.charAt(1))){
                insert(grammar.left,grammar.right.charAt(1));
            }
        }
        while (!S.isEmpty()){
            VnVt temp=S.pop();
            char V=temp.Vn;
            char b=temp.Vt;
            for(Grammar grammar:grammarList){
                if(grammar.right.charAt(0)==V){
                    insert(grammar.left,b);
                }
            }
        }
    }
    static Stack<VnVt> ST=new Stack<>();
    static void insert1(char U,char b){
        if(!lastvt.get(U).contains(b)){
            lastvt.get(U).add(b);
            ST.push(new VnVt(U,b));
        }
    }
    static void findLast(){
        for(Grammar grammar:grammarList){
            if(vt.contains(grammar.right.charAt(grammar.right.length()-1))){
                insert1(grammar.left,grammar.right.charAt(grammar.right.length()-1));
            }
            else if(grammar.right.length()>=2&&vt.contains(grammar.right.charAt(grammar.right.length()-2))){
                insert1(grammar.left,grammar.right.charAt(grammar.right.length()-2));
            }
        }
        while (!ST.isEmpty()){
            VnVt temp=ST.pop();
            char V=temp.Vn;
            char a=temp.Vt;
            for(Grammar grammar:grammarList){
                if(grammar.right.charAt(grammar.right.length()-1)==V){
                    insert1(grammar.left,a);
                }
            }
        }
    }
    static char lastChar(Stack<Character>stack){
        for(int i=stack.size()-1;i>=0;i--){
            if(stack.elementAt(i)!='&'){
                return stack.elementAt(i);
            }
        }
        return '&';
    }
    static int analyze(String input){
        input=input+"#";
        Stack<Character> stack=new Stack<>();
        stack.push('#');
        int count=0;
        try {
            for(int i=0;i<input.length();i++){
                char next=input.charAt(i);
                while (priority.get(lastChar(stack)).get(next)==2){
                    StringBuilder temp=new StringBuilder();
                    temp.insert(0,stack.pop());
                    while (!newGrammar.contains(temp.toString())){
                        if(stack.peek()=='#'){
                            return -1;
                        }
                        temp.insert(0,stack.pop());
                    }
                    stack.push('&');
                    count++;
                }
                stack.push(next);
                count++;
            }
        }catch (Exception e){
            count=-1;
        }

        return count;
    }
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int o,p,g;
        o=scanner.nextInt();
        p=scanner.nextInt();
        g=scanner.nextInt();

        for(int i=0;i<o;i++){
            vn.add(scanner.next().charAt(0));
        }
        for(int i=0;i<p;i++){
            vt.add(scanner.next().charAt(0));
        }
        scanner.next();
        for(int i=0;i<g;i++){
            Grammar grammer=new Grammar(scanner.next());
            grammarList.add(grammer);
        }
        initVt();
        findFirst();
        findLast();
        initPriority();
        initNewGrammar();
        int T;
        T=scanner.nextInt();
        while ((T--)>0){
            String input=scanner.next();
            int count=analyze(input)-1;
            if(count<0){
                System.out.println("F");
            }
            else {
                System.out.println("T");
            }
        }
    }
}
class Grammar {
    char left;
    String right;

    public Grammar(String input) {
        left=input.charAt(0);
        right=input.substring(3);
    }
}
class VnVt{
    char Vn;
    char Vt;

    public VnVt(char vn, char vt) {
        Vn = vn;
        Vt = vt;
    }
}
