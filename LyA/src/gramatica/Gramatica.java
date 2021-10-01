package gramatica;

import analisislexico.AnalizadorLexico;
import analisislexico.Lista;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class Gramatica {  
    public int[] terminales; //Guarda atributo de terminales
    public int[] noTerminales; //números >900 representan no terminales
    public Lista<Integer>[] producciones; //cada lista es un lado derecho
    int sInicial = 900; // símbolo inicial
    //Guarda los Strings de los no terminales, sin repetir
    public LinkedHashSet<String> nt = new LinkedHashSet();
    //Guarda los Strings de los terminales, sin repetir
    public LinkedHashSet<String> te = new LinkedHashSet();
    //Guarda los Strings de los lados derechos
    public String[] laDerecho;
    File archivo; //Lectura de archivo de gramática
    Scanner entrada; //Lee el archivo de la gramática
    //Auxiliar para determinar el atributo de los terminales
    AnalizadorLexico l = new AnalizadorLexico();
    
    public Gramatica(String a)throws IOException{        
        archivo = new File(a);
        entrada = new Scanner(archivo);
    }
        
    public void leeGramatica() throws IOException{
        String aux; // cada una de las líneas del archivo
        String prod=""; //Guarda las líneas separadas por un \n
        
        while(entrada.hasNext()){
            aux = entrada.nextLine();
            nt.add(aux.split(" -> ")[0]);
            prod += aux.split(" -> ")[1]+"\n";
        }
        entrada.close(); //Termina de leer y cierra el flujo
        
        laDerecho = prod.split("\n"); //separa lados derechos por \n
        guardaNoTerminales(); //Llena noTerminales
        guardaTerminales();  //Llena terminales
        guardaDerechos();  //Llena producciones
    }
    
    public void guardaNoTerminales(){
        noTerminales = new int[nt.size()];
        //Asigna números empezando por el 900
        for (int i = 0; i < noTerminales.length; i++) 
            noTerminales[i]=900+i;                                     
    }   
    
    void guardaTerminales(){
        String[] prod; //Cada una de las palabras de la produccion
        //Prepara el conjunto de terminales
        for (int i = 0; i < laDerecho.length; i++) {
            prod = laDerecho[i].split(" ");
            for (int j = 0; j < prod.length; j++) 
                if(!nt.contains(prod[j]) && prod[j].charAt(0)!='^')
                    //Sin repetir, almacena lo que se encuentre en el lado                     
                    te.add(prod[j]);// derecho y que no sea no terminal
        } 
        
        String[] aux = new String[te.size()];
        te.toArray(aux);//Contiene los strings de los terminales
        terminales = new int[aux.length];        
        for (int i = 0; i < terminales.length; i++){
            if(aux[i].equals("intliteral"))
                terminales[i]=333; //Se añade el atributo de los enteros
            else if(aux[i].equals("realliteral"))
                terminales[i]=334; //Se añade el atributo de los reales
            else
                //Se almacena el atributo de palabras reservadas o símbolos
                terminales[i] = l.generaToken(aux[i]).atributo;
        }
    }
    
    void guardaDerechos(){
        //Cada elemento es una lista de enteros que representa la producción
        producciones = new Lista[laDerecho.length];
        Lista<Integer> pAux; //Cada elemento es un entero que representa
        //a un terminal o no terminal de la producción
        String[] d; // auxiliar para separ los lados derechos
        
        for (int i = 0; i < producciones.length; i++) {
            d = laDerecho[i].split(" ");
            pAux = new Lista("Produccion "+(i+1));            
            for (int j = 0; j < d.length; j++) {
                if(te.contains(d[j])){//Si es terminal, guarda atributos
                    if(d[j].equals("intliteral"))
                        pAux.addI(333);
                    else if(d[j].equals("realliteral"))
                        pAux.addI(334);
                    else
                        pAux.addI(l.generaToken(d[j]).atributo);
                } else //Si es no terminal, busca el número correspondiente
                    pAux.addI(buscaEntero(d[j])); 
            }  
            producciones[i] = pAux; //Asigna la lista al número de producción
        }
    }
    
    public int buscaEntero(String s){//Devuelve el índice de no terminal
        String[] n2 = new String[nt.size()];
        nt.toArray(n2);
        for (int i = 0; i < n2.length; i++) {
            if(s.equals(n2[i]))
                return noTerminales[i];
        }return 0; //Regresa 0 si no encuentra coincidencia
    }
    
    public int buscaEntero(int atr){//Devuelve el índice de terminal
        for (int i = 0; i < terminales.length; i++) {
            if(terminales[i]==atr)
                return i;
        }return 1000; //Regresa error
    }
    
    public int getSInicial(){
        return sInicial; //Regresa el símbolo inicial
    }
    
    @Override
    public String toString(){ //Método para impresión
        String ter = "\nTerminales:\n";
        String ntr = "\nNo terminales:\n";
        String pd = "\nProducciones lado derecho:\n";
        
        for (int i = 0; i < terminales.length; i++) 
            ter += terminales[i]+"\n";
        for (int i = 0; i < noTerminales.length; i++) 
            ntr += noTerminales[i]+"\n";
        for (int i = 0; i < producciones.length; i++) 
            pd += producciones[i]+"\n";  
//        return "\nTerminales:\n"+te.toString()
//              +"\nNo terminales: "+nt.toString()
//              +"\nProducciones lado producciones: "+Arrays.toString(laDerecho);
        return ter+ntr+pd+"\n\n";
    }    
}
