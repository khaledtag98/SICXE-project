import java.math.BigInteger;
import java.util.ArrayList;

public class Records {
    ArrayList<String> ObjectCode = new ArrayList<>();
    ArrayList<String> TRECORD = new ArrayList<>();
    int TotalLength = 0 ;
    String HRecord;
    String ERecord;
    public Records(ArrayList<String> ObjectCode){
        this.ObjectCode = ObjectCode;
        HTERecord();
    }

public void HTERecord(){
    boolean firstIteration = true;
    boolean ResetTRecordAndTotalLength = true;
    boolean LastiIeration = false;
    String[] nextLine = new String[20];
    String TRecord = "T" ;
    int StartAddr;
    int EndAddr;

    for (int i = 0;i < ObjectCode.size(); i++) {
        String s = ObjectCode.get(i);
        String[] line = s.split(" ");
        if (ResetTRecordAndTotalLength){
            ResetTRecordAndTotalLength = false;
            TRecord = "T."+String.format("%06X",Integer.parseInt(line[0],16)); ;
            TotalLength = 0;
        }
        if(firstIteration){
            firstIteration = false ;

            int index = ObjectCode.size() - 1;
            String[] LastLine = ObjectCode.get(index).split(" ");
             StartAddr  = Integer.parseInt(line[3],16);
             EndAddr = Integer.parseInt(LastLine[0],16);
            int x = EndAddr+1-StartAddr;
            String Hlenght = String.format("%06X",x);
            String StartAddressHex = String.format("%06X",StartAddr);
            String progName = String.format("%-6s",line[1]);
            HRecord = "H."+progName+"."+StartAddressHex+"."+Hlenght ;
            ERecord= "E."+StartAddressHex;
            continue;
        }if (line[2].equals("RESB") || line[2].equals("RESW") || line[0].equals("BASE") ){
            ResetTRecordAndTotalLength = true;
            continue;
        }else
        {
            if (i <= ObjectCode.size()-2){
                 nextLine = ObjectCode.get(i+1).split(" ");
            }else LastiIeration = true;
                if (nextLine[2].equals("RESB")||nextLine[2].equals("RESW")) {
                    TRECORD.add(TRecord);
                    ResetTRecordAndTotalLength = true;
                }
            else{
                int length = line[line.length - 1].length();
                if (TotalLength > 26 ){
                    switch (TotalLength) {
                        case 27:
                            if (length  == 8){

                            }else{
                                String TObjCode = String.format("%06X",Integer.parseInt(line[line.length - 1],16));
                                TRecord = TRecord+"."+TObjCode;
                                TotalLength = TotalLength + length/2;
                            }
                            break;
                        case 28:
                            if(length  == 6 || length == 8){
                                TRECORD.add(TRecord);
                                ResetTRecordAndTotalLength = true;
                            }else{
                                String TObjCode = String.format("%06X",Integer.parseInt(line[line.length - 1],16));
                                TRecord = TRecord+"."+TObjCode;
                                TotalLength = TotalLength + length/2;
                            }
                            break;
                        case 29:
                            if (length == 2 ){
                                String TObjCode = String.format("%06X",Integer.parseInt(line[line.length - 1],16));
                                TRecord = TRecord+"."+TObjCode;
                                TotalLength = TotalLength + length/2;
                            }else{
                                TRECORD.add(TRecord);
                                ResetTRecordAndTotalLength = true;
                            }
                            break;
                        default:
                            TRECORD.add(TRecord);
                            ResetTRecordAndTotalLength = true;
                    }
                }
                else {

                    String TObjCode = line[line.length - 1];
                    TRecord = TRecord+"."+TObjCode;
                    TotalLength = TotalLength + length/2;
                    if (LastiIeration){
                        int index = 8;
                        System.out.println(HRecord);
                        for (int j = 0;j < TRECORD.size(); j++){
                            String t = TRECORD.get(j);
                            String newString = t.substring(0, index + 1)+ "Hambozo." + t.substring(index + 1);
                            System.out.println(newString);
                        }
                        System.out.println(ERecord);

                    }
                }
            }
        }

    }
}
}
