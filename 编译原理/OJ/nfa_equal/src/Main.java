import java.util.*;

public class Main {
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
    static List<Integer> endSet;
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
        System.out.println(setList.size()+" "+endSet.size()+" "+lineNum);
        StringBuilder out=new StringBuilder();
        for(int i=0;i<setList.size();i++){
            out.append(i+" ");
        }
        System.out.println(out.toString().trim());
        System.out.println("0");
        out=new StringBuilder();
        for(int i=0;i< endSet.size();i++){
            out.append(endSet.get(i)+" ");
        }
        System.out.println(out.toString().trim());
        for(List<Line> i:setLineList){
            for(Line j:i){
                System.out.println(j.from+" \""+j.value+"\" "+j.to);
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
