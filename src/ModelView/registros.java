
package ModelView;

import java.util.ArrayList;

public class registros {
    
    ArrayList<String> Transacciones;
    
    public registros() {
        Transacciones = new ArrayList<>();
    }

    public void registrar(String Operacion) {
        this.Transacciones.add(Operacion);
    }

    public ArrayList<String> getTransacciones() {
        return Transacciones;
    }
    
    
    
}
