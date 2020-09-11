package arboles;

import java.util.List;

public interface IArbolBusqueda<K extends Comparable<K>, V> {
    void insertar(K clave, V valor) throws ExcepcionClaveYaExiste;
    V eliminar(K clave) throws ExcepcionClaveNoExiste;
    V buscar(K clave);
    boolean contiene(K clave);
    int size();
    int altura();
    void vaciar();
    boolean esArbolVacio();
    int nivel();
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoEnPostOrden();
    List<K> recorridoPorNiveles();
}