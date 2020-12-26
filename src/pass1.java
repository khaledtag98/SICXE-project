import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.math.BigInteger;

public class pass1 {

    ArrayList<String> prog;
    ArrayList<String> symbols = new ArrayList<>();
    ArrayList<String> literals = new ArrayList<>();
    BigInteger start;
    String startAddress = "";
    String litStart = startAddress;
    String lit="";
    String size="";
    String add = "";

    public ArrayList<String> read_file() {
        //System.out.println("PASS 1 READ: \n");
        prog = new ArrayList<>();
        //Read file
        try {

            File myObj = new File("in3.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.charAt(0) != '.') {
//                    System.out.println(data);
                    prog.add(data);
                } else {
                    continue;
//                    data = data.substring(1);
//                    System.out.println("{{ " + data + " }}");
                }

            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return prog;
    }

    public ArrayList<String> Generate_LOCCR() {

        String progName = "";
        for (int j = 0; j < prog.size(); j++) {
            String sWithoutComments ="";
            String sWithComments = prog.get(j);
            String[] CommentIndecator = sWithComments.split(" ");
            for (int k = 0; k < CommentIndecator.length; k++) {
                if (CommentIndecator[k].charAt(0) != '.') {
                    if (k != 0){
                        sWithoutComments = sWithoutComments + " " + CommentIndecator[k];
                    }else sWithoutComments = CommentIndecator[k];

                } else
                    break;
            }
            prog.set(j, sWithoutComments);
          // System.out.println(prog.get(j));
        }


        for (int i = 0; i < prog.size(); i++) {
            int k =i+1;
            String s = prog.get(i);
            String[] line = s.split(" ");

            // START CHECKING FOR ERRORS
            if(i==0 && !line[1].equals("Start")){
                System.out.println("Program failed to start.");
                System.exit(0);
            }

            else if(line.length == 2){
                if(line[0].charAt(0) == '+'){
                    if(!validateInstruction(line[0].substring(1))){
                        System.out.println("Instruction " + line[0].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(line[0].charAt(0) == '$'){
                    if(!validateInstruction(line[0].substring(1))){
                        System.out.println("Instruction " + line[0].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(!validateInstruction(line[0])){
                    System.out.println("Instruction " + line[0] +" at line "+ k + " doesn't exist");
                    System.exit(0);
                }
            }
            else if(line.length == 3 && i!=0){
                if(line[1].charAt(0) == '+'){
                    if(!validateInstruction(line[1].substring(1))){
                        System.out.println("Instruction " + line[1].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(line[1].charAt(0) == '$'){
                    if(!validateInstruction(line[1].substring(1))){
                        System.out.println("Instruction " + line[1].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(!validateInstruction(line[1])){
                    System.out.println("Instruction " + line[1] +" at line "+ k + " doesn't exist");
                    System.exit(0);
                }
            }
            else if(line.length==1 && i!=0){
                if(line[0].charAt(0) == '+'){
                    if(!validateInstruction(line[0].substring(1))){
                        System.out.println("Instruction " + line[0].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(line[0].charAt(0) == '$'){
                    if(!validateInstruction(line[0].substring(1))){
                        System.out.println("Instruction " + line[0].substring(1) +" at line "+ k + " doesn't exist");
                        System.exit(0);
                    }
                }
                else if(!validateInstruction(line[0])){
                    System.out.println("Instruction " + line[0] +" at line "+ k + " doesn't exist");
                    System.exit(0);
                }
            }


            //START GENERATING LOCATION COUNTER
            if(line.length>1){
                if (line[1].equals("Start")) {
                    start = new BigInteger(line[2], 16);
                    String addressHex = String.format("%04X", start);
                    s = addressHex + " " + s;
                    startAddress = addressHex;
                    prog.set(i, s);
                } else if(line[0].equals("BASE") || line[0].equals("Base")){
                    continue;
                } else if(line[0].equals("End") || line[0].equals("END")){
                    break;
                }
                else if (line[1].equals("RESW") && Integer.parseInt(line[2]) > 1) {
                    int address = Integer.parseInt(line[2]) * 3;
                    String num = Integer.toHexString(address);
                    BigInteger n = new BigInteger(num, 16);
                    String addressHex = String.format("%04X", start);
                    s = addressHex + " " + s;
                    prog.set(i, s);
                    start = start.add(n);
                }
                else if (line[1].equals("RESDW") && Integer.parseInt(line[2]) > 1) {
                    int address = Integer.parseInt(line[2]) * 6;
                    String num = Integer.toHexString(address);
                    BigInteger n = new BigInteger(num, 16);
                    String addressHex = String.format("%04X", start);
                    s = addressHex + " " + s;
                    prog.set(i, s);
                    start = start.add(n);
                }
                else if (line[1].equals("RESB")) {
                    int address = Integer.parseInt(line[2]);
                    String num = Integer.toHexString(address);
                    BigInteger n = new BigInteger(num, 16);
                    String addressHex = String.format("%04X", start);
                    s = addressHex + " " + s;
                    prog.set(i, s);
                    start = start.add(n);
                }

                else if (i == 1) {
                    prog.set(i, startAddress + " " + s);
                    start = start.add(new BigInteger("3", 16));
                }else if (line[1].equals("EQU")) {
                    if (line[2].equals("*")){
                        String addressHex = String.format("%04X", start);
                        s = addressHex + " " + s;
                        prog.set(i, s);
                    }
                    else{
                        s = "----" + " " + s;
                        prog.set(i, s);
                    }

                } else {

                    String addressHex = String.format("%04X", start);
                    s = addressHex + " " + s;
                    prog.set(i, s);
                    if(line.length==3){
                        //FORMAT 4 INSTRUCTIONS
                        if(line[1].charAt(0) == '+'){
                            start = start.add(new BigInteger("4", 16));
                        }
                        //FORMAT 2 INSTRUCTIONS
                        else if(line[1].equals("ADDR") || line[1].equals("COMPR") || line[1].equals("CLEAR") || line[1].equals("DIVR")
                                || line[1].equals("MULR") || line[1].equals("RMO") || line[1].equals("SHIFTL") | line[1].equals("SHIFTR")
                                || line[1].equals("SUBR") || line[1].equals("SVC") || line[1].equals("TIXR")){

                            start = start.add(new BigInteger("2", 16));

                        }
                        else if(line[1].equals("BYTE") || line[1].equals("FIX") || line[1].equals("FLOAT") || line[1].equals("HIO")
                                || line[1].equals("NORM") || line[1].equals("SIO") || line[1].equals("TIO")){

                            if(line[1].equals("BYTE") && line[2].charAt(0)=='C' && line[2].charAt(1)== 39 ){
                                int n = line[2].substring(2,line[2].length()-1).length();
                                String num = Integer.toHexString(n);
                                BigInteger x = new BigInteger(num, 16);
                                start = start.add(x);
                            }
                            else if(line[1].equals("BYTE") && line[2].charAt(0)=='X' && line[2].charAt(1)== 39 ){
                                int n = line[2].substring(2,line[2].length()-1).length()/2;
                                String num = Integer.toHexString(n);
                                BigInteger x = new BigInteger(num, 16);
                                start = start.add(x);
                            }
                            else
                                start = start.add(new BigInteger("1", 16));
                        }
                        else{
                            start = start.add(new BigInteger("3", 16));
                        }
                    }
                    else if(line.length==2){
                        if(line[0].charAt(0)=='+'){
                            start = start.add(new BigInteger("4", 16));
                        }
                        else if(line[0].equals("ADDR") || line[0].equals("COMPR") || line[0].equals("CLEAR") || line[0].equals("DIVR")
                                || line[0].equals("MULR") || line[0].equals("RMO") || line[0].equals("SHIFTL") | line[0].equals("SHIFTR")
                                || line[0].equals("SUBR") || line[0].equals("SVC") || line[0].equals("TIXR")){

                            start = start.add(new BigInteger("2", 16));

                        }
                        else if(line[0].equals("BYTE") || line[0].equals("FIX") || line[0].equals("FLOAT") || line[0].equals("HIO")
                                || line[0].equals("NORM") || line[0].equals("SIO") || line[0].equals("TIO")){

                            if(line[0].equals("BYTE") && line[1].charAt(0)=='C' && line[1].charAt(1)== 39 ){
                                int n = line[1].substring(2,line[1].length()-1).length();
                                String num = Integer.toHexString(n);
                                BigInteger x = new BigInteger(num, 16);
                                start = start.add(x);
                            }
                            else if(line[0].equals("BYTE") && line[1].charAt(0)=='X' && line[1].charAt(1)== 39){
                                int n = line[1].substring(2,line[1].length()-1).length()/2;
                                String num = Integer.toHexString(n);
                                BigInteger x = new BigInteger(num, 16);
                                start = start.add(x);
                            }
                            else
                                start = start.add(new BigInteger("1", 16));
                        }

                        else{
                            start = start.add(new BigInteger("3", 16));
                        }
                    }


                }
            }
            else {
                String addressHex = String.format("%04X", start);
                s = addressHex + " " + s;
                prog.set(i, s);
                if(line[0].equals("ADDR") || line[0].equals("COMPR") || line[0].equals("CLEAR") || line[0].equals("DIVR")
                        || line[0].equals("MULR") || line[0].equals("RMO") || line[0].equals("SHIFTL") | line[0].equals("SHIFTR")
                        || line[0].equals("SUBR") || line[0].equals("SVC") || line[0].equals("TIXR")){
                    start = start.add(new BigInteger("2", 16));
                }
                else if(line[0].charAt(0)=='+'){
                    start = start.add(new BigInteger("4", 16));
                }
                else{
                    start = start.add(new BigInteger("3", 16));
                }
            }

        }
        try{
            File file = new File("LOCCTR.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (int i = 0; i < prog.size(); i++) {
                osw.write(prog.get(i)+"\n");
            }
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return prog;
    }

    public ArrayList<String> Symbol_table() {
        for (int i = 1; i < prog.size(); i++) {
            String instruction = prog.get(i);
            String[] line = instruction.split(" ");
            if (line.length > 3) {
                symbols.add(line[0] + " " + line[1]);
            }
        }

        try{
            File file = new File("SYMBOLS.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (int i = 0; i < symbols.size(); i++) {
                osw.write(symbols.get(i)+"\n");
            }
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return literals;
    }

    public ArrayList<String> Literals() {

        int start=1;
        int flagPosition;
        int noLits = 1;
        for (int i = 1; i < prog.size(); i++) {
            String instruction = prog.get(i);
            String[] line = instruction.split(" ");
            if(line[1].equals("LTORG")){
                flagPosition = i;
                add = line[0];
                for(int j = start; j<flagPosition; j++){
                    String instruction2 = prog.get(j);
                    String[] line2 = instruction2.split(" ");
                    if(line2.length==4){
                        if(line2[3].charAt(0)== 61){
                            lit = line2[3].substring(1);
                            if(line2[3].charAt(1)=='X' && line2[3].charAt(2)==39){
                                size = Integer.toHexString(line2[3].substring(2,line2[3].length()-2).length()/2);
                                BigInteger sizeHex = new BigInteger(size, 16);
                                BigInteger addHex = new BigInteger(add, 16);
                                String addressHex = String.format("%04X", addHex);
                                literals.add(lit+" "+ size+" "+ addressHex);
                                addHex = addHex.add(sizeHex);
                                addressHex = String.format("%04X", addHex);
                                add = addressHex;

                            }
                            else if(line2[3].charAt(1)=='C' && line2[3].charAt(2)==39){
                                size = Integer.toHexString(line2[2].substring(2,line2[2].length()-2).length());

                                BigInteger sizeHex = new BigInteger(size, 16);
                                BigInteger addHex = new BigInteger(add, 16);
                                String addressHex = String.format("%04X", addHex);
                                literals.add(lit+" "+ size+" "+ addressHex);
                                addHex = addHex.add(sizeHex);
                                addressHex = String.format("%04X", addHex);
                                add = addressHex;
                            }


                        }
                    }
                     else if(line2.length==3){
                        if(line2[2].charAt(0)== 61){
                            lit = line2[2].substring(1);

                            if(line2[2].charAt(1)=='X' && line2[2].charAt(2)==39){
                                size = Integer.toHexString(line2[2].substring(2,line2[2].length()-2).length()/2);
                                BigInteger sizeHex = new BigInteger(size, 16);
                                BigInteger addHex = new BigInteger(add, 16);
                                String addressHex = String.format("%04X", addHex);
                                literals.add(lit+" "+ size+" "+ addressHex);
                                addHex = addHex.add(sizeHex);
                                addressHex = String.format("%04X", addHex);
                                add = addressHex;

                            }
                            else if(line2[2].charAt(1)=='C' && line2[2].charAt(2)==39){
                                size = Integer.toHexString(line2[2].substring(2,line2[2].length()-2).length());

                                BigInteger sizeHex = new BigInteger(size, 16);
                                BigInteger addHex = new BigInteger(add, 16);
                                String addressHex = String.format("%04X", addHex);
                                literals.add(lit+" "+ size+" "+ addressHex);
                                addHex = addHex.add(sizeHex);
                                addressHex = String.format("%04X", addHex);
                                add = addressHex;
                            }


                        }
                    }
                }
                start = flagPosition+1;
            }
            else{
                noLits=0;
            }

        }

        try{
            File file = new File("LITERALS TABLE.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            if(noLits==0){
               for (int i = 0; i < literals.size(); i++) {
                  osw.write(literals.get(i)+"\n");
               }
            }
            else{
                osw.write("No Literals handled");
            }
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return literals;
    }

    public boolean validateInstruction(String s){
        String[] instructions = new String[]{"ADD", "ADDF", "ADDR", "AND", "CLEAR", "COMP", "COMPF", "COMPR", "DIV", "DIVF",
                "DIVR", "FIX", "FLOAT", "HIO", "J", "JEQ", "JGT", "JLT", "JSUB", "LDA", "LDB",
                "LDCH", "LDF", "LDL", "LDS", "LDT", "LDX", "LPS", "MUL", "MULF", "MULR", "OR",
                "NORM", "RD", "RMO", "RSUB", "SHIFTL", "SHIFTR", "SIO", "SSK", "STL", "STA", "STB",
                "STCH", "STCHI", "STF", "STS", "STSW", "STT", "STX", "SUB", "SUBF", "SUBR", "SVC",
                "TD", "TIO", "TIX", "TIXR", "WD"};

        List<String> instructionSet = Arrays.asList(instructions);
        if(instructionSet.contains(s) || s.equals("BASE")
                || s.equals("Base") || s.equals("END") || s.equals("End") || s.equals("EQU")
                || s.equals("Equ") || s.equals("RESW") || s.equals("RESDW") || s.equals("RESB")
                || s.equals("WORD") || s.equals("BYTE") || s.equals("LTORG"))
            return true;
        else
            return false;

    }

}
