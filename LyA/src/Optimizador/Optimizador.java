package Optimizador;

import AnalizadorLS.Archivo;
import GeneradorCodigo.Traductor;
import analisislexico.Lista;
import java.io.File;
import java.io.IOException;
import java.util.*;
import pila.Nodo;


public class Optimizador {
    Scanner sc;
    public Archivo optimizado;
    Codigo cOp = new Codigo();
    Lista<String> lineas=new Lista("");
    Map<String,String> rep = new HashMap();
    
    public Optimizador(String uno, String dos)throws IOException, InterruptedException{
        Traductor t = new Traductor(uno, "codigo_intermedio.txt");
        t.transformar();
        t.generaIntermedio();
        optimizado = new Archivo(dos);
    }
    
    public void leer() throws IOException{
        sc = new Scanner(new File("codigo_intermedio.txt"));
        
        while(sc.hasNext())
            lineas.add(sc.nextLine());        
    }
    
    public void optimizar() throws IOException{
        Nodo<String> aux = lineas.primero;
        
        while(aux!=null){
            aux = revisaMapa(aux);
            if(cOp.getOp(aux).equals("div")&&
              (cOp.getVar2(aux).equals("1") || cOp.getVar2(aux).equals("1.0"))){
                aux = aux.siguiente;
            }else if(cOp.getOp(aux).equals("add")||cOp.getOp(aux).equals("res")){
               if(cOp.getVar2(aux).equals("0")||cOp.getVar2(aux).equals("0.0"))
                   aux = aux.siguiente;
               else{
                   addACodigo(aux);
                   aux=aux.siguiente;
               }
            }
            else if(cOp.getOp(aux).equals("mov") && cOp.getOp(aux.siguiente).equals("add")){
                reduceSuma(aux);
                aux = aux.siguiente.siguiente;
            }else{
                addACodigo(aux);
                aux = aux.siguiente;
            }
        }revisaVariables();
    }
    
    private Nodo<String> revisaMapa(Nodo<String> aux){
        Nodo<String> nuevo=null;        
        
        if(rep.containsKey(cOp.getVar1(aux)))
            nuevo = new Nodo(cOp.getOp(aux)+" "+
                    rep.get(cOp.getVar1(aux))+" "+cOp.getVar2(aux));
        else if(rep.containsKey(cOp.getVar2(aux)))
            nuevo = new Nodo(cOp.getOp(aux)+" "+
                    cOp.getVar1(aux)+" "+rep.get(cOp.getVar2(aux)));  
        else{
            return aux;
        }
        nuevo.siguiente = aux.siguiente;
        return nuevo;
    }
    
    private void addACodigo(Nodo<String> aux){
        if(cOp.getOp(aux).contains("ld")){
            cOp.addVariable(aux.contenido);    
        }else{            
            cOp.addOperacion(aux.contenido);
            cOp.addVarEnOp(cOp.getVar1(aux));
            cOp.addVarEnOp(cOp.getVar2(aux));
        } 
    }

    private void reduceSuma(Nodo<String> aux) {
        String nueva = "add ";
        String prim =cOp.getVar2(aux.siguiente);
        String segu =cOp.getVar2(aux);
        
        try{
            Float.parseFloat(prim);
            nueva += segu + " " + prim;
            rep.put(cOp.getVar1(aux), segu);
        }catch(Exception ex){
            nueva += prim + " " + segu;
            rep.put(cOp.getVar1(aux), cOp.getVar2(aux.siguiente));
        }        
        addACodigo(new Nodo(nueva));       
    }
    
    private void revisaVariables() throws IOException{
        Iterator<String> it2;
        Nodo<String> aux= cOp.variables.primero;
        String dec="";
        
        if(cOp.sobranVariables()){
            it2 = cOp.varEnDe.iterator();
            
            while(it2.hasNext()){
                if(cOp.varEnOp.contains(it2.next()))
                    dec+=aux.contenido+"\n";
                aux=aux.siguiente;
            }aux=cOp.operaciones.primero;
            while(aux!=null){
                dec+=aux.contenido+"\n";
                aux=aux.siguiente;
            }optimizado.escribir(dec);
        }else{
            optimizado.escribir(cOp.toString());
        }
    }
}
