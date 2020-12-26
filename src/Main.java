import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args){

        pass1 p1 = new pass1();
        p1.read_file();
        p1.Generate_LOCCR();
        p1.Symbol_table();

    }


}
