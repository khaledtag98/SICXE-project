import java.util.*;

public class Main {
    public static void main(String[] args){
        ArrayList<String> LOCCTR = new ArrayList<>();
        ArrayList<String> symbols = new ArrayList<>();
        ArrayList<String> Literals = new ArrayList<>();
        pass1 p1 = new pass1();
        p1.read_file();
        LOCCTR = p1.Generate_LOCCR();
        symbols= p1.Symbol_table();
        Literals = p1.Literals();
        p1.finalIteration();
        p1.Literals();
        ObjectCode ob = new ObjectCode(LOCCTR, symbols, Literals);
        Records Records = new Records(ob.ObjectCodeList());

    }


}
