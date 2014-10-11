// -------------------------------------------------------------------------
/**
 *  main object.
 *  Test cases for Linked list
 *  @author Yosub Lee
 *  @version June 7, 2014
 */
public class main
{

    public static void main(String[] args)
    {
        // Test Cases for Singly Linked List
        LinkedList<Integer> linkedlist = new LinkedList<Integer>();

        // Add at the end
        linkedlist.addNodeAtEnd(20);
        linkedlist.addNodeAtEnd(30);
        linkedlist.addNodeAtEnd(40);
        linkedlist.addNodeAtEnd(50);

        System.out.println("[Add Node at the End]");
        System.out.println(linkedlist.toString());
        System.out.println("Size: " + linkedlist.size());

        // Add at the beginning
        linkedlist.addNodeAtBegin(2);
        linkedlist.addNodeAtBegin(1);
        System.out.println("\n[Add Node at the Beginning]");
        System.out.println(linkedlist.toString());
        System.out.println("Size: " + linkedlist.size());

        // Delete
        linkedlist.deleteNode(20);
        System.out.println("\n[Delete a Node (20) ]");
        System.out.println(linkedlist.toString());
        System.out.println("Size: " + linkedlist.size());

        // Delete a number that doesn't exist
        linkedlist.deleteNode(100);

        // Clear
        linkedlist.clear();
        System.out.println("\n[ Clear this list ]");
        System.out.println(linkedlist.toString());
        System.out.println("Size: " + linkedlist.size());
    }

}
