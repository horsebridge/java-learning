import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class HW1_1800011036 extends Tetris{

    public String id=new String("1800011036");

    int new_y=0;
    int new_x=-1;
    int goal=-1;
    private boolean rot[][][];
    Stack<PieceOperator> path;

    class Weight{
        int x;
        int y;
        int figure;
        int height;
        Weight pre;
        PieceOperator operator;
        int alarm;
        Weight(int Y,int X,int Figure,int Height,Weight w,PieceOperator op,int al) {
            x=X;
            y=Y;
            figure=Figure;
            height=Height;
            pre=w;
            operator=op;
            alarm=al;
        }
        public int distance() {
            return Math.abs(x-new_x)+Math.abs(y-new_y)+(goal-figure+4)%4;
        }
    }

    static Comparator<Weight> weightCom1=new Comparator<Weight>() {
        @Override
        public int compare(Weight o1, Weight o2) {
            return o1.height-o2.height;
        }
    };
    static Comparator<Weight> weightCom2=new Comparator<Weight>() {
        @Override
        public int compare(Weight o1, Weight o2) {
            return o1.distance()-o2.distance();
        }
    };

    void rotate() {
        rot=new boolean[4][4][4];
        for(int y=0;y<4;y++) {
            for(int x=0;x<4;x++) {
                rot[0][y][x]=piece[y][x];
            }
        }
        for(int k=1;k<4;k++) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    rot[k][y][x] = rot[k - 1][x][3 - y];
                }
            }
        }
    }

    private boolean reach[][];
    private void dfs(int y,int x) {
        if(reach[y][x]) return;
        if(board[y][x]) {
            //reach[y][x]=false;
            //System.out.println("brick");
            return;
        }
        reach[y][x]=true;
        if(y>0) dfs(y-1,x);
        if(x>0) dfs(y,x-1);
        if(x<w-1) dfs(y,x+1);
    }

    private int put(int y, int x, boolean[][] aPiece) {
        int height=0;
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                if(aPiece[i][j]) {
                    if(y-i<0 || y-i>=h) return -1;
                    if(x+j>=w || x+j<0) return -1;
                    if(!reach[y-i][x+j]) return -1;
                    height+=y-i;
                    if(i==3||aPiece[i+1][j]) continue;
                    int k=y-i-1;
                    while(k>=0&&!board[k][x+j]) {
                        height++;
                        k--;
                    }
                }
            }
        }
        return height;
    }

    Weight arrival() {
        int height=put(piece_y,piece_x,rot[0]);
        Weight start=new Weight(piece_y,piece_x,0,height,null,null,0);
        PriorityQueue<Weight> pq=new PriorityQueue<Weight>(weightCom2);
        pq.add(start);
        int out=0;
        while (!pq.isEmpty()) {
            Weight t=pq.poll();
            out++;
            if(out>100) return null;
            if(t.distance()==0) {
                return t;
            }
            if(t.alarm==5) {
                int dheight=put(t.y-1,t.x,rot[t.figure]);
                if(dheight==-1) continue;
                t=new Weight(t.y-1,t.x,t.figure,dheight,t.pre,t.operator,0);
            }
            int rheight=put(t.y,t.x,rot[(t.figure+1)%4]);
            if(rheight!=-1) {
                Weight next=new Weight(t.y,t.x,(t.figure+1)%4,rheight,t,PieceOperator.Rotate,t.alarm+1);
                pq.add(next);
            }
            if(put(t.y,t.x-1,rot[t.figure])!=-1) {
                Weight next=new Weight(t.y,t.x-1,t.figure,t.height,t,PieceOperator.ShiftLeft,t.alarm+1);
                pq.add(next);
            }
            if(put(t.y,t.x+1,rot[t.figure])!=-1) {
                Weight next=new Weight(t.y,t.x+1,t.figure,t.height,t,PieceOperator.ShiftRight,t.alarm+1);
                pq.add(next);
            }
            if(put(t.y-1,t.x,rot[t.figure])!=-1) {
                Weight next=new Weight(t.y-1,t.x,t.figure,t.height-4,t,PieceOperator.Drop,t.alarm+1);
                pq.add(next);
            }
        }
        return null;
    }

    private boolean findPlace(int bottom,int left) {
        rotate();
        reach=new boolean[h][w];
        dfs(h-1,0);
        for(int y=h-1;y>=h-nBufferLines;y--) {
            for(int x=0;x<w;x++) {
                reach[y][x]=true;
            }
        }
        PriorityQueue<Weight> pq=new PriorityQueue<Weight>(weightCom1);
        boolean stop=false;
        for(int y=bottom;y<h-nBufferLines;y++) {
            for (int x=left;x<w;x++) {
                for(int k=0;k<4;k++) {
                    int height=put(y,x,rot[k]);
                    if(height!=-1) {
                        Weight w=new Weight(y,x,k,height,null,null,0);
                        pq.add(w);
                    }
                }
            }
            if(stop) break;
            if(!pq.isEmpty()) stop=true;
        }
        path=new Stack<PieceOperator>();
        while (!pq.isEmpty()) {
            Weight t=pq.poll();
            new_y=t.y;
            new_x=t.x;
            goal=t.figure;
            Weight end=arrival();
            if(end!=null) {
                Weight next=end;
                while (next.operator!=null) {
                    path.push(next.operator);
                    next=next.pre;
                }
                return true;
            }
        }
        return false;
    }

    public PieceOperator robotPlay() {
        if(path==null || path.isEmpty()) {
            if(movePiece(PieceOperator.Drop)==false) return PieceOperator.Keep;
            else {
                new_y = 0;
                new_x = -1;
                goal = -1;
                findPlace(new_y, new_x);
            }
        }
        if(!path.isEmpty()) {
            PieceOperator op=path.pop();
            return op;
        }
        return PieceOperator.Keep;
        //else return PieceOperator.Drop;
    }
}
