/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author Yogi
 */
public class Internet {
    private static String serverTime=null;
    @Deprecated
    // Use postRequest instead.
    // Pass Url, content and requestProperties
    public static String getResult(String url, String rno, String referer, String textBoxName)
    {
        Map<String, String> content = new HashMap<String, String>();
        content.put(textBoxName, rno);

        Map<String, String> requestProps = new HashMap<String, String>();
        requestProps.put("Referer", referer);

        return postRequest(url, content, requestProps);
    }
    // This function will post the request to a URL
    // With the values you want to post in content
    // And the request Properties in requestProperties Map.
    // Content will hold the name value pairs, which you want to send to the
    // URL, which are usually sent by the click of submit button
    // RequestProperties are hidden properties usually like a Referrer.
    public static String postRequest(String url, Map<String, String> content,
            Map<String, String> requestProperties)
    {
        URL hp;
        //SocketAddress addr = new InetSocketAddress("10.152.80.42", 80);
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        StringBuilder output=new StringBuilder("");
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

            // Iterate over the request Properties
            // and set them in hpCon
            if(requestProperties!=null)
            {
                Set<String> keys = requestProperties.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext())
                {
                    String key = it.next();
                    String value = requestProperties.get(key);
                    hpCon.setRequestProperty(key, value);
                }
            }

            //hpCon.setRequestProperty("Referer", referer);

            out = new DataOutputStream(hpCon.getOutputStream());
            // If the data that you are posting contains URLs then you need to
            // encode the data using URLEncoder.encode(rno)
            // However this is not our case.
            // String content = "regno=" + URLEncoder.encode(rno);
            // String content = textBoxName + "=" + rno;
            StringBuilder requestBody = new StringBuilder("");
            if(content!=null)
            {
                Set<String> contentKeys = content.keySet();
                Iterator<String> contentIterator = contentKeys.iterator();
                while(contentIterator.hasNext())
                {
                    String key = contentIterator.next();
                    String value = requestProperties.get(key);
                    requestBody.append(key);
                    requestBody.append("=");
                    requestBody.append(value);
                    // if it has more values then add an &
                    if(contentIterator.hasNext()) requestBody.append("&");
                }
            }
            out.writeBytes (requestBody.toString());
            out.flush ();
            out.close ();
            // get input connection

            // Always BufferedReader to read the InputStream
            // reads the website correctly.
            InputStream in = hpCon.getInputStream();
            InputStreamReader inStream = new InputStreamReader(in);
            BufferedReader input = new BufferedReader(inStream);

