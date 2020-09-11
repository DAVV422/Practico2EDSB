package arboles;

public class ExcepcionOrdenInvalido extends Exception {
    public ExcepcionOrdenInvalido() {
        super("El orden de su arbol debe ser mayor o igual a 3");
    }
    
    public ExcepcionOrdenInvalido(String mensaje) {
        super(mensaje);
    }    
    
}
