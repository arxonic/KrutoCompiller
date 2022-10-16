package com.company;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception{
        String input = "C:/Users/HP/Desktop/Program n2.txt";
        String out_tac = "C:/Users/HP/Desktop/out_tac.txt";
        String out = "C:/Users/HP/Desktop/out.asm";

        CharStream charStream = CharStreams.fromFileName(input);
        KrutoLexer lexer = new KrutoLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        KrutoParser parser = new KrutoParser(tokenStream);
        ParseTree tree = parser.program();
        //Trees.inspect(tree, parser); // не работает вывод дерева

        // Строит таблицу символов и обрабатывает ошибки
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        HelloWalker helloWalker = new HelloWalker();
        parseTreeWalker.walk(helloWalker, tree);

        // Распечатывает таблицу символов
        for (Map.Entry<String, Hashtable<String, ArrayList>> table : helloWalker.TABLES.entrySet()){
            System.out.println(table);
        }

        // Генерация ТАК и запись его в файл
        ThreeAddressWalker threeAddressWalker = new ThreeAddressWalker(tree.getChild(0), helloWalker.TABLES);
        threeAddressWalker.DFS();
        try(FileWriter writer = new FileWriter(out_tac, false))
        {
            for (ArrayList<String> code : threeAddressWalker.threeAddressCode){
                String str = "";
                for (String s : code)
                    str = s != null ? str.concat(s) + " " : str.concat("");
                writer.write(str + "\n");
            }
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        // Генерация ассемблерного кода по ТАК
        Target target = new Target(helloWalker.TABLES, threeAddressWalker.threeAddressCode, out);
        target.generator();
    }
}
