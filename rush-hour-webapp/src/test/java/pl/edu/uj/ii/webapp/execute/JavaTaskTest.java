package pl.edu.uj.ii.webapp.execute;

import org.junit.Ignore;
import org.junit.Test;
import pl.edu.uj.ii.webapp.execute.tasks.ExecutionTask;
import pl.edu.uj.ii.webapp.execute.tasks.JavaTask;

import java.io.IOException;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
@Ignore
public class JavaTaskTest {

    private final String javaClassSourceCode = "package test; import java.util.Scanner;import java.util.concurrent.TimeUnit; public class Reader {public static void main(String[] args) throws InterruptedException {TimeUnit.SECONDS.sleep(5);System.out.println(\"Hello\");Scanner scanner = new Scanner(System.in);while (scanner.hasNext()) {String line = scanner.nextLine();System.out.println(Reader.class.getClass() + \" read line = \" + line);}String s = \"class Test\";System.out.println(\"Executed \" + Reader.class.getName());}}";

    @Test
    public void compilesProvidedSourceCode() throws IOException, ClassNotFoundException {
        ExecutionTask executionTask = new JavaTask(CONFIG.getCompiledFileDirForJava8(), CONFIG.getJava8Home());
        executionTask.processUpload(new UploadFile("Reader.java", javaClassSourceCode));
    }

//    static class Reader {
//        public static void main(String[] args) throws InterruptedException {
//            TimeUnit.SECONDS.sleep(5);
//            System.out.println("Hello");
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                System.out.println(Reader.class.getClass() + " read line = " + line);
//            }
//            System.out.println("Executed " + Reader.class.getName());
//        }
//    }

}