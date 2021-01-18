import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args){
        ArrayList<String> LOCCTR = new ArrayList<>();
        ArrayList<String> symbols = new ArrayList<>();
        pass1 p1 = new pass1();
        p1.read_file();
        LOCCTR = p1.Generate_LOCCR();
        symbols= p1.Symbol_table();
        p1.finalIteration();
        p1.Literals();
        ObjectCode ob = new ObjectCode(LOCCTR, symbols);

    }


}
