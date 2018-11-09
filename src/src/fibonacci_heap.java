import java.util.ArrayList;

public class fibonacci_heap {

    public static void main(String[] args) {
        System.out.println("hello world");
    }
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

        // Set parents fields of the children of removed node to null
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
        // After removing the max_node would be null
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
        for (Node ele = max_node; temp.isEmpty() || temp.get(0) != ele; ele = ele.right) {
            temp.add(ele);
        }

        // Go through all the nodes in the temp list
        // Store in table at index == degree
        // If there is a collision, then take out the node, merge and insert again
        // continue till this element or its further trees are stored successfully.

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

                // Remove smaller out of its circular doubly linked list
                // Then make it a child of the bigger by merging into its child list
                smaller.left.right = smaller.right;
                smaller.right.left = smaller.left;

                // Make the element a circular linked list so that merge function works properly
                smaller.left = smaller;
                smaller.right = smaller;
                //merge
                bigger.child = merge(bigger.child, smaller);

                smaller.parent = bigger;

                // Set the child's childcut to false
                smaller.child_cut = false;

                bigger.degree += 1;

                // Continue till we store without collision
                ele = bigger;
            }

            // Update max if necessary
            // IF THIS DOESN'T WORK THEN ITERATE AND FIND MAX AT THE END
            if (ele.frequency >= max_node.frequency)
                max_node = ele;
        }
        return node_to_return;
    }

    public Node increase_frequency(Node node, int new_frequency) {

        // The frequency has to be positive otherwise it is a way to decrease frequency
        if (new_frequency < 0) {
            System.out.println("Cannot decrease frequency like that");
            System.exit(0);
        }

        // Increase frequency
        node.frequency = node.frequency + new_frequency;

        if (node.parent != null && node.frequency > node.parent.frequency) {
            cascading_cut(node);
        }

        // Test this
        if (node.frequency > max_node.frequency)
            max_node = node;

        return node;
    }

    public void cascading_cut(Node node_to_cut) {
        node_to_cut.child_cut = false;

        if (node_to_cut.parent == null)
            return;

        // Remove node
        if (node_to_cut.left != node_to_cut && node_to_cut.right != node_to_cut) {
            node_to_cut.left.right = node_to_cut.right;
            node_to_cut.right.left = node_to_cut.left;
        }

        // Clear the parent's child field if it points to this node
        if (node_to_cut.parent.child == node_to_cut) {
//            node_to_cut.parent.child = node_to_cut.left;

            // check if it is the only child
            if (node_to_cut.left != node_to_cut && node_to_cut.right != node_to_cut) {

                // If it is not, then set the parent's child field randomly
                node_to_cut.parent.child = node_to_cut.left;
            } else {
                // If it is then since this node was removed, set the parent's child field to null
                node_to_cut.parent.child = null;
            }
        }

        node_to_cut.parent.degree -= 1;

        // Make the element a circular linked list so that merge function works properly
        node_to_cut.left = node_to_cut;
        node_to_cut.right = node_to_cut;

        //merge
        max_node = merge(max_node, node_to_cut);

        // Cascade
        if (node_to_cut.parent.child_cut) {
            cascading_cut(node_to_cut.parent);
        } else {
            node_to_cut.parent.child_cut = true;
        }

        node_to_cut.parent = null;


    }

    public Node merge(Node node1, Node node2) {

//        TODO : WRITE THIS
        return max_node;

    }
}
//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)
