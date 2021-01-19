import javax.print.attribute.standard.JobKOctets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class ObjectCode {
    ArrayList<String> LOCCTR = new ArrayList<>();
    ArrayList<String> symbols = new ArrayList<>();
    ArrayList<String> ObjectCode = new ArrayList<>();
    ArrayList<String> Literals = new ArrayList<>();
    InstructionSet instructionSet = new InstructionSet();
    String n,i,x,b,p,e;
    String RA="", RX="", RL="", RB="", RS="", RT="", RF="", SW="";
    String OP;
    String displacement;
    String label;
    String address;
    String labelAddress;
    String baseLabel;
    String baseAddress;
    String PC;
    String R1, R2;
    String ObCode;
    String n1="0";
    String n2="0";
    boolean isNumber;
    int imm;
    int errorFlag =0;
    int format;
    String binaryObjectCode;
    public ObjectCode(ArrayList<String> LOCCTR, ArrayList<String> symbols, ArrayList<String> Literals ) {
        this.LOCCTR = LOCCTR;
        this.symbols = symbols;
        this.Literals = Literals;
        calculateObjectCode();
        //calculateDisplacement5("007");
        //getObjectHexa("0110001011001111");
        printOBCODE();
    }

    public void calculateObjectCode(){
        getBaseAddress();
        ObjectCode.add(0, LOCCTR.get(0)+" "+"No Object Code");
        for(int j=1; j<LOCCTR.size();j++){
            String s = LOCCTR.get(j);
            String[] line = s.split(" ");
            if(line[0].equals("BASE"))
            {
                s= s + " No Object Code.";
                ObjectCode.add(s);

            }
            if(line.length==2){
                if(instructionSet.getFormat(line[1])==1){
                    OP = instructionSet.getOpcode(line[1]);
                    ObCode = OP;
                    s = s+" "+ObCode;
                    ObjectCode.add(s);
                    continue;
                }
                else if(line[1].equals("RSUB")){
                    OP = instructionSet.getOpcode(line[1]);
                    n="1";
                    i="1";
                    b="0";
                    p="0";
                    x="0";
                    displacement="000";
                    ObCode = getObjectHexa(OP+n+i+x+b+p+e)+displacement;
                    s = s+" "+ObCode;
                    ObjectCode.add(s);
                    continue;
                }

            }

            if(line.length==3){
                int k =j+1;
                if(k>=LOCCTR.size())
                    break;
                String instruction = filterInstruction(line[1]);
                if(instruction.equals("RESB") || instruction.equals("RESW") || instruction.equals("EQU")|| instruction.equals("RESDW")){
                    ObCode = "No Object Code";
                    s = s+" "+ObCode;
                    ObjectCode.add(s);
                    continue;
                }


                if(instruction.equals("BYTE") || instruction.equals("WORD")){
                    if(line[2].charAt(0)=='C' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                        String Chars = line[2].substring(2, line[2].length()-2);
                        ObCode = getAsciiCode(Chars);
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }
                    else if(line[2].charAt(0)=='X' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                        String Chars = line[2].substring(2, line[2].length()-2);
                        ObCode = Chars;
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }
                    else{
                        String o = decimalToHexa(Integer.parseInt(line[2]));
                        BigInteger bigInteger = new BigInteger(o, 16);
                        String bb = String.format("%06X", bigInteger);
                        ObCode = bb;
                        s=s+" "+ ObCode;
                        ObjectCode.add( s);
                    }
                }



                    if (getPC(LOCCTR.get(k)).equals("BASE")) {
                        k++;
                        PC = getPC(LOCCTR.get(k));
                    }

                else PC = getPC(LOCCTR.get(k));
                OP = instructionSet.getOpcode(instruction);
              if(line[2].charAt(line[2].length()-1)=='X' && line[2].charAt(line[2].length()-2)==',' ){
                  x="1";
              }
              else x="0";
              if(instructionSet.getFormat(instruction)==3){
                  if(line[1].charAt(0)=='$')
                      format = 5;
                  else if(line[1].charAt(0)=='+')
                      format = 4;
                  else
                      format = 3;

                  label = filterLabel(line[2]);
                  labelAddress = getLabelAddress(label);
              }
              else
                  format = instructionSet.getFormat(instruction);

              if(format==3 || format == 4){
                  if(line[2].charAt(0)=='#'){
                      i="1"; n="0";
                  }
                  else if(line[2].charAt(0)=='@'){
                      n="1"; i="0";
                  }
                  else{
                      n="1"; i="1";
                  }
                  if(line[2].charAt(0)=='C' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                      String Chars = line[2].substring(2, line[2].length()-2);
                      ObCode = getAsciiCode(Chars);
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
                  else if(line[2].charAt(0)=='X' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                      String Chars = line[2].substring(2, line[2].length()-2);
                      ObCode = Chars;
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
                  if(isInteger(label)){
                      b="0";
                      p="0";
                      if(format==4)
                          e="1";
                      else
                          e="0";
                      if(format==4 && label.length()<5){
                          displacement= extend(decimalToHexa(Integer.parseInt(label)));
                      }else
                          displacement=decimalToHexa(Integer.parseInt(label));

                      ObCode = getObjectHexa(OP+n+i+x+b+p+e) +displacement;
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;

                  }
                  if(format==4){
                      b="0";
                      p="0";
                      e="1";
                      displacement = extend(labelAddress);
                      ObCode = getObjectHexa(OP+n+i+x+b+p+e) +displacement;
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
                  if(format==3){
                      e="0";
                      //System.out.println("LABEL: "+labelAddress);
                      if(line[2].charAt(0)=='=' && line[2].charAt(1)=='C' && line[2].charAt(2)==39&& line[2].charAt(line[2].length()-1)==39){
                          getLiteralAddress(line[2].substring(1));
                          calculateDisplacement3(labelAddress, PC);
                          ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                          s = s+" "+ObCode;
                          ObjectCode.add(s);
                          continue;
                      }
                      else if(line[2].charAt(0)=='=' && line[2].charAt(1)=='X' && line[2].charAt(2)==39&& line[2].charAt(line[2].length()-1)==39){
                          getLiteralAddress(line[2].substring(1));
                          calculateDisplacement3(labelAddress, PC);
                          ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                          s = s+" "+ObCode;
                          ObjectCode.add(s);
                          continue;
                      }
                      else {
                          try {
                              calculateDisplacement3(labelAddress, PC);
                          } catch (NumberFormatException ex) {
                              System.out.println("ITERATION " + j);
                          }
                          if (errorFlag == 1) {
                              System.out.println("Format 3 is not compatible on line " + j);
                              System.exit(1);
                          }
                          ObCode = getObjectHexa(OP + n + i + x + b + p + e) + displacement;
                          s = s + " " + ObCode;
                          ObjectCode.add(s);
                          continue;

                      }
                  }
              }
              else if(format==5){
                  e="0";
                  if(line[2].charAt(0)=='C' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                      String Chars = line[2].substring(2, line[2].length()-2);
                      ObCode = getAsciiCode(Chars);
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
                  else if(line[2].charAt(0)=='X' && line[2].charAt(1)==39 && line[2].charAt(line[2].length()-1)==39){
                      String Chars = line[2].substring(2, line[2].length()-2);
                      ObCode = Chars;
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
                  else if(line[2].charAt(0)=='=' && line[2].charAt(1)=='C' && line[2].charAt(2)==39&& line[2].charAt(line[2].length()-1)==39){
                      getLiteralAddress(line[2].substring(1));
                  }
                  else if(line[2].charAt(0)=='=' && line[2].charAt(1)=='X'  &&line[2].charAt(2)==39&& line[2].charAt(line[2].length()-1)==39){
                      getLiteralAddress(line[2].substring(1));

                  }
                  else if(line[2].charAt(0)=='#'|| line[2].charAt(0)=='@'){
                      System.out.println("Format 5 doesn't support indirect or immediate addressing");
                      System.exit(1);
                  }
                  calculateDisplacement3(labelAddress, PC);
                  calculateDisplacement5();
                  ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                  s = s+" "+ObCode;
                  ObjectCode.add(s);
                  continue;

              }
             else if(format==2){
                  if(line[2].length()>1) {
                      if (line[2].charAt(line[2].length() - 2) == ',') {
                          String registers[] = line[2].split(",");
                          R1 = getRegNo(registers[0]);
                          R2 = getRegNo(registers[1]);
                          ObCode = OP + R1 + R2;
                          s = s+" "+ObCode;
                          ObjectCode.add(s);
                          continue;
                      }
                  }
                  else{
                      if(instruction.equals("CLEAR")){
                          assignRegister(line[2], "0");
                      }
                      R1 = getRegNo(line[2]);
                      R2 = "0";
                      ObCode = OP + R1 + R2;
                      s = s+" "+ObCode;
                      ObjectCode.add(s);
                      continue;
                  }
              }
              else if(format==1){
                  ObCode = OP;
                  s = s+" "+ObCode;
                  ObjectCode.add(s);
                  continue;
              }

            }
            else if(line.length==4){
                    int d =j+1;
                    if(d>=LOCCTR.size())
                        break;
                    String instruction = filterInstruction(line[2]);
                if(instruction.equals("RESB") || instruction.equals("RESW") || instruction.equals("EQU") || instruction.equals("RESDW" )){
                    ObCode = "No Object Code";
                    s = s+" "+ObCode;
                    ObjectCode.add(s);
                    continue;
                }

                if(instruction.equals("BYTE") || instruction.equals("WORD")){
                    if(line[3].charAt(0)=='C' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                        String Chars = line[3].substring(2, line[3].length()-1);
                        ObCode = getAsciiCode(Chars);
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }
                    else if(line[3].charAt(0)=='X' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                        String Chars = line[3].substring(2, line[3].length()-1);
                        ObCode = Chars;
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }
                    else{
                        String o = decimalToHexa(Integer.parseInt(line[3]));
                        BigInteger bigInteger = new BigInteger(o, 16);
                        String bb = String.format("%06X", bigInteger);
                        ObCode = bb;
                        s=s+" "+ ObCode;
                        ObjectCode.add(s);
                    }
                }
                    if(getPC(LOCCTR.get(d)).equals("BASE")){
                        d++;
                        PC = getPC(LOCCTR.get(d));
                    }
                    else PC = getPC(LOCCTR.get(d));
                    OP = instructionSet.getOpcode(instruction);
                    if(instructionSet.getFormat(instruction)==3){
                        label = filterLabel(line[3]);
                        labelAddress = getLabelAddress(label);
                        if(line[2].charAt(0)=='$')
                            format = 5;
                        else if(line[2].charAt(0)=='+')
                            format = 4;
                        else
                            format = 3;

                        if(line[3].charAt(line[3].length()-1)=='X' && line[3].charAt(line[3].length()-2)==',' ){
                            x="1";

                        }
                        else x="0";
                    }
                    else
                        format = instructionSet.getFormat(instruction);

                    if(format==3 || format == 4){

                        if(line[3].charAt(0)=='C' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                            String Chars = line[3].substring(2, line[3].length()-1);
                            ObCode = getAsciiCode(Chars);
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                        else if(line[3].charAt(0)=='X' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                            String Chars = line[2].substring(2, line[3].length()-2);
                            ObCode = Chars;
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                        if(line[3].charAt(0)=='#'){
                            i="1"; n="0";
                        }
                        else if(line[3].charAt(0)=='@'){
                            n="1"; i="0";
                        }
                        else{
                            n="1"; i="1";
                        }
                        if(format==4){
                            b="0";
                            p="0";
                            e="1";
                            displacement =extend(labelAddress);
                            ObCode = getObjectHexa(OP+n+i+x+b+p+e) +displacement;
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                        if(isInteger(label)){
                            b="0";
                            p="0";
                            if(format==4 && label.length()<5){
                            displacement= extend(decimalToHexa(Integer.parseInt(label)));
                            }
                            else
                                displacement= decimalToHexa(Integer.parseInt(label));
                            ObCode = getObjectHexa(OP+n+i+x+b+p+e) +displacement;
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;

                        }
                        if(format==3){
                            //System.out.println("LABEL: "+labelAddress);
                            e="0";
                            if(line[3].charAt(0)=='=' && line[3].charAt(1)=='C' && line[3].charAt(2)==39&& line[3].charAt(line[3].length()-1)==39){
                                getLiteralAddress(line[3].substring(1));
                                calculateDisplacement3(labelAddress, PC);
                                ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                                s = s+" "+ObCode;
                                ObjectCode.add(s);
                                continue;
                            }
                            else if(line[3].charAt(0)=='=' && line[3].charAt(1)=='X' && line[3].charAt(2)==39&& line[3].charAt(line[3].length()-1)==39){
                                getLiteralAddress(line[3].substring(1));
                                calculateDisplacement3(labelAddress, PC);
                                ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                                s = s+" "+ObCode;
                                ObjectCode.add(s);
                                continue;
                            }
                            else {
                                try {
                                    calculateDisplacement3(labelAddress, PC);
                                } catch (NumberFormatException ex) {
                                    System.out.println("ITERATION " + j);
                                }
                                if (errorFlag == 1) {
                                    System.out.println("Format 3 is not compatible on line " + j);
                                    System.exit(1);
                                }
                                if (line[3].charAt(0) == '=' && (line[3].charAt(1) == 'C' || line[3].charAt(1) == 'X') && line[3].charAt(2) == 39 && line[3].charAt(line[3].length() - 1) == 39) {
                                    getLiteralAddress(line[3].substring(1));
                                    calculateDisplacement3(labelAddress, PC);

                                }
                                ObCode = getObjectHexa(OP + n + i + x + b + p + e) + displacement;
                                s = s + " " + ObCode;
                                ObjectCode.add(s);
                                continue;
                            }
                        }

                    }
                    else if(format==5){
                        e="0";
                        if(line[3].charAt(0)=='C' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                            String Chars = line[2].substring(2, line[2].length()-2);
                            ObCode = getAsciiCode(Chars);
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                        else if(line[3].charAt(0)=='X' && line[3].charAt(1)==39 && line[3].charAt(line[3].length()-1)==39){
                            String Chars = line[3].substring(2, line[3].length()-2);
                            ObCode = Chars;
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                        else if(line[3].charAt(0)=='=' && line[3].charAt(1)=='C' && line[3].charAt(2)==39&& line[3].charAt(line[3].length()-1)==39){
                            getLiteralAddress(line[3].substring(1));
                        }
                        else if(line[3].charAt(0)=='=' && line[3].charAt(1)=='X' && line[3].charAt(2)==39&& line[3].charAt(line[3].length()-1)==39) {
                            getLiteralAddress(line[3].substring(1));
                        }
                        else if(line[3].charAt(0)=='#'|| line[3].charAt(0)=='@'){
                            System.out.println("Format 5 doesn't support indirect or immediate addressing");
                            System.exit(1);
                        }
                        calculateDisplacement3(labelAddress, PC);
                        calculateDisplacement5();
                        ObCode = getObjectHexa(OP + n+i+x+b+p+e)+displacement;
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }
                    else if(format==2){
                        if(line[3].length()>1) {
                            if (line[3].charAt(line[3].length() - 2) == ',') {
                                String registers[] = line[3].split(",");
                                R1 = getRegNo(registers[0]);
                                R2 = getRegNo(registers[1]);
                                ObCode = OP + R1 + R2;
                                s = s+" "+ObCode;
                                ObjectCode.add(s);
                                continue;
                            }
                        }
                        else{
                            if(instruction.equals("CLEAR")){
                                assignRegister(line[3], "0");
                            }
                            R1 = getRegNo(line[3]);
                            R2 = "0";
                            ObCode = OP + R1 + R2;
                            s = s+" "+ObCode;
                            ObjectCode.add(s);
                            continue;
                        }
                    }
                    else if(format==1){
                        ObCode = OP;
                        s = s+" "+ObCode;
                        ObjectCode.add(s);
                        continue;
                    }

            }
        }
        String oo = LOCCTR.get(LOCCTR.size()-1);
        ObjectCode.add(oo+" No Object Code.");

    }



    public String binaryToHexa(String bin){
        if(bin.equals("0000"))
            return "0";
        if(bin.equals("0001"))
            return "1";
        if(bin.equals("0010"))
            return "2";
        if(bin.equals("0011"))
            return "3";
        if(bin.equals("0100"))
            return "4";
        if(bin.equals("0101"))
            return "5";
        if(bin.equals("0110"))
            return "6";
        if(bin.equals("0111"))
            return "7";
        if(bin.equals("1000"))
            return "8";
        if(bin.equals("1001"))
            return "9";
        if(bin.equals("1010"))
            return "A";
        if(bin.equals("1011"))
            return "B";
        if(bin.equals("1100"))
            return "C";
        if(bin.equals("1101"))
            return "D";
        if(bin.equals("1110"))
            return "E";
        if(bin.equals("1111"))
            return "F";

        else return " ";



    }
    public String getRegNo(String reg){
        if(reg.equals("A"))
            return "0";
        else if(reg.equals("X"))
            return "1";
        else if(reg.equals("L"))
            return "2";
        else if(reg.equals("B"))
            return "3";
        else if(reg.equals("S"))
            return "4";
        else if(reg.equals("T"))
            return "5";
        else if(reg.equals("F"))
            return "6";
        else if(reg.equals("PC"))
            return "8";
        else if(reg.equals("SW"))
            return "9";
        else return"";
    }
    public String decimalToHexa(int n){
        String temp = Integer.toString(n,16);
        BigInteger bd = new BigInteger(temp, 16);
        String hexa = String.format("%03X", bd);
        return  hexa;
    }
    public String subtractHexa(String n1, String n2){
        BigInteger num1 = new BigInteger(n1, 16);
        BigInteger num2 = new BigInteger(n2, 16);
        BigInteger diff = num1.subtract(num2);
        String res = String.format("%03X", diff);
        return res;
    }
    public String addHexa(String n1, String n2){
        BigInteger num1 = new BigInteger(n1, 16);
        BigInteger num2 = new BigInteger(n2, 16);
        BigInteger sum = num1.add(num2);
        String res = String.format("%03X", sum);
        return res;
    }
    public String getLabelAddress(String label){
        for(int j=0; j<symbols.size();j++){
            String s = symbols.get(j);
            String line[] = s.split(" ");
            if(line[1].equals(label)){
                return line[0];
            }
        }
        return "";
    }
    public void getLiteralAddress(String lit){
        for(int j=0; j<Literals.size();j++){
            String s = Literals.get(j);
            String[] line = s.split(" ");
            if(line[0].equals(lit))
            {
                labelAddress = line[2];
                return;
            }
        }
    }
    public String filterLabel(String label){
        if(label.charAt(0)=='#' || label.charAt(0)=='@')
            label = label.substring(1);
            if (label.charAt(label.length() - 1) == 'X' && label.charAt(label.length() - 2) == ',')
                label = label.substring(0, label.length() - 2);


        return label;
    }
    public String filterInstruction(String instruction){
        if(instruction.charAt(0)=='+' || instruction.charAt(0)=='$')
            return instruction.substring(1);
        else return instruction;
    }
    public String getObjectHexa(String binaryObjectCode){
        String obc="";
        for(int j=0; j<binaryObjectCode.length(); j+=4){
            int c=j+4;
          obc = obc + binaryToHexa(binaryObjectCode.substring(j,Math.min(binaryObjectCode.length(), j + 4)));
        }
        return obc;
    }
    public String getPC(String nextInstruction){
        String nI[] = nextInstruction.split(" ");
        if(nI[0].equals("BASE"))
            return "BASE";
        else return nI[0];
    }
    public void calculateDisplacement3(String labelAddress, String PC){

        String disp = subtractHexa(labelAddress, PC);
        BigInteger displace = new BigInteger(disp,16);
        if(x.equals("1")&& !RX.equals("")){
            displace = displace.add(new BigInteger(RX, 16));
        }

            if (displace.compareTo(new BigInteger("0", 16)) == 1 && displace.compareTo(new BigInteger("7FF", 16)) == -1) {
                p = "1";
                b = "0";
                displacement = disp;
                return;
            }
            else if(displace.compareTo(new BigInteger("0",16))==-1){
                BigInteger max = new BigInteger("4095");
                BigInteger res = max.add(displace).add(new BigInteger("1",16));
                if(res.compareTo(new BigInteger("2048"))==-1 || res.compareTo(new BigInteger("4095",16))==1){
                    b="1";
                    p="0";
                }
                else {
                    displacement = String.format("%03X", res);
                    p = "1";
                    b = "0";
                    return;
                }
            }
            if(b.equals("1")){
                disp = subtractHexa(labelAddress,baseAddress);
                displace = new BigInteger(disp, 16);
                if(displace.compareTo(new BigInteger("0"))==-1 || displace.compareTo(new BigInteger("4095"))==1){
                    errorFlag=1;
                    return;
                }
                else{
                    p="0";
                    b="1";
                    displacement= String.format("%03X", displace);
                }
            }


    }
    public boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }
         catch (Exception e){
            return false;
         }
    }
    public String extend(String s){
        if(s.length()==4){
            return "0"+s;
        }
        else if(s.length()==3){
            return "00"+s;
        }
        else if(s.length()==2){
            return "000"+s;
        }
        else if(s.length()==1){
            return "0000"+s;
        }
        else return s;
    }
    public void getBaseAddress(){
        int flag =0;
        for(int j=0; j<LOCCTR.size(); j++){
            String s = LOCCTR.get(j);
            String[] line = s.split(" ");
            if(line[0].equals("BASE")){
                baseAddress = getLabelAddress(line[1]);
                flag =1;
                return;
            }
        }
        if(flag==0){
            baseAddress="-1";
        }
    }
    public void assignRegister(String reg, String val){
        if(reg.equals("A"))
            RA = val;
        else if(reg.equals("X"))
            RX = val;
        else if(reg.equals("L"))
            RL = val ;
        else if(reg.equals("B"))
            RB = val;
        else if(reg.equals("S"))
            RS = val;
        else if(reg.equals("T"))
            RT = val;
        else if(reg.equals("F"))
            RF = val;
        else if(reg.equals("PC"))
            PC = val;
        else if(reg.equals("SW"))
            SW = val;

    }
    public String getAsciiCode(String s){
        String part="";
        for(int j=0; j<s.length();j++) {
            char ch = s.charAt(j);

            int in = (int) ch;
            part = part+ Integer.toHexString(in);
        }
        return part;
    }
    public void calculateDisplacement5(){
        BigInteger bg = new BigInteger(displacement,16);
        BigInteger labelAdd = new BigInteger(labelAddress,16);
        BigInteger currPC = new BigInteger(PC, 16);

        if(bg.remainder(new BigInteger("2")).compareTo(new BigInteger("0"))==0){
            n="1";
        }
        else
            n="0";

        if(labelAdd.compareTo(currPC)==1){
            i="1";
        }
        else{
            i="0";
        }
    }
    public void printOBCODE(){
        try {
            File file = new File("OBJECT CODE.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
                for (int j = 0; j < ObjectCode.size(); j++) {
                    osw.write(ObjectCode.get(j) + "\n");
                }

            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> ObjectCodeList(){
        return ObjectCode;
    }

}
