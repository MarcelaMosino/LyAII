package AnalizadorS;

import AnalizadorLS.*;
import analisislexico.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import pila.Nodo;

public class AnalizadorSemantico {
    AnalizadorSintactico as;
    Scanner lector;
    Lista<Token> tTok;
    HashSet<String> simbolos = new HashSet();
    Lista<Simbolo> tS = new Lista("*****************\nTabla de Simbolos\n"
            + "Lexema\t\tToken\t\tTipo\t\tValor\t\tRepeticiones\tLineas");
    
    public AnalizadorSemantico(String nA) throws IOException{
        as = new AnalizadorSintactico(nA, "CorridaLS.txt"); 
        lector = new Scanner(new File(nA));
        as.analizar();
        tTok = as.al.tT;  
    }
    
    public void llenarTablaSimbolos(){
        Nodo<Token> aux = tTok.primero;
        
        for (int i = 0; i < tTok.size(); i++) {
            if(aux.contenido.tipo==15 || aux.contenido.tipo==4 
                    || aux.contenido.tipo==5)
                if(!simbolos.contains(aux.contenido.lexema)){
                    tS.add(new Simbolo(aux.contenido));  
                    simbolos.add(aux.contenido.lexema);
                }
            aux = aux.siguiente;
        }
    }
    
    public void contarRenglones(){
        Pattern patron;
        Matcher match, match2, match3;
        String regex, regex2, regex3;
        String linea=null;
        int contador=1;
        Nodo<Simbolo> aux;
        
        while(lector.hasNext()){
            linea = lector.nextLine();
            aux= tS.primero;
            for (int i = 0; i < tS.size()-1; i++) {
                regex = "[ ,(]"+aux.contenido.lexema+"[ ),=;]";
                regex2= aux.contenido.lexema+"[ ),=;]";
                regex3= "[ ,(]"+aux.contenido.lexema;
                patron = Pattern.compile(regex);
                match = patron.matcher(linea);
                patron = Pattern.compile(regex2);
                match2 = patron.matcher(linea);
                patron = Pattern.compile(regex3);
                match3 = patron.matcher(linea);
                if(match.find() || match2.find() || match3.find())
                    revision(aux.contenido, contador);                
                aux= aux.siguiente;
            }contador++;
        }
    }
    
    public void revision(Simbolo s, int c){
        if(simbolos.contains(s.lexema)){
            s.addRenglon(c);
        }else{
            simbolos.add(s.lexema);
            s.addRenglon(c);
        }
    }
    
    public static void main(String[] args) throws IOException{
        Scanner r = new Scanner(System.in);
        Archivo salida;
        try{
            AnalizadorSemantico a = new AnalizadorSemantico(r.next());
            salida = new Archivo(r.next());
            a.llenarTablaSimbolos();
            a.contarRenglones();
            System.out.println(a.tS);
            salida.escribir(a.tS.toString()+"\n\n"+a.tTok);
        }catch(IOException e){
            System.out.println("Error, los archivos no son correctos");
            System.out.println("Uso Codigo.txt Salida.txt");
        }catch(Exception e){
            System.out.println("Error desconocido");
            e.printStackTrace();
        } 
    }
}
