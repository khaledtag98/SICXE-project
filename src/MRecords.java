import java.math.BigInteger;
import java.util.ArrayList;

public class MRecords {
    ArrayList<String> program  = new ArrayList<>();
    public  MRecords(ArrayList<String> program){
        this.program = program;

        for(int i=0; i<program.size();i++){
            String M ="M.";
            String s = program.get(i);
            String[] line = s.split(" ");
            if(line.length==3)
                getRecords(line[0], line[1], line[2]);
            else if(line.length==4)
                getRecords(line[0], line[2], line[3]);
        }
    }
    public void getRecords(String address, String instruction, String label){
        BigInteger add;
        String loc="M.";
        if(instruction.charAt(0)=='+'){
            if(label.charAt(0)=='#')
                return;

            add = new BigInteger(address, 16);
            add = add.add(new BigInteger("1",16));
            loc = loc+ String.format("%06X", add)+".05";
            System.out.println(loc);
        }

    }
}
