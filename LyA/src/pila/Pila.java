
package pila;

public class Pila<T> implements Apilable<T>, Cloneable{
    Nodo<T> tope;

    @Override
    public T pop() {
        if(isEmpty())
            return null;
        T top = tope.contenido;
        tope = tope.siguiente;
        return top;
    }

    @Override
    public T peek() {
        if(isEmpty())
            return null;
        return tope.contenido;
    }

    @Override
    public void push(T elemento) {
        Nodo<T> nuevo = new Nodo(elemento);
        if(!isEmpty())            
            nuevo.siguiente = tope;
        tope = nuevo;
    }

    @Override
    public boolean isEmpty(){
        return tope==null;
    }
    
    @Override
    public String toString(){
        String p = "\n";
        Nodo<T> auxiliar = tope;
        if(isEmpty())
            return "Pila vacia";
        while(auxiliar.siguiente!=null){
            p += auxiliar.contenido+"\n";
            auxiliar = auxiliar.siguiente;
        }
        return p+auxiliar.contenido+"\n";
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
