package pl.edu.uj.ii.webapp.execute;

import org.junit.Test;

import java.io.IOException;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class JavaTaskTest {

    private final String javaClassSourceCode = "package test; import java.util.Scanner;import java.util.concurrent.TimeUnit; public class Reader {public static void main(String[] args) throws InterruptedException {TimeUnit.SECONDS.sleep(5);System.out.println(\"Hello\");Scanner scanner = new Scanner(System.in);while (scanner.hasNext()) {String line = scanner.nextLine();System.out.println(Reader.class.getClass() + \" read line = \" + line);}String s = \"class Test\";System.out.println(\"Executed \" + Reader.class.getName());}}";

    @Test
    public void compilesProvidedSourceCode() throws IOException, ClassNotFoundException {
        Task task = new JavaTask(CONFIG.getCompiledFileDirForJava8(), CONFIG.getJava8Home());
        task.processUpload(new UploadFile("Reader.java", javaClassSourceCode));
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