package CommonFiles;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class SourceFilter extends FilterFramework {

    private String fileName;
    private final static Frame END_FRAME = new Frame() {
        @Override
        public long getTime() {
            return Long.MAX_VALUE;
        }

        @Override
        public boolean isEndFrame() {
            return true;
        }
    };

    public SourceFilter(String fileName) {
        this.fileName = fileName;
    }

    public void run() {

        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName));) {
            System.out.println("\n" + this.getName() + "::Source reading file...");

            Frame frame = new Frame();
            int id = in.readInt();
            frame.setTime(in.readLong());
            while (in.available() > 0) {
                id = in.readInt();
                if (id == 0) {
                    writeNextFilterOutputPort(frame);
                    frame = new Frame();
                    frame.setTime(in.readLong());
                } else {
                    frame.put(id, in.readDouble());
                }
            }
            writeNextFilterOutputPort(frame);
        } catch (IOException iox) {

            /***********************************************************************************
             * The following exception is raised should we have a problem opening the file.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        }
        writeNextFilterOutputPort(END_FRAME);
        System.out.print("\n \n File is read completely");
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}