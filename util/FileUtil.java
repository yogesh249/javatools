/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Yogi
 */
public class FileUtil {
    public static boolean write2File(String content, String filePath, boolean append) throws FileNotFoundException, IOException
    {
        // We want to overwrite YES, whether we want to append or not.
        return write2File(content, filePath, append, true);
    }
    /* 
     * Utility method for writing to file
     * @Params
     * 1. content -- Content to write
     * 2. filePath -- Complete path of the file including filename
     * 3. append -- whether it should start writing from first byte or the last byte of the file
     * 4. overwrite -- if you want to be safe, not to overwrite an existing file.
     */
    public static boolean write2File(String content, String filePath, boolean append, boolean overwrite) throws FileNotFoundException, IOException
    {
        File ff = new File(filePath);
        if(ff.exists() && overwrite==false && append==false)
        {
            // if you do not wan to overwite
            // and also do not want to append.
            // That's confusing, don't do anything !!!
            return false;
        }
        FileOutputStream fis = new FileOutputStream(ff, append);
        fis.write(content.getBytes());
        fis.close();
        return true;
    }
    /* 
     * This is a utility method that reads from the given file.
     * Please make sure that the size of the file does not go
     * beyond the maximum permissible limit of Integer.MAX_VALUE
     * i.e. 2,147,483,647 bytes i.e. 2.047 GB or 0x7fffffff
     */
    public static String readFromFile(String filePath) throws FileNotFoundException, IOException
    {

        File file = new File(filePath);
        char[] fileContents = new char[(int)file.length()];
        FileReader fr = new FileReader(file);
        fr.read(fileContents);
        return new String(fileContents);
    }
    /*
     * Utility method for copying file from one location to another.
     * Arguments
     * 1. srcFilePath - Source file Path
     * 2. destinationDir -- Destination directory where file is to be copied
     * 3. overwrite -- if the file already exists, do you want to overwrite?
     */
    public static String copy(String srcFilePath, String destinationDir, boolean overwrite)
    {
        File srcFile = new File(srcFilePath);
        File ff = new File(destinationDir+"\\"+srcFile.getName());
        if(ff.exists() && overwrite==false)
        {
            return "Destination file already exists. Please change the name of the file or remove destination file.";
        }
        return DOS.executeCommand("copy " + srcFilePath + " " + destinationDir);
    }
//    public static void main(String args[]) throws FileNotFoundException, IOException
//    {
//        String output = copy("C:\\Users\\Yogi\\Desktop\\category.txt", "D:\\", false);
//        System.out.println(output);
//    }
}
