package Yosub.P4;
// The test harness will belong to the following package; the BST
// implementation will belong to it as well.  In addition, the BST
// implementation will specify package access for the inner node class
// and all data members in order that the test harness may have access
// to them.
//

import java.lang.Math;

// -------------------------------------------------------------------------
/**
 *  AVL Tree. Always Balanced so Search operations has Complexity O(Log n)
 *  This Class was implemented based on BST Tree. Remove Operation is not
 *  implemented since it's not required for Projec4
 *  @param <T> Generic type
 *
 *  @author Yosub Lee
 *  @version Jun 23, 2014
 */
public class AVL<T extends Comparable<? super T>> {

    // -------------------------------------------------------------------------
    /**
     *  AVL Node. It's the same as typical Binary tree node except the variable
     *  for height.
     *
     *  @author Yosub Lee
     *  @version Jun 23, 2014
     */
    class AVLNode {

        public AVLNode( T elem ) {
            element = elem;
            height = 0;
            left = null;
            right = null;
        }

        public AVLNode( T elem, AVLNode lt, AVLNode rt ) {
            element = elem;
            height = 0;
            left = lt;
            right = rt;
        }

        T        element;  // the data in the node
        AVLNode left;     // pointer to the left child
        AVLNode right;    // pointer to the right child
        int height;       // height of this node

    }

    AVLNode root;        // pointer to root node, if present
    AVLNode pool;        // pointer to first node in the pool
    int        pSize;       // size limit for node pool
    int        numOfPool;
    int        numOfNodes;

    public AVL( ) {
        root = null;
        pool = null;
        pSize = 0;
        numOfPool = 0;
        numOfNodes = 0;
    }

    public AVL( int Sz ) {
        root = null;
        pool = null;
        pSize = Sz;
        numOfPool = 0;
        numOfNodes = 0;
    }

    public boolean isEmpty( ) {
        if ( numOfNodes > 0 ) return false;
        else return true;
    }

    public T find( T x ) {
        return findHelper(x, root);
    }

    private T findHelper(T target, AVLNode currNode)
    {
        if ( currNode == null ) return null;

        if ( target.compareTo(currNode.element) == 0 )
            return currNode.element;
        else if ( target.compareTo(currNode.element) < 0 )
            // target is smaller than Data of this node --> Left
            return findHelper(target, currNode.left);
        else
            return findHelper(target, currNode.right);
    }

    // ----------------------------------------------------------
    /**
     * Insert Operation. After insertion, it re-balances tree
     * @param x new data
     * @return if there is duplicate data then returns false
     * otherwise true.
     */
    public boolean insert( T x ) {
        int sizeBefore = numOfNodes;
        root = insertHelper(x, root);

        if ( sizeBefore == numOfNodes) return false;
        else return true;

    }


    private AVLNode insertHelper( T data, AVLNode currNode) {
        if ( currNode == null ) {
            numOfNodes ++;
            currNode = new AVLNode(data);
        }
        else {
            if ( data.compareTo(currNode.element) < 0) {
                currNode.left = insertHelper(data, currNode.left);
            }
            else if ( data.compareTo(currNode.element) > 0) {
                currNode.right = insertHelper(data, currNode.right);
            }
            else {
                // Duplicate data.
                currNode = null;
            }
        }
        // re-balance this tree ///////////////////
        // Four cases to be handled here         //
        // 1. right-rotation                     //
        // 2. left-rotation                      //
        // 3. right-double (right-left) rotation //
        // 4. left-double (left-right) rotation  //
        ///////////////////////////////////////////
        int heightCompare = getHeight(currNode.left) - getHeight(currNode.right);
        if ( heightCompare > 1) {
            if ( data.compareTo( currNode.left.element ) < 0 )
                currNode = rightRotate(currNode);
            else {
                // Double right Rotation case
                currNode.left = leftRotate(currNode.left);
                currNode = rightRotate(currNode);
            }
        }
        else if ( heightCompare < -1) {
            if ( data.compareTo( currNode.right.element ) > 0 )
                currNode = leftRotate(currNode);
            else {
                // Double left Rotation case
                currNode.right = rightRotate(currNode.right);
                currNode = leftRotate(currNode);
            }
        }

        // Update height
        currNode.height = 1 +
            Math.max( getHeight(currNode.left), getHeight( currNode.right));

        return currNode;
    }

