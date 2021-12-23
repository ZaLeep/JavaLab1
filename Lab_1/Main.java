import java.util.*;
import java.util.concurrent.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BlockingQueue<File> q = new ArrayBlockingQueue<File>(10);
        
        System.out.println("Enter directory path: ");
        String dir = sc.nextLine();
        Boolean flag = true;
        File f;
        while(flag) {
            f = new File(dir);
            if(f.exists() && f.isDirectory()) {
                flag = false;
                new Thread(new FileSearch(q, f)).start();
            }
            else {
                System.out.println("Something wrong with your path. Please enter it again.");
                System.out.println("Enter directory path: ");
                dir = sc.nextLine();
            }
        }

        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<List<String>>> result = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            result.add(pool.submit(new FileComputing(q)));
        }
        FileWriter writer;
        try {
            writer = new FileWriter("D:\\Course III\\WEB_Java\\Lab_1\\result.txt");
            for(Future<List<String>> r: result) {
                List<String> l = r.get();
                for(String s: l) {
                    writer.write(s + "\n");
                    System.out.println(s + "\n");
                }
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        
        sc.close();
        pool.shutdown();
    }
}
