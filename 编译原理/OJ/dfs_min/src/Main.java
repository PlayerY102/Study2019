import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static boolean[] visitBack;
    static boolean[] visit;
    static List<Point> pointList1;
    static int pointNumber;
    static int endpointNumber;
    static int lineNumber;
    static int startPoint1;
    static List<List<Integer>> seg;
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        pointNumber=scanner.nextInt();
        endpointNumber=scanner.nextInt();
        lineNumber=scanner.nextInt();

        pointList1=new ArrayList<Point>();

        pointList1.add(new Point());
        for(int i=0;i<pointNumber;i++){
            Point temp=new Point();
            temp.setName(scanner.next());
            pointList1.add(temp);
        }
        startPoint1=findByName(pointList1,scanner.next());
        for(int i=0;i<endpointNumber;i++){
            pointList1.get(findByName(pointList1,scanner.next())).setEnd(true);
        }
        for(int i=0;i<lineNumber;i++) {
            int from = findByName(pointList1, scanner.next());
            String input = scanner.next();
            int to = findByName(pointList1, scanner.next());
            pointList1.get(from).addNext(input.substring(1, 2), to);
            pointList1.get(to).addBack(input.substring(1,2),from);
        }

        seg=new ArrayList<List<Integer>>();

        dfsValid(startPoint1);

        seg.add(new ArrayList<Integer>());
        seg.add(new ArrayList<Integer>());
        for(int i=1;i<=pointNumber;i++){
            Point temp=pointList1.get(i);
            if(!temp.name.isEmpty()){
                if(!temp.isEnd){
                    temp.setSegNum(0);
                    seg.get(0).add(i);

                }
                else{
                    temp.setSegNum(1);
                    seg.get(1).add(i);
                }
            }
        }

        List<List<Integer>> newGroup=cut();
        while (newGroup.size()!=seg.size()){
            seg=newGroup;
            newGroup=cut();
        }

        show();
    }
    public static void show(){
        pointNumber=seg.size();
        endpointNumber=0;
        lineNumber=0;
        StringBuilder endPointName=new StringBuilder();
        StringBuilder line=new StringBuilder();
        for(List<Integer> i:seg){
            Point now=pointList1.get(i.get(0));
            if(now.isEnd){
                endpointNumber++;
                endPointName.append(now.getName()+" ");
            }
            for(int j=32;j<=126;j++){
                if(now.getNext()[j]!=0){
                    lineNumber++;
                    line.append(now.getName()+" ");
                    char c=(char)j;
                    line.append("\""+c+"\" ");
                    int groupNum=pointList1.get(now.getNext()[j]).getSegNum();
                    line.append(pointList1.get(seg.get(groupNum).get(0)).getName()+"\n");
                }
            }
        }
        System.out.println(pointNumber+" "+endpointNumber+" "+lineNumber);
        for(int i=0;i<seg.size()-1;i++){
            System.out.print(pointList1.get(seg.get(i).get(0)).getName()+" ");
        }
        System.out.println(pointList1.get(seg.get(seg.size()-1).get(0)).getName());

        int startIndex=pointList1.get(startPoint1).getSegNum();
        System.out.println(pointList1.get(seg.get(startIndex).get(0)).getName());


        System.out.println(endPointName.toString().trim());

        System.out.print(line);

    }
    public static int findByName(List<Point> list,String name){
        for(int i=0;i<list.size();i++){
            if(list.get(i).name.equals(name)){
                return i;
            }
        }
        return -1;
    }
    public static void dfsValid(int start){
        visitBack=new boolean[pointNumber+1];
        visit=new boolean[pointNumber+1];
        dfs(start);
        for(int i=1;i<=pointNumber;i++){
            Point temp=pointList1.get(i);
            if(temp.isEnd){
                dfsBack(i);
            }
        }
        for(int i=1;i<=pointNumber;i++){
            if(!visit[i]||!visitBack[i]){
                Point temp=pointList1.get(i);
                temp.setName("");
            }
        }
    }
    public static void dfs(int i){
        visit[i]=true;
        Point temp=pointList1.get(i);
        for(int nextC=32;nextC<=126;nextC++){
            if(!visit[temp.getNext()[nextC]]){
                dfs(temp.getNext()[nextC]);
            }
        }
    }
    public static void dfsBack(int i){
        visitBack[i]=true;
        Point temp=pointList1.get(i);
        for(int bakcC=32;bakcC<=126;bakcC++){
            List<Integer> backList=temp.getBack()[bakcC];
            for(int z:backList){
                if(!visitBack[z]){
                    dfsBack(z);
                }
            }
        }
    }
    public static boolean judge(int x,int y){
        Point point1=pointList1.get(x);
        Point point2=pointList1.get(y);
        int[] next1=point1.getNext();
        int[] next2=point2.getNext();
        for(int i=32;i<=126;i++){
            int group1=pointList1.get(next1[i]).segNum;
            int group2=pointList1.get(next2[i]).segNum;
            if(group1!=group2){
                return false;
            }
        }
        return true;
    }
    public static List<List<Integer>> cut(){
        List<List<Integer>> temp=new ArrayList<List<Integer>>();
        for(List<Integer> list: seg){
            for(int i=0;i<list.size();i++){
                List<Integer> newGroup=new ArrayList<Integer>();
                int nowIndex=list.get(i);
                Point nowPoint=pointList1.get(nowIndex);

                newGroup.add(nowIndex);

                for(int j=i+1;j<list.size();j++){
                    int tarIndex=list.get(j);
                    Point tarPoint=pointList1.get(tarIndex);
                    if(judge(nowIndex,tarIndex)){
                        newGroup.add(tarIndex);
                        list.remove(j);
                        j--;
                    }
                }
                temp.add(newGroup);
            }

        }
        for(int groupNum=0;groupNum<temp.size();groupNum++){
            List<Integer> list=temp.get(groupNum);
            for(int nowPointNum:list){
                pointList1.get(nowPointNum).setSegNum(groupNum) ;
            }
        }
        return temp;
    }
}
class Point{
    String name;
    boolean isEnd;
    int[] next;
    List<Integer>[] back;
    int segNum;
    public Point() {
        name="";
        isEnd=false;
        next=new int[127];
        back=new List[127];
        for(int i=0;i<back.length;i++){
            back[i]=new ArrayList<Integer>();
        }
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

    public int getSegNum() {
        return segNum;
    }

    public void setSegNum(int segNum) {
        this.segNum = segNum;
    }

    public List<Integer>[] getBack() {
        return back;
    }

    public void setBack(List<Integer>[] back) {
        this.back = back;
    }

    public void addNext(String value, int to){
        char[] temp=new char[10];
        value.getChars(0,1,temp,0);
        int index=temp[0];
        this.next[index]=to;
    }
    public void addBack(String value,int to){
        char[] temp=new char[10];
        value.getChars(0,1,temp,0);
        int index=temp[0];
        this.back[index].add(to);
    }
}
