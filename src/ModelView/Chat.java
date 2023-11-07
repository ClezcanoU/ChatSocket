
package ModelView;

import java.io.*;
import java.net.*;

public interface Chat {
    void listen() throws IOException;
    void send(String message) throws IOException;
    void sendFile(File file) throws IOException;
}


