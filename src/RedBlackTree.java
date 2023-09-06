import java.util.ArrayList;
import java.util.List;

public class RedBlackTree {

    private Node root;

    public boolean add(int value) {
        if (root != null) {
            boolean result = addNode(root, value);
            root = rebalance(root);
            root.color = Color.BLACK;
            return result;
        } else {
            root = new Node();
            root.color = Color.BLACK;
            root.value = value;
            return true;
        }
    }

    private boolean addNode(Node node, int value) {
        // cannot add node with the same value
        if (node.value == value) {
            return false;
        } else {
            // for left child
            if (node.value > value) {
                // if left child exists - going deeper to find better place
                if (node.leftChild != null) {
                    boolean result = addNode(node.leftChild, value);
                    node.leftChild = rebalance(node.leftChild);
                    return result;
                } else {
                    // if no left node exists - placing new node here
                    node.leftChild = new Node();
                    node.leftChild.color = Color.RED;
                    node.leftChild.value = value;
                    return true;
                }
            } else {
                // for right child - the same as above for left child
                if (node.rigthChild != null) {
                    boolean result = addNode(node.rigthChild, value);
                    node.rigthChild = rebalance(node.rigthChild);
                    return result;
                } else {
                    node.rigthChild = new Node();
                    node.rigthChild.color = Color.RED;
                    node.rigthChild.value = value;
                    return true;
                }
            }
        }
    }

    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance = true;

        do {
            needRebalance = false;
            // left child is BLACK and right child is RED => right swap + rebalance
            if (result.rigthChild != null && result.rigthChild.color == Color.RED
                    && (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            // left child is RED, but its own left child is RED too => left swap + rebalance
            if (result.leftChild != null && result.leftChild.color == Color.RED && result.leftChild.leftChild != null
                    && result.leftChild.leftChild.color == Color.RED) {
                needRebalance = true;
                result = leftSwap(result);
            }
            // both children are RED => color swap
            if (result.leftChild != null && result.leftChild.color == Color.RED && result.rigthChild != null
                    && result.rigthChild.color == Color.RED) {
                needRebalance = true;
                colorsSwap(result);
            }

        } while (needRebalance);

        return result;
    }

    private Node rightSwap(Node node) {
        Node rightChild = node.rigthChild;
        Node midChild = rightChild.leftChild;

        rightChild.leftChild = node;
        node.rigthChild = midChild;
        rightChild.color = node.color;
        node.color = Color.RED;

        return rightChild;
    }

    private Node leftSwap(Node node) {
        Node leftChild = node.leftChild;
        Node midChild = leftChild.rigthChild;

        leftChild.rigthChild = node;
        node.leftChild = midChild;
        leftChild.color = node.color;
        node.color = Color.RED;

        return leftChild;
    }

    private void colorsSwap(Node node) {
        node.rigthChild.color = Color.BLACK;
        node.leftChild.color = Color.BLACK;
        node.color = Color.RED;
    }

    private class Node {
        private int value;
        private Color color;
        private Node leftChild;
        private Node rigthChild;

        @Override
        public String toString() {
            return "Node [value = " + value + ", color = " + color + ", leftChild = " + leftChild + ", rigthChild = "
                    + rigthChild + "]";
        }

    }

    private enum Color {
        RED, BLACK
    }

    @Override
    public String toString() {

        int depth = maxDepth(root) + 1;

        List<Node> line = new ArrayList<>();
        line.add(root);

        String tree = "";

        // until there is still some children in line
        int cnt = 0;
        while (true) {
            boolean isNode = false;

            List<Node> nextLine = new ArrayList<>();

            cnt++;

            String preStr = "";
            for (int i = 0; i < Math.pow(2, depth - cnt - 1) - 1; i++) {
                preStr = preStr + "\t";
            }

            String midStr = "";
            for (int i = 0; i < Math.pow(2, depth - cnt); i++) {
                midStr = midStr + "\t";
            }
            if (cnt < depth) {
                preStr = preStr + "    ";
                midStr = midStr + "    ";
            }

            String str = preStr;

            for (Node node : line) {

                if (node.value != 0) {
                    str = str + node.value + " " + midStr;
                } else {
                    str = str + "na" + midStr;
                }

                if (node.leftChild != null) {
                    isNode = true;
                    nextLine.add(node.leftChild);
                } else {
                    nextLine.add(new Node());
                }

                if (node.rigthChild != null) {
                    isNode = true;
                    nextLine.add(node.rigthChild);
                } else {
                    nextLine.add(new Node());
                }
            }

            line = nextLine;
            tree = tree + "\n" + str;

            if (!isNode)
                break;
        }

        return tree + "\n";
    }

    int maxDepth(Node node) {
        if (node == null) {
            return (-1); // an empty tree has height −1
        } else {
            // compute the depth of each subtree
            int leftDepth = maxDepth(node.leftChild);
            int rightDepth = maxDepth(node.rigthChild);
            // use the larger one
            if (leftDepth > rightDepth)
                return (leftDepth + 1);
            else
                return (rightDepth + 1);
        }
    }

}