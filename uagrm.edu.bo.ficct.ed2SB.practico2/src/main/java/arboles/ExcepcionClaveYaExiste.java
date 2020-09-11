package arboles;

public class ExcepcionClaveYaExiste extends Exception {
    public ExcepcionClaveYaExiste() {
        super("La clave ya existe en el arbol");
    }
    
    public ExcepcionClaveYaExiste(String mensaje) {
        super(mensaje);
    }    
}
