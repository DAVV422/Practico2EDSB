package arboles;

public class ExcepcionClaveNoExiste extends Exception {
    public ExcepcionClaveNoExiste() {
        super("La clave no existe en el arbol");
    }
    
    public ExcepcionClaveNoExiste(String mensaje) {
        super(mensaje);
    }    
}
