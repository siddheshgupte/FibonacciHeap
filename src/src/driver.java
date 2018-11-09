import java.util.HashMap;
import java.util.Scanner;

public class driver {

    public static void main(String args[]) {

        HashMap dicti = new HashMap();
        fibonacci_heap fib_heap = new fibonacci_heap();
        Scanner reader = new Scanner(System.in);

        while (true) {
            System.out.println("Enter next");
            String ip = reader.nextLine();

            if (ip.equals("0")) {
                break;
            } else {
                System.out.println("entered: " + ip);
                String[] ip_arr = ip.split(" ");
            }
        }


    }
}
//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)
