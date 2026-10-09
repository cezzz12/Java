public class CyclicThread extends Thread {
    static int p;
    static int th_id;
    static double[] a,b,c;
    CyclicThread(int p,int th_id,double[] a,double[] b,double[] c) {
        this.p=p;
        this.th_id=th_id;
        this.a=a;
        this.b=b;
        this.c=c;

    }
    @Override
    public void run() {
        for (int i = th_id; i < a.length; i += p) {
            c[i] = a[i] + b[i];
        }
    }
}
