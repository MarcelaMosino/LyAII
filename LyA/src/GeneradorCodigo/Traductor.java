package GeneradorCodigo;

import AnalizadorLS.*;
import AnalizadorS.*;
import analisislexico.*;
import cola.Cola;
import java.io.IOException;
import java.util.*;
import pila.Nodo;
import pila.Pila;

public class Traductor {
    AnalizadorSemantico as;
    Lista<Lista<Token>> expresiones;
    Archivo coIntermedio;
    Cola<String> operaciones = new Cola();
    HashSet<String> id = new HashSet();
    Cola<Operando> map = new Cola();
    int count=1;
    
    public Traductor(String uno, String dos) throws IOException{
        as = new AnalizadorSemantico(uno, "NotacionPosfija.txt");
        coIntermedio = new Archivo(dos);
        as.analizar();
        this.expresiones = as.expresiones;
    } 
        
    public void generaIntermedio() throws IOException{
        String todo="";
        
        while(!map.isEmpty())
            todo += map.dequeue().getDeclaracion()+"\n";        
        while(!operaciones.isEmpty())
            todo+=operaciones.dequeue()+"\n";        
        coIntermedio.escribir(todo);
    }
    
    public void transformar() throws InterruptedException{
        Nodo<Lista<Token>> exp = expresiones.primero;
        Token tokEnd;
        Token tokFirst;
        
        while(exp!=null){
            tokEnd=exp.contenido.ultimo.contenido;
            tokFirst=exp.contenido.primero.contenido;
            if(tokEnd.tipo==3){
                if(!id.contains(tokFirst.lexema)){
                    map.enqueue(new Operando(tokFirst));
                    id.add(tokFirst.lexema);
                }
            }else if(tokEnd.tipo==1){
                if(tokEnd.lexema.equals("Leer"))
                    operaciones.enqueue("rd"+tokFirst.tipoString.toLowerCase().
                            charAt(0)+" "+tokFirst.lexema);
                else
                    operaciones.enqueue("wt"+tokFirst.tipoString.toLowerCase().
                            charAt(0)+" "+tokFirst.lexema);
            }else
                transformaExpresion(exp.contenido);            
            exp=exp.siguiente;
        }
    }
        
    private void transformaExpresion(Lista<Token> expresion) throws InterruptedException {
        Pila<String> pila = new Pila();
        String op1, op2, operador;
        Nodo<Token> elem = expresion.primero;        
        
        do{
            if(elem.contenido.tipo==4 || elem.contenido.tipo==5){
                pila.push(elem.contenido.lexema);
                if(!id.contains(elem.contenido.lexema)){
                    map.enqueue(new Operando(elem.contenido));
                    id.add(elem.contenido.lexema);
                }
            }else{
                op2 = pila.pop(); op1 = pila.pop();
                operador=elem.contenido.lexema;
                if(operador.equals("="))
                    operaciones.enqueue("mov "+op1+" "+op2);
                else{
                    operaciones.enqueue("mov t"+ count+" "+op1);
                    pila.push("t"+count);
                    if(!id.contains(elem.contenido.lexema)){
                        map.enqueue(new Operando
                            ("t"+count, expresion.primero.contenido.tipo));
                        id.add("t"+count);
                    }switch(operador){
                        case "+": operaciones.enqueue("add t"+count+" "+op2); break;
                        case "/": operaciones.enqueue("div t"+count+" "+op2); break;
                        case "-": operaciones.enqueue("res t"+count+" "+op2); break;
                    }count++;
                }
            }elem=elem.siguiente;
        }while(!pila.isEmpty());
    }   
}
