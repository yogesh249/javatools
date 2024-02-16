import java.io.File;
import java.util.Date;

public class ListModifiedFiles {

    public static void listFilesModifiedAfter(File directory, long givenDate) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFilesModifiedAfter(file, givenDate);
                } else {
                    long modifiedTime = file.lastModified();
                    if (modifiedTime > givenDate) {
                        System.out.println(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String earPath = "path/to/your/ear/file.ear"; // Replace with the actual path
        long givenDateInSeconds = 1645030800; // Replace with the desired date in seconds since epoch

        File earFile = new File(earPath);

        if (!earFile.exists()) {
            System.out.println("The specified path does not exist.");
            System.exit(1);
        }

        listFilesModifiedAfter(earFile, givenDateInSeconds * 1000); // Convert seconds to milliseconds
    }
}
