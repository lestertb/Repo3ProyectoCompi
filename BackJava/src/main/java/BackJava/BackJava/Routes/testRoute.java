package BackJava.BackJava.Routes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import BackJava.BackJava.AC.MiVisitor;
import BackJava.BackJava.Classes.MyErrorListener;
import BackJava.BackJava.Classes.response;
import BackJava.BackJava.INTPR.MiInterprete;
import generated.miParser;
import generated.miScanner;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@RestController
public class testRoute {

    private static String res = "";
    private static String res2 = "";
    private List<String> listTokens = new ArrayList<>();
    private List<String> listInfo = new ArrayList<>();

    @GetMapping("/test")
    public response greeting() {
        return new response(res);
    }
    @GetMapping("/test2")
    public response greeting1() {
        return new response(res2);
    }

    @PostMapping("/test3")
    public response greeting2(@RequestBody String test) {
        guardarTokens(test);
        return new response( "\nTokens utilizados:\n"+"\n"+listTokens.toString());
    }

    @PostMapping("/test4")
    public response greeting3(@RequestBody String test) {
        guardarInfo(test);
        return new response( "\nElementos actuales creados respectivamente:\n"+"\n"+listInfo.toString());
    }

    @GetMapping("/test5")
    void greeting4() {
        LimpiarList();
    }

    @PostMapping("/agregar")
    void agregar (@RequestBody String test){
        String errorVisit =  "";
        String resultVisitINTPR =  "";
        miScanner inst = null;
        miParser parser = null;
        ParseTree tree=null;

        CommonTokenStream tokens = null;
        MyErrorListener errorListener = null;
        try {
            inst = new miScanner(CharStreams.fromString(test));
            tokens = new CommonTokenStream(inst);
            parser = new miParser(tokens);

            errorListener = new MyErrorListener();

            inst.removeErrorListeners();
            inst.addErrorListener( errorListener );

            parser.removeErrorListeners();
            parser.addErrorListener ( errorListener );

            try {
                tree = parser.program();
                //MiVisitor mv = new MiVisitor();
                //mv.visit(tree);
                //errorVisit += mv.errores;

                MiInterprete inter = new MiInterprete();
                inter.visit(tree);
                resultVisitINTPR += inter.resultINTPR;
            }
            catch(RecognitionException e){
                System.out.println("Error!!!");
                e.printStackTrace();
            }

            if ( !errorListener.hasErrors() ) {
                res2 = "Compilacion exitosa\n";
                //java.util.concurrent.Future<JFrame> treeGUI = org.antlr.v4.gui.Trees.inspect(tree, parser);
                //treeGUI.get().setVisible(true);

                res = errorListener.toString();

                res += errorVisit;

                res += resultVisitINTPR;
            }
            else {
                res2 = "Compilacion fallida\n";

                res = errorListener.toString();
            }
        }
        catch(Exception e){System.out.println("No hay archivo");e.printStackTrace();}
    }

   void guardarTokens (String test2){
       miScanner inst2 = null;
       try {
           inst2 = new miScanner(CharStreams.fromString(test2));
           List<Token> lista2 = (List<Token>) inst2.getAllTokens();
           String mensaje = "";
           for (Token t : lista2) {
               mensaje = "\n"+miScanner.VOCABULARY.getSymbolicName(t.getType()) + ":" + t.getText() + "\n";
               listTokens.add(mensaje);
           }
       }
       catch(Exception e){System.out.println("No hay archivo");e.printStackTrace();}
    }

    void guardarInfo (String test3){

        miScanner inst3 = null;
        miParser parser3 = null;
        ParseTree tree3=null;

        CommonTokenStream tokens3 = null;
        try {
            inst3 = new miScanner(CharStreams.fromString(test3));
            tokens3 = new CommonTokenStream(inst3);
            parser3 = new miParser(tokens3);

            for (String j :parser3.getRuleNames()
            ) {
                listInfo.addAll(Collections.singleton(parser3.variableDeclaration().getText()));
                //System.out.println(parser.variableDeclaration().ID());
            }
            listInfo.removeIf(p -> p.contains("<missing ID>"));


            try {
                tree3 = parser3.program();
            }
            catch(RecognitionException e){
                System.out.println("Error!!!");
                e.printStackTrace();
            }
        }

        catch(Exception e){System.out.println("No hay archivo");e.printStackTrace();}
    }

    void LimpiarList(){
        listTokens.clear();
        listInfo.clear();
    }
}
