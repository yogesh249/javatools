package util;

import java.util.*;
import java.io.*;

public class CSVReader implements Iterable<HashMap<String, String>> {

    private BufferedReader _reader;
    private String[] headers;
    public CSVReader(String filePath) throws Exception {
        // Initialize the _reader
        _reader = new BufferedReader(new FileReader(filePath));
        // Initialize the headers.
        String headerLine = _reader.readLine();
        headers = headerLine.split(",");
        if(headers==null) throw new Exception("No headers present in the file");
    }

    public void Close() {
        try {
            _reader.close();
        } catch (Exception ex) {
        }
    }

    @Override
    public Iterator<HashMap<String, String>> iterator() {
        return new CSVIterator();
    }

    private class CSVIterator implements Iterator<HashMap<String, String>> {
        private HashMap<String, String> _currentMap;
        @Override
        public boolean hasNext() {
            try {
                // Read the next line from the CSV.
                String currentLineString = _reader.readLine();
                if(currentLineString!=null)
                {
                    // Populate _currentMap with the values.
                    String[] elements=currentLineString.split(",");
                    _currentMap = new HashMap<String, String>();
                    for(int h=0; h<headers.length; h++)
                    {
                        String key=headers[h];
                        String value = elements[h];
                        _currentMap.put(key, value);
                    }
                }
                else
                {
                    return false;
                }
            } catch (Exception ex) {
                _currentMap = null;
                ex.printStackTrace();
            }

            return _currentMap != null;
        }
        @Override
        public void remove() {
        }

        @Override
        public HashMap<String, String> next() {
            return _currentMap;
        }
    }
}