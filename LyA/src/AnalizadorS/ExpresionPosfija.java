
package AnalizadorS;

import AnalizadorLS.AnalizadorSintactico;
import analisislexico.*;
import java.io.IOException;
import pila.Nodo;
import pila.Pila;

public class ExpresionPosfija {
    Pila<Token> pila = new Pila();
    Lista<Token> expresion;
    Lista<Token> nuevaExpresion;
    
    public ExpresionPosfija(Lista<Token> expresion){
        this.expresion=expresion;
        nuevaExpresion = new Lista("Expresion...");
    }

    ExpresionPosfija() {
       
    }
    
    public Lista<Token> convierte(){
        Nodo<Token> aux = expresion.primero;
        Token auxParentesis= null;
        
        while(aux!=null){
            if(aux.contenido.tipo==4 || aux.contenido.tipo==5){
                nuevaExpresion.add(aux.contenido);
                aux = aux.siguiente;
            }else{
                if(!pila.isEmpty()){
                    if(aux.contenido.prioridad <= pila.peek().prioridad){
                        if(pila.peek().tipo!=8 && pila.peek().tipo!=9)
                            nuevaExpresion.add(pila.pop());
                        else pila.pop();
                    }else if(aux.contenido.tipo==9){
                        do{
                            auxParentesis = pila.pop();
                            if(auxParentesis.tipo!=9 && auxParentesis.tipo!=8)
                                nuevaExpresion.add(auxParentesis);    
                        }while(auxParentesis.tipo != 8);
                    }else{
                        pila.push(aux.contenido);
                        aux = aux.siguiente;
                    }
                }else {
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
    
}       