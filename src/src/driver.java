import java.util.HashMap;
import java.util.Scanner;

public class driver {

    public static void main(String args[]) {

        // Hash table
        HashMap<String, Node> dicti = new HashMap<>();

        // Instantiate fibonacci heap
        fibonacci_heap fib_heap = new fibonacci_heap();
        Scanner reader = new Scanner(System.in);

        // Keep taking input and break when required
        while (true) {

            System.out.println("Enter next");
            String ip = reader.nextLine();
            String[] ip_arr = ip.split(" ");

            if (ip.equals("0")) {
                break;
            } else if (ip_arr.length == 2) {
//                System.out.println("entered: " + ip);

                // If the key exists then increase frequency
                if (dicti.containsKey(ip_arr[0])) {
                    Node node = fib_heap.increase_frequency(dicti.get(ip_arr[0]),
                            Integer.parseInt(ip_arr[1]));
                } else {
                    // If the key doesn't exist then make the Node and put in the hash table
                    dicti.put(ip_arr[0], fib_heap.insert(new Node(ip_arr[0], Integer.parseInt(ip_arr[1]))));
                }
            } else if (ip_arr.length == 1) {

                // We want to only peek so remove and then reinsert
                int elements_to_remove = Integer.parseInt(ip_arr[0]);

                // Check the number of nodes
                if (elements_to_remove > fib_heap.num_of_nodes) {
                    System.out.println("Elements to remove more than number of node");
                    continue;
                }

                // Answer stack
                Node[] stack = new Node[elements_to_remove];

                // Remove
                for (int i = 0; i < elements_to_remove; i++) {
                    Node curr_max_node = fib_heap.remove_max();

                    // store in stack
                    stack[i] = curr_max_node;
                }

                // Print
                for (Node ele : stack) {
                    System.out.print(ele.name + ", ");
                }
                System.out.println();

                // Reinsert
                for (int i = 0; i < elements_to_remove; i++) {
                    // Reinsert one by one
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
