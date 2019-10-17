import java.util.*;
public class Main{
    public static void main(String[] args) {
        Main1.main1();
        Main1.main1();
        Main2.main2(Main1.input.toString());
    }
}
class Main1 {
    static int statusN;
    static int startNum;
    static int endN;
    static int lineN;

    static List<Point> pointList = new ArrayList<Point>();
    static List<Line> lineList = new ArrayList<Line>();

    static List<Set<Point>> setList = new LinkedList<>();
    static List<List<Line>> setLineList=new LinkedList<>();
    static Queue<Set<Point>> unProcessSet = new LinkedList<>();

    static boolean[] visit1;
    static boolean[] visit2;

    static Scanner scanner = new Scanner(System.in);
    static StringBuilder input=new StringBuilder();


    static List<Integer> endSet;

    static Set<Point> findClosure(Point startPoint) {
        visit1 = new boolean[pointList.size()];
        Set<Point> result = new HashSet<>();
        //result.addAll(findClosureFrom(startPoint));
        result.addAll(findClosureTo(startPoint));
        return result;
    }

    static Set<Point> findClosureTo(Point startPoint) {
        Set<Point> result = new HashSet<>();

        visit1[findByName(startPoint.name)] = true;
        result.add(startPoint);

        for (Line i : startPoint.linesTo) {
            if (i.value == 0) {
                if (!visit1[i.to]) {
                    visit1[i.to] = true;
                    result.addAll(findClosureTo(pointList.get(i.to)));
                }
            }
        }
        return result;
    }

    static Set<Point> findClosureFrom(Point startPoint) {
        Set<Point> result = new HashSet<>();

        visit1[findByName(startPoint.name)] = true;
        result.add(startPoint);

        for (Line i : startPoint.linesFrom) {
            if (i.value == 0) {
                if (!visit1[i.from]) {
                    visit1[i.from] = true;
                    result.addAll(findClosureFrom(pointList.get(i.from)));
                }
            }
        }
        return result;
    }

    static Set<Point> findSetTo(Set<Point> startSet, char value) {
        Set<Point> result = new HashSet<>();
        for (Point i : startSet) {
            for (Line j : i.linesTo) {
                if (j.value == value) {
                    result.addAll(findClosure(pointList.get(j.to)));
                }
            }
        }
        return result;
    }

    static int findByName(String name) {
        for (int i = 0; i < pointList.size(); i++) {
            if (pointList.get(i).name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    static int findBySet(List<Set<Point>> list,Set<Point> set){
        for(int i=0;i<list.size();i++){
            if(setEquals(list.get(i),set)){
                return i;
            }
        }
        return -1;
    }
    static void addLine(int from, char value, int to) {
        Line newLine = new Line();
        newLine.from = from;
        newLine.value = value;
        newLine.to = to;
        lineList.add(newLine);
        pointList.get(from).linesTo.add(newLine);
        pointList.get(to).linesFrom.add(newLine);
    }

    static int setAtList(List<Set<Point>> list, Set<Point> set) {
        for (int i=0;i<list.size();i++) {
            if (setEquals(list.get(i), set)) {
                return i;
            }
        }
        return -1;
    }

    static boolean setEquals(Set<Point> set1, Set<Point> set2) {
        if (set1 == null && set2 == null) {
            return true;
        }

        if (set1 == null || set2 == null || set1.size() != set2.size()) {
            return false;
        }

        Iterator ite2 = set2.iterator();

        while (ite2.hasNext()) {
            if (!set1.contains(ite2.next())) {
                return false;
            }
        }
        return true;
    }

    static void findAllSet() {
        while (unProcessSet.size() != 0) {

            Set<Point> nowSet=unProcessSet.poll();
            if (setAtList(setList, nowSet)!=-1) {
                continue;
            }
            setList.add(nowSet);
            setLineList.add(new ArrayList<Line>());
            for (char nextC = 32; nextC <= 126; nextC++) {
                Set newSet = findSetTo(nowSet, nextC);
                if(newSet.size()>0){
                    Line newLine=new Line();
                    newLine.from=findBySet(setList,nowSet);
                    newLine.value=nextC;
                    if(setAtList(setList,newSet)!=-1){
                        newLine.to= setAtList(setList,newSet);
                    }
                    else if(setAtList((List)unProcessSet,newSet)!=-1){
                        newLine.to=setList.size()+findBySet((List)unProcessSet,newSet);
                    }
                    else{
                        newLine.to=setList.size()+unProcessSet.size();
                        unProcessSet.offer(newSet);
                    }
                    setLineList.get(newLine.from).add(newLine);
                }
            }
        }
        confirmEndSet();
    }
    static void confirmEndSet(){
        endSet=new ArrayList<>();
        for(int i=0;i<setList.size();i++){
            Set<Point> tempSet=setList.get(i);
            for(Point tempPoint:tempSet){
                if(tempPoint.isEnd){
                    endSet.add(i);
                    break;
                }
            }
        }
    }

    static void show(){
        int lineNum=0;
        for(List<Line>i:setLineList){
            lineNum+=i.size();
        }
        input.append(setList.size()+" "+endSet.size()+" "+lineNum+"\n");
        for(int i=0;i<setList.size();i++){
            input.append(i+" ");
        }
        input.append("\n");
        input.append("0\n");
        for(int i=0;i< endSet.size();i++){
            input.append(endSet.get(i)+" ");
        }
        input.append("\n");
        for(List<Line> i:setLineList){
            for(Line j:i){
                input.append(j.from+" \""+j.value+"\" "+j.to+"\n");
            }
        }
    }
    public static void main1() {
        pointList = new ArrayList<Point>();
        lineList = new ArrayList<Line>();
        setList = new LinkedList<>();
        setLineList=new LinkedList<>();
        unProcessSet = new LinkedList<>();


        statusN = scanner.nextInt();
        endN = scanner.nextInt();
        lineN = scanner.nextInt();

        pointList.add(new Point(""));//add island point

        for (int i = 0; i < statusN; i++) {
            Point newPoint = new Point(scanner.next());
            pointList.add(newPoint);
        }
        startNum = findByName(scanner.next());
        for (int i = 0; i < endN; i++) {
            int index = findByName(scanner.next());
            pointList.get(index).isEnd = true;
        }

        for (int i = 0; i < lineN; i++) {
            int from = findByName(scanner.next());
            String getValue = scanner.next().replace("\"", "");
            char value = 0;
            if (getValue.length() > 0) {
                value = getValue.charAt(0);
            }
            int to = findByName(scanner.next());
            addLine(from, value, to);
        }
        unProcessSet.add(findClosure(pointList.get(startNum)));
        findAllSet();
        show();
    }
}

class Point {
    String name;
    List<Line> linesTo;
    List<Line> linesFrom;
    Boolean isEnd;

    public Point(String name) {
        this.name = name;
        this.linesTo = new LinkedList<>();
        this.linesFrom = new LinkedList<>();
        this.isEnd = false;
    }
}

class Line {
    int from;
    int to;
    char value;
}

class Main2 {
    static List[] newGra;
    static boolean[][] flag=new boolean[2000][2000];
    public static void main2(String myInput) {

        Scanner scanner=new Scanner(myInput);
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
