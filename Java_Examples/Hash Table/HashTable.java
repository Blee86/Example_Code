import java.io.IOException;
import java.io.FileWriter;

public class HashTable<T extends Hashable> {
    T[]         Table;        // stores data objects
    slotState[] Status;       // stores corresponding status values
    int         Size;         // dimension of Table
    int         Usage;        // # of data objects stored in Table
    FileWriter  Log;          // target of logged output
    boolean     loggingOn;    // write output iff this is true

    public HashTable(int Sz) {
        Size = Sz;	//Size of this table
        Table = (T[]) new Hashable[Size];
        Status = new slotState[Sz];
        Usage = 0;
        Log = null;
        loggingOn = false;
        
        for ( int i = 0; i < Sz; i++) {
            Table[i] = null;
            Status[i] = slotState.EMPTY;
        }
    }

   public T Insert(T Elem) throws IOException {
        // If it is necessary, resize the arrays
        // the condition to resize the arrays is loadFactor >= 90%
        int homeslot = Elem.Hash() % Size;
        if ( !checkInsert(homeslot, 0, Elem) ) return null;

        float loadFactor = (float)Usage/(float)Size;
        if ( loadFactor >= 0.7 ) resizeArray();
        return insertHelp(homeslot, 0, Elem);
    }

    // Recursive method for insert operation
    // It also writes logs if it is requested
    private T insertHelp(int homeslot, int k, T elem) throws IOException {
        int newHomeslot = (homeslot + quadraticProbe(k)) % Size;

        if ( Status[newHomeslot] == slotState.FULL ) {
            // I know It has been checked from 'checkInsert' method, but
            // just in case..in case I don't want to us checkInsert..
            if ( elem.equals(Table[newHomeslot]) == true ) {
                return null;
            }
            else
                return insertHelp( homeslot, k+1, elem);
        }
        else {
            if ( loggingOn == true ) Log.write("   inserted");
            Table[newHomeslot] = elem;
            Status[newHomeslot] = slotState.FULL;
            Usage++;
            return Table[newHomeslot];
        }
    }

    // Before insert new record it checks duplicate data
    // Return: If there is duplicate data then return true
    // otherwise return false
    private boolean checkInsert(int homeslot, int k, T elem) throws IOException {
        int newHomeslot = (homeslot + quadraticProbe(k)) % Size;

        if ( loggingOn == true ) Log.write("    " + newHomeslot);

        if ( Status[newHomeslot] == slotState.FULL ) {
            if ( elem.equals(Table[newHomeslot]) == true ) {
                if ( loggingOn == true ) Log.write("   duplicate");
                return false;
            }
            else
                return checkInsert( homeslot, k+1, elem);
        }
        else if ( Status[newHomeslot] == slotState.TOMBSTONE ) {
            // Tombstone, there is still a possibility of that the duplicate
            // data was inserted and probed
            return checkInsert( homeslot, k+1, elem);
        }
        else {
            // It's EMPTY which means there is no duplicate data. it's good to
            // add new data
            return true;
        }
    }


    public T Find(T Elem) throws IOException {
        int homeslot = Elem.Hash() % Size;
        return findHelper(homeslot, 0, Elem);
    }

    private T findHelper(int homeslot, int k, T elem) throws IOException {
        int newHomeslot = (homeslot + quadraticProbe(k)) % Size;

        if ( loggingOn == true ) Log.write("    " + newHomeslot);

        if ( Status[newHomeslot] == slotState.FULL ) {
            if ( elem.equals(Table[newHomeslot]) == true ) {
                if ( loggingOn == true ) Log.write("   found");
                return Table[newHomeslot];
            }
            else
                return findHelper( homeslot, k+1, elem);
        }
        else if ( Status[newHomeslot] == slotState.TOMBSTONE ){
            // found Tombstone, but it doesn't mean elem does not exist
            // so go further
            return findHelper( homeslot, k+1, elem);
        }
        else {
            // meet EMPTY, now we are sure that there is no record we are
            // looking for..
            if ( loggingOn == true ) Log.write("   record not found");
            return null;
        }

    }


    public T Delete(T Elem) throws IOException {
        int homeSlot = Elem.Hash() % Size;
        return deleteHelper(homeSlot, 0, Elem);
    }

    private T deleteHelper(int homeslot, int k, T elem) throws IOException {
        int newHomeslot = (homeslot + quadraticProbe(k)) % Size;

        if ( loggingOn == true ) Log.write("    " + newHomeslot);

        if ( Status[newHomeslot] == slotState.FULL ) {
            if ( elem.equals(Table[newHomeslot]) == true) {
                T temp = Table[newHomeslot];
                Status[newHomeslot] = slotState.TOMBSTONE;
                Table[newHomeslot] = null;
                if ( loggingOn == true ) Log.write("   deleted");
                return temp;
            }
            else
                return deleteHelper(homeslot, k+1, elem);
        }
        else if ( Status[newHomeslot] == slotState.TOMBSTONE)
            return deleteHelper(homeslot, k+1, elem);
        else {
            // Meet Empty. There is no target record
            if ( loggingOn == true ) Log.write("   not found");
            return null;
        }
    }

