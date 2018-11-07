// Structure for individual nodes in the max fibonacci heap
public class Node {

    // Number of children of this node
    int degree = 0;

    // Pointer to one child of the node
    Node child = null;

    // Data -
    //      1. name. eg: facebook
    //      2. frequency. eg: 5
    String name = null;
    int frequency = 0;

    // Pointers to left and right neighbours in the circular doubly linked list
    Node left = null;
    Node right = null;

    // Pointer to the parent for child_cut in increase_key()
    Node parent = null;

    // Boolean to check if this node has previously lost a child
    boolean child_cut = false;

    // Node will take input name and frequency for init
    Node(String ip_name, int ip_frequency) {
        this.name = ip_name;
        this.frequency = ip_frequency;

        // Circular doubly linked list has only one element
        this.left = this;
        this.right = this;
    }
}
