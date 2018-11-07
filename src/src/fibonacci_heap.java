import java.util.ArrayList;
import java.util.List;

public class fibonacci_heap {

    Node max_node = null;

    // Number of nodes in heap to check if the heap is empty and also for pairwise merge table size
    int num_of_nodes = 0;

    // Insert
    public Node insert(String name, int frequency) {

        // New value is made a single node
        Node new_node = new Node(name, frequency);

        // Merge new node with top level circular doubly linked list
        // TODO: Make sure merge returns the max node as max_node
        max_node = merge(max_node, new_node);

        // Increment number of nodes
        num_of_nodes += 1;

        return max_node;
    }


    public Node remove_max() {

        // If there are no nodes then return null
        if (num_of_nodes == 0) {
            System.out.println("Heap is empty");
            return null;
        }

        // Store the max element because we have to return it
        Node node_to_return = max_node;

        // If this is the only element, then make the top level list null
        if (num_of_nodes == 1) {
            max_node = null;
        } else {
            // Remove node
            max_node.left.right = max_node.right;
            max_node.right.left = max_node.left;
        }

        // Decrement the number of nodes (Do this after removing node)
        num_of_nodes -= 1;

        // Set parents of the children of removed node to null
        // Iterate through the children of the removed node
        Node current = node_to_return.child;
        current.parent = null;
        current = current.right;

        while (current != node_to_return.child) {
            current.parent = null;
            current = current.right;
        }

        // Merge the children list into the top level list
        max_node = merge(max_node, node_to_return.child);


        // If there was only one top level node with no children, end
        if (max_node == null)
            return node_to_return;
        // otherwise do pairwise combine

//        Node[] table = new Node[(int)(Math.log(num_of_nodes)/Math.log(2))];
        ArrayList<Node> table = new ArrayList<Node>();
        // fill the table till log2n with null
        for (int i = 0; i < (int) (Math.log(num_of_nodes) / Math.log(2)); i++) {
            table.add(null);
        }

        // temp list to store nodes because we have to merge in the middle
        ArrayList<Node> temp = new ArrayList<Node>();

        //Fill the temp list
        for (Node ele = node_to_return; temp.isEmpty() || temp.get(0) != ele; ele = ele.right) {
            temp.add(ele);
        }

        // Go through all the nodes in the temp list
        // Store in table at index == degree
        // If there is a collision, then take out the node, merge and insert again
        // continue till this element is stored successfully.

        for (Node ele : temp) {
            // continue till this element is stored successfully and then break.
            while (true) {

                // We have put log2n nulls in the table
                // But just to be sure insert nulls if needed
                while (table.size() <= ele.degree)
                    table.add(null);

                // If we store the element successfully, then break and go for next ele
                if (table.get(ele.degree) == null) {
                    table.set(ele.degree, ele);
                    break;
                }

                // otherwise there is a collision
                Node existing_node = table.get(ele.degree);
                table.set(ele.degree, null);

                Node bigger;
                Node smaller;
                if (existing_node.frequency > ele.frequency) {
                    bigger = existing_node;
                    smaller = ele;
                } else {
                    bigger = ele;
                    smaller = existing_node;
                }


            }
        }

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
