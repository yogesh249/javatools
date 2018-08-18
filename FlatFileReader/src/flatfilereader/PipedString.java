/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flatfilereader;

import java.util.Iterator;

public class PipedString implements Iterable<String> {
    private String line;
    private int index = 0;
    @Override
    public String toString()
    {
        return line;
    }
    public PipedString(String x) {
        this.line = x;
    }
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>()
        {
            private String _currentElement;
            String[] symbols=null;
            @Override
            public boolean hasNext() {
                try {
                     if(symbols==null) 
                     {
                         symbols = line.split("\\|");
                     }
                    _currentElement = symbols[index];
                } catch (Exception ex) {
                    line = null;
                }

                return (index < symbols.length);
            }

            @Override
            public String next() {
                index++;
                return _currentElement;
            }

            @Override
            public void remove() {
            }
        };
    }
}
