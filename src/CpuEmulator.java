import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CpuEmulator {
    private static HashMap<Integer,String> map;
    private static int AC;
    private static int PC;
    private static int FFlag;
    private static int[] M; //memory
    public static void main(String[] args) throws IOException{
        map = new HashMap<>();
        AC=0;
        FFlag =0;
        PC=0;
        M = new int[256];
        String filename = "program.txt";//args[0];
        readyMap(filename,map);
        run();
    }
    public static void run(){
        while (!map.get(PC).equals("start")) {
            PC++;
        }
        if (map.get(PC).equals("start"))
            PC++;
        while(!map.get(PC).equals("halt")){
            if (map.get(PC).contains("disp")){
                disp();
                PC++;
                continue;
            }

            String[] command = map.get(PC).split(" ");
            int num =0;
            if (command[1] != null)
                num = Integer.parseInt(command[1]);

            switch (command[0]){
                case "load" -> load(num);
                case "loadm" -> loadM(num);
                case "store" -> store(num);
                case "cmpm" -> cmpM(num);
                case "cjmp" -> cJMP(num);
                case "jmp" -> jmp(num);
                case "add" -> add(num);
                case "addm" -> addM(num);
                case "subm" -> subM(num);
                case "sub" -> sub(num);
                case "mul" -> mul(num);
                case "mulm" -> mulM(num);
            }

            PC++;
        }
    }

    public static void readyMap(String filename, Map<Integer, String> map) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fileReader);

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                int i = 0;
                char ch;
                while (i < line.length()) {
                    ch = line.charAt(i);
                    if (ch == '%') {
                        break;
                    } else if (ch == ' ') {

                        int key = Integer.parseInt(line.substring(0,i));
                        String value = line.substring(i + 1).toLowerCase();
                        map.put(key, value);
                        break;
                    }
                    i++;
                }
            }
        } finally {
            reader.close();
        }
    }


    private static void load(int x){
        AC = x;
    }

    private static void loadM(int x){
        AC=M[x];
    }

    private static void store(int x){
        M[x]= AC;
    }

    private static void cmpM(int x){
        int diff = AC - M[x];

        if (diff > 0)
            FFlag=1;
        else if (diff < 0 )
            FFlag=-1;
        else
            FFlag = 0;
    }

    private static void cJMP(int x){
        if (FFlag == 1)
            PC = x-1;
    }

    private static void jmp(int x){
        PC = x-1;
    }

    private static void add(int x){
        AC =  AC + x;
    }

    private static void addM(int x){
        AC = AC + M[x];
    }

    private static void subM(int x){
        AC = AC - M[x];
    }

    private static void sub(int x){
        AC = AC - x;
    }

    private static void mul(int x){
        AC = AC*x;
    }
    private static void mulM(int x){
        AC = AC * M[x];
    }

    private static void disp(){
        System.out.println(AC);
    }
}
