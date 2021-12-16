
package GeneradorCodigo;

import java.util.HashMap;

public class Tres {
    public String n1,n2,n3;
    HashMap<String,String> mapa = new HashMap();
    HashMap<String,String> variables;
    
    public Tres(HashMap<String,String> m){
        variables = m;
        mapa.put("mov","MOVE");
        mapa.put("add","ADD");
        mapa.put("res","SUB");
        mapa.put("div","DIV");
        mapa.put("wte","WRINT");
        mapa.put("rde","ININT");
    }
    
    public void setA1(String a1){
        n1 = mapa.get(a1);
    }
    
    public void setA2(String a2){
        try{
            Integer.parseInt(a2);
            n2 = a2;
        }catch(NumberFormatException nfe){
            n2 = variables.get(a2);
        }
    }
    
    public void setA3(String a3){
        if(a3.equals("")){
            n3 = "";
            return;
        }
        try{
            Integer.parseInt(a3);
            n3 =", #"+ a3;
        }catch(NumberFormatException nfe){
            n3 =", " + variables.get(a3);
        }
    }       
    
    @Override
    public String toString(){
        if(!n1.equals("MOVE"))
            return n1 +" "+ n2 + n3;
        else
            return n1 +" "+ n3.substring(2, n3.length()) +", "+ n2;
    }
}
