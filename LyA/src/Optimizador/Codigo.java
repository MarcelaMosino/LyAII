
package Optimizador;

import analisislexico.Lista;
import java.util.*;
import pila.Nodo;


public class Codigo {
    Set<String> varEnOp = new LinkedHashSet();
    Set<String> varEnDe = new LinkedHashSet();
    Lista<String> operaciones = new Lista("");
    Lista<String> variables = new Lista("");
    
    public Codigo(){
        
    }
    
    public void addVariable(String n){
        variables.add(n);
        String cad = n.split(" ")[1];
        varEnDe.add(cad);
    }
    
    public void addVarEnOp(String v){
        if(v!=null)
            varEnOp.add(v);
    }
    
    public void addOperacion(String n){
        operaciones.add(n);        
    }
    
    public boolean sobranVariables(){
        return varEnDe.size() > varEnOp.size();
    }
    
    public String getVar1(Nodo<String> nodo){        
        return nodo.contenido.split(" ")[1];
    }
    
    public String getVar2(Nodo<String> nodo){
        try{
            return nodo.contenido.split(" ")[2];
        }catch(Exception a){
            return null;
        }        
    }
    
    public String getOp(Nodo<String> nodo){
        return nodo.contenido.split(" ")[0];
    }
    
    @Override
    public String toString(){
        Nodo<String> aux = variables.primero;
        String todo="";
        
        while(aux!=null){
            todo += aux.contenido+"\n";
            aux=aux.siguiente;
        }aux=operaciones.primero;
        
        while(aux!=null){
            todo+= aux.contenido+"\n";
            aux=aux.siguiente;
        }return todo;
    }
}
