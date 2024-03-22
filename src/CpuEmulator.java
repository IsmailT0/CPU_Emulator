import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CpuEmulator {
    public static void main(String[] args) throws IOException{
        CPU cpu = new CPU("program.txt");
        cpu.run();
    }
}

class CPU{

    private HashMap<Integer,String> map;
    private int AC;
    private int PC;
    private int FFlag;
    private int[] M; //memory
    public CPU(String filename) throws IOException{
        map = new HashMap<>();
        readyMap(filename,map);
        this.AC=0;
        this.FFlag =0;
        this.PC=0;
        M = new int[256];
    }

    public void run(){
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
                case "add" -> add(num   );
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

    //Instruction functions
    private void load(int x){
        this.AC = x;
    }

    private void loadM(int x){
        this.AC=M[x];
    }

    private void store(int x){
        this.M[x] =this.AC;
    }

    private void cmpM(int x){
        int diff = AC - M[x];

        if (diff > 0)
            this.FFlag=1;
        else if (diff < 0 )
            this.FFlag=-1;
        else
            this.FFlag = 0;
    }

    private void cJMP(int x){
        if (FFlag == 1)
            this.PC = x-1;
    }

    private void jmp(int x){
        this.PC = x-1;
    }

    private void add(int x){
        this.AC =  AC + x;
    }

    private void addM(int x){
        this.AC = AC + M[x];
    }

    private void subM(int x){
        this.AC = AC - M[x];
    }

    private void sub(int x){
        this.AC = AC - x;
    }

    private void mul(int x){
        this.AC = AC*x;
    }
    private void mulM(int x){
        this.AC = AC * M[x];
    }

    private void disp(){
        System.out.println(AC);
    }


}