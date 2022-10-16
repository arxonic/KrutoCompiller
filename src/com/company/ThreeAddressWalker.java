package com.company;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.*;

public class ThreeAddressWalker {
    ParseTree tree;

    Hashtable<String, Hashtable<String, ArrayList>> TABLES;
    String curFunc = "main";

    String cur_F_OR_T = "t";

    LinkedList<ParseTree> queue = new LinkedList<>();
    int countT = 0;
    int countL = 0;
    LinkedList<String> queueT = new LinkedList<>();
    LinkedList<String> queueL = new LinkedList<>();
    int curWhile = -1;
    LinkedList<String> curWhileL = new LinkedList<>();
    LinkedList<String> curWhileLBreak = new LinkedList<>();
    LinkedList<ArrayList<String>> threeAddressCode = new LinkedList<>();

    Map<String, String> alternativeRelOp =  new HashMap<>();

    public ThreeAddressWalker(ParseTree tree, Hashtable<String, Hashtable<String, ArrayList>> TABLES) {
        this.tree = tree;
        this.TABLES = TABLES;
    }
    
    public void DFS(){
        alternativeRelOp.put("==", "!=");
        alternativeRelOp.put("!=", "==");
        alternativeRelOp.put("<", ">=");
        alternativeRelOp.put("<=", ">");
        alternativeRelOp.put(">=", "<");
        alternativeRelOp.put(">", "<=");
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof KrutoParser.FunctionDeclarationPartContext) {
                curFunc = child.getChild(2).getText();

                ArrayList<String> command = new ArrayList<>();
                command.add(child.getChild(2).getText().concat(":"));
                threeAddressCode.add(command);

                _DFS(child);

                curFunc = "main";
                continue;
            }
            if (child instanceof KrutoParser.CompoundStatementContext) {
                ArrayList<String> command = new ArrayList<>();
                command.add("main:");
                threeAddressCode.add(command);
            }
            _DFS(child);
        }
    }

    private void _DFS(ParseTree child) {
        for (int i = 0; i < child.getChildCount(); i++) {
            ParseTree newChild = child.getChild(i);
            if (newChild instanceof KrutoParser.AssignmentStatementContext) {
                assignmentStatementBFS(newChild);
                continue;
            }
            if (newChild instanceof KrutoParser.FunctionCallContext) {
                functionCallBFS(newChild);
                continue;
            }
            if (newChild instanceof KrutoParser.ReturnStatementContext) {
                ArrayList<String> command = new ArrayList<>();
                command.add("re");
                command.add(curFunc + "_" + ((KrutoParser.ReturnStatementContext) newChild).variable().getText());
                command.add(null);
                command.add(null);
                threeAddressCode.add(command);
                continue;
            }
            if (newChild instanceof KrutoParser.IfStatementContext) {
                structuredExprWalker(newChild.getChild(1));
                _DFS(newChild.getChild(3));
                ArrayList<String> command = new ArrayList<>();
                command.add(queueL.poll());
                threeAddressCode.add(command);
                continue;
            }
            if (newChild instanceof KrutoParser.WhileStatementContext) {
                String l = "L".concat(String.valueOf(++countL));

                curWhile++;
                curWhileL.add(curWhile,l);
                curWhileLBreak.add(curWhile, "L".concat(String.valueOf(countL + 1)));

                ArrayList<String> loop = new ArrayList<>();
                loop.add(l.concat(":"));
                threeAddressCode.add(loop);

                structuredExprWalker(newChild.getChild(1));
                _DFS(newChild.getChild(3));

                ArrayList<String> command = new ArrayList<>();
                command.add("j");
                command.add(l);
                command.add(null);
                command.add(null);
                threeAddressCode.add(command);

                ArrayList<String> exit = new ArrayList<>();
                exit.add(queueL.poll());
                threeAddressCode.add(exit);

                curWhileL.remove(curWhile);
                curWhileLBreak.remove(curWhile);
                curWhile--;
                continue;
            }
            if (newChild instanceof KrutoParser.BreakStatementContext) {
                if (curWhileLBreak.isEmpty())
                    continue;
                ArrayList<String> exit = new ArrayList<>();
                exit.add("j");
                exit.add(curWhileLBreak.get(curWhile));
                exit.add(null);
                exit.add(null);
                threeAddressCode.add(exit);
                continue;
            }
            if (newChild instanceof KrutoParser.ContinueStatementContext) {
                if (curWhileL.isEmpty())
                    continue;
                ArrayList<String> exit = new ArrayList<>();
                exit.add("j");
                exit.add(curWhileL.get(curWhile));
                exit.add(null);
                exit.add(null);
                threeAddressCode.add(exit);
                continue;
            }
            if (newChild instanceof KrutoParser.PrintStatementContext) {
                ArrayList<String> print = new ArrayList<>();
                print.add("print");
                print.add((String) TABLES.get(curFunc).get(curFunc + "_" + newChild.getChild(2).getChild(0).getText()).get(0));
                print.add(curFunc + "_" + newChild.getChild(2).getChild(0).getText());
                print.add(null);
                threeAddressCode.add(print);
                continue;
            }

            _DFS(newChild);
        }
    }

    private void structuredExprWalker(ParseTree expression) {
        // <= a1 a2 L
        boolean flag = false;
        queue.add(expression);
        LinkedList<ArrayList<String>> expressionTAC = new LinkedList<>();
        while (!queue.isEmpty()){
            ArrayList<String> command = new ArrayList<>();
            ParseTree child = queue.poll();

            if (child instanceof KrutoParser.ExpressionContext){
                if (child.getChildCount() == 1){
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                expressionCommand(command,child, flag);
                expressionTAC.addFirst(command);
                flag = true;
                continue;
            }

            if (child instanceof KrutoParser.SimpleExpressionContext){
                // if SE -> term (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SE -> term additiveoperator SE
                simpleExpressionCommand(command, child);
                expressionTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.TermContext){
                // if Term -> SF (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if Term -> SF multiplicativeoperator Term
                termCommand(command, child);
                expressionTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.SignedFactorContext){
                // if SF -> Factor (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SF -> (+|-) Term
                signedFactorCommand(command, child);
                expressionTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.FactorContext){
                // if Factor -> ! factor
                if (child.getChild(0).getText().equals("!")) {
                    queue.addFirst(child.getChild(1));
                    continue;
                }
                // if Factor -> ( expression )
                if (child.getChild(0).getText().equals("(")) {
                    queue.addFirst(child.getChild(1));
                    continue;
                }
                factorCommand(command, child, expressionTAC);
                //expressionTAC.addFirst(command);
                continue;
            }

            for (int i = 0; i < child.getChildCount(); i++)
                queue.addFirst(child.getChild(i));
        }
        threeAddressCode.addAll(expressionTAC);
        //System.out.println(threeAddressCode);
    }

    private void expressionCommand(ArrayList<String> command, ParseTree child, boolean flag) {
        if (flag) {
            command.add(child.getChild(1).getText());
            command.add(queueT.poll());
        } else {
            command.add(alternativeRelOp.get(child.getChild(1).getText()));
            String l = "L".concat(String.valueOf(++countL));
            command.add(l);
            queueL.addFirst(l.concat(":"));
        }
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));
        queue.add(child.getChild(0));
        queue.add(child.getChild(2));
        queueT.add(command.get(2));
        queueT.add(command.get(3));
    }

    private void functionCallBFS(ParseTree functionTree) {
        queue.add(functionTree);
        LinkedList<ArrayList<String>> functionCallTAC = new LinkedList<>();
        while (!queue.isEmpty()){
            ArrayList<String> command = new ArrayList<>();
            ParseTree child = queue.poll();

            if (child instanceof KrutoParser.FunctionCallContext){
                functionCallCommand(command, child, functionCallTAC);
                //functionCallTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.ExpressionContext && child.getChildCount() != 1){
                System.err.println("ERROR!");
                System.exit(1);
            }

            if (child instanceof KrutoParser.SimpleExpressionContext){
                // if SE -> term (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SE -> term additiveoperator SE
                simpleExpressionCommand(command, child);
                functionCallTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.TermContext){
                // if Term -> SF (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if Term -> SF multiplicativeoperator Term
                termCommand(command, child);
                functionCallTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.SignedFactorContext){
                // if SF -> Factor (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SF -> (+|-) Term
                signedFactorCommand(command, child);
                functionCallTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.FactorContext){
                // if Factor -> ! factor
                if (child.getChild(0).getText().equals("!")) {
                    System.err.println("ERROR!");
                    System.exit(1);
                }
                // if Factor -> ( expression )
                if (child.getChild(0).getText().equals("(")) {
                    queue.addFirst(child.getChild(1));
                    continue;
                }
                factorCommand(command, child, functionCallTAC);
                //functionCallTAC.addFirst(command);
                continue;
            }

            for (int i = 0; i < child.getChildCount(); i++)
                queue.addFirst(child.getChild(i));
        }
        threeAddressCode.addAll(functionCallTAC);
        //System.out.println(threeAddressCode);
    }

    private void assignmentStatementBFS(ParseTree assignmentTree) {
        queue.add(assignmentTree);
        LinkedList<ArrayList<String>> assignmentStatementTAC = new LinkedList<>();
        while (!queue.isEmpty()){
            ArrayList<String> command = new ArrayList<>();
            ParseTree child = queue.poll();

            if (child instanceof KrutoParser.AssignmentStatementContext){
                assignmentStatementCommand(command,child);
                assignmentStatementTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.ExpressionContext && child.getChildCount() != 1){
                System.err.println("ERROR!");
                System.exit(1);
            }

            if (child instanceof KrutoParser.SimpleExpressionContext){
                // if SE -> term (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SE -> term additiveoperator SE
                simpleExpressionCommand(command, child);
                assignmentStatementTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.TermContext){
                // if Term -> SF (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if Term -> SF multiplicativeoperator Term
                termCommand(command, child);
                assignmentStatementTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.SignedFactorContext){
                // if SF -> Factor (only)
                if (child.getChildCount() == 1) {
                    queue.addFirst(child.getChild(0));
                    continue;
                }
                // if SF -> (+|-) Term
                signedFactorCommand(command, child);
                assignmentStatementTAC.addFirst(command);
                continue;
            }

            if (child instanceof KrutoParser.FactorContext){
                // if Factor -> ! factor
                if (child.getChild(0).getText().equals("!")) {
                    System.err.println("ERROR!");
                    System.exit(1);
                }
                // if Factor -> ( expression )
                if (child.getChild(0).getText().equals("(")) {
                    queue.addFirst(child.getChild(1));
                    continue;
                }
                factorCommand(command, child, assignmentStatementTAC);
                //assignmentStatementTAC.addFirst(command);
                continue;
            }

            for (int i = 0; i < child.getChildCount(); i++)
                queue.addFirst(child.getChild(i));
        }
        threeAddressCode.addAll(assignmentStatementTAC);
        cur_F_OR_T = "t";
    }

    private void functionCallCommand(ArrayList<String> command, ParseTree child, LinkedList<ArrayList<String>> TAC) {
        command.add("vc");  // void call
        command.add(null);
        String funcName = child.getChild(0).getText();
        command.add(funcName);
        ArrayList<String> params = new ArrayList<>();
        if (child.getChildCount() == 3) {
            command.add("0");
            TAC.addFirst(command);
        }else{
            ParseTree exprList = ((KrutoParser.FunctionCallContext) child).expressionList();
            int childCount = 0;
            for (int i = 0; i < exprList.getChildCount(); i++) {
                if (exprList.getChild(i).getText().equals(",")) {
                    continue;
                }

                String temp = "";
                if (((ArrayList) TABLES.get("main").get(funcName).get(4)).get(childCount).equals("int")) {
                    temp = "t".concat(String.valueOf(++countT));
                } else {
                    temp = "f".concat(String.valueOf(++countT));
                }

                queue.add(exprList.getChild(i));
                queueT.add(temp);

                params.add(temp);

                childCount++;
            }
            command.add(String.valueOf(childCount));
            TAC.addFirst(command);
            for (int i = params.size() - 1; i >= 0; i--) {
                ArrayList<String> p = new ArrayList<>();
                p.add("param");
                p.add(params.get(i));
                TAC.addFirst(p);
            }
        }
    }

    private void factorCommand(ArrayList<String> command, ParseTree child, LinkedList<ArrayList<String>> TAC) {
        // if Factor -> variable
        if (child.getChild(0) instanceof KrutoParser.VariableContext) {
            command.add("lw");
            command.add(queueT.poll());
            command.add(curFunc + "_" + ((KrutoParser.VariableContext) child.getChild(0)).IDENTIFIER().getText());
            command.add(null);
            TAC.addFirst(command);
        }
        // if Factor -> functionCall
        if (child.getChild(0) instanceof KrutoParser.FunctionCallContext) {
            command.add("rc");   //returned call
            command.add(queueT.poll());
            String funcName = ((KrutoParser.FunctionCallContext) child.getChild(0)).IDENTIFIER().getText();
            command.add(funcName);
            ArrayList<String> params = new ArrayList<>();
            if (child.getChild(0).getChildCount() == 3) {
                command.add("0");
                TAC.addFirst(command);
            } else {
                ParseTree exprList = ((KrutoParser.FunctionCallContext) child.getChild(0)).expressionList();
                int childCount = 0;
                for (int i = 0; i < exprList.getChildCount(); i++) {
                    if (exprList.getChild(i).getText().equals(",")) {
                        continue;
                    }

                    String temp = "";
                    if (((ArrayList) TABLES.get("main").get(funcName).get(4)).get(childCount).equals("int")) {
                        temp = "t".concat(String.valueOf(++countT));
                    } else {
                        temp = "f".concat(String.valueOf(++countT));
                    }

                    queue.add(exprList.getChild(i));
                    queueT.add(temp);

                    params.add(temp);

                    childCount++;
                }
                command.add(String.valueOf(childCount));
                TAC.addFirst(command);
                for (int i = params.size() - 1; i >= 0; i--) {
                    ArrayList<String> p = new ArrayList<>();
                    p.add("param");
                    p.add(params.get(i));
                    TAC.addFirst(p);
                }
            }
        }
        // if Factor -> unsignedNumber
        if (child.getChild(0) instanceof KrutoParser.UnsignedNumberContext) {
            command.add("li");
            command.add(queueT.poll());
            command.add(child.getChild(0).getChild(0).getText());
            command.add(null);
            TAC.addFirst(command);
        }
    }

    private void signedFactorCommand(ArrayList<String> command, ParseTree child) {
        command.add(child.getChild(0).getText());
        command.add(queueT.poll());

        String curFT = cur_F_OR_T;
        if (command.get(1).contains("t"))
            cur_F_OR_T = "t";
        else
            cur_F_OR_T = "f";
        command.add("0");
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));

        cur_F_OR_T = curFT;

        queue.add(child.getChild(1));
        queueT.add(command.get(3));
    }

    private void termCommand(ArrayList<String> command, ParseTree child) {
        command.add(child.getChild(1).getText());
        command.add(queueT.poll());

        String curFT = cur_F_OR_T;
        if (command.get(1).contains("t"))
            cur_F_OR_T = "t";
        else
            cur_F_OR_T = "f";
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));

        cur_F_OR_T = curFT;

        queue.add(child.getChild(0));
        queue.add(child.getChild(2));
        queueT.add(command.get(2));
        queueT.add(command.get(3));
    }

    private void simpleExpressionCommand(ArrayList<String> command, ParseTree child) {
        command.add(child.getChild(1).getText());
        command.add(queueT.poll());

        String curFT = cur_F_OR_T;
        if (command.get(1).contains("t"))
            cur_F_OR_T = "t";
        else
            cur_F_OR_T = "f";
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));

        cur_F_OR_T = curFT;

        queue.add(child.getChild(0));
        queue.add(child.getChild(2));
        queueT.add(command.get(2));
        queueT.add(command.get(3));
    }

    private void assignmentStatementCommand(ArrayList<String> command, ParseTree child) {
        String var = curFunc + "_" + ((KrutoParser.VariableContext) child.getChild(0)).IDENTIFIER().getText();
        if (TABLES.get(curFunc).get(var).get(0).equals("float"))
            cur_F_OR_T = "f";
        command.add("sw");
        command.add(var);
        command.add(cur_F_OR_T.concat(String.valueOf(++countT)));
        command.add(null);
        queue.add(child.getChild(2));
        queueT.add(command.get(2));
    }
}
