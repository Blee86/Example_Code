
public class main
{

    public static void main(String[] args)
    {
        // Double Linked List Test Cases
        DoubleLinkedList<Integer> list = new DoubleLinkedList<Integer>();

        // Insertion at the begging
        list.insertAtBegin(20);
        list.insertAtBegin(30);
        list.insertAtBegin(40);
        list.insertAtBegin(50);

        System.out.println(list.toString());
        System.out.println("Size: " + list.size());

        // Insertion at the end
        list.insertAtEnd(2);
        list.insertAtEnd(3);
        list.insertAtEnd(4);
        list.insertAtEnd(5);


        System.out.println(list.toString());
        System.out.println("Size: " + list.size());

        // Deletion Test

        list.delete(3);
        list.delete(50);
        System.out.println(list.toString());
        System.out.println("Size: " + list.size());

        System.out.println("Head: " + list.head.getValue());
        System.out.println("Tail: " + list.tail.getValue());

        list.delete(30);
        list.delete(5);
        list.delete(4);

        System.out.println(list.toString());
        System.out.println("Size: " + list.size());

        System.out.println("Head: " + list.head.getValue());
        System.out.println("Tail: " + list.tail.getValue());


        list.clear();
        System.out.println(list.toString());
        System.out.println("Size: " + list.size());

        System.out.println("Head: " + list.head.getValue());
        System.out.println("Tail: " + list.tail.getValue());



    }

}
