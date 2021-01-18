import java.util.Arrays;
import java.util.List;

public class InstructionSet {
    String[] f3 = new String[]{"ADD", "ADDF",  "AND",  "COMP", "COMPF", "DIV", "DIVF",
            "J", "JEQ", "JGT", "JLT", "JSUB", "LDA", "LDB",
            "LDCH", "LDF", "LDL", "LDS", "LDT", "LDX", "LPS", "MUL", "MULF", "OR",
            "RD",  "RSUB",   "SSK", "STL", "STA", "STB",
            "STCH", "STCHI", "STF", "STS", "STSW", "STT", "STX", "SUB", "SUBF",
            "TD",  "TIX",  "WD"};

    String[] f2 = new String[]{"ADDR", "CLEAR", "COMPR", "DIVR", "MULR", "RMO", "SHIFTL", "SHIFTR",
            "SUBR", "SVC", "TIXR"};

    String[] f1 = new String[]{"TIO", "SIO","NORM","FIX", "FLOAT", "HIO",};

    List<String>  Format3 = Arrays.asList(f3);
    List<String>  Format2 = Arrays.asList(f2);
    List<String>  Format1 = Arrays.asList(f1);


    public int getFormat(String instruction){
        if(Format3.contains(instruction))
            return 3;
        else if(Format2.contains(instruction))
            return 2;
        else if(Format1.contains(instruction))
            return 1;
        else
            return 0;
    }

    public String getOpcode(String instruction){
        if(instruction.equals("ADD"))
            return "000110";
        else if(instruction.equals("ADDF"))
            return "010110";
        else if(instruction.equals("ADDR"))
            return "90";
        else if(instruction.equals("AND"))
            return "010000";
        else if(instruction.equals("CLEAR"))
            return "B4";
        else if(instruction.equals("COMP"))
            return "001010";
        else if(instruction.equals("COMPF"))
            return "100010";
        else if(instruction.equals("COMPR"))
            return "A0";
        else if(instruction.equals("DIV"))
            return "001001";
        else if(instruction.equals("DIVF"))
            return "010101";
        else if(instruction.equals("DIVR"))
            return "9C";
        else if(instruction.equals("FIX"))
            return "C4";
        else if(instruction.equals("FLOAT"))
            return "C0";
        else if(instruction.equals("HIO"))
            return "F4";
        else if(instruction.equals("J"))
            return "001111";
        else if(instruction.equals("JEQ"))
            return "001100";
        else if(instruction.equals("JGT"))
            return "001101";
        else if(instruction.equals("JLT"))
            return "001110";
        else if(instruction.equals("JSUB"))
            return "010010";
        else if(instruction.equals("LDA"))
            return "000000";
        else if(instruction.equals("LDB"))
            return "011010";
        else if(instruction.equals("LDCH"))
            return "010100";
        else if(instruction.equals("LDF"))
            return "011100";
        else if(instruction.equals("LDL"))
            return "000010";
        else if(instruction.equals("LDS"))
            return "011011";
        else if(instruction.equals("LDT"))
            return "011101";
        else if(instruction.equals("LDX"))
            return "000001";
        else if(instruction.equals("LPS"))
            return "110100";
        else if(instruction.equals("MUL"))
            return "001000";
        else if(instruction.equals("MULF"))
            return "011000";
        else if(instruction.equals("MULR"))
            return "98";
        else if(instruction.equals("NORM"))
            return "C8";
        else if(instruction.equals("ADDF"))
            return "010110";
        else if(instruction.equals("OR"))
            return "010001";
        else if(instruction.equals("RD"))
            return "110110";
        else if(instruction.equals("ADDF"))
            return "010110";
        else if(instruction.equals("RMO"))
            return "AC";
        else if(instruction.equals("RSUB"))
            return "010011";
        else if(instruction.equals("SHIFTL"))
            return "A4";
        else if(instruction.equals("SHIFTR"))
            return "A8";
        else if(instruction.equals("SIO"))
            return "F0";
        else if(instruction.equals("SSK"))
            return "111011";
        else if(instruction.equals("STA"))
            return "000011";
        else if(instruction.equals("STB"))
            return "011110";
        else if(instruction.equals("STCH"))
            return "010101";
        else if(instruction.equals("STF"))
            return "100000";
        else if(instruction.equals("STI"))
            return "110101";
        else if(instruction.equals("STL"))
            return "000101";
        else if(instruction.equals("STS"))
            return "011111";
        else if(instruction.equals("STSW"))
            return "111010";
        else if(instruction.equals("STT"))
            return "100001";
        else if(instruction.equals("STX"))
            return "000100";
        else if(instruction.equals("SUB"))
            return "000111";
        else if(instruction.equals("SUBF"))
            return "010111";
        else if(instruction.equals("SUBR"))
            return "94";
        else if(instruction.equals("SVC"))
            return "B0";
        else if(instruction.equals("TD"))
            return "111000";
        else if(instruction.equals("TIO"))
            return "F8";
        else if(instruction.equals("TIX"))
            return "001011";
        else if(instruction.equals("TIXR"))
            return "B8";
        else if(instruction.equals("WD"))
            return "110111";
        else return
            "No Opcode";



    }
}
