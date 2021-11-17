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
    LinkedHashSet<String> errores = new LinkedHashSet();
    HashSet<String> simbolos = new HashSet();
    Lista<Simbolo> tS = new Lista("*****************\nTabla de Simbolos\n"
            + "Lexema\t\tToken\t\tTipo\t\tValor\t\tRepeticiones\tLineas");
    public Lista<Lista<Token>> expresiones=new Lista("\n*********"
            + "****************\nLista de Expresiones\n");
    String impresion="";
    Archivo salida;
    
    public AnalizadorSemantico(String nA, String nS) throws IOException{
        as = new AnalizadorSintactico(nA, "CorridaLS.txt"); 
        lector = new Scanner(new File(nA));
        salida = new Archivo(nS);  
        as.analizar();
        tTok = as.al.tT;       
    }
    
    public void llenarExpresiones() throws IOException{
        Nodo<Token> aux = tTok.primero;
        Lista<Token> expr=new Lista("");
        
        while (aux!=null) {
            expr=new Lista("");
            if(aux.contenido.tipo!=2 
                    &&!aux.contenido.lexema.equals("Programa")&&
                    aux.contenido.tipo!=6 && aux.contenido.tipo!=16 
                    && aux.contenido.tipo!=7){
                while(aux.contenido.tipo!=7){
                    if(aux.contenido.tipo!=6)
                        expr.add(aux.contenido);
                    aux = aux.siguiente;
                }
            }
            else if(aux.contenido.tipo==3)
                while(aux.contenido.tipo!=7)
                    aux=aux.siguiente;
            else
                aux=aux.siguiente;
            if(expr.size()>0)
                addPosfija(expr);
        }
    }
    
    public void addPosfija(Lista<Token> e) throws IOException{
        ExpresionPosfija expresion = new ExpresionPosfija(e);
        expresiones.add(expresion.convierte());
        impresion+=expresion.toString()+"\n";
    }
    
    public void llenarTablaSimbolos(){
        Nodo<Token> aux = tTok.primero;
        
        while (aux!=null) {
            if(aux.contenido.tipoString.equals("Error"))
                errores.add("No se declaró el identificador: "
                +aux.contenido.lexema);
            if(aux.contenido.tipoString.equals("Error0"))
                errores.add("El identificador "+aux.contenido.lexema
                        +" ya ha sido declarado");
            if(aux.contenido.tipo==4 || aux.contenido.tipo==5)
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
            while (aux!=null) {
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
    
    public void inspeccionaTipos(){
        Nodo<Lista<Token>> exp = expresiones.primero;
        Nodo<Token> op;
        int tipo;
        
        while(exp!=null){
        op = exp.contenido.primero;
        tipo = op.contenido.tipo==4? 4 : 5;
            while(op!=null){
                if(op.contenido.tipo ==4 || op.contenido.tipo==5){
                    if(tipo!= op.contenido.tipo){
                        errores.add("El tipo de "+op.contenido.lexema +
                                " no es compatible con el de "
                                +exp.contenido.primero.contenido.lexema);
                        break;
                    }
                }
                op=op.siguiente;
            }exp=exp.siguiente;
        }
    }
    
    public void analizar() throws IOException{
        llenarTablaSimbolos();
        contarRenglones();
        llenarExpresiones();
        inspeccionaTipos();
        Nodo<Lista<Token>> aux;
        if(!errores.isEmpty()){
            Iterator<String> i = errores.iterator();
            String er="";
            for (int j = 0; j < errores.size(); j++) 
                er+= i.next()+"\n";
            System.out.println("****************\nErrores\n"+er);
            salida.escribir("****************\nErrores\n"+er+"\n"+tS);
        }else{
            System.out.println("El programa no tiene errores");
            salida.escribir(impresion);
            System.out.println(impresion);
        }
    }
    
    public static void main(String[] args) throws IOException{
        Scanner r = new Scanner(System.in);
        try{
            AnalizadorSemantico a = new AnalizadorSemantico(r.next(),r.next());
            a.analizar();
        }catch(IOException e){
            System.out.println("Error, los archivos no son correctos");
            System.out.println("Uso Codigo.txt Salida.txt");
        }catch(Exception e){
            System.out.println("Error desconocido");
            e.printStackTrace();
        } 
    }
}
