package BackJava.BackJava.AC;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;
import java.util.List;

public class TablaSimbolos {

    private static TablaSimbolos inst = null;

    LinkedList<Object> tabla;

    public int nivelActual = 0;


    private TablaSimbolos() {
        tabla = new LinkedList<Object>();
    }

    public static TablaSimbolos getInstance(){
        if(inst == null){
            inst = new TablaSimbolos();
        }
        return inst;
    }

    public void insertar(Token id, Type tipo, ParserRuleContext decl)
    {
        //no se puede insertar un elemento repetido en el mismo nivel
        Ident i = new Ident(id,tipo,nivelActual,decl, null);
        tabla.addFirst(i);
    }

    public void insertarMethod(Token id, Type tipo, ParserRuleContext decl, List<ParamMethod> listParamas)
    {
        Ident i = new Ident(id,tipo,nivelActual,decl, listParamas);
        tabla.addFirst(i);
    }



    public void insertarIdentClass(String id, String tipo)
    {
        //no se puede insertar un elemento repetido en el mismo nivel
        IdentClass i = new IdentClass(id,tipo,nivelActual);
        tabla.addFirst(i);
    }


    public Ident buscar(String nombre)
    {
        Ident temp = null;
        try {
            for(Object id : tabla)
                if (id.getClass() != IdentClass.class){
                    if (((Ident)id).tok.getText().equals(nombre))
                        return ((Ident)id);
                }
        } catch (Exception ignored){ }
        return temp;
    }

    public IdentClass buscarClass(String nombre)
    {
        IdentClass temp = null;
        try {
            for(Object id : tabla)
                if (id.getClass() != Ident.class){
                    if (((IdentClass)id).nombre.equals(nombre))
                        return ((IdentClass)id);
                }
        } catch (Exception ignored){ }
        return temp;
    }


    public void openScope(){
        nivelActual++;
    }

    public void closeScope(){
        tabla.removeIf(n -> ( (n.getClass() != IdentClass.class) && (((Ident)n).nivel == nivelActual)));
        nivelActual--;
    }

    public void imprimir() {
        System.out.println("----- INICIO TABLA ------");
        for (int i = 0; i < tabla.size(); i++) {
            try {
                Token s = (Token) ((Ident) tabla.get(i)).tok;
                System.out.println("Nombre: " + s.getText() + " - " +"Nivel: " + ((Ident) tabla.get(i)).nivel + " - " +" Tipo: "+((Ident) tabla.get(i)).type);
            } catch (Exception e){
                String s = ((IdentClass) tabla.get(i)).nombre;
                System.out.println("Nombre: " + s + " - " +"Nivel: " + ((IdentClass) tabla.get(i)).nivel + " - " +" Tipo: "+((IdentClass) tabla.get(i)).type);
            }
        }
        System.out.println("----- FIN TABLA ------\n");
    }

}
