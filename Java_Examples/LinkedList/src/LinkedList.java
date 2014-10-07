// -------------------------------------------------------------------------
/**
 *  Singly Linked List.
 *  @param <T> Generic type data in a Node
 *
 *  @author Yosub Lee
 *  @version June 7, 2014
 */
public class LinkedList<T>
{
    // -------------------------------------------------------------------------
    /**
     *  Node Class (Inner Class)
     *  @param <T> generic type value
     *
     *  @author Yosub Lee
     *  @version June 7, 2014
     */
    public class Node<T> {
        private T data;
        private Node<T> next;
        public Node(T value) {
            data = value;
            next = null;
        }

        public void setNext(Node<T> node) {
            next = node;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setValue(T value) {
            data = value;
        }

        public T getValue() {
            return data;
        }

        public String toString() {
            return data.toString();
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // ----------------------------------------------------------
    /**
     * Constructor.
     * @param value new data for the head node
     */
    public LinkedList(T value) {
        head = new Node<T>(value);
        tail = head;
        size = 1;
    }

    public void addNodeAtBegin(T value) {
        Node<T> temp = new Node<T>(value);
        temp.setNext(head);
        head = temp;
        size++;
    }

    // ----------------------------------------------------------
    /**
     * Add new node to the list
     * @param value new data
     */
    public void addNodeAtEnd(T value) {
        Node<T> temp = new Node<T>(value);
        tail.setNext(temp);
        tail = temp;
        size++;
    }

    // ----------------------------------------------------------
    /**
     * Delete a node which has a given data
     * @param value target data
     */
    public void deleteNode(T value) {
        Node<T> temp, prev;
        temp = head;
        prev = head;
        for (int i=0; i < size; i++) {

            if ( temp.getValue().equals(value)) {
                prev.setNext(temp.getNext());
                size--;
                return;
            }
            prev = temp;
            temp = temp.getNext();
        }
    }

    // ----------------------------------------------------------
    /**
     * Find the node has a specific data
     * @param value
     * @return Node
     */
    public Node<T> search(T value) {
        Node<T> temp = head;
        for (int i=0; i < size; i++) {
            if (temp.getValue().equals(value)) {
                return temp;
            }
            temp = temp.getNext();
        }

        return null;
    }

    // ----------------------------------------------------------
    /**
     * Print out data in all nodes
     * @return String of all data
     */
    public String toString() {
        StringBuffer output = new StringBuffer();
        Node<T> temp = head;
        for (int i=0; i< size; i++) {
            output.append(temp.toString() + " ");
            temp = temp.getNext();
        }

        return output.toString();
    }

    public int size() {
        return size;
    }
}
