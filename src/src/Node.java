public class Node {

    int degree = 0;

    Node child = null;

    // Data
    String name = null;
    int frequency = 0;

    Node left = null;
    Node right = null;

    Node parent = null;

    boolean child_cut = false;

    Node(String ip_name, int ip_frequency) {
        name = ip_name;
        frequency = ip_frequency;
    }
}
