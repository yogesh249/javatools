import java.net.*;
import java.io.*;
class Result
{
    public static void main(String args[])
    {
        OutputStream f1 = null;
        System.getenv("PATH");
        try {
            f1 = new FileOutputStream("results.txt");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        for (long i = 1234566; i<=1234575; i++)
        {
            String rno = new Long(i).toString();
            String output = getResult("http://www.cbseresults.nic.in/class12/cbse12.asp", rno);

            String filtered_output = new String("");
            filtered_output = filtered_output + rno + "\t" + readResult(output);
            System.out.println(filtered_output);
            AppendIntoFile(f1, filtered_output+"\r\n");

        }
        try {
            f1.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    public static void AppendIntoFile(OutputStream f1, String matter)
    {
        try
        {
            byte buf[]=matter.getBytes();
            f1.write(buf);
        }
        catch (Exception e)
        {}

    }
    public static String getResult(String url, String rno)
    {
        URL hp;
        SocketAddress addr = new InetSocketAddress("10.152.80.42", 80);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

        String output="";
        HttpURLConnection hpCon;
        DataOutputStream out;

        try
        {
            hp = new URL(url);
            // Usually companies use proxy servers to grant access to all machines
            // If you do not want to use proxy
            // then use hpCon = hp.openConnection() with no arguments.
            hpCon = (HttpURLConnection)hp.openConnection();
            hpCon.setRequestMethod("POST");
            hpCon.setDoOutput(true);
            hpCon.setUseCaches (false);
            hpCon.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            hpCon.setInstanceFollowRedirects(true);
            hpCon.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");

            //Referer is mandatory as some sites check it for security.
            hpCon.setRequestProperty("Referer", "http://www.cbseresults.nic.in/class12/cbse12.htm");

            out = new DataOutputStream(hpCon.getOutputStream());
            //At some places it may happen that the following line doesn't wworks
            // then use String content = "regno=" + URLEncoder.encode(rno);
            String content = "regno=" + rno;
            out.writeBytes (content);
            out.flush ();
            out.close ();
            // get input connection
            InputStream input = hpCon.getInputStream();
//          in = new BufferedReader(new InputStreamReader(hpCon.getInputStream()));
			byte[] b = new byte[1024];
			String tempString="";
			while(input.read(b)!=-1)
            {
				tempString = new String(b);
                output = output + tempString;
            }
            hp = null;
            hpCon = null;
        }
        catch(Exception e)
        {

        }
        return output;
    }
    public static String readResult(String in)
    {
        String output =new String("");
        int s_index = in.indexOf("GRADE");
        int e_index = -1;
        for (int i = 1; i<= 8; i++)
        {
            s_index = in.indexOf("<tr", s_index);
            e_index = in.indexOf("</tr>", s_index);
            String block = in.substring(s_index, e_index);
            output = output + parseBlock(block) ;
            s_index = e_index;
        }
        return output ;

    }
    public static String parseBlock(String block)
    {
        String output =new String("");
        int s_index = block.indexOf("size=2>")+ "size=2>".length();
        int e_index = block.indexOf("<", s_index);
        //output = block.substring(s_index, e_index); //Subject code
        //output = output + "\t";

        s_index = e_index;
        s_index = block.indexOf("size=2>", s_index)+ "size=2>".length();
        e_index = block.indexOf("</font>", s_index);
        //output = output + block.substring(s_index, e_index);//SUBJECT NAME
        //output = output + "\t";

        s_index = e_index;
        s_index = block.indexOf("size=2>", s_index)+ "size=2>".length();
        e_index = s_index + 3;
        output = output + block.substring(s_index, e_index);// MARKS
        output = output + "\t";

//      s_index = e_index;
//      s_index = block.indexOf("size=2>", s_index)+ "size=2>".length();
//      e_index = s_index + 2;
//      output = output + block.substring(s_index, e_index);//GRADE
        return output;

    }

}