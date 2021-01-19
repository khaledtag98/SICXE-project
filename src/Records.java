import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class Records {
    ArrayList<String> ObjectCode = new ArrayList<>();
    ArrayList<String> TRECORD = new ArrayList<>();
    ArrayList<String> MRecord = new ArrayList<>();
    int TotalLength = 0 ;
    String HRecord;
    String ERecord;
    int cutt = 8;
    public Records(ArrayList<String> ObjectCode, ArrayList<String> MRecord){
        this.ObjectCode = ObjectCode;
        this.MRecord = MRecord;
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
                if (!firstIteration){
                    TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                    TRECORD.add(TRecord);
                }
                if (i == ObjectCode.size() -1 && (nextLine[2].equals("RESB")||nextLine[2].equals("RESW")) ){
                    printHTERecord();
                    continue;
                }
                TRecord = "T."+String.format("%06X",Integer.parseInt(line[0],16));
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
                int v = 0;
                while (v == 0){
                    String[] l =  ObjectCode.get(ObjectCode.size()-1).split(" ");
                    if (l[2].equals("RESB") || l[2].equals("RESW")){
                        ObjectCode.remove(ObjectCode.size()-1);
                    }else v = 1;
                }
                continue;
            }
            if (line[2].equals("RESB") || line[2].equals("RESW") || line[0].equals("BASE") ||line[2].equals("EQU")){
                continue;
            }
            else {
                if (i <= ObjectCode.size()-2){
                    nextLine = ObjectCode.get(i+1).split(" ");
                }else LastiIeration = true;
                if (nextLine[2].equals("RESB")||nextLine[2].equals("RESW")) {
                    int length = line[line.length - 1].length();
                    if (TotalLength > 26){
                        switch (TotalLength){
                            case 27:
                                if (length  == 8){
                                    TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }else
                                {
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }
                                break;
                            case 28:
                                if (length  == 6 || length == 8){
                                    TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }else
                                {
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }
                                break;
                            case 29:
                                if (length == 2){
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }else {
                                    TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                                    TRECORD.add(TRecord);
                                    ResetTRecordAndTotalLength = true;
                                }
                                break;
                            default:
                                TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                                TRecord = pushToTRecord(TRecord,line[line.length - 1],line[0]);
                                ResetTRecordAndTotalLength = true;
                                break;

                        }
                    }
                    else
                    {
                        // TRecord = TRecordLenghtCalc(TRecord,nextLine[0]);
                        String ObjCode = line[line.length - 1];
                        TRecord = TRecord+"."+ObjCode;
                        ResetTRecordAndTotalLength = true;

                    }
                }
                else{
                    int length = line[line.length - 1].length();
                    if (TotalLength > 26 ){
                        switch (TotalLength) {
                            case 27:
                                if (length  == 8){
                                    TRecord = TRecordLenghtCalc(TRecord,line[0]);
                                    TRecord = pushToTRecord(TRecord,line[line.length - 1],line[0]);
                                }else{
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TotalLength = TotalLength + length/2;
                                }
                                break;
                            case 28:
                                if(length  == 6 || length == 8){
                                    TRecord = TRecordLenghtCalc(TRecord,line[0]);
                                    TRecord = pushToTRecord(TRecord,line[line.length - 1],line[0]);
                                }else{
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TotalLength = TotalLength + length/2;
                                }
                                break;
                            case 29:
                                if (length == 2 ){
                                    String TObjCode = line[line.length - 1];
                                    TRecord = TRecord+"."+TObjCode;
                                    TotalLength = TotalLength + length/2;
                                }else{
                                    TRecord = TRecordLenghtCalc(TRecord,line[0]);
                                    TRecord = pushToTRecord(TRecord,line[line.length - 1],line[0]);
                                }
                                break;
                            default:
                                TRECORD.add(TRecord);
                                ResetTRecordAndTotalLength = true;
                        }
                    }
                    else {
                        String[] previousLine = ObjectCode.get(i-1).split(" ");
                        if (previousLine[2].equals("RESB") || previousLine[2].equals("RESW")){
                            TRecord = "T."+String.format("%06X",Integer.parseInt(line[0],16));
                        }
                        String TObjCode = line[line.length - 1];
                        int zft = TotalLength + TObjCode.length()/2;
                        if (zft >=31){
                            TRecord = TRecordLenghtCalc(TRecord,line[0]);
                            TRecord = pushToTRecord(TRecord,line[line.length - 1],line[0]);
                        }else
                        {
                            TRecord = TRecord+"."+TObjCode;
                            TotalLength = TotalLength + length/2;
                        }
                    }
                    if (LastiIeration){
                        int plus = 0;
                        if (line[line.length - 1].length() == 2 ){
                            plus = 1;

                        }else if(line[line.length - 1].length() == 4){
                            plus = 2;
                        }else if(line[line.length - 1].length() == 6){
                            plus = 3;
                        }else if(line[line.length - 1].length() == 8){
                            plus = 4;
                        }
                        String StartAssress = TRecord.substring(2, 7 + 1);
                        int StartAssressDECIMAL =Integer.parseInt(StartAssress,16);
                        int EndAssressDECIMAL =Integer.parseInt(line[0],16);
                        EndAssressDECIMAL = EndAssressDECIMAL + plus;
                        int TrecordLenghtDECIMAL = EndAssressDECIMAL - StartAssressDECIMAL;
                        String TrecordLenght = String.format("%02X",TrecordLenghtDECIMAL);
                        TRecord = TRecord.substring(0, cutt + 1)+TrecordLenght +"." + TRecord.substring(cutt + 1);
                        TRECORD.add(TRecord);
                        printHTERecord();
                    }
                }
            }

        }
    }
    public String pushToTRecord(String TRecord,String TObjCode,String StartingAddress){
        TRECORD.add(TRecord);
        int length1 = TObjCode.length();
        TotalLength = length1/2;
        return TRecord = "T."+String.format("%06X",Integer.parseInt(StartingAddress,16))+"."+TObjCode;
    }
    public String TRecordLenghtCalc(String Trecord,String EndAssress){
        String StartAssress = Trecord.substring(2, 7 + 1);
        int StartAssressDECIMAL =Integer.parseInt(StartAssress,16);
        int EndAssressDECIMAL =Integer.parseInt(EndAssress,16);
        int TrecordLenghtDECIMAL = EndAssressDECIMAL - StartAssressDECIMAL;
        String TrecordLenght = String.format("%02X",TrecordLenghtDECIMAL);
        return Trecord.substring(0, cutt + 1)+TrecordLenght +"." + Trecord.substring(cutt + 1);
    }
    public void printHTERecord(){
        try {
            File file = new File("HTERecord.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(HRecord + "\n");
            for (int j = 0; j < TRECORD.size(); j++) {
                osw.write(TRECORD.get(j) + "\n");
            }
            for (int j = 0; j < MRecord.size(); j++) {
                osw.write(MRecord.get(j) + "\n");
            }
            osw.write(ERecord + "\n");

            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}