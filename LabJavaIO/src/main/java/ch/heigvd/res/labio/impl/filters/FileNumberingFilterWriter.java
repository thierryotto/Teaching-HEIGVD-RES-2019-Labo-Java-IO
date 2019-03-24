package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  private int nbLine = 1;
  private boolean newLine = true;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    for (int index = off; index < (off + len); index++)
      this.write(str.charAt(index));
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (int index = off; index < (off + len); index++)
      this.write(cbuf[index]);
  }

  @Override
  public void write(int c) throws IOException {

    // New line
    if (c == '\n') {
      super.write(c);
      writeLineNbr();
      newLine = false;

    } else if (c == '\r') { // Carriage return
      newLine = true;
      super.write(c);

    } else { // Not an escape char
      if (newLine) {
        writeLineNbr();
        newLine = false;
      }

      super.write(c);
    }
  }

  private void writeLineNbr() throws IOException {

    int len = String.valueOf(nbLine).length();
    super.write(Integer.toString(nbLine++), 0, len);
    super.write('\t');
  }

}
