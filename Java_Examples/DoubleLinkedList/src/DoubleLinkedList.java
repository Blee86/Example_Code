
// -------------------------------------------------------------------------
/**
 *  Doubly Linked List
 *
 *  @author Yosub Lee
 *  @email yosublee86@gmail.com
 *  @version June 8, 2014
 */
public class DoubleLinkedList<T>
{
    // -------------------------------------------------------------------------
    /**
     *  Inner Node Class
     *  @param <T> Generic type data value
     *
     *  @author Yosub Lee
     *  @version Oct 8, 2014
     */
    public class Node<T> {
        T data;
        Node<T> next;   // Pointer to next
        Node<T> prev;   // Pointer to prev

        public Node(T value) {
            data = value;
            next = null;
            prev = null;
        }

        // Getters + Setters
        public void setValue(T value) {
            data = value;
        }

        public T getValue() {
            return data;
        }

        public void setNext(Node<T> node) {
            next = node;
        }

        public void setPrev(Node<T> node) {
            prev = node;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }
    }

    public Node<T> head;
    public Node<T> tail;
    private int size;

    public DoubleLinkedList() {
        head = new Node<T>(null);
        tail = new Node<T>(null);
        head.setNext(tail);
        tail.setPrev(head);

        size = 0;
    }

    public void insertAtBegin(T value) {
        if ( head.getValue() == null) {
            head.setValue(value);
        }
        else {
            Node<T> newNode = new Node<T>(value);
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }

        size++;
    }

    public void insertAtEnd(T value) {
        if ( tail.getValue() == null) {
            tail.setValue(value);
        }
        else {
            Node<T> newNode = new Node<T>(value);
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }

        size ++;
    }

    public Node<T> search(T value) {
        if (size != 0) {
            Node<T> temp = head;
            for (int i=0; i < size; i++) {
                if ( temp.getValue().equals(value))
                    return temp;
                else
                    temp = temp.getNext();
            }
        }
        return null;
    }

    public T delete(T value) {
        Node<T> target = search(value);

        if (target != null) {
            if ( target == head )
                head = head.getNext();
            else if ( target == tail )
                tail = tail.getPrev();
            else {
                Node<T> next = target.getNext();
                Node<T> prev = target.getPrev();

                if ( prev != null)
                    prev.setNext(next);

                if ( next != null)
                    next.setPrev(prev);
            }
            size--;
            return target.getValue();
        }

        return null;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuffer array = new StringBuffer();
        Node<T> temp = head;
        for(int i=0; i<size; i++) {
            if ( temp != null ) {
                array.append("["+ temp.getValue().toString() + "] ");
                temp = temp.getNext();
            }
        }

        return array.toString();
    }

    public void clear() {
        head.setValue(null);
        tail.setValue(null);

        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }
}
