public class BlockThread extends Thread {
    static int p_start;
    static int p_end;
    static double[] a,b,c;
    BlockThread(int p_start,int p_end,double[] a,double[] b,double[] c) {
        this.p_start=p_start;
        this.p_end=p_end;
        this.a=a;
        this.b=b;
        this.c=c;

    }
    @Override
    public void run() {
        for (int i = p_start; i < p_end; i += 1) {
            c[i] = a[i] + b[i];
        }
    }
}
