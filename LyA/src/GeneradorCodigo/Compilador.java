
package GeneradorCodigo;

import AnalizadorLS.Archivo;
import Optimizador.Optimizador;
import analisislexico.Lista;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import pila.Nodo;


public class Compilador {
    HashMap<String,String> mapa = new HashMap();
    Lista<Tres> operaciones = new Lista("");
    Scanner lector;
    String archivo="";
    Archivo ensamblador;
    
    public Compilador() 
            throws IOException, InterruptedException{
        lector = new Scanner(System.in);
        Optimizador o = new Optimizador(lector.next(), "codigo_optimizado.txt");
        o.leer();
        o.optimizar();
        ensamblador = new Archivo(lector.next()) ;
    }
    
    public void llenaMapas() throws FileNotFoundException{
        lector = new Scanner(new File("codigo_optimizado.txt"));
        String line;
        int r=1;
        Tres aux;
        
        while(lector.hasNext() ){
            line = lector.nextLine();
            if(line.split(" ")[0].contains("ldve")){
                mapa.put(line.split(" ")[1], ".R"+r);
                r++;
            }else{
                if(!line.split(" ")[0].contains("ld")){
                    aux = new Tres(mapa);
                    aux.setA1(line.split(" ")[0]);
                    aux.setA2(line.split(" ")[1]);
                    if(line.split(" ").length==3)
                        aux.setA3(line.split(" ")[2]);
                    else
                        aux.setA3("");
                    operaciones.add(aux);
                }
            }
        }
    }
    
    public void cambiaOperaciones(){
        Set<String> set = mapa.keySet();
        Iterator<String> it = set.iterator();
        String aux;
        Nodo<Tres> aux2=operaciones.primero;
        
        while(it.hasNext()){
            aux = it.next();
            archivo+= "MOVE "+"#0, "+mapa.get(aux)+"\n";
        }
        while(aux2!=null){
            archivo+=aux2.contenido.toString()+"\n";
            if(aux2.contenido.n1.equals("ADD") ||
               aux2.contenido.n1.equals("SUB") ||
               aux2.contenido.n1.equals("DIV")) {
                archivo+= "MOVE .A,"+aux2.contenido.n2+"\n";
            }
            aux2=aux2.siguiente;
        }archivo+="HALT";
    }
    
    public void generaEnsamblador() throws IOException{
        llenaMapas();
        cambiaOperaciones();
        ensamblador.escribir(archivo);
    }
    
    public static void main(String[] args) throws IOException {
        try{
            Compilador c = new Compilador();
            c.generaEnsamblador();
            System.out.println(c.archivo);
        }catch(IOException e){
            System.out.println("Error, los archivos no son correctos");
            System.out.println("Uso CodigoIntermedio.txt Salida.txt");
        }catch(Exception e){
            System.out.println("Error desconocido");
        }
    }
}
