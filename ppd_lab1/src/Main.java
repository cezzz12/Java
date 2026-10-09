import java.util.Random;

public class Main {
    static int N = 1000000;
    static int P = 8;

    static final Random random = new Random();

    public static void sum(double[] a, double[] b, double[] c){
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }
    }

    public static void sumCyclic(double[] a, double[] b, double[] c) throws InterruptedException {
        CyclicThread[] threads = new CyclicThread[P];
        for (int thread_id = 0; thread_id < P; thread_id++) {
            threads[thread_id] = new CyclicThread(P, thread_id, a, b, c);
            threads[thread_id].start();
        }
        for (int thread_id = 0; thread_id < P; thread_id++) {
            threads[thread_id].join();
        }
    }

    public static void sumBlock(double[] a, double[] b, double[] c) throws InterruptedException {
        int p_start = 0, p_end;
        int work_per_thread = a.length / P;
        int work_remainder = a.length % P;
        p_end = work_per_thread;

        BlockThread[] threads = new BlockThread[P];

        for (int thread_id = 0; thread_id < P; thread_id++) {
            if(work_remainder > 0){
                p_end++;
                work_remainder--;
            }
            threads[thread_id] = new BlockThread(p_start, p_end, a, b, c);
            threads[thread_id].start();
            p_start = p_end;
            p_end += work_per_thread;
        }

        for (int thread_id = 0; thread_id < P; thread_id++) {
            threads[thread_id].join();
        }
    }

    public static void initializeArray(double[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextDouble();
        }
    }

    public static boolean validateSum(double[] a, double[] b, double[] c){
        if(a.length != b.length || a.length != c.length)
            return false;

        for (int i = 0; i < a.length; i++) {
            if (a[i] + b[i] != c[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0) N = Integer.parseInt(args[0]);
        if (args.length > 1) P = Integer.parseInt(args[1]);
        if (N < 1 || P < 1) throw new IllegalArgumentException("Size and thread count must be positive");
        double[] a = new double[N];
        double[] b = new double[N];
        double[] c = new double[N];

        initializeArray(a);
        initializeArray(b);

        var sequentialStartTime = System.nanoTime();
        sum(a,b,c);
        var sequentialEndTime = System.nanoTime();

        if(!validateSum(a,b,c)) {
            System.out.println("Error");
        }

        double timeSequential = (double)(sequentialEndTime - sequentialStartTime) / 1e6;

        System.out.println("Sequential time: " + timeSequential);

        var cyclicStartTime = System.nanoTime();
        sumCyclic(a,b,c);
        var cyclicEndTime = System.nanoTime();

        if(!validateSum(a,b,c)) {
            System.out.println("Error");
        }

        double timeCyclic = (double)(cyclicEndTime - cyclicStartTime) / 1e6;

        System.out.println("Cyclic time: " + timeCyclic);

        var blockStartTime = System.nanoTime();
        sumBlock(a,b,c);
        var blockEndTime = System.nanoTime();

        if(!validateSum(a,b,c)) {
            System.out.println("Error");
        }

        double timeBlock = (double)(blockEndTime - blockStartTime) / 1e6;

        System.out.println("Block time: " + timeBlock);
    }
}