    private int getHeight(AVLNode node) {
        if ( node == null ) return -1;
        else return node.height;
    }

    //////////////// [ Rotation ] ////////////////////////
    //////////////////////////////////////////////////////
    private AVLNode leftRotate(AVLNode node) {
        AVLNode rightChild = node.right;
        node.right = rightChild.left;
        rightChild.left = node;

        rightChild.height = 1 +
            Math.max(getHeight(node), getHeight(rightChild.left));
        node.height = 1 +
            Math.max(getHeight(node.left), getHeight(node.right));

        return rightChild;
    }

    private AVLNode rightRotate(AVLNode node) {
        AVLNode leftChild = node.left;
        node.left = leftChild.right;
        leftChild.right = node;

        leftChild.height = 1 +
            Math.max(getHeight(node), getHeight(leftChild.right));
        node.height = 1 +
            Math.max(getHeight(node.left), getHeight(node.right));

        return leftChild;
    }

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////

//
//    public boolean remove( T x ) {
//    }
//
//    private AVLNode removeHelper( T data, AVLNode currNode ) {
//    }
//
//    public AVLNode deleteMin(AVLNode currNode) {
//        if ( currNode == null ) return null;
//
//        if ( currNode.left == null ) {
//            return currNode.right;
//        }
//
//        if ( currNode.left != null )
//            currNode.left = deleteMin(currNode.left);
//
//        return currNode;
//    }

    private AVLNode getMin(AVLNode currNode) {
        if ( currNode == null ) return null;

        if ( currNode.left == null ) return currNode;
        else return getMin( currNode.left );
    }

    private boolean pushPool(AVLNode node) {
        if ( node == null ) return false;

        if ( pSize > 0 && numOfPool < pSize) {
            numOfPool++;
            pool = pushPoolHelper(node, pool);
            return true;
        }
        else
            return false;
    }

    // ----------------------------------------------------------
    /**
     * Recursive Method for pushPool method. this method cleans out the target
     * node.
     * @param node target node into Pool array
     * @param poolNode root pool node
     * @return root pool node added new node
     */
    private AVLNode pushPoolHelper(AVLNode node, AVLNode poolNode) {
        if ( poolNode == null ) {
            node.element = null;
            node.left = null;
            node.right = null;
            return node;
        }
        else {
            poolNode.right = pushPoolHelper( node, poolNode.right);
            return poolNode;
        }
    }

    // ----------------------------------------------------------
    /**
     * Returns pool node if it is available
     * @return if pool is not empty, then returns pool node,
     * otherwise it returns null
     */
    private AVLNode popPool() {
        if ( pool != null && numOfPool > 0) {
            AVLNode temp = pool;

            pool = pool.right;
            numOfPool--;
            return temp;
        }
        else {
            return null;
        }
    }

    // ----------------------------------------------------------
    /**
     * Find node containing target data
     * @param x target data
     * @return node containing target data
     */
    public AVLNode findNode( T x ) {
        return findNodeHelper( x, root);
    }

    // ----------------------------------------------------------
    /**
     * Recursive method to find target dta
     * @param x target data
     * @return node containing target data
     */
    private AVLNode findNodeHelper ( T x, AVLNode currNode) {
        if ( currNode == null ) return null;

        if ( x.compareTo(currNode.element) < 0 )
            return findNodeHelper( x, currNode.left);
        else if ( x.compareTo(currNode.element) > 0)
            return findNodeHelper( x, currNode.right);
        else
            return currNode;
    }

    // Delete the subtree, if any, whose root contains an element matching x.
    // Pre:   x is null or points to a valid object of type T
    // Post:  if the tree contained x, then it the subtree rooted at that
    //           node has been removed
    public boolean prune( T x) {
        if ( root == null ) return false;

        root = pruneHelper( x, root);
        return true;
    }

