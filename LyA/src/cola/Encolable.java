
package cola;

public interface Encolable<T> {
    public T dequeue();
    public void enqueue(T elemento);
    public boolean isEmpty();
}
