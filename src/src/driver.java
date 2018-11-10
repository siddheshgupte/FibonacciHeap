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
            String[] ip_arr = ip.split(" ");

            if (ip.equals("0")) {
                break;
            } else if (ip_arr.length == 2) {
//                System.out.println("entered: " + ip);

                if (dicti.containsKey(ip_arr[0])) {
                    Node node = fib_heap.increase_frequency((Node) dicti.get(ip_arr[0]),
                            Integer.parseInt(ip_arr[1]));
                } else {
                    dicti.put(ip_arr[0], fib_heap.insert(new Node(ip_arr[0], Integer.parseInt(ip_arr[1]))));
                }
            } else if (ip_arr.length == 1) {
                // We want to only peek so remove and then reinsert

                int elements_to_remove = Integer.parseInt(ip_arr[0]);

                if (elements_to_remove > fib_heap.num_of_nodes) {
                    System.out.println("Elements to remove more than number of node");
                    continue;
                }

                Node[] stack = new Node[elements_to_remove];

                for (int i = 0; i < elements_to_remove; i++) {
                    // Remove
                    Node curr_max_node = fib_heap.remove_max();

                    // store in stack
                    stack[i] = curr_max_node;

                    // Print
                    System.out.println(stack[i].name);
                    System.out.println(stack[i].frequency);
                }

                for (int i = 0; i < elements_to_remove; i++) {
                    // Reinsert
                    fib_heap.insert(stack[i]);
                }
            }
        }


    }
}
//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)
