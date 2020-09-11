package arboles;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ArbolB<K extends Comparable<K>, V> extends ArbolMViasBusqueda<K, V> {

    private int nroMaximoDeDatos;
    private int nroMinimoDeDatos;
    private int nroMinimoDeHijos;

    public ArbolB() {
        super();
        this.nroMaximoDeDatos = 2;
        this.nroMinimoDeDatos = 1;
        this.nroMinimoDeHijos = 2;
    }

    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDeDatos = super.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos = this.nroMinimoDeDatos + 1;
    }

    @Override
    public void insertar(K clave, V valor) throws ExcepcionClaveYaExiste {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(orden, clave, valor);
            return;
        }
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (this.existeDatoEnNodo(nodoActual, clave)) {
                throw new ExcepcionClaveYaExiste();
            }
            if (nodoActual.esHoja()) {
                super.insertarEnOrden(nodoActual, clave, valor);
                if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaximoDeDatos) {
                    this.dividir(nodoActual, pilaDeAncestros);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual, clave);
                pilaDeAncestros.push(nodoActual);
                nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
            }
        }
    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        if (pilaDeAncestros.isEmpty()) {
            NodoMVias<K, V> nuevoNodoRaiz = new NodoMVias<>(orden);
            NodoMVias<K, V> nuevoHijoDeSgtePosicion = new NodoMVias<>(orden);
            NodoMVias<K, V> nuevoNodoActual = new NodoMVias<>(orden);
            this.dividirNodo(nodoActual, nuevoNodoActual, nuevoNodoRaiz, nuevoHijoDeSgtePosicion, pilaDeAncestros);
            this.raiz = nuevoNodoRaiz;
            nuevoNodoRaiz.setHijo(0, nuevoNodoActual);
            nuevoNodoRaiz.setHijo(1, nuevoHijoDeSgtePosicion);
        } else {
            NodoMVias<K, V> nodoPadre = pilaDeAncestros.peek();
            NodoMVias<K, V> nuevoHijoDeSgtePosicion = new NodoMVias<>(orden);
            NodoMVias<K, V> nuevoNodoActual = new NodoMVias<>(orden);
            this.dividirNodo(nodoActual, nuevoNodoActual, nodoPadre, nuevoHijoDeSgtePosicion, pilaDeAncestros);
            pilaDeAncestros.pop();
            nodoActual = nodoPadre;
            while (!pilaDeAncestros.isEmpty()) {
                if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaximoDeDatos) {
                    nodoPadre = pilaDeAncestros.pop();
                    nuevoHijoDeSgtePosicion = new NodoMVias<>(orden);
                    this.dividirNodo(nodoActual, nuevoNodoActual, nodoPadre, nuevoHijoDeSgtePosicion, pilaDeAncestros);
                    nodoActual = nodoPadre;
                } else {
                    nodoActual = pilaDeAncestros.pop();
                }
            }
            if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaximoDeDatos) {
                nodoPadre = new NodoMVias<>(orden);
                nuevoHijoDeSgtePosicion = new NodoMVias<>(orden);
                nuevoNodoActual = new NodoMVias<>(orden);
                this.dividirNodo(nodoActual, nuevoNodoActual, nodoPadre, nuevoHijoDeSgtePosicion, pilaDeAncestros);
                this.raiz = nodoPadre;
                nodoPadre.setHijo(0, nodoActual);
                nodoPadre.setHijo(1, nuevoHijoDeSgtePosicion);
            }
        }
    }

    private void dividirNodo(NodoMVias<K, V> nodoActual, NodoMVias<K, V> nuevoNodoActual, NodoMVias<K, V> nodoPadre,
            NodoMVias<K, V> nodoHijoSgtePosicion, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        int posicionDatoParaNodoPadre = nodoActual.cantidadDeDatosNoVacios() / 2;
        K claveNodoPadre = nodoActual.getClave(posicionDatoParaNodoPadre);
        V valorNodoPadre = nodoActual.getValor(posicionDatoParaNodoPadre);
        if (pilaDeAncestros.isEmpty()) {
            nodoPadre.setClave(0, claveNodoPadre);
            nodoPadre.setValor(0, valorNodoPadre);
        } else {
            this.insertarEnOrden(nodoPadre, claveNodoPadre, valorNodoPadre);
        }
        int posicion = 0;
        K claveParaNodo;
        V valorParaNodo;
        for (int i = 0; i < posicionDatoParaNodoPadre; i++) {
            claveParaNodo = nodoActual.getClave(i);
            valorParaNodo = nodoActual.getValor(i);
            nuevoNodoActual.setClave(i, claveParaNodo);
            nuevoNodoActual.setValor(i, valorParaNodo);
        }
        for (int i = posicionDatoParaNodoPadre + 1; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
            claveParaNodo = nodoActual.getClave(i);
            valorParaNodo = nodoActual.getValor(i);
            nodoHijoSgtePosicion.setClave(posicion, claveParaNodo);
            nodoHijoSgtePosicion.setValor(posicion, valorParaNodo);
            posicion = posicion + 1;
        }
        if (pilaDeAncestros.isEmpty()) {
            nodoPadre.setHijo(0, nuevoNodoActual);
            nodoPadre.setHijo(1, nodoHijoSgtePosicion);
        } else {
            posicion = super.posicionDeClave(nodoPadre, claveNodoPadre);
            nodoPadre.setHijo(posicion, nuevoNodoActual);
            if (nodoPadre.esHijoVacio(posicion + 1)) {
                nodoPadre.setHijo(posicion + 1, nodoHijoSgtePosicion);
            } else {
                nodoPadre.setHijo(posicion + 2, nodoPadre.getHijo(posicion + 1));
                nodoPadre.setHijo(posicion + 1, nodoHijoSgtePosicion);
            }
        }
        posicion = 0;
        if (nodoActual.cantidadDeHijosNoVacios() > 0) {
            for (int i = 0; i < nodoActual.cantidadDeDatosNoVacios(); i++) {
                if (!nodoActual.esHijoVacio(i)) {
                    if (i <= posicionDatoParaNodoPadre) {
                        nuevoNodoActual.setHijo(i, nodoActual.getHijo(i));
                    } else {
                        nodoHijoSgtePosicion.setHijo(posicion, nodoActual.getHijo(i));
                        posicion = posicion + 1;
                    }
                }
            }
            nodoHijoSgtePosicion.setHijo(nodoHijoSgtePosicion.cantidadDeDatosNoVacios(),
                    nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios()));
        }
    }

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDeAncestros);
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste();
        }
        int posicionDeLaClaveEnElNodo = this.porDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeLaClaveEnElNodo);
        if (nodoActual.esHoja()) {
            super.eliminarDatoDeNodo(nodoActual, posicionDeLaClaveEnElNodo);
            if (nodoActual.cantidadDeDatosNoVacios() < this.nroMinimoDeDatos) {
                if (pilaDeAncestros.isEmpty()) {
                    if (nodoActual.estanDatosVacios()) {
                        super.vaciar();
                    }
                } else {
                    this.prestarOFusionar(nodoActual, pilaDeAncestros);
                }
            }
        } else {
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K, V> nodoDelPredecesor = this.buscarNodoDelPredecesor(pilaDeAncestros,
                    nodoActual.getHijo(posicionDeLaClaveEnElNodo));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeDatosNoVacios() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarDatoDeNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeLaClaveEnElNodo, clavePredecesora);
            nodoActual.setValor(posicionDeLaClaveEnElNodo, valorPredecesor);
            if (nodoDelPredecesor.cantidadDeDatosNoVacios() < this.nroMinimoDeDatos) {
                this.prestarOFusionar(nodoDelPredecesor, pilaDeAncestros);
            }
        }
        return valorARetornar;
    }

    private NodoMVias<K, V> buscarNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> nodoActual) {
        while (!nodoActual.esHijoVacio(nodoActual.cantidadDeDatosNoVacios())) {
            pilaDeAncestros.push(nodoActual);
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
        }
        return nodoActual;
    }

    private void prestarOFusionar(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        if (nodoActual.esHoja()) {
            int posicionPrestamo = puedoPrestarAdelante(pilaDeAncestros) + 1;
            if (posicionPrestamo != -1) {
                NodoMVias<K, V> nodoPadre = pilaDeAncestros.pop();
                int i = 0;
                while (nodoPadre.getHijo(i).cantidadDeDatosNoVacios() > this.nroMinimoDeDatos) {

                }
            }
        }
    }

    private int puedoPrestarAdelante(Stack<NodoMVias<K, V>> pilaDeAncestros) {
        NodoMVias<K, V> nodoPadre = pilaDeAncestros.peek();
        for (int i = 0; i < nodoPadre.cantidadDeDatosNoVacios(); i++) {
            if (nodoPadre.getHijo(i).cantidadDeDatosNoVacios() < this.nroMinimoDeDatos) {
                if (nodoPadre.getHijo(i + 1).cantidadDeDatosNoVacios() - 1 < this.nroMinimoDeDatos) {
                    return i;
                }
            }
        }
        return -1;
    }

    private NodoMVias<K, V> buscarNodoDeLaClave(K claveABuscar, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int tamanhioNodoActual = nodoActual.cantidadDeDatosNoVacios();
            NodoMVias<K, V> nodoAnterior = nodoActual;
            for (int i = 0; i < tamanhioNodoActual && nodoActual == nodoAnterior; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual;
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (!nodoActual.esHoja()) {
                        pilaDeAncestros.push(nodoActual);
                        nodoActual.getHijo(i);
                    } else {
                        nodoActual = NodoMVias.nodoVacio();
                    }
                }
            }
            if (nodoActual == nodoAnterior) {
                if (!nodoActual.esHoja()) {
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(tamanhioNodoActual);
                } else {
                    nodoActual = NodoMVias.nodoVacio();
                }
            }
        }
        return NodoMVias.nodoVacio();
    }

    //---------------------- Ejercicio 8 -----------------------
    @Override
    public int cantidadNodosConDatosVacios() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidadNodosConDatosVacios = 0;
        Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaDeNodos.poll();
            if (nodoActual.cantidadDeDatosNoVacios() != orden - 1) {
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
        return cantidadNodosConDatosVacios;
    }
}
