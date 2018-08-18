package flatfilereader;
import java.util.*;
import java.io.*;
  
public class FlatFileReader implements Iterable<PipedString>
{
    private BufferedReader _reader;
  
    public FlatFileReader(String filePath) throws Exception
    {
        _reader = new BufferedReader(new FileReader(filePath));
    }
  
    public void Close()
    {
        try
        {
            _reader.close();
        }
        catch (Exception ex) {}
    }
  
    public Iterator<PipedString> iterator()
    {
        return new PipedStringIterator();
    }
    private class PipedStringIterator implements Iterator<PipedString>
    {
        private PipedString _currentLine;
  
        public boolean hasNext()
        {
            try
            {
                String cl = _reader.readLine();
                if(cl==null) return false;
                _currentLine = new PipedString(cl);
            }
            catch (Exception ex)
            {
                _currentLine = null;
            }
  
            return _currentLine != null;
        }
  
        @Override
        public PipedString next()
        {
            return _currentLine;
        }
  
        public void remove()
        {
        }
    }
}