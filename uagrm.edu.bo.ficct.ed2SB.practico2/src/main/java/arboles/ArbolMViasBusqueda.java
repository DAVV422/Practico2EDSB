package arboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolMViasBusqueda<K extends Comparable<K>, V>
        implements IArbolBusqueda<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;

    public ArbolMViasBusqueda() {
        this.orden = 3;
    }

    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido {
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
    }

    protected NodoMVias<K, V> nodoVacioParaElArbol() {
        return (NodoMVias<K, V>) NodoMVias.nodoVacio();
    }

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(orden, clave, valor);
            return;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeDatoEnNodo(nodoActual, clave)) {
                throw new ExcepcionClaveYaExiste();
            }
            if (nodoActual.esHoja()) {
                if (nodoActual.estanDatosLlenos()) {
                    int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(orden, clave, valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                } else {
                    this.insertarEnOrden(nodoActual, clave, valor);

                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(orden, clave, valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }
    }

    public void insertarEnOrden(NodoMVias<K, V> nodoActual, K clave, V valor) {
        K claveActual;
        V valorActual;
        for (int i = nodoActual.cantidadDeDatosNoVacios() - 1; i > 0; i--) {
            claveActual = nodoActual.getClave(i);
            valorActual = nodoActual.getValor(i);
            if (clave.compareTo(claveActual) < 0) {
                nodoActual.setClave(i + 1, claveActual);
                nodoActual.setValor(i + 1, valorActual);
            } else {
                nodoActual.setClave(i + 1, clave);
                nodoActual.setValor(i + 1, valor);
                return;
            }
        }
        claveActual = nodoActual.getClave(0);
        valorActual = nodoActual.getValor(0);
        if (clave.compareTo(claveActual) < 0) {
            nodoActual.setClave(1, claveActual);
            nodoActual.setValor(1, valorActual);
            nodoActual.setClave(0, clave);
            nodoActual.setValor(0, valor);
        } else {
            nodoActual.setClave(1, clave);
            nodoActual.setValor(1, valor);
        }
    }

    protected int porDondeBajar(NodoMVias<K, V> nodoActual, K clave) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (clave.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return nodoActual.cantidadDeDatosNoVacios();
    }

    protected boolean existeDatoEnNodo(NodoMVias<K, V> nodoActual, K clave)
            throws ExcepcionClaveYaExiste {
        for (int i = 0; i < orden - 1; i++) {
            if (!nodoActual.esDatoVacio(i)) {
                K claveActual = nodoActual.getClave(i);
                if (claveActual.compareTo(clave) == 0) {
                    throw new ExcepcionClaveYaExiste();
                }
            }
        }
        return false;
    }

    @Override
    public V eliminar(K clave) throws ExcepcionClaveNoExiste {
        V valorARetornar = buscar(clave);
        if (valorARetornar == null) {
            throw new ExcepcionClaveNoExiste();
        }
        this.raiz = eliminar(this.raiz, clave);
        return valorARetornar;
    }

    @SuppressWarnings("InfiniteRecursion")
    private NodoMVias eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                if (nodoActual.esHoja()) {
                    this.eliminarDatoDeNodo(nodoActual, i);
                    if (nodoActual.estanDatosVacios()) {
                        return NodoMVias.nodoVacio();
                    } else {
                        return nodoActual;
                    }
                }
                K claveDeReemplazo;
                if (this.hayMasHijosAdelante(nodoActual, i)) {
                    claveDeReemplazo = this.sucesorInOrden(nodoActual, claveAEliminar);
                } else {
                    claveDeReemplazo = this.predecesorInOrden(nodoActual, claveAEliminar);
                }
                V valorDeReemplazo = this.buscar(claveDeReemplazo);
                eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;
            }
            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(orden - 1), claveAEliminar);
        nodoActual.setHijo(orden - 1, supuestoNuevoHijo);
        return nodoActual;
    }

    private boolean hayMasHijosAdelante(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion + 1; i < orden; i++) {
            if (!nodoActual.esHijoVacio(i)) {
                return true;
            }
        }
        return false;
    }

    public int posicionDeClave(NodoMVias<K, V> nodoActual, K clave) {
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveActual.compareTo(clave) == 0) {
                return i;
            }
        }
        return -1;
    }

    private K predecesorInOrden(NodoMVias<K, V> nodoActual, K clave) {
        int posicion = this.posicionDeClave(nodoActual, clave);
        if (!nodoActual.esHijoVacio(posicion)) {
            nodoActual = nodoActual.getHijo(posicion);
            while (!nodoActual.esHijoVacio(nodoActual.cantidadDeDatosNoVacios())) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
            }
        } else {
            return nodoActual.getClave(posicion - 1);
        }
        return nodoActual.getClave(nodoActual.cantidadDeDatosNoVacios() - 1);
    }

    private K sucesorInOrden(NodoMVias<K, V> nodoActual, K clave) {
        int posicion = this.posicionDeClave(nodoActual, clave);
        if (!nodoActual.esHijoVacio(posicion + 1)) {
            nodoActual = nodoActual.getHijo(posicion + 1);
            while (!nodoActual.esHijoVacio(0)) {
                nodoActual = nodoActual.getHijo(0);
            }
        } else {
            return nodoActual.getClave(posicion + 1);
        }
        return nodoActual.getClave(0);
    }

    public void eliminarDatoDeNodo(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion; i < orden - 2; i++) {
            nodoActual.setClave(i, nodoActual.getClave(i + 1));
            nodoActual.setValor(i, nodoActual.getValor(i + 1));
            nodoActual.setClave(i + 1, (K) NodoMVias.datoVacio());
            nodoActual.setValor(i + 1, (V) NodoMVias.datoVacio());
        }
        if (posicion == orden - 1) {
            nodoActual.setClave(orden - 1, (K) NodoMVias.datoVacio());
            nodoActual.setValor(orden - 1, (V) NodoMVias.datoVacio());
        }
    }

    @Override
    public V buscar(K claveABuscar) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            NodoMVias<K, V> nodoAnterior = nodoActual;
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios()
                    && nodoActual == nodoAnterior; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (nodoActual.esHijoVacio(i)) {
                        return (V) NodoMVias.datoVacio();
                    }
                    nodoActual = nodoActual.getHijo(i);
                }
            }
            if (nodoActual == nodoAnterior) {
                nodoActual = nodoActual.getHijo(orden - 1);
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != NodoMVias.datoVacio();
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + size(nodoActual.getHijo(i));
        }
        cantidad = cantidad + 1;
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if (alturaDeHijo > alturaMayor) {
                alturaMayor = alturaDeHijo;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = nodoVacioParaElArbol();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public int nivel() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int nivelDelArbol = -1;
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int nodosDelNivel = colaDeNodos.size();
            nivelDelArbol = nivelDelArbol + 1;
            while (nodosDelNivel > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
                nodosDelNivel = nodosDelNivel - 1;
            }
        }
        return nivelDelArbol;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoInOrden(NodoMVias<K, V> nodoActual,
            List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorridoInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoInOrden(nodoActual.getHijo(orden - 1), recorrido);
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPreOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoPreOrden(NodoMVias<K, V> nodoActual,
            List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoPreOrden(nodoActual.getHijo(i), recorrido);
        }
        recorridoPreOrden(nodoActual.getHijo(orden - 1), recorrido);
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoPostOrden(NodoMVias<K, V> nodoActual,
            List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            recorridoPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
            if (!nodoActual.esHijoVacio(orden - 1)) {
                colaDeNodos.offer(nodoActual.getHijo(orden - 1));
            }
        }
        return recorrido;
    }

    public int hojasAPartirDeNivel(int nivelObjetivo) {
        return hojasAPartirDeNivel(this.raiz, nivelObjetivo, 0);
    }

    private int hojasAPartirDeNivel(NodoMVias<K, V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelActual >= nivelObjetivo) {
            if (nodoActual.esHoja()) {
                return 1;
            }
        }
        int cantidadDeHojas = 0;
        for (int i = 0; i < orden; i++) {
            cantidadDeHojas = cantidadDeHojas
                    + this.hojasAPartirDeNivel(nodoActual.getHijo(i), nivelObjetivo, nivelActual + 1);
        }
        return cantidadDeHojas;
    }

    public void mostrarArbol() {
        String linea = "";
        if (!NodoMVias.esNodoVacio(this.raiz)) {
            imprimirLineas(linea, this.raiz);
        } else {
            System.out.println(linea + "Arbol Vacio");
        }
    }

    private void imprimirLineas(String linea, NodoMVias<K, V> nodoActual) {
        List<K> listaDeClaves = new LinkedList();
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            añadirClaves(listaDeClaves, nodoActual);
            System.out.println(linea + "--" + listaDeClaves);
            String lineaHijo = linea + " |";
            for (int i = 0; i < this.orden - 1; i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    imprimirLineas(lineaHijo, nodoActual.getHijo(i));
                } else {
                    System.out.println(lineaHijo + "--|");
                }
            }
            String ultimaLinea = linea + " ±";
            imprimirLineas(ultimaLinea, nodoActual.getHijo(this.orden - 1));
        } else {
            System.out.println(linea + "--|");
        }
    }

    private void añadirClaves(List<K> listaDeClaves, NodoMVias<K, V> nodoActual) {
        for (int i = 0; i < this.orden - 1; i++) {
            listaDeClaves.add(nodoActual.getClave(i));
        }
    }

    //---------------------- Practico 2 ---------------------
    //1.Implemente el métodoinsertar de unárbolm-vias de búsqueda
    //Comienza en linea de codigo nro: 29.
    //2.Implemente el métodoeliminar de un árbolm-vias de búsqueda
    //Comienza en linea de codigo nro: 115.
    //3.Implemente el métodoinsertar del árbolb
    // Implementado en arbol B.
    //4.Implemente el métodoeliminar delárbolb
    // Implementado en arbol B.
    //5.Implemente  un  método  recursivo  que  retorne  la  cantidad  nodos 
    //con  datos vacíos en  un árbol B
    //La implementacion viene a ser la misma para el ejercicio Nro. 6
    //Ya que funcionaria del mismo modo para ambos arboles.
    //6.Implemente un  método  recursivo  que  retorne  la  cantidad  nodos 
    //con  datos vacíos en  unárbolm-vias de búsqueda
    public int cantidadNodosConDatosVacios() {
        return cantidadNodosConDatosVacios(this.raiz);
    }

    private int cantidadNodosConDatosVacios(NodoMVias<K, V> nodoActual) {
        int cantidadNodosConDatosVacios = 0;
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        } else {
            int cantidadDeNodoConDatosVaciosDeHijos = 0;
            for (int i = 0; i < orden; i++) {
                cantidadDeNodoConDatosVaciosDeHijos = cantidadDeNodoConDatosVaciosDeHijos
                        + cantidadNodosConDatosVacios(nodoActual.getHijo(i));
            }
            cantidadNodosConDatosVacios = cantidadDeNodoConDatosVaciosDeHijos;
            if (nodoActual.cantidadDeDatosNoVacios() != orden - 1) {
                cantidadNodosConDatosVacios = cantidadNodosConDatosVacios + 1;
            }
        }
        return cantidadNodosConDatosVacios;
    }

    //7.Implemente un  método  recursivo  que  retorne  la  cantidad  nodos
    //con  datos vacíos en  unárbolB, pero solo en el nivel N
    public int cantidadNodosConDatosVaciosEnNivelN(int nivel) {
        return cantidadNodosConDatosVaciosEnNivelN(this.raiz, nivel, 0);
    }

    private int cantidadNodosConDatosVaciosEnNivelN(NodoMVias<K, V> nodoActual, int nivelN, int nivelActual) {
        int cantidadNodosConDatosVacios;
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        } else {
            int cantidadDeNodoConDatosVaciosDeHijos = 0;
            for (int i = 0; i < orden; i++) {
                cantidadDeNodoConDatosVaciosDeHijos = cantidadDeNodoConDatosVaciosDeHijos
                        + cantidadNodosConDatosVaciosEnNivelN(nodoActual.getHijo(i), nivelN, nivelActual + 1);
            }
            cantidadNodosConDatosVacios = cantidadDeNodoConDatosVaciosDeHijos;
            if ((nodoActual.cantidadDeDatosNoVacios() != orden - 1) && (nivelN == nivelActual)) {
                cantidadNodosConDatosVacios = cantidadNodosConDatosVacios + 1;
            }
        }
        return cantidadNodosConDatosVacios;
    }

    //8.Implemente un método iterativo que retorne la cantidad nodos 
    //con datos vacíos en un árbolb, pero solo en el nivel N
    // Implementado en arbol B.
    public int cantidadNodosConDatosVaciosEnNivelNIterativo(int nivelN) {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidadNodosConDatosVacios = 0;
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        int nivelActual = -1;
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodos = colaDeNodos.size();
            nivelActual = nivelActual + 1;
            while (cantidadDeNodos > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                cantidadDeNodos = cantidadDeNodos - 1;
                if ((nodoActual.cantidadDeDatosNoVacios() != orden - 1) && (nivelN == nivelActual)) {
                    cantidadNodosConDatosVacios = cantidadNodosConDatosVacios + 1;
                }
                for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
            }
        }
        return cantidadNodosConDatosVacios;
    }
    //9.Implemente un  método que  retorne  verdadero  si  solo  hay  hojas 
    //en  el último nivel  de un árbolm-vias de búsqueda.Falso en caso contrario.

    public boolean soloHojasEnUltimoNviel() {
        if (this.esArbolVacio()) {
            return false;
        }
        int cantidadNodosConDatosVacios = 0;
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        int nivelActual = -1;
        while (!colaDeNodos.isEmpty()) {
            int cantidadDeNodos = colaDeNodos.size();
            nivelActual = nivelActual + 1;
            while (cantidadDeNodos > 0) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                cantidadDeNodos = cantidadDeNodos - 1;
                if ((nodoActual.esHoja()) && (this.nivel() != nivelActual)) {
                    return false;
                }
                for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                if (!nodoActual.esHijoVacio(orden - 1)) {
                    colaDeNodos.offer(nodoActual.getHijo(orden - 1));
                }
            }
        }
        return true;
    }

    //10.Implemente un método que retorne verdadero si un árbolm-vias 
    //esta balanceado segúnlas reglas de un árbolB. Falso en caso contrario.
    public boolean arbolBalanceado() {
        return arbolBalanceado(this.raiz);
    }

    private boolean arbolBalanceado(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return true;
        }
        boolean arbolBalanceado = true;
        int minimoDeDatos = (this.orden - 1) / 2;
        int minimoDeHijos = minimoDeDatos + 1;
        if (nodoActual.cantidadDeDatosNoVacios() < minimoDeDatos
                && this.raiz != nodoActual) {
            arbolBalanceado = false;
        }
        if (!nodoActual.esHoja() && nodoActual.cantidadDeHijosNoVacios() < minimoDeHijos
                && this.raiz != nodoActual) {
            arbolBalanceado = false;
        }
        for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios()
                && arbolBalanceado == true; i++) {
            arbolBalanceado = arbolBalanceado(nodoActual.getHijo(i));
        }
        if (arbolBalanceado == true) {
            arbolBalanceado(nodoActual.getHijo(orden - 1));
        }
        return arbolBalanceado;
    }

    //11.Implemente un método privado que reciba un dato como parámetro
    //y que retorne cual seria el predecesor inorden de dicho dato, 
    //sin realizar el recorrido en inOrden.
    public K predecesor(K clave) throws ExcepcionClaveNoExiste {
        return predecesorDeClave(clave);
    }

    private K predecesorDeClave(K clave) throws ExcepcionClaveNoExiste {
        NodoMVias<K, V> nodoActual = this.buscarNodo(clave);
        int posicion = this.posicionDeClave(nodoActual, clave);
        if (nodoActual.esHijoVacio(posicion)) {
            throw new ExcepcionClaveNoExiste();
        } else {
            nodoActual = nodoActual.getHijo(posicion);
        }
        while (!nodoActual.esHijoVacio(nodoActual.cantidadDeDatosNoVacios())) {
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
        }
        return nodoActual.getClave(nodoActual.cantidadDeDatosNoVacios() - 1);
    }

    public NodoMVias<K, V> buscarNodo(K claveABuscar) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            NodoMVias<K, V> nodoAnterior = nodoActual;
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios()
                    && nodoActual == nodoAnterior; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual;
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (nodoActual.esHijoVacio(i)) {
                        return (NodoMVias<K, V>) NodoMVias.datoVacio();
                    }
                    nodoActual = nodoActual.getHijo(i);
                }
            }
            if (nodoActual == nodoAnterior) {
                nodoActual = nodoActual.getHijo(orden - 1);
            }
        }
        return (NodoMVias<K, V>) NodoMVias.datoVacio();
    }

    //12.Para un árbol m-vias implemente un método que retorne
    //la cantidad de nodos que tienen todos sus hijos distintos de vacío
    //luego del nivel N(La excepción a la regla son las hojas).
    public int cantidadNodosConHijosNoVaciosExceptuandoNodosHojas() {
        return cantidadNodosConHijosNoVaciosExceptuandoNodosHojas(this.raiz);
    }

    private int cantidadNodosConHijosNoVaciosExceptuandoNodosHojas(NodoMVias<K, V> nodoActual) {
        int cantidadNodosConDatosVacios = 0;
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        } else {
            int cantidadDeNodoConDatosVaciosDeHijos = 0;
            for (int i = 0; i < orden; i++) {
                cantidadDeNodoConDatosVaciosDeHijos = cantidadDeNodoConDatosVaciosDeHijos
                        + cantidadNodosConHijosNoVaciosExceptuandoNodosHojas(nodoActual.getHijo(i));
            }
            cantidadNodosConDatosVacios = cantidadDeNodoConDatosVaciosDeHijos;
            if (nodoActual.cantidadDeHijosNoVacios() == orden && !nodoActual.esHoja()) {
                cantidadNodosConDatosVacios = cantidadNodosConDatosVacios + 1;
            }
        }
        return cantidadNodosConDatosVacios;
    }

}
