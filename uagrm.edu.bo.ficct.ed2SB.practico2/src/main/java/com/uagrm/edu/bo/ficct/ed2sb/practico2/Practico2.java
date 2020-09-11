package com.uagrm.edu.bo.ficct.ed2sb.practico2;

import arboles.ArbolB;
import arboles.ArbolMViasBusqueda;
import arboles.ExcepcionClaveNoExiste;
import arboles.ExcepcionClaveYaExiste;
import arboles.ExcepcionOrdenInvalido;
import arboles.IArbolBusqueda;

public class Practico2 {

    public static void main(String[] args) throws ExcepcionClaveYaExiste, ExcepcionOrdenInvalido, ExcepcionClaveNoExiste {
        IArbolBusqueda<Integer, String> arbol;
        //arbol = new ArbolMViasBusqueda<>();
        arbol = new ArbolB<>();
        arbol.insertar(54, "Christian");
        arbol.insertar(65, "Liz");
        arbol.insertar(5, "Julio");
        //*
        arbol.insertar(19, "Dilker");
        arbol.insertar(82, "Gabriel");
        arbol.insertar(18, "Luis");
        arbol.insertar(72, "Alejandro" );
        arbol.insertar(32, "Carlos");
        arbol.insertar(2, "Martha");
        arbol.insertar(30, "Belen");
        arbol.insertar(60, "Victor");
        //*/
        ((ArbolMViasBusqueda)arbol).mostrarArbol();
        System.out.println("---------------------------------");
        System.out.println("Cantidad Nodos con Datos Vacios: "
                +((ArbolMViasBusqueda)arbol).cantidadNodosConDatosVacios());
        System.out.println("Cantidad Nodos con Datos Vacios en nivel N: "
                +((ArbolMViasBusqueda)arbol).cantidadNodosConDatosVaciosEnNivelN(1));
        System.out.println("Cantidad Nodos con Datos Vacios en nivel N: "
                +((ArbolMViasBusqueda)arbol).cantidadNodosConDatosVaciosEnNivelNIterativo(1));        
        System.out.println("¿Solo hay nodos hojas en el Ultimo Nivel("+ arbol.nivel() + ")?: " 
                +((ArbolMViasBusqueda)arbol).soloHojasEnUltimoNviel());
        System.out.println("¿Arbol Balanceado?: " 
                +((ArbolMViasBusqueda)arbol).arbolBalanceado());
        System.out.println("Predecesor : " 
                +((ArbolMViasBusqueda)arbol).predecesor(54));
        System.out.println("Cantidad Nodos con todos sus Hijos distintos de vacio (No se toma en cuenta nodos Hojas) : " 
                +((ArbolMViasBusqueda)arbol).cantidadNodosConHijosNoVaciosExceptuandoNodosHojas());
        //arbol.eliminar(19);
        ((ArbolMViasBusqueda)arbol).mostrarArbol();
        System.out.println("---------------------------------");
    }
    
}