            // Do not use array method to read InputStreams
            // It gives corrupted data. The method used above is correct.
            //byte[] b = new byte[1024];
            String tempString="";
            while(true)
            {

                tempString = input.readLine();
                if(tempString == null) break;
                output.append(tempString);
            }
            hp = null;
            hpCon = null;
        }
        catch(Exception e)
        {
            System.out.println("Exception occured in intenet.java" + e.getMessage());
            System.exit(0);
        }
        return output.toString();
    }

    private static String getServerTime()
    {
        StringBuilder output=new StringBuilder("");
        try {
            Socket s = new Socket("time-A.timefreq.bldrdoc.gov", 13);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            boolean more = true;
            while (more) {
                String line = in.readLine();
                if (line == null) {
                    more = false;
                } else {
                    output.append(line);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        String date = output.substring(6, 14);
        serverTime = date;
        return date;

    }

    public static int getYear()
    {
        if(serverTime == null)
        {
            return Integer.parseInt(getServerTime().substring(0, 2));
        }
        else
        {
            return Integer.parseInt(serverTime.substring(0, 2));
        }

    }
    public static int getMonth()
    {
        if(serverTime == null)
        {
            return Integer.parseInt(getServerTime().substring(4, 5));
        }
        else
        {
            return Integer.parseInt(serverTime.substring(4, 5));
        }
    }
    public static int getDay()
    {
        if(serverTime == null)
        {
            return Integer.parseInt(getServerTime().substring(6, 8));
        }
        else
        {
            return Integer.parseInt(serverTime.substring(6, 8));
        }
    }
    // This will get the HTML code of the website of the URL passed.
    public static String getWebsite(String url)
    {
        URL hp;
        URLConnection hpCon;
        StringBuilder output = new StringBuilder("");
        try
        {
            // make the URL
            hp = new URL(url);
            // Open the connection to the URL
            hpCon = hp.openConnection();
            //This is mandatory to get the latest values.
            hpCon.setUseCaches(false);

            // DO NOT CHECK THE CONTENT LENGTH.
            // AS IT ALWAYS GIVE -1 when content Length is not known.
            // So it doesn't work in case of google.com
            // But it works in case of ICICI, don't know why ??

            System.out.println("Content Length = " + hpCon.getContentLength());
//            if (-1 == hpCon.getContentLength()) {
//                System.out.println("Connection Lost !!!");
//                return "Connection Lost";
//            }
            InputStream in = hpCon.getInputStream();
            InputStreamReader inStream = new InputStreamReader(in);
            BufferedReader input = new BufferedReader(inStream);
            String tempString="";
            while(true)
            {

                tempString = input.readLine();
                if(tempString == null) break;
                output.append(tempString);
            }
            hp = null;
            hpCon = null;
        }
        catch(Exception e)
        {
            System.out.println("Exception occured in internet.java : " + e.getMessage());
            System.exit(0);
        }
        return output.toString();
    }
    // This will get the HTML code of the website of the URL passed.
    /*
     * URL - URL of the website
     * server - The IP address of the proxy server
     * port - Port of the proxy server.
     */
    public static String getWebsite(String url, String server, String port)
    {
        URL hp;
        URLConnection hpCon;
        StringBuilder output = new StringBuilder("");
        SocketAddress addr = null;
        Proxy proxy = null;
        try
        {
            // make the URL
            hp = new URL(url);
            addr = new InetSocketAddress(server, new Integer(port).intValue());
            proxy = new Proxy(Proxy.Type.HTTP, addr);
            hpCon = hp.openConnection(proxy);
            //This is mandatory to get the latest values.
            hpCon.setUseCaches(false);

            // DO NOT CHECK THE CONTENT LENGTH.
            // AS IT ALWAYS GIVE -1 when content Length is not known.
            // So it doesn't work in case of google.com
            // But it works in case of ICICI, don't know why ??


//            if (-1 == hpCon.getContentLength()) {
//                System.out.println("Connection Lost !!!");
//                return "Connection Lost";
//            }
            InputStream in = hpCon.getInputStream();
            InputStreamReader inStream = new InputStreamReader(in);
            BufferedReader input = new BufferedReader(inStream);
            String tempString="";
            while(true)
            {

                tempString = input.readLine();
                if(tempString == null) break;
                output.append(tempString);
            }
            hp = null;
            hpCon = null;
        }
        catch(Exception e)
        {
            System.out.println("Exception occured in internet.java : " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }

        return output.toString();
    }

    // This function extracts out the website target URL from
    // the referer URL. It will get you the URL where we need to
    // post the request.
    // The input parametere will usually be a HTML page.
    // But it can be a ASP page also, it'll just find action
    // in the form and then get the URL and return it.
    public static String getWebsiteFromReferrer(String url)
    {
        String htmlCode = getWebsite(url);
        int indexOfAction = htmlCode.indexOf("action");
        int indexOfEqual = htmlCode.indexOf("=", indexOfAction);
        int indexOfSpace = htmlCode.indexOf(" ", indexOfEqual);
        // beginIndex is inclusive and endIndex is exclusive.
        String targetPage = htmlCode.substring(indexOfEqual+1, indexOfSpace);
        
        // If targetPage starts and ends with a double quote
        // remove the prefix and the suffix ""
        if(targetPage.startsWith("\""))
        {
            targetPage = targetPage.substring(1);
        }

        if(targetPage.endsWith("\""))
        {
            targetPage = targetPage.substring(0, targetPage.length()-1);
        }
        int indexOfLastSlash = url.lastIndexOf("/");
        return url.substring(0, indexOfLastSlash+1).concat(targetPage);
    }
    /*
     * postMail function -- You can send email using this function.
     * Arguments
     * username -- E-mail you want to send e-mail from
     * password -- Password of the mail supplied above
     * SMTP_HOST_NAME -- SMTP server of the email server (in case of gmail it is smtp.gmail.com)
     * recipientsTo - Set having the To Recepients
     * recipientsCC - Set having the CC Recipients (Pass null if you don't want to keep somebody in cc)
     * recipientsBCC - Set having the BCC Recipients (Pass null if you don't want to keep somebody in BCC)
     * subject - Subject of the mail
     * message - E-mail Body.
     * 
     * Points to be noted:
     * 1. You will not be able to send forged mail using this function
     *    Atleast I was not able to when I use gmail.
     * 2. Whereever you call this postMail function, following jars should 
     * be in your classpath at the time of execution.
     * The path on my machine is.
     * 1. C:\Programs\Java World\JavaBeansActivationFrameWork\jaf-1.1.1\activation.jar
     * 2. C:\Programs\Java World\javamail\javamail-1.4.1\mail.jar
     * 
     * To read more visit the following links
     * http://javakafunda.blogspot.com/2010/04/how-to-send-e-mails-using-java.html
     * JAVAMAIL : http://www.oracle.com/technetwork/java/javamail/index-138643.html
     */
    public static boolean postMail(
            final String username, 
            final String password, 
            final String SMTP_HOST_NAME, 
            Set<String> recipientsTo, 
            Set<String> recipientsCC, 
            Set<String> recipientsBCC, 
            String subject,
             String message) throws MessagingException
    {
        try
        {
             boolean debug = false;

             //Set the host smtp address
             Properties props = new Properties();
             props.put("mail.smtp.host", SMTP_HOST_NAME);
            // Some SMTP servers require a username and password authentication before you
            // can use their Server for Sending mail. This is most common with couple
            // of ISP's who provide SMTP Address to Send Mail.
            // We have used fully qualified name of javax.mail.Authenticator
            // don't mix it java.net.Authenticator.
             javax.mail.Authenticator auth = new javax.mail.Authenticator ()
             {
                @Override
                public javax.mail.PasswordAuthentication getPasswordAuthentication()
                {      
                       return new javax.mail.PasswordAuthentication(username, password);
                }  
             };         
             props.put("mail.smtp.auth", "true");
             props.put("mail.smtp.starttls.enable","true");
             Session session = Session.getDefaultInstance(props, auth);
             session.setDebug(debug);

            // create a message
            Message msg = new MimeMessage(session);
            msg.setFrom(null);
            //Read from the recipientsTo Set and put it in addressTo
            msg.setRecipients(Message.RecipientType.TO, convertToInternetAddresses(recipientsTo));
            msg.setRecipients(Message.RecipientType.CC, convertToInternetAddresses(recipientsCC));
            msg.setRecipients(Message.RecipientType.BCC, convertToInternetAddresses(recipientsCC));
            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // return false if some exception occured.
            return false;
        }
    }// ends postMail function
    
    // A private function that is used to convert 
    // Set of Strings to InternetAddress.
    private static InternetAddress[] convertToInternetAddresses(Set<String> addresses)
    {
        
        try
        {
            InternetAddress[] addressArr = new InternetAddress[addresses.size()];
            Iterator it = addresses.iterator();
            int i=0;
            if(addresses!=null)
            {
                System.out.println("Email "+ i + "=");
                it =  addresses.iterator();
                while(it.hasNext())
                {
                    String email = (String)it.next();
                    addressArr[i++]=new InternetAddress(email);
                    System.out.print(email+", ");
                }
                return addressArr;
            }  
            else
            {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public static boolean postMail(
            final String username, 
            final String password, 
            final String SMTP_HOST_NAME, 
            Set<String> recipientsTo, 
            String subject,
             String message) throws MessagingException
    {
        return postMail(username, password, SMTP_HOST_NAME, recipientsTo, null, null, subject, message);
    }  
    public static boolean postMail(
            final String username, 
            final String password, 
            final String SMTP_HOST_NAME, 
            String recipientsTo, 
            String subject,
             String message) throws MessagingException
    {
        Set<String> set = new HashSet<String>();
        set.add(recipientsTo);
        return postMail(username, password, SMTP_HOST_NAME, set, null, null, subject, message);
    }    
}

