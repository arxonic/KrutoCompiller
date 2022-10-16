package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public class Target2 {
    String out;
    LinkedList<ArrayList<String>> TAC;
    Hashtable<String, Hashtable<String, ArrayList>> TABLES;

    ArrayList<String> tempT = new ArrayList<>();
    Hashtable<String, String> tempAlternativeT = new Hashtable<>();
    ArrayList<String> tempF = new ArrayList<>();
    Hashtable<String, String> tempAlternativeF = new Hashtable<>();

    public Target2(Hashtable<String, Hashtable<String, ArrayList>> TABLES, LinkedList<ArrayList<String>> TAC, String out) {
        this.TABLES = TABLES;
        this.TAC = TAC;
        this.out = out;

        tempT.add("t0");
        tempT.add("t1");
        tempT.add("t2");
        tempT.add("t3");
        tempT.add("t4");
        tempT.add("t5");
        tempT.add("t6");
        tempT.add("t7");
        tempT.add("t8");
        tempT.add("t9");

        tempF.add("f0");
        tempF.add("f1");
        tempF.add("f2");
        tempF.add("f3");
        tempF.add("f4");
        tempF.add("f5");
        tempF.add("f6");
        tempF.add("f7");
        tempF.add("f8");
        tempF.add("f9");
    }


    public void generator() {
        try(FileWriter writer = new FileWriter(out, false))
        {
            writer.write(".data\n");
            writer.write("newLineChar: .byte '\\n'\n");
            writer.write("floatZero: .float 0.0\n");
            for (Map.Entry<String, Hashtable<String, ArrayList>> table : TABLES.entrySet()){
                for (Map.Entry<String, ArrayList> var : table.getValue().entrySet()){
                    if (var.getValue().get(0) != null){
                        String str = "";
                        str = str.concat(var.getKey() + ": ");
                        if (var.getValue().get(0).equals("float"))
                            str = str.concat(".float 0.0\n");
                        if (var.getValue().get(0).equals("int"))
                            str = str.concat(".word 0\n");
                        writer.write(str);
                    }
                }
            }

            writer.write("\n.text\nj main\n");
            for (ArrayList<String> str : TAC){
                String outString = "";

                if (str.get(0).equals("print")){
                    if (str.get(1).equals("int"))
                        outString = outString.concat("li $v0, 1\n").concat("lw $a0, " + str.get(2) + "\n").concat("syscall\n");
                    if (str.get(1).equals("float"))
                        outString = outString.concat("li $v0, 2\n").concat("lwc1 $f12, " + str.get(2) + "\n").concat("syscall\n");
                    outString = outString.concat("li $v0, 4\n").concat("la $a0, newLineChar\n").concat("syscall\n");
                }
                else if (str.size() == 1){
                    outString = "\n" + str.get(0) + "\n";
                }
                else if (str.get(0).equals("j")){
                    outString = str.get(0) + " " + str.get(1) + "\n";
                }
                // +
                // temp = variable
                // $s7 and $f30, $f31 needs for (int to float and vice versa)
                else if (str.get(0).equals("lw")){
                    ArrayList varArr = getVarArrFromTables(str.get(2));
                    if (varArr == null)
                        System.exit(5);
                    // if variable = int
                    if (varArr.get(0).equals("int")) {
                        // float = int
                        if (str.get(1).contains("f")) {
                            outString = "lw $s7, " + str.get(2)
                                    + "\nmtc1 $s7, $" + str.get(1)
                                    + "\ncvt.s.w $" + str.get(1) + ", $" + str.get(1) + "\n";
                        }
                        // int = int
                        else {
                            outString = "lw $" + str.get(1) + ", " + str.get(2) + "\n";
                        }
                    }
                    // if variable = float
                    else if (varArr.get(0).equals("float")) {
                        // int = float
                        if (str.get(1).contains("t")) {
                            outString = "lwc1 $f30, " + str.get(2)
                                    + "\ncvt.w.s $f30, $f30"
                                    + "\nmfc1 $" + str.get(1) + ", $f30\n";
                        }
                        // float = float
                        else {
                            outString = "lwc1 $" + str.get(1) + ", " + str.get(2) + "\n";
                        }
                    }
                }
                // temp = number
                else if (str.get(0).equals("li")) {
                    // if temp = float
                    if (str.get(2).contains(".")){
                        // if int = float
                        if (str.get(1).contains("t")) {
                            outString = "li $" + str.get(1) + ", " + str.get(2).split("\\.")[0] + "\n";
                        }
                        // if float = float
                        else {
                            String newStr = "";
                            if (str.get(2).length() > 10)
                                newStr = str.get(2).substring(0, 10);
                            else
                                newStr = str.get(2);

                            String a = newStr.split("\\.")[0];                      // _.
                            String[] bArr = newStr.split("\\.")[1].split(""); // .[_]
                            int bLen = 0;
                            for (int i = 0; i < bArr.length; i++) {
                                a = a.concat(bArr[i]);
                                bLen++;
                                if (i == 7)
                                    break;
                            }
                            // find head 0's
                            int c = 0;
                            String[] arr = newStr.replace(".", "").split("");
                            while (arr[c].equals("0"))
                                c++;
                            a = a.substring(c);

                            String b = String.valueOf((int) Math.pow(10, bLen));
                            outString = "li $s7, " + a
                                    + "\nmtc1 $s7, $f30"
                                    + "\ncvt.s.w $f30, $f30"
                                    + "\nli $s7, " + b
                                    + "\nmtc1 $s7, $f31"
                                    + "\ncvt.s.w $f31, $f31"
                                    + "\ndiv.s $" + str.get(1) + ", $f30, $f31\n";
                        }
                    }
                    // if temp = int
                    else {
                        // if float = int
                        if (str.get(1).contains("f")) {
                            outString = "li $s7, " + str.get(2)
                                    + "\nmtc1 $s7, $" + str.get(1)
                                    + "\ncvt.s.w $" + str.get(1) + ", $" + str.get(1) + "\n";
                        }
                        // if int = int
                        else {
                            outString = "li $" + str.get(1) + ", " + str.get(2) + "\n";
                        }
                    }
                }
                // variable = temp
                else if (str.get(0).equals("sw")) {
                    ArrayList varArr = getVarArrFromTables(str.get(1));
                    if (varArr == null)
                        System.exit(5);
                    // var = int
                    if (varArr.get(0).equals("int")) {
                        outString = "sw $" + str.get(2) + ", " + str.get(1) + "\n";
                    }
                    // var = float
                    else if (varArr.get(0).equals("float")) {
                        outString = "swc1 $" + str.get(2) + ", " + str.get(1) + "\n";
                    }
                }
                // +
                else if (str.get(0).equals("+")) {
                    if (str.get(1).contains("t")) {
                        outString = "add $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                    } else if (str.get(1).contains("f")) {
                        outString = "add.s $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                    }
                }
                // -
                else if (str.get(0).equals("-")) {
                    // 0 - temp
                    if (str.get(2).equals("0")){
                        if (str.get(1).contains("t")) {
                            outString = "sub $" + str.get(1) + ", $zero, $" + str.get(3) + "\n";
                        } else if (str.get(1).contains("f")) {
                            outString = "lwc1 $f30, floatZero"
                                    + "\nsub.s $" + str.get(1) + ", $f30, $" + str.get(3) + "\n";
                        }
                    }
                    // temp - temp
                    else {
                        if (str.get(1).contains("t")) {
                            outString = "sub $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                        } else if (str.get(1).contains("f")) {
                            outString = "sub.s $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                        }
                    }
                }
                // *
                else if (str.get(0).equals("*")){
                    // int * int
                    if (str.get(1).contains("t"))
                        outString = "mul $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                    // float * float
                    else if (str.get(1).contains("f"))
                        outString = "mul.s $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                }
                // /
                else if (str.get(0).equals("/")){
                    // int / int
                    if (str.get(1).contains("t")) {
                        outString = "div $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                    }
                    // float / float
                    else if (str.get(1).contains("f")) {
                        outString = "div.s $" + str.get(1) + ", $" + str.get(2) + ", $" + str.get(3) + "\n";
                    }
                }
                // rc
                else if (str.get(0).equals("rc")) {
                    outString = "\n";
                }


                writer.write(outString);
            }
            writer.write("#EXIT\nli $v0, 10\nsyscall");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

    }

    private ArrayList getVarArrFromTables(String varName) {
        for (Map.Entry<String, Hashtable<String, ArrayList>> table : TABLES.entrySet())
            for (Map.Entry<String, ArrayList> var : table.getValue().entrySet())
                if (var.getKey().equals(varName))
                    return var.getValue();
        return null;
    }
}
