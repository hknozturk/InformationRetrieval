package BinarySearchTree;

/**
 * reference: https://www.moreofless.co.uk/binary-search-tree-bst-java/
 * Each node in the tree has at most only two children
 * Each node is represented with a key and associated data
 * Key in left children is less than the parent node and key in the right node is greater
 * Each node is in itself a binary search tree
 *
 * search on B-Tree is a bit slower than hash. B-Tree O(logN) and hash O(N)
 * However, it is better on wild-card queries.
 * Balancing B-Trees are expensive.
 *
 * TO-DO: Didn't implement search functionality for wild-card queries.
 **/

public class BinarySearchTree {
    private String data;
    private BinarySearchTree left;
    private BinarySearchTree right;

    public BinarySearchTree() {
        this.data = null;
        this.left = null;
        this.right = null;
    }

    public BinarySearchTree(String data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public static BinarySearchTree createTree(String content) {
        BinarySearchTree bst = new BinarySearchTree();
        if (content != null) {
            // Remove the punctuation from the content
            // We don't need this in our case. Our analyzer will do the job.
            content = content.replaceAll("(\\w+)\\p{Punct}(\\s|$)", "$1$2");
            String[] words = content.split( " " );
            bst = new BinarySearchTree();

            for( int i = 0; i < words.length; i++ ) {
                bst.addNode( words[i]);
            }
        }
        return bst;
    }

    /**
     * If key is less than node, then add to left child
     * If key is greater than node, then add to right child
     **/
    public void addNode(String data) {
        if (this.data == null) {
            this.data = data;
        } else {
            if (this.data.compareTo(data) < 0) {
                if (this.left != null) {
                    this.left.addNode(data);
                } else {
                    this.left = new BinarySearchTree(data);
                }
            } else if (this.data.compareTo(data) > 1) {
                if (this.right != null) {
                    this.right.addNode(data);
                } else {
                    this.right = new BinarySearchTree(data);
                }
            } else {
                // update the existing node
                this.data = data;
            }
        }
    }

    public void traversePreOrder() {
        System.out.println(this.data);

        if (this.left != null) {
            this.left.traversePreOrder();
        }
        if (this.right != null) {
            this.right.traversePreOrder();
        }
    }

    public void traverseInOrder() {
        if (this.left != null) {
            this.left.traverseInOrder();
        }
        System.out.println(this.data);
        if (this.right != null) {
            this.right.traverseInOrder();
        }
    }

    public void traversePostOrder() {
        if (this.left != null) {
            this.left.traversePostOrder();
        }
        if (this.right != null) {
            this.right.traversePostOrder();
        }
        System.out.println(this.data);
    }
}
