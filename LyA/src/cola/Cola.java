
package cola;

import pila.Nodo;

public class Cola<T> implements Encolable<T>{
    Nodo<T> primero;
    Nodo<T> ultimo;
    int tam=0;
    
    public Cola (){
        primero = null;
        ultimo=null;
    }
    
    @Override
    public T dequeue() {
        if(!isEmpty()){
            T valor = primero.contenido;
            primero = primero.siguiente;
            return valor;            
        }else
            return null;
    }

    @Override
    public void enqueue(T elemento) {
        Nodo<T> nuevo = new Nodo(elemento);
        if(isEmpty()){
            primero = nuevo;
            ultimo = nuevo;
        }
        else{
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }   tam++;     
    }
    
    public void vaciar(){
        while(!isEmpty())
            dequeue();
    }
    
    public String toString(){
        return "Tamano nodo ="+tam;
    }

    @Override
    public boolean isEmpty() {
        return primero==null;
    }    
}
