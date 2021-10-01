
package analisislexico;

import java.util.Iterator;
import pila.Nodo;


public class Lista<T>{
    public Nodo<T> primero;
    Nodo<T> ultimo;
    String cabecera;
    int size=0;
    
    public Lista(String cabecera){
        //Crea lista con cabecera para impresión
        this.cabecera = cabecera; 
        primero = ultimo = null;
        size=1;
    }
    
    public void add(T elemento){
        Nodo<T> nuevo = new Nodo(elemento);
        if(estaVacia()){
            primero = nuevo;
            ultimo = nuevo;
        }
        else{
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }size++;      
    }
    
    public void addI(T elemento){
        Nodo<T> nuevo = new Nodo(elemento);
        if(!isEmpty())            
            nuevo.siguiente = primero;
        else
            ultimo = nuevo;
        primero = nuevo ;
        size++;
    }
    
    public boolean estaVacia(){
        return primero==null;
    }
    
    public void vaciar(){
        primero=ultimo=null;
        size=0;
    }
    
    public boolean isEmpty(){
        return primero==null;
    }
    
    public int size(){
        return size;
    }
    
    //Devuelve el contenido de la lista
    @Override
    public String toString(){
        String cadena = cabecera+"\n";
        Nodo<T> auxiliar = primero;
        if(isEmpty())
            return "Lista vacía";
        while(auxiliar.siguiente!=null){
            cadena += auxiliar.contenido.toString()+"\n";
            auxiliar = auxiliar.siguiente;
        } return cadena+auxiliar.contenido+"\n";
    }
    
    public String aRenglon(){
        String cadena="";
        Nodo<T> auxiliar = primero;
        if(isEmpty())
            return "Lista vacía";
        while(auxiliar.siguiente!=null){
            cadena += auxiliar.contenido.toString()+", ";
            auxiliar = auxiliar.siguiente;
        } return cadena+auxiliar.contenido;
    }
}
