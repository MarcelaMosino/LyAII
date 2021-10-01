/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorS;

import analisislexico.*;
import java.util.*;

/**
 *
 * @author TecNM
 */
public class Simbolo extends Token{
    //Lexema\t\tToken\t\tTipo\t\tValor\t\tRepeticiones\tLineas
    Lista<Integer> lineas = new Lista("Renglones");
    int repeticiones=0;

    public Simbolo(Token tok) {
        super(tok);
        cambiaTipo();
    }
    
    public void cambiaTipo(){
        
    }
    
    public void addRenglon(int renglon){
        repeticiones++;
        lineas.add(renglon);
    }
    
    @Override
    public String toString(){
        return super.lexema+"\t\t"+super.clasificacion+"\t\t"
                +super.tipoString+"\t\t"+super.atributo+"\t\t"
                +repeticiones+"\t"+lineas.aRenglon();
    }
}
