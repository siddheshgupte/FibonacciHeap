import java.util.ArrayList;

public class fibonacci_heap {

    Node max_node = null;

    // Number of nodes in heap to check if the heap is empty and also for pairwise merge table size
    int num_of_nodes = 0;

    // Insert
//    public Node insert(String name, int frequency) {
    public Node insert(Node new_node) {
        // New value is made a single node
//        Node new_node = new Node(name, frequency);

        new_node.left = new_node;
        new_node.right = new_node;
        new_node.child = null;

        // Merge new node with top level circular doubly linked list

        if (max_node == null)
            System.out.println(String.format("merging null and %s", new_node.name));
        else
            System.out.println(String.format("merging %s and %s", max_node.name, new_node.name));
        max_node = merge(max_node, new_node);

        // Increment number of nodes
        num_of_nodes += 1;

        test_linkedlist(max_node);

        //return max_node;
        return new_node;
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
        if (node_to_return.right == node_to_return) {
            max_node = null;

        } else {
            // Remove node
            max_node.left.right = max_node.right;
            max_node.right.left = max_node.left;

            // Important
            max_node = max_node.right;
        }

        // Decrement the number of nodes (Do this after removing node)
        num_of_nodes -= 1;

        // Set parents fields of the children of removed node to null
        // Iterate through the children of the removed node
        if (node_to_return.child != null) {

            System.out.println(String.format("child of %s is %s", node_to_return.name, node_to_return.child.name));
            test_linkedlist(node_to_return.child);

            Node current = node_to_return.child;
//            current.parent = null;
//            current = current.right;
//
//            while (current != node_to_return.child) {
//                System.out.println("Stuck");
//                current.parent = null;
//                current = current.right;
//            }

            do {
                current.parent = null;
//                System.out.println("Stuck");
                current = current.right;
            } while (current != node_to_return.child);
        }

        // Merge the children list into the top level list
        max_node = merge(max_node, node_to_return.child);


        // If there was only one top level node with no children, end
        // After removing the max_node would be null
        if (max_node == null)
            return node_to_return;
        // otherwise do pairwise combine

//        Node[] table = new Node[(int)(Math.log(num_of_nodes)/Math.log(2))];
        ArrayList<Node> table = new ArrayList<>();
        // fill the table till log2n with null
        for (int i = 0; i < (int) (Math.log(num_of_nodes) / Math.log(2)); i++) {
            table.add(null);
        }

        // temp list to store nodes because we have to merge in the middle
        ArrayList<Node> temp = new ArrayList<>();

        //Fill the temp list
        for (Node ele = max_node.right; temp.isEmpty() || temp.get(0) != ele; ele = ele.right) {
//            System.out.println(ele.name);
            temp.add(ele);
        }
//        System.out.println(temp);

//        temp.add(max_node);
//        for (Node ele = max_node.right; temp.get(0) != max_node; ele = ele.right) {
//            System.out.println(ele.name);
//            temp.add(ele);
//        }
//        System.out.println(temp);

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
//                if (existing_node.frequency > ele.frequency) {
//                    bigger = existing_node;
//                    smaller = ele;
//                } else {
//                    bigger = ele;
//                    smaller = existing_node;
//                }

                bigger = (existing_node.frequency > ele.frequency) ? existing_node : ele;
                smaller = (existing_node.frequency > ele.frequency) ? ele : existing_node;

                // Remove smaller out of its circular doubly linked list
                // Then make it a child of the bigger by merging into its child list
                smaller.left.right = smaller.right;
                smaller.right.left = smaller.left;

                // If there are only two nodes, then the bigger ka left and right is messed up here
                // check if the right and left after removing the smaller is the smaller node itself
                // if it is then make the bigger a singleton

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

        if (node1 == null && node2 == null) {
            return null;
        }
        if (node1 == null) {
            return node2;
        }
        if (node2 == null) {
            return node1;
        }

        // Merge two circular doubly linked lists
        Node temp = node1.right;
        node1.right = node2.right;
        node1.right.left = node1;

        node2.right = temp;
        node2.right.left = node2;

        test_linkedlist(node1);

        // Return the max node
        if (node1.frequency > node2.frequency) {
            return node1;
        }
        return node2;

    }

    public void test_linkedlist(Node first) {

        ArrayList<String> lst = new ArrayList<>();

        lst.add(first.name);
        for (Node ele = first.right; ele != first; ele = ele.right) {
            lst.add(ele.name);

//            System.out.println(ele.name);
        }

//        System.out.println(lst);
    }
}


//    def process_inputs(){
//        if input.name in dicti:
//            increasekey(name, freq)
//        else:
//            insert(name, freq)

//a 10
//b 10
//c 15
//1
//d 10
//4
//1
//f 10
//5
