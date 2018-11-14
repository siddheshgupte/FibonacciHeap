import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class keywordcounter {

    private static boolean first_time_opening_file = true;

    public static boolean is_first_file_open() {
        if (first_time_opening_file) {
            first_time_opening_file = false;
            return true;
        }
        return false;
    }

    public static List<String> read_lines_from_file(String file_name) {
        List<String> all_lines = new ArrayList<>();

        try {
            all_lines = Files.readAllLines(Paths.get(file_name));
        } catch (IOException e) {
            System.out.println(e);
        }

        return all_lines;
    }

    public static void process_lines(List<String> lines_in_file) {
        // Hash table
        HashMap<String, Node> dicti = new HashMap<>();

        // Instantiate fibonacci heap
        fibonacci_heap fib_heap = new fibonacci_heap();

        for (String line : lines_in_file) {
            process_line_to_fibonacci_heap(dicti, fib_heap, line);
        }
    }

    private static void process_line_to_fibonacci_heap(HashMap<String, Node> dicti, fibonacci_heap fib_heap, String line) {
//        line = line.strip();

        line = line.trim();
//        System.out.println(line);
        String[] ip_arr = line.split(" ");

        if (line.toLowerCase().equals("stop")) {
            System.exit(0);
        } else if (ip_arr.length == 2 && ip_arr[0].charAt(0) == '$') {
//                System.out.println("entered: " + line);

            ip_arr[0] = ip_arr[0].substring(1);

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
                return;
            }

            // Answer stack
            Node[] stack = new Node[elements_to_remove];

            // Remove
            for (int i = 0; i < elements_to_remove; i++) {
                Node curr_max_node = fib_heap.remove_max();

                // store in stack
                stack[i] = curr_max_node;
            }

            // Print (Replace this by writing to file)
            try {
                File file = new File("output_file.txt");

                if (file.exists() && is_first_file_open()) {
                    file.delete();
                    file.createNewFile();
                }

                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));

                List<String> names = new ArrayList<>();
                for (Node ele : stack) {
//                    System.out.print(ele.name + ", ");
                    names.add(ele.name);
                }
                writer.append(String.join(",", names));
                // Use this instead of "\n" as "\n" does not work in notepad
                writer.append(System.getProperty("line.separator"));
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }


            // Reinsert
            for (int i = 0; i < elements_to_remove; i++) {
                // Reinsert one by one
                fib_heap.insert(stack[i]);
            }
        }
    }

    public static void main(String args[]) {

        String file_name = "";
        if (args.length == 1) {
            file_name = args[0];
        } else {
            Scanner reader = new Scanner(System.in);
            System.out.println("Enter file_name");
            file_name = reader.nextLine();
        }

        List<String> lines_from_file = read_lines_from_file(file_name);

        process_lines(lines_from_file);
    }

    public void one_by_one() {
        // Hash table
        HashMap<String, Node> dicti = new HashMap<>();

        // Instantiate fibonacci heap
        fibonacci_heap fib_heap = new fibonacci_heap();
        Scanner reader = new Scanner(System.in);

        // Keep taking input and break when required
        while (true) {

            System.out.println("Enter next");
            String ip = reader.nextLine();
            process_line_to_fibonacci_heap(dicti, fib_heap, ip);
        }
    }
}


//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)
