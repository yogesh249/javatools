/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flatfilereader;

/**
 *
 * @author yogesh.gandhi
 */
public class TestFlatFileReader {
    public static void main(String[] s) throws Exception
    {
        FlatFileReader f= new FlatFileReader("C:/Documents and Settings/yogesh.gandhi/Desktop/190913SECURED0436.txt");
        int i=1;
        for(PipedString pps : f)
        {
            for(String eachElement : pps)
            {
                System.out.print(i+":");
                System.out.println(eachElement);
                i++;
            }
            System.out.println("**************************");
        }
        f.Close();
    }
}