    // Reset hash table to (almost) same state as when first constructed.
    // Post:
    //      this.Table is an array of dimension Sz; all entries are null
    //      this.Status is an array of dimension Sz; all entries are
    //           EMPTY
    //      this.Opt is unchanged
    //      this.Usage == 0
    //      this.Log  is unchanged
    //      this.loggingOn is unchanged
    //
    public void Clear() {
        for ( int i = 0; i < Size; i++ ) {
            Status[i] = slotState.EMPTY;
            Table[i] = null;
        }

        if ( loggingOn == true ) {
            try {
                Log.write("table reset");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Reset hash table's log output target.
    // Pre:
    //      Log is an open on a file, or is null
    // Post:
    //      If Log is null, no changes are made.
    //      Otherwise: this.Log == Log
    // Return true iff this.Log has been changed and is not null.
    //
    public boolean setLog(FileWriter Log) {
        if ( this.Log == Log ) return false;

        this.Log = Log;
        if ( this.Log == null ) return false;

        loggingOn = true;
        return true;
    }

    // Turn internal logging on.
    // Post:
    //      If this.Log is not null, loggingOn == true
    //      Otherwise, loggingOn == false
    // Returns final value of loggingOn.
    //
    public boolean logOn() {
        if ( this.Log != null )
            loggingOn = true;
        else
            loggingOn = false;

        return loggingOn;
    }

    // Turn internal logging off.
    // Post:
    //      loggingOn == false
    // Returns final value of loggingOn.
    //
    public boolean logOff() {
        loggingOn = false;
        return loggingOn;
    }

    // Probing Method.
    // It will use the quadratic function (K^2 + K)/2 to compute the probe steps.
    public int quadraticProbe(int k) {
        if ( k <= 0 ) return 0;
        return ((k*k + k) / 2) % Size;
    }

    // Free code from Professor
    public void Display(FileWriter Log) throws IOException {

        if ( Usage == 0 ) {
            Log.write("Hash table is empty.\n");
            return;
        }

        for (int pos = 0; pos < Size; pos++) {

            if ( Status[pos] == slotState.FULL ) {
                Log.write(pos + ":  " + Table[pos] + "\n");
            }
            else if ( Status[pos] == slotState.TOMBSTONE ) {
                Log.write(pos + ":  tombstone" + "\n");
            }
            else {
                Log.write(pos + ":  empty" + "\n");
            }
        }
    }

    // This method resizes Table and Status Arrays.
    // It is called in Insert method. whenever insert operations is called
    // Insert operation calculates load factor. if it is bigger than 90%
    // it resizes the arary.
    private void resizeArray() {
        int newSize = Size * 2;
        T[] TableTwice = (T[])new Hashable[newSize];
        slotState[] StatusTwice = new slotState[newSize];

        for ( int i = 0; i < Size; i++) {
            StatusTwice[i] = slotState.EMPTY;
            TableTwice[i] = null;
        }

        for ( int j = 0; j < Size; j++) {
            if ( Table[j] != null )
                resizeInsert(Table[j], newSize, TableTwice, StatusTwice);
        }

        Status = StatusTwice;
        Table = TableTwice;
        Size = newSize;
        // Let the garbage collector handle these
        StatusTwice = null;
        TableTwice = null;
    }
/////////////////////////////////////////////////////////////////////////
////////////        Resize Insert     ///////////////////////////////////
/////////////////////////////////////////////////////////////////////////
    private T resizeInsert(T Elem, int newSize,T[] targetTable,
        slotState[] targetStatus) {

        int homeslot = Elem.Hash() % newSize;
        return resizeInsertHelp(homeslot, 0, Elem, targetTable, targetStatus);
    }

    // Recursive method for insert operation
    // It also writes logs if it is requested
    private T resizeInsertHelp(int homeslot, int k, T elem, T[] targetTable,
        slotState[] targetStatus) {
        int newHomeslot = (homeslot + quadraticProbe(k)) % Size;

        if ( targetStatus[newHomeslot] == slotState.FULL ) {
            if ( elem.equals(targetTable[newHomeslot]) == true ) {
                return null;
            }
            else
                return resizeInsertHelp( homeslot, k+1, elem, targetTable,
                    targetStatus);
        }
        else {
            targetTable[newHomeslot] = elem;
            targetStatus[newHomeslot] = slotState.FULL;
            return targetTable[newHomeslot];
        }
    }

/////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////

//    // Display Method. It writes current table values into log file.
//    public void Display(FileWriter Log) {
//
//        try
//        {
//            for ( int i = 0; i < Size; i++) {
//                if ( Table[i] == null)
//                    Log.write(i + ":  empty\n");
//                else
//                    Log.write(i + ":  " + Table[i].toString() + "\n");
//            }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//
//    }

//    // ----------------------------------------------------------
//    /**
//     * This method prints out all item in Table and Status arrays.
//     * It is designed for debug purpose.
//     * this will be commented out on Submission version
//     */
//    public void displayAll() {
//        System.out.println("====[Table]====    =====[Status]====");
//        for (int i = 0; i < Size; i++ ) {
//            if ( Table[i] == null )
//            {
//                System.out.printf("%12s", "Null");
//                System.out.printf("%12s\n", Status[i]);
//            }
//            else {
//                System.out.printf("%12s", Table[i].toString());
//                System.out.printf("%12s\n", Status[i]);
//            }
//        }
//    }
}
