package com.company;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Target {
    String out;
    LinkedList<ArrayList<String>> TAC;
    Hashtable<String, Hashtable<String, ArrayList>> TABLES;

    Map<String, String> renaming = new HashMap<>();

    String curFuncName = "";

    ArrayList<String> tempT = new ArrayList<>();
    Hashtable<String, String> tempAlternativeT = new Hashtable<>();
    ArrayList<String> tempF = new ArrayList<>();
    Hashtable<String, String> tempAlternativeF = new Hashtable<>();

    ArrayList<String> tempArgT = new ArrayList<>();
    ArrayList<String> tempArgF = new ArrayList<>();

    public Target(Hashtable<String, Hashtable<String, ArrayList>> TABLES, LinkedList<ArrayList<String>> TAC, String out) {
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

        tempArgT.add("a1");
        tempArgT.add("a2");
        tempArgT.add("a3");

        tempArgF.add("f14");
        tempArgF.add("f15");
        tempArgF.add("f16");
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
                    if (renaming.containsKey(str.get(2))) {
                        if (str.get(1).equals("int"))
                            outString = outString.concat("li $v0, 1\n").concat("lw $a0, " + renaming.get(str.get(2)) + "\n").concat("syscall\n");
                        if (str.get(1).equals("float"))
                            outString = outString.concat("li $v0, 2\n").concat("lwc1 $f12, " + renaming.get(str.get(2)) + "\n").concat("syscall\n");
                        outString = outString.concat("li $v0, 4\n").concat("la $a0, newLineChar\n").concat("syscall\n");
                    }else{
                        if (str.get(1).equals("int"))
                            outString = outString.concat("li $v0, 1\n").concat("lw $a0, " + str.get(2) + "\n").concat("syscall\n");
                        if (str.get(1).equals("float"))
                            outString = outString.concat("li $v0, 2\n").concat("lwc1 $f12, " + str.get(2) + "\n").concat("syscall\n");
                        outString = outString.concat("li $v0, 4\n").concat("la $a0, newLineChar\n").concat("syscall\n");
                    }
                }
                // metka:
                else if (str.size() == 1){
                    outString = "\n" + str.get(0) + "\n";
                    // if metka is func
                    String metkaName = str.get(0).replace(":", "");
                    if (TABLES.get("main").get(metkaName) != null){
                        curFuncName = metkaName;
                        ArrayList arr = TABLES.get("main").get(metkaName);
                        outString = outString.concat(
                                "addiu $sp, $sp, -" + ((Integer.parseInt(String.valueOf(arr.get(3))) + 1) * 4)
                                + "\nsw $ra, 0($sp)\n"
                        );
                        ArrayList argTypes = (ArrayList) arr.get(4);
                        ArrayList argNames = (ArrayList) arr.get(5);
                        int arg_a = 1;
                        int arg_f = 14;
                        int point = 4;
                        for (int i = 0; i < argTypes.size(); i++) {
                            if (argTypes.get(i).equals("int")){
                                outString = outString.concat("sw $a" + arg_a + ", " + point + "($sp)\n");
                                //outString = outString.concat("sw $a" + arg_a + ", " + argNames.get(i) + "\n");
                                renaming.put(argNames.get(i).toString(), point + "($sp)");
                                arg_a++;
                            }else{
                                outString = outString.concat("swc1 $f" + arg_f + ", " + point + "($sp)\n");
                                //outString = outString.concat("swc1 $f" + arg_f + ", " + argNames.get(i) + "\n");
                                renaming.put(argNames.get(i).toString(), point + "($sp)");
                                arg_f++;
                            }
                            point += 4;
                        }

                    }
                }
                // j
                else if (str.get(0).equals("j")){
                    outString = str.get(0) + " " + str.get(1) + "\n";
                }
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
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            if (renaming.containsKey(str.get(2)))
                                outString = "lw $s7, " + renaming.get(str.get(2))
                                        + "\nmtc1 $s7, $" + fft
                                        + "\ncvt.s.w $" + fft + ", $" + fft + "\n";
                            else
                                outString = "lw $s7, " + str.get(2)
                                    + "\nmtc1 $s7, $" + fft
                                    + "\ncvt.s.w $" + fft + ", $" + fft + "\n";
                        }
                        // int = int
                        else {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            if (renaming.containsKey(str.get(2)))
                                outString = "lw $" + fft + ", " + renaming.get(str.get(2)) + "\n";
                            else
                                outString = "lw $" + fft + ", " + str.get(2) + "\n";
                        }
                    }
                    // if variable = float
                    else if (varArr.get(0).equals("float")) {
                        // int = float
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            if (renaming.containsKey(str.get(2)))
                                outString = "lwc1 $f30, " + renaming.get(str.get(2))
                                        + "\ncvt.w.s $f30, $f30"
                                        + "\nmfc1 $" + fft + ", $f30\n";
                            else
                                outString = "lwc1 $f30, " + str.get(2)
                                        + "\ncvt.w.s $f30, $f30"
                                        + "\nmfc1 $" + fft + ", $f30\n";
                        }
                        // float = float
                        else {
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            if (renaming.containsKey(str.get(2)))
                                outString = "lwc1 $" + fft + ", " + renaming.get(str.get(2)) + "\n";
                            else
                                outString = "lwc1 $" + fft + ", " + str.get(2) + "\n";
                        }
                    }
                }
                // temp = number
                else if (str.get(0).equals("li")) {
                    // if temp = float
                    // Оптимизация!!!!!!!!!! Глава 1.2.5
                    if (str.get(2).contains(".")){
                        // if int = float
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            outString = "li $" + fft + ", " + str.get(2).split("\\.")[0] + "\n";
                        }
                        // if float = float
                        else {
                            String newStr = "";
                            if (str.get(2).length() > 10)
                                newStr = str.get(2).substring(0, 10);
                            else
                                newStr = str.get(2);

                            String _a = newStr.split("\\.")[0];                      // _.
                            String[] bArr = newStr.split("\\.")[1].split(""); // .[_]
                            int bLen = 0;
                            for (int i = 0; i < bArr.length; i++) {
                                _a = _a.concat(bArr[i]);
                                bLen++;
                                if (i == 7)
                                    break;
                            }
                            // find head 0's
                            int c = 0;
                            String[] arr = newStr.replace(".", "").split("");
                            while (arr[c].equals("0"))
                                c++;
                            _a = _a.substring(c);

                            String _b = String.valueOf((int) Math.pow(10, bLen));

                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            outString = "li $s7, " + _a
                                    + "\nmtc1 $s7, $f30"
                                    + "\ncvt.s.w $f30, $f30"
                                    + "\nli $s7, " + _b
                                    + "\nmtc1 $s7, $f31"
                                    + "\ncvt.s.w $f31, $f31"
                                    + "\ndiv.s $" + fft + ", $f30, $f31\n";
                        }
                    }
                    // if temp = int
                    else {
                        // if float = int
                        if (str.get(1).contains("f")) {
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            outString = "li $s7, " + str.get(2)
                                    + "\nmtc1 $s7, $" + fft
                                    + "\ncvt.s.w $" + fft + ", $" + fft + "\n";
                        }
                        // if int = int
                        else {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            outString = "li $" + fft + ", " + str.get(2) + "\n";
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
                        String a = tempAlternativeT.get(str.get(2));

                        tempAlternativeT.remove(str.get(2));

                        if (renaming.containsKey(str.get(1)))
                            outString = "sw $" + a + ", " + renaming.get(str.get(1)) + "\n";
                        else
                            outString = "sw $" + a + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                    }
                    // var = float
                    else if (varArr.get(0).equals("float")) {
                        String a = tempAlternativeF.get(str.get(2));

                        tempAlternativeF.remove(str.get(2));

                        if (renaming.containsKey(str.get(1)))
                            outString = "swc1 $" + a + ", " + renaming.get(str.get(1)) + "\n";
                        else
                            outString = "swc1 $" + a + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, a);
                    }
                }
                // +
                else if (str.get(0).equals("+")) {
                    if (str.get(1).contains("t")) {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "add $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                        //System.out.println(tempAlternativeT);
                    } else if (str.get(1).contains("f")) {
                        String fft = firstFreeTemp(2);
                        tempAlternativeF.put(str.get(1), fft);
                        String a = tempAlternativeF.get(str.get(2));
                        String b = tempAlternativeF.get(str.get(3));

                        tempAlternativeF.remove(str.get(2));
                        tempAlternativeF.remove(str.get(3));

                        outString = "add.s $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, b);
                    }
                }
                // -
                else if (str.get(0).equals("-")) {
                    // 0 - temp
                    if (str.get(2).equals("0")){
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            String b = tempAlternativeT.get(str.get(3));

                            tempAlternativeT.remove(str.get(3));

                            outString = "sub $" + fft + ", $zero, $" + b + "\n";

                            int o = Integer.parseInt(b.split("")[1]);
                            tempT.remove(o);
                            tempT.add(o, b);
                        } else if (str.get(1).contains("f")) {
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            String b = tempAlternativeF.get(str.get(3));

                            tempAlternativeF.remove(str.get(3));

                            outString = "lwc1 $f30, floatZero"
                                    + "\nsub.s $" + fft + ", $f30, $" + b + "\n";

                            int o = Integer.parseInt(b.split("")[1]);
                            tempF.remove(o);
                            tempF.add(o, b);
                        }
                    }
                    // temp - temp
                    else {
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            String a = tempAlternativeT.get(str.get(2));
                            String b = tempAlternativeT.get(str.get(3));

                            tempAlternativeT.remove(str.get(2));
                            tempAlternativeT.remove(str.get(3));

                            outString = "sub $" + fft + ", $" + a + ", $" + b + "\n";

                            int o = Integer.parseInt(a.split("")[1]);
                            tempT.remove(o);
                            tempT.add(o, a);
                            o = Integer.parseInt(b.split("")[1]);
                            tempT.remove(o);
                            tempT.add(o, b);
                        } else if (str.get(1).contains("f")) {
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            String a = tempAlternativeF.get(str.get(2));
                            String b = tempAlternativeF.get(str.get(3));

                            tempAlternativeF.remove(str.get(2));
                            tempAlternativeF.remove(str.get(3));

                            outString = "sub.s $" + fft + ", $" + a + ", $" + b + "\n";

                            int o = Integer.parseInt(a.split("")[1]);
                            tempF.remove(o);
                            tempF.add(o, a);
                            o = Integer.parseInt(b.split("")[1]);
                            tempF.remove(o);
                            tempF.add(o, b);
                        }
                    }
                }
                // *
                else if (str.get(0).equals("*")){
                    // int * int
                    if (str.get(1).contains("t")) {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "mul $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    // float * float
                    else if (str.get(1).contains("f")) {
                        String fft = firstFreeTemp(2);
                        tempAlternativeF.put(str.get(1), fft);
                        String a = tempAlternativeF.get(str.get(2));
                        String b = tempAlternativeF.get(str.get(3));

                        tempAlternativeF.remove(str.get(2));
                        tempAlternativeF.remove(str.get(3));

                        outString = "mul.s $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, b);
                    }
                }
                // /
                else if (str.get(0).equals("/")){
                    // int / int
                    if (str.get(1).contains("t")) {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "div $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    // float / float
                    else if (str.get(1).contains("f")) {
                        String fft = firstFreeTemp(2);
                        tempAlternativeF.put(str.get(1), fft);
                        String a = tempAlternativeF.get(str.get(2));
                        String b = tempAlternativeF.get(str.get(3));

                        tempAlternativeF.remove(str.get(2));
                        tempAlternativeF.remove(str.get(3));

                        outString = "div.s $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, b);
                    }
                }
                // param
                // int: a1-a3, float: f14-f16
                else if (str.get(0).equals("param")) {
                    if (str.get(1).contains("t")){
                        String ffArg = firstFreeArg(1);
                        String b = tempAlternativeT.get(str.get(1));
                        tempAlternativeT.remove(str.get(1));

                        outString = "move $" + ffArg + ", $" + b + "\n";

                        int o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String ffArg = firstFreeArg(2);
                        String b = tempAlternativeF.get(str.get(1));
                        tempAlternativeF.remove(str.get(1));

                        outString = "mov.s $" + ffArg + ", $" + b + "\n";

                        int o = Integer.parseInt(b.split("")[1]);
                        tempF.remove(o);
                        tempF.add(o, b);
                    }
                }
                // rc
                // int: v1, float: f13
                else if (str.get(0).equals("rc")) {
                    String funcName = str.get(2);
                    outString = "jal " + funcName + "\n";
                    if (TABLES.get("main").get(funcName).get(1).equals("int")) {
                        // int = int
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            outString = outString.concat("move $" + fft + ", $v1\n");
                        }
                        // float = int
                        else{
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            outString = outString.concat(
                                    "move $s7, $v1"
                                    + "\nmtc1 $s7, $" + fft
                                    + "\ncvt.s.w $" + fft + ", $" + fft + "\n");
                        }
                    }
                    else {
                        // int = float
                        if (str.get(1).contains("t")) {
                            String fft = firstFreeTemp(1);
                            tempAlternativeT.put(str.get(1), fft);
                            outString = outString.concat(
                                    "mov.s $f30, $f13"
                                    + "\ncvt.w.s $f30, $f30"
                                    + "\nmfc1 $" + fft + ", $f30\n");
                        }
                        // float = float
                        else{
                            String fft = firstFreeTemp(2);
                            tempAlternativeF.put(str.get(1), fft);
                            outString = outString.concat("mov.s $" + fft + ", $f13\n");
                        }
                    }
                    restoreArg(1);
                    restoreArg(2);
                }
                // vc
                // !!!!!!!!!! доделать !!!!!!!!!!!!
                else if (str.get(0).equals("vc")){
                    String funcName = str.get(2);
                    outString = "jal " + funcName + "\n";
                    restoreArg(1);
                    restoreArg(2);
                }
                // описать стек и ретурн
                else if (str.get(0).equals("re")) {
                    if (renaming.containsKey(str.get(1)))
                        if (TABLES.get("main").get(curFuncName).get(1).equals("int"))
                            outString = outString.concat("lw $v1, " + renaming.get(str.get(1)));
                        else
                            outString = outString.concat("lwc1 $f13, " + renaming.get(str.get(1)));
                    else
                        if (TABLES.get("main").get(curFuncName).get(1).equals("int"))
                            outString = outString.concat("lw $v1, " + str.get(1));
                        else
                            outString = outString.concat("lwc1 $f13, " + str.get(1));

                    outString = outString.concat("\nlw $ra, 0($sp)\n");
                    ArrayList arr = TABLES.get("main").get(curFuncName);
                    ArrayList argTypes = (ArrayList) arr.get(4);
                    int arg_a = 1;
                    int arg_f = 14;
                    int point = 4;
                    for (int i = 0; i < argTypes.size(); i++) {
                        if (argTypes.get(i).equals("int")){
                            outString = outString.concat("lw $a" + arg_a + ", " + point + "($sp)\n");
                            arg_a++;
                        }else{
                            outString = outString.concat("lwc1 $f" + arg_f + ", " + point + "($sp)\n");
                            arg_f++;
                        }
                        point += 4;
                    }
                    outString = outString.concat("addiu $sp, $sp, " + ((Integer.parseInt(String.valueOf(arr.get(3))) + 1) * 4));
                    outString = outString.concat("\njr $ra\n");
                }
                // >=
                else if (str.get(0).equals(">=")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "bge $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "sge $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                }
                // <=
                else if (str.get(0).equals("<=")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "ble $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "sle $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                }
                // >
                else if (str.get(0).equals(">")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "bgt $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "sgt $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                }
                // <
                else if (str.get(0).equals("<")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "blt $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "slt $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                }
                // ==
                else if (str.get(0).equals("==")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "beq $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "seq $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                }
                // !=
                else if (str.get(0).equals("!=")) {
                    if (str.get(1).contains("L")) {
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "bne $" + a + ", $" + b + ", " + str.get(1) + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
                    else {
                        String fft = firstFreeTemp(1);
                        tempAlternativeT.put(str.get(1), fft);
                        String a = tempAlternativeT.get(str.get(2));
                        String b = tempAlternativeT.get(str.get(3));

                        tempAlternativeT.remove(str.get(2));
                        tempAlternativeT.remove(str.get(3));

                        outString = "sne $" + fft + ", $" + a + ", $" + b + "\n";

                        int o = Integer.parseInt(a.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, a);
                        o = Integer.parseInt(b.split("")[1]);
                        tempT.remove(o);
                        tempT.add(o, b);
                    }
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

    private void restoreArg(int way) {
        if (way == 1){
            tempArgT.clear();
            tempArgT.add("a1");
            tempArgT.add("a2");
            tempArgT.add("a3");
        } else if (way == 2){
            tempArgF.clear();
            tempArgF.add("f14");
            tempArgF.add("f15");
            tempArgF.add("f16");
        }
    }

    private String firstFreeArg(int way) {
        if (way == 1) {
            for (int i = 0; i < tempArgT.size(); i++)
                if (tempArgT.get(i) != null) {
                    String ret = tempArgT.get(i);
                    tempArgT.remove(i);
                    tempArgT.add(i, null);
                    return ret;
                }
        } else if (way == 2) {
            for (int j = 0; j < tempArgF.size(); j++)
                if (tempArgF.get(j) != null) {
                    String ret = tempArgF.get(j);
                    tempArgF.remove(j);
                    tempArgF.add(j, null);
                    return ret;
                }
        }
        return null;
    }

    private String firstFreeTemp(int way) {
        if (way == 1) {
            for (int i = 0; i < tempT.size(); i++)
                if (tempT.get(i) != null) {
                    String ret = tempT.get(i);
                    tempT.remove(i);
                    tempT.add(i, null);
                    return ret;
                }
        } else if (way == 2) {
            for (int j = 0; j < tempF.size(); j++)
                if (tempF.get(j) != null) {
                    String ret = tempF.get(j);
                    tempF.remove(j);
                    tempF.add(j, null);
                    return ret;
                }
        }
        return null;
    }

    private ArrayList getVarArrFromTables(String varName) {
        for (Map.Entry<String, Hashtable<String, ArrayList>> table : TABLES.entrySet())
            for (Map.Entry<String, ArrayList> var : table.getValue().entrySet())
                if (var.getKey().equals(varName)) {
                    return var.getValue();
                }
        return null;
    }
}
