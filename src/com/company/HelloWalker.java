package com.company;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HelloWalker extends KrutoParserBaseListener {

    //ArrayList<Hashtable<String, ArrayList>> TABLES = new ArrayList<>();
    Hashtable<String, Hashtable<String, ArrayList>> TABLES = new Hashtable<>();

    //int curTable = 0;
    String curFunc = "main";

    public void addTableInTree(String id, Hashtable<String, ArrayList> symbolTable){
        TABLES.put(id, symbolTable);
    }

    private void putInTable(String table, String id, ArrayList arr) {
        TABLES.get(table).put(String.valueOf(id), arr);
    }

    public void checkForVariableRedeclaring(String table, String id, Token start){
        if (TABLES.get(table).containsKey(id))
            try {
                throw new VariableDeclarationException(String.valueOf(start));
            } catch (VariableDeclarationException e) {
                e.printStackTrace();
                System.exit(-1);
            }
    }



    public void enterProgram(KrutoParser.ProgramContext ctx) {
        Hashtable<String, ArrayList> symbolTable = new Hashtable<>();
        addTableInTree("main", symbolTable);
    }

    public void enterVariableDeclarationPart(KrutoParser.VariableDeclarationPartContext ctx) {
        for (KrutoParser.VariableDeclarationContext varDec : ctx.variableDeclaration()){
            for (TerminalNode id : varDec.identifierList().IDENTIFIER()){
                String newID = curFunc + "_" + id.getText();
                checkForVariableRedeclaring(curFunc, newID, varDec.start);
                ArrayList arr = new ArrayList();
                arr.add(varDec.typeIdentifier().getChild(0).getText());
                arr.add(null);
                arr.add(null);
                arr.add(null);
                arr.add(null);
                putInTable(curFunc, newID, arr);
            }
        }
    }

    public void enterFunctionDeclarationPart(KrutoParser.FunctionDeclarationPartContext ctx) {
        curFunc = ctx.IDENTIFIER().getText();
        checkForVariableRedeclaring("main", ctx.IDENTIFIER().getText(), ctx.start);
        if (ctx.IDENTIFIER().getText().equals("main")){
            System.err.println("Нельзя назвать функцию - 'main'");
            System.exit(1);
        }

        Hashtable<String, ArrayList> symbolTable = new Hashtable<>();
        addTableInTree(curFunc, symbolTable);

        ArrayList funcArr = new ArrayList();
        funcArr.add(null);
        funcArr.add(ctx.resultType().getChild(0).getText());
        funcArr.add(null);
        ArrayList<String> funcTypeOfVars = new ArrayList<>();
        ArrayList<String> funcNameOfVars = new ArrayList<>();
        for (KrutoParser.FormalParameterSectionContext fPS : ctx.formalParameterList().formalParameterSection()){
            KrutoParser.VariableDeclarationContext varDec = fPS.variableDeclaration();
            for (TerminalNode id : varDec.identifierList().IDENTIFIER()){
                String newID = curFunc + "_" + id.getText();
                checkForVariableRedeclaring(curFunc, newID, varDec.start);
                ArrayList arr = new ArrayList();
                ParseTree typeId = varDec.typeIdentifier().getChild(0);
                arr.add(typeId.getText());
                funcTypeOfVars.add(String.valueOf(typeId));
                funcNameOfVars.add(newID);
                arr.add(null);
                arr.add(null);
                arr.add(null);
                arr.add(null);
                putInTable(curFunc, newID, arr);
            }
        }
        funcArr.add(symbolTable.size());
        funcArr.add(funcTypeOfVars);
        funcArr.add(funcNameOfVars);

        putInTable("main", ctx.IDENTIFIER().getText(), funcArr);
    }

    public void exitFunctionDeclarationPart(KrutoParser.FunctionDeclarationPartContext ctx) {
        curFunc = "main";
    }

    public void enterVariable(KrutoParser.VariableContext ctx) {
        String newID = curFunc + "_" + ctx.IDENTIFIER().getText();
        if (!TABLES.get(curFunc).containsKey(newID))
            try {
                throw new VariableDeclarationException(String.valueOf(ctx.start));
            } catch (VariableDeclarationException e) {
                e.printStackTrace();
                System.exit(-1);
            }
    }

    public void enterFunctionCall(KrutoParser.FunctionCallContext ctx) {
        if (!TABLES.get("main").containsKey(String.valueOf(ctx.IDENTIFIER())))
            try {
                throw new VariableDeclarationException(String.valueOf(ctx.start));
            } catch (VariableDeclarationException e) {
                e.printStackTrace();
                System.exit(-1);
            }
    }

    public void enterReturnStatement(KrutoParser.ReturnStatementContext ctx){
        // проверка return
        if (TABLES.get("main").get(curFunc).get(1).equals("void")){
            System.err.println("Несоответствие типов в функции " + curFunc);
            System.exit(1);
        }
        String var = curFunc + "_" + ctx.getChild(1).getChild(0).getText();
        String funcReturnType = TABLES.get("main").get(curFunc).get(1).toString();
        String ret = TABLES.get(curFunc).get(var).get(0).toString();
        if (!funcReturnType.equals(ret)){
            System.err.println("Несоответствие типов в функции " + curFunc);
            System.exit(1);
        }
    }
}