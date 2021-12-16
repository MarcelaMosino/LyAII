
package AnalizadorS;

import analisislexico.*;
import pila.Nodo;
import pila.Pila;

public class ExpresionPosfija {
    Lista<Token> expresion;
    Lista<Token> nuevaExpresion;
    
    public ExpresionPosfija(Lista<Token> expresion){
        this.expresion=expresion;
        nuevaExpresion = new Lista("Expresion...");
    }
    
    public Lista<Token> convierte(){
        Pila<Token> pila = new Pila();
        Nodo<Token> aux = expresion.primero;
        Token auxParentesis= null;
        
        while(aux!=null){
            if(aux.contenido.tipo==4 || aux.contenido.tipo==5){
                nuevaExpresion.add(aux.contenido);
                aux = aux.siguiente;
            }else{
                if(!pila.isEmpty()){
                    if(aux.contenido.prioridad <= pila.peek().prioridad){
                        if(pila.peek().tipo!=8)
                            nuevaExpresion.add(pila.pop());
                        else pila.pop();
                    }else if(aux.contenido.tipo==9){
                        do{
                            auxParentesis = pila.pop();
                            if(auxParentesis.tipo!=8)
                                nuevaExpresion.add(auxParentesis);    
                        }while(auxParentesis.tipo != 8);
                    }else{
                        if(aux.contenido.tipo==8)
                            aux.contenido.prioridad=0;
                        pila.push(aux.contenido);
                        aux = aux.siguiente;
                    }
                }else {
                    if(aux.contenido.tipo==8)
                        aux.contenido.prioridad=0;
                    pila.push(aux.contenido);
                    aux = aux.siguiente;
                }
            }
        }
        while(!pila.isEmpty()){
            if(pila.peek().tipo!=8 && pila.peek().tipo!=9)
                nuevaExpresion.add(pila.pop());
            else pila.pop();
        }
        return nuevaExpresion;
    }   
    
    public String obtenTodas(){
        Nodo<Token> aux = nuevaExpresion.primero;
        String acc="";
        String tipo = nuevaExpresion.ultimo.contenido.lexema;
        
        while(aux.siguiente!=null){
            acc += aux.contenido.lexema +" "+tipo+"\n";
            aux= aux.siguiente;
        } return acc;
    }
    
    @Override
    public String toString(){
        Nodo<Token> aux = nuevaExpresion.primero;
        String acc="";
        
        if(nuevaExpresion.ultimo.contenido.tipo==3)
            acc+= obtenTodas();
        else{
            while (aux != null) {
                acc += aux.contenido.lexema + " ";
                aux = aux.siguiente;
            }
        }
        
        return acc;
    }    
}       