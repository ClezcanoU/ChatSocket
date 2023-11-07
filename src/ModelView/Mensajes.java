
package ModelView;

import View.*;


public class Mensajes {

    public static registros registro = new registros();
    static ChatView chat = new ChatView();
    static HistorialMensajes historial = new HistorialMensajes();
    
    public static void main(String[] args) {
        
        chat.setVisible(true);
    }

    public static void guardar(String operacion) {
        registro.registrar(operacion);
  
    }

    public static void MostrarHistorial() {
        historial.setVisible(true);
    }
}
