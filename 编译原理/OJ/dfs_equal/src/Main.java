import java.util.*;

public class Main {
    static List[] newGra;
    static boolean[][] flag=new boolean[2000][2000];
    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        int pointNumber=scanner.nextInt();
        int endpointNumber=scanner.nextInt();
        int lineNumber=scanner.nextInt();

        List<Point2> pointList1=new ArrayList<Point2>();

        pointList1.add(new Point2());
        for(int i=0;i<pointNumber;i++){
            Point2 temp=new Point2();
            temp.setName(scanner.next());
            pointList1.add(temp);
        }
        int startPoint1=findByName(pointList1,scanner.next());
        for(int i=0;i<endpointNumber;i++){
            pointList1.get(findByName(pointList1,scanner.next())).setEnd(true);
        }
        for(int i=0;i<lineNumber;i++){
            int from=findByName(pointList1,scanner.next());
            String input=scanner.next();
            int to=findByName(pointList1,scanner.next());
            pointList1.get(from).addNext(input.substring(1,2),to);
        }
        List<Point2> reList= Point2.reverse(pointList1);

        pointNumber=scanner.nextInt();
        endpointNumber=scanner.nextInt();
        lineNumber=scanner.nextInt();

        List<Point2> pointList2=new ArrayList<Point2>();

        pointList2.add(new Point2());
        for(int i=0;i<pointNumber;i++){
            Point2 temp=new Point2();
            temp.setName(scanner.next());
            pointList2.add(temp);
        }
        int startPoint2=findByName(pointList2,scanner.next());
        for(int i=0;i<endpointNumber;i++){
            pointList2.get(findByName(pointList2,scanner.next())).setEnd(true);
        }
        for(int i=0;i<lineNumber;i++){
            int from=findByName(pointList2,scanner.next());
            String input=scanner.next();
            int to=findByName(pointList2,scanner.next());
            pointList2.get(from).addNext(input.substring(1,2),to);
        }
        newGra=new List[2];
        newGra[0]=reList;
        newGra[1]=pointList2;


        if(dfs(startPoint1,startPoint2)){
            System.out.println("No");
        }else{
            System.out.println("Yes");
        }

    }
    public static int findByName(List<Point2> list, String name){
        for(int i=0;i<list.size();i++){
            if(list.get(i).name.equals(name)){
                return i;
            }
        }
        return -1;
    }
    public static boolean dfs(int point1,int point2){
        flag[point1][point2]=true;
        Point2 temp1=(Point2)newGra[0].get(point1);
        Point2 temp2=(Point2)newGra[1].get(point2);
        if(temp1.isEnd==temp2.isEnd){
            return true;
        }
        for(int i=32;i<=126;i++){
            int next1=temp1.next[i];
            int next2=temp2.next[i];
            if(next1==2&&next2==1){
                System.out.println("in");
            }
            if(flag[next1][next2]==false){
                if(dfs(next1,next2)){
                    return true;
                }
            }
        }
        return false;
    }

}
class Point2 {
    String name;
    boolean isEnd;
    int[] next;

    public Point2() {
        name="";
        isEnd=false;
        next=new int[127];
    }

    public static List<Point2> reverse(List<Point2> pointList){
        for(Point2 i:pointList){
            i.setEnd(!i.isEnd);
        }
        return pointList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int[] getNext() {
        return next;
    }

    public void setNext(int[] next) {
        this.next = next;
    }

    public void addNext(String value, int to){
        char[] temp=new char[10];
        value.getChars(0,1,temp,0);
        int index=temp[0];
        this.next[index]=to;
    }
}