    /**
     * Recursive method for prune method. delete each node by using
     * post-order traverse
     * @param x target data
     * @param currNode start node to search target bide to prune
     * @return deleted node
     */
    private AVLNode pruneHelper( T x, AVLNode currNode) {
        if ( currNode == null )  return null;

        if ( x.compareTo(currNode.element) < 0 )
            currNode.left = pruneHelper( x, currNode.left);
        else if ( x.compareTo(currNode.element) > 0)
            currNode.right = pruneHelper( x, currNode.right);
        else {
            trimming(currNode);
            return null;
        }

        return currNode;
    }

    /**
     * Delete all children of this node
     * @param currNode root of subtree
     * @return deleted node
     */
    private AVLNode trimming( AVLNode currNode) {
        if ( currNode == null ) return null;

        if ( currNode.left != null )
            currNode.left = trimming(currNode.left);
        if ( currNode.right != null )
            currNode.right = trimming(currNode.right);

        pushPool(currNode);

        numOfNodes--;
        return currNode;
    }

    public void clear( ) {
        if ( root == null ) return;

        T data = root.element;

        prune(data);
    }

    public boolean equals(Object other) {
        if ( other == null ) return false;
        if ( this.getClass() != other.getClass()) return false;

        String thisBST = "";
        String targetBST = "";

        thisBST = allNodesToString(this.root);
        targetBST = allNodesToString( ((AVL)other).root );

        if ( thisBST.equals(targetBST) )
            return true;
        else
            return false;
    }

    // ----------------------------------------------------------
    /**
     * Returns the list of all nodes in String form
     * @param currNode root node of any tree
     * @return String of a list of all nodes in this tree
     */
    private String allNodesToString(AVLNode currNode) {
        if ( currNode == null ) return "";

        String nodes = "";
        nodes += allNodesToString(currNode.left);
        nodes += allNodesToString(currNode.right);

        nodes += currNode.element.toString();
        return nodes;
    }

    public int height() {
        int height;

        if ( root == null) height = 0;
        else height = heightHelper(root);

        return height;

    }

    // ----------------------------------------------------------
    /**
     * helper method for height method. Recursion. it records the height of
     * the deepest nodes of left subtree and right subtree then returns bigger one
     * @param currNode start node
     * @return height of the deepest node
     */
    private int heightHelper(AVLNode currNode) {
        if ( currNode == null ) return 0;

        int left = heightHelper(currNode.left) + 1;
        int right = heightHelper(currNode.right) + 1;

        if ( left < right) return right;
        else return left;
    }



    /**
     * Traverse In-order and Display this Tree in form of '(level) data'
     */
    public void displayTree() {
        System.out.println("=== Traverse [Pre-Order] ===    " + "Total: " + numOfNodes);
        System.out.println("Height: " + getHeight(root.left) + " / " + getHeight(root.right) );
        displayHelper(root, 0);
    }

    /**
     * recursive method for displayTree method.
     */
    private void displayHelper( AVLNode currNode, int deep ) {
        if ( currNode == null ) return;

        String space = "";

        if ( currNode.left != null )
            displayHelper( currNode.left, deep + 1 );

        for (int i = 0; i < deep ; i++)
            space += "-";
        System.out.println( space + " " + currNode.element.toString() + " | H:" + currNode.height);

        if ( currNode.right != null )
            displayHelper( currNode.right, deep + 1);
    }

    // ----------------------------------------------------------
    /**
     * Display number of pool nodes
     */
    public void displayPool() {
        System.out.println("=== Pool Nodes ===");
        System.out.println("Num of pool: " + numOfPool);
        if ( pool == null ) System.out.println("Pool array is Empty");
        else displayPoolHelper(pool);

        System.out.println("");
    }

    // ----------------------------------------------------------
    /**
     * Recursive method for displayPool method
     */
    private void displayPoolHelper( AVLNode currNode ) {
        if ( currNode == null ) return;

        System.out.print("[" + currNode.element + "]  ");

        if ( currNode.right != null)
            displayPoolHelper( currNode.right);
    }
}