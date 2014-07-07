package Minor.P2.DS;

public class BST<T extends Comparable<? super T>> {

    class BinaryNode {

        public BinaryNode( T elem ) {
            element = elem;
            left = null;
            right = null;
        }

        public BinaryNode( T elem, BinaryNode lt, BinaryNode rt ) {
            element = elem;
            left = lt;
            right = rt;
        }

        T          element;
        BinaryNode left;
        BinaryNode right;

    }

    BinaryNode root;
    BinaryNode pool;
    int        pSize;
    int        numOfPool;
    int        numOfNodes;

    public BST( ) {
        root = null;
        pool = null;
        pSize = 0;
        numOfPool = 0;
        numOfNodes = 0;
    }

    public BST( int Sz ) {
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

    // ----------------------------------------------------------
    /**
     * Finds a record including the target record.
     * @param x the target record
     * @return the target node, if it doesn't exist it returns null
     */
    public T find( T x ) {
        return findHelper(x, root);
    }

    // ----------------------------------------------------------
    /**
     * Find helper method. It does recursive work to find target data
     * @param target Target Data T
     * @param currNode target node to compare
     */
    private T findHelper(T target, BinaryNode currNode)
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

    // Insert element x into BST, unless it is already stored.  Return true
    // if insertion is performed and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree contains x
    public boolean insert( T x ) {
        int sizeBefore = numOfNodes;
        root = insertHelper(x, root);

        if ( sizeBefore == numOfNodes ) return false;
        else return true;
    }

    // ----------------------------------------------------------
    /**
     * Recursive Insert Method. It finds appropriate position to insert new
     * value and add to this tree
     * @param data new data
     * @param currNode start node to find correct position for insertion
     */
    private BinaryNode insertHelper( T data, BinaryNode currNode) {
        if ( currNode == null ) {
            BinaryNode poolNode = popPool();

            if ( poolNode != null ) {
                poolNode.element = data;
                poolNode.right = null;
                numOfNodes ++;
                return poolNode;
            }
            else {
                numOfNodes ++;
                return new BinaryNode(data);
            }
        }

        if ( data.compareTo(currNode.element) < 0)
            currNode.left = insertHelper(data, currNode.left);
        else if ( data.compareTo(currNode.element) > 0)
            currNode.right = insertHelper(data, currNode.right);
        else {
            // found the same data, doing nothing
        }

        return currNode;
    }

    // Delete element matching x from the BST, if present.  Return true if
    // matching element is removed from the tree and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree does not contain x
    public boolean remove( T x ) {
        int SizeBefore = numOfNodes;
        if ( root == null ) return false;

        root = removeHelper( x, root );
        if ( SizeBefore == numOfNodes ) return false;
        else return true;
    }

    // ----------------------------------------------------------
    /**
     * Recursion Method for remove method.
     * @param currNode start node to remove target
     * @param data target Data
     * @return a subtree with deleted target
     */
    private BinaryNode removeHelper( T data, BinaryNode currNode ) {
        if ( currNode == null ) return null;

        int comparison = data.compareTo(currNode.element);

        if ( comparison < 0 )
            currNode.left = removeHelper(data, currNode.left);
        else if ( comparison > 0 )
            currNode.right = removeHelper(data, currNode.right);
        else {
            if ( currNode.right == null) {
                BinaryNode tempLeft = currNode.left;
                pushPool(currNode);
                numOfNodes--;
                return tempLeft;
            }
            if ( currNode.left== null ) {
                BinaryNode tempRight = currNode.right;
                pushPool(currNode);
                numOfNodes--;
                return tempRight;
            }


            BinaryNode tempLeft = currNode.left;
            BinaryNode tempRight = currNode.right;
            BinaryNode temp = currNode;
            BinaryNode min = getMin(currNode.right);

            currNode.right = deleteMin(currNode.right);
            min.left = currNode.left;
            min.right = currNode.right;
            currNode = min;

            pushPool(temp);
            numOfNodes--;
        }

        return currNode;
    }

    // ----------------------------------------------------------
    /**
     * It finds the minimum of this subtree and makes it null.
     * % It does not actually deleting the node! if you want to delete the minimum
     * completely then Use following two method 1. getMin() 2. deleteMin
     * @param currNode root node of sub tree
     */
    public BinaryNode deleteMin(BinaryNode currNode) {
        if ( currNode == null ) return null;

        if ( currNode.left== null ) {
            return currNode.right;
        }

        if ( currNode.left != null )
            currNode.left = deleteMin(currNode.left);

        return currNode;
    }

    // ----------------------------------------------------------
    /**
     * Swap values of two nodes. It swaps data and children pointers
     * @param a first target node
     * @param b second target node
     */
    private void swapData(BinaryNode A, BinaryNode B) {
        BinaryNode temp = A;

        BinaryNode tempLeftB = B.left;
        BinaryNode tempRightB = B.right;

        BinaryNode tempLeftA = A.left;
        BinaryNode tempRightA = A.right;

        A = B;
        B = temp;
        B.left = tempLeftB;
        B.right = tempRightB;
        A.left = tempLeftA;
        A.right = tempRightA;

    }
    // ----------------------------------------------------------
    /**
     * Find Minimum value of this tree
     * @param currNode start node to find minimum
     */
    private BinaryNode getMin(BinaryNode currNode) {
        if ( currNode == null ) return null;

        if ( currNode.left == null ) return currNode;
        else return getMin( currNode.left );
    }

// ----------------------------------------------------------
    /**
     * Push node into Pool array if pool has room for it
     * @param node target node into Pool array
     * @return if it is available to push and succeed, returns true, otherwise
     * return false
     */
    private boolean pushPool(BinaryNode node) {
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
    private BinaryNode pushPoolHelper(BinaryNode node, BinaryNode poolNode) {
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
    private BinaryNode popPool() {
        if ( pool != null && numOfPool > 0) {
            BinaryNode temp = pool;

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
    public BinaryNode findNode( T x ) {
        return findNodeHelper( x, root);
    }

    // ----------------------------------------------------------
    /**
     * Recursive method to find target dta
     * @param x target data
     * @return node containing target data
     */
    private BinaryNode findNodeHelper ( T x, BinaryNode currNode) {
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
    private BinaryNode pruneHelper( T x, BinaryNode currNode) {
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
    private BinaryNode trimming( BinaryNode currNode) {
        if ( currNode == null ) return null;

        if ( currNode.left != null )
            currNode.left = trimming(currNode.left);
        if ( currNode.right != null )
            currNode.right = trimming(currNode.right);

        pushPool(currNode);

        numOfNodes--;
        return currNode;
    }

    // Return the tree to an empty state.
    // Pre:   none
    // Post:  the binary tree contains no elements
    public void clear( ) {
        if ( root == null ) return;
        T data = root.element;
        prune(data);
    }

    // Return true iff other is a BST that has the same physical structure
    // and stores equal data values in corresponding nodes.  "Equal" should
    // be tested using the data object's equals() method.
    // Pre:   other is null or points to a valid BST<> object, instantiated
    //           on the same data type as the tree on which equals() is invoked
    // Post:  both binary trees are unchanged
    public boolean equals(Object other) {
        if ( other == null ) return false;
        if ( this.getClass() != other.getClass()) return false;

        String thisBST = "";
        String targetBST = "";

        thisBST = allNodesToString(this.root);
        targetBST = allNodesToString( ((BST)other).root );

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
    private String allNodesToString(BinaryNode currNode) {
        if ( currNode == null ) return "";

        String nodes = "";
        nodes += allNodesToString(currNode.left);
        nodes += allNodesToString(currNode.right);

        nodes += currNode.element.toString();
        return nodes;
    }
    // Return number of levels in the tree.
    // Pre:   tree is a valid BST<> object
    // Post:  the binary tree is unchanged
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
    private int heightHelper(BinaryNode currNode) {
        if ( currNode == null ) return 0;

        int left = heightHelper(currNode.left) + 1;
        int right = heightHelper(currNode.right) + 1;

        if ( left < right) return right;
        else return left;
    }



    // ----------------------------------------------------------
    // Following methods are implemented for debugging purpose.
    // displayInorder() method prints out each nodes with In-order Traverse
    // displayPool() method prints out all nodes in pool array
    // -Yosub Lee
    /**
     * Traverse In-order and Display this Tree in form of '(level) data'
     */
    public void displayTree() {
        System.out.println("=== Traverse [Pre-Order] ===    " + "Total: " + numOfNodes);
        displayHelper(root, 0);
    }

    /**
     * recursive method for displayTree method.
     */
    private void displayHelper( BinaryNode currNode, int deep ) {
        if ( currNode == null ) return;

        String space = "";

        if ( currNode.left != null )
            displayHelper( currNode.left, deep + 1 );

        for (int i = 0; i < deep ; i++)
            space += "---";
        System.out.println( space + " " + currNode.element.toString() );

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
    private void displayPoolHelper( BinaryNode currNode ) {
        if ( currNode == null ) return;

        System.out.print("[" + currNode.element + "]  ");

        if ( currNode.right != null)
            displayPoolHelper( currNode.right);
    }
}