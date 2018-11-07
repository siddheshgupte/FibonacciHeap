public class fibonacci_heap {

    Node max_node = null;

    int num_of_nodes = 0;

    public Node insert(String name, int frequency) {

        Node new_node = new Node(name, frequency);

        max_node = merge(max_node, new_node);

        num_of_nodes += 1;

        return max_node;
    }


    public Node remove_max() {

//        TODO : WRITE THIS
        if (num_of_nodes == 0) {
            System.out.println("Heap is empty");
            return null;
        }
        Node node_to_return = max_node;

        num_of_nodes -= 1;


        return node_to_return;
    }

    public Node merge(Node max_node, Node new_node) {

//        TODO : WRITE THIS
        return max_node;

    }
}
//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)
