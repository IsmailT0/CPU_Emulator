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
        long start = System.currentTimeMillis();
        map = new HashMap<>();
        AC=0;
        FFlag =0;
        PC=0;
        M = new int[256];
        String filename = "program.txt";//args[0];
        readyMap(filename,map);
        run();
        long stop = System.currentTimeMillis();
        System.out.println("time " + (stop-start));

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

            if (num>255 || num<0){
                System.out.println("A value greater than byte entered");
                System.exit(0);
            }



            int result = -1;
            boolean PC_check=false;
            switch (command[0]){
                case "load" -> load(num);
                case "loadm" -> loadM(num);
                case "store" -> store(num);
                case "cmpm" ->  cmpM(num);
                case "cjmp" -> PC_check = cJMP(num);
                case "jmp" -> PC_check = jmp(num);
                case "add" -> result = add(num);
                case "addm" -> result = addM(num);
                case "subm" -> result = subM(num);
                case "sub" -> result = sub(num);
                case "mul" -> result = mul(num);
                case "mulm" -> result = mulM(num);
            }

            if (result<0 || result>255){
                System.out.println("After an operation a value greater than byte occurred exiting");
                System.exit(0);
            }
            if (PC_check){
                System.out.println("Wrong jmp");
                System.exit(0);
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

    private static boolean cJMP(int x){
        if (x> map.size())
            return true;
        if (FFlag == 1)
            PC = x-1;
        return false;
    }

    private static boolean jmp(int x){
        if (x> map.size())
            return true;
        PC = x-1;
        return false;
    }

    private static int add(int x){
        AC =  AC + x;
        return AC;
    }

    private static int addM(int x){
        AC = AC + M[x];
        return AC;
    }

    private static int subM(int x){
        AC = AC - M[x];
        return AC;
    }

    private static int sub(int x){
        AC = AC - x;
        return AC;
    }

    private static int mul(int x){
        AC = AC*x;
        return AC;
    }
    private static int mulM(int x){
        AC = AC * M[x];
        return AC;
    }

    private static void disp(){
        System.out.println(AC);
    }
}
