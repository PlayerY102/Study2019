import java.util.*;

public class Main {
    static Map<Character,Map<Character,Integer>> priority;
    static int[][] matrix={
            {2,1,1,2,1,2},
            {2,2,1,2,1,2},
            {1,1,1,3,1,2},
            {2,2,-1,2,-1,2},
            {2,2,-1,2,-1,2},
            {1,1,1,1,1,3}
    };
    static char[] symbols={'+','*','(',')','i','#'};
    static List<String> grammaer=new ArrayList<String>(Arrays.asList(new String[]{"N+N", "N*N", "(N)", "i"}));
    static void initPriority(){
        priority=new HashMap<>();
        for(int i=0;i<symbols.length;i++){
            char from=symbols[i];
            Map<Character,Integer> map=new HashMap<>();
            for(int j=0;j<symbols.length;j++){
                char to=symbols[j];
                int compare=matrix[i][j];
                map.put(to,compare);
            }
            priority.put(from,map);
        }
    }
    static char lastChar(Stack<Character>stack){
        for(int i=stack.size()-1;i>=0;i--){
            if(stack.elementAt(i)!='N'){
                return stack.elementAt(i);
            }
        }
        return 'N';
    }
    static int analyze(String input){
        input=input+"#";
        Stack<Character> stack=new Stack<>();
        stack.push('#');
        int count=0;
        for(int i=0;i<input.length();i++){
            char next=input.charAt(i);
            while (priority.get(lastChar(stack)).get(next)==2){
                StringBuilder temp=new StringBuilder();
                temp.insert(0,stack.pop());
                while (!grammaer.contains(temp.toString())){
                    if(stack.peek()=='#'){
                        return -1;
                    }
                    temp.insert(0,stack.pop());
                }
                stack.push('N');
                count++;
            }
            stack.push(next);
            count++;
        }
        return count;
    }
    public static void main(String[] args) {
        initPriority();
        Scanner scanner=new Scanner(System.in);
        int T;
        T=scanner.nextInt();
        scanner.nextLine();
        while ((T--)>0){
            String input=scanner.nextLine();
            int count=analyze(input)-1;
            if(count<0){
                System.out.println("F");
            }
            else {
                System.out.println("T "+count);
            }
        }
    }
}
