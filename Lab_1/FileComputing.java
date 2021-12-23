import java.util.concurrent.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileReader;

public class FileComputing implements Callable<List<String>>{
    private BlockingQueue<File> q;
    private static final String regex1 = "\\S+.txt$";
    private static final String regex2 = "^\\d+(\\.\\d+)";

    @Override
    public List<String> call() {
        try {
            List<String> result =  new ArrayList<>();
            Boolean flag = true;
            while(flag) {
                File f = q.take();
                if(f == FileSearch.FIN && !q.contains(FileSearch.FIN)) {
                    q.put(f);
                    flag = false;
                }
                else {
                    if(f.getName().matches(regex1)) {
                        int count = FileCount(f);
                        if(count > 0) 
                            result.add(f.getPath() + ": " + Integer.toString(count));
                    }
                }
            }
        return result;
        } catch(Exception e) {
            return null;
        }
    }
    
    public FileComputing(BlockingQueue<File> q) {
        this.q = q;
    }

    private int FileCount(File f) {
        try {
            int count = 0;
            Scanner read = new Scanner(new FileReader(f));
            while(read.hasNextLine()) {
                String line = read.nextLine();
                Boolean flag = true;
                int left = 0;
                int right = 0;
                int lastRight = 0;
                while(flag) {
                    int point = line.indexOf('.', right + 1);
                    if(point == -1) {
                        flag = false;
                    }
                    else {
                        left = point;
                        right = point;
                        while(left != lastRight + 1 && ("" + line.charAt(left - 1)).matches("[0-9]")) {
                            left--;
                        }
                        while(right != line.length() - 1 && ("" + line.charAt(right + 1)).matches("[0-9]")) {
                            right++;
                        }
                        if(line.substring(left, right + 1).matches(regex2)) {
                            count++;
                            lastRight = right;
                        }
                    }
                    
                }
            }
            return count;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
