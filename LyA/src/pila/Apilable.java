
package pila;

public interface Apilable <T>{
    public T pop();
    public T peek();
    public void push(T elemento);
    public boolean isEmpty();
}
