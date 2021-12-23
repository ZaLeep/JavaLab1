import java.util.concurrent.BlockingQueue;
import java.io.File;

public class FileSearch implements Runnable {
    private File start;
    private BlockingQueue<File> q;
    public static final File FIN = new File("");

    @Override
    public void run() {
        try {
            GetFiles(start);
            q.put(FIN);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public FileSearch(BlockingQueue<File> q, File start) {
        this.q = q;
        this.start = start;
    }

    private void GetFiles(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()) {
                new Thread(new FileSearch(q, files[i])).start();
            }
            else {
                q.put(files[i]);
            }
        }
    }
}