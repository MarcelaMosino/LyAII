
package pila;

public class Nodo<T> {
    public Nodo<T> siguiente;
    public T contenido;
    
    public Nodo(T elemento){
        contenido = elemento;
        siguiente = null;
    }
}
