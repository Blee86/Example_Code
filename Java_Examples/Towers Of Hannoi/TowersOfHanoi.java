
public class TowersOfHanoi
{
    private int disks;
    String from, to, aux;

    public TowersOfHanoi(int numOfDisk, String from, String to, String aux) {
        this.disks = numOfDisk;
        this.from = from;
        this.to = to;
        this.aux = aux;
    }

    public void start() {
        solution(disks, from, to, aux);
    }

    private void solution(int n, String from, String to, String aux) {
        if ( n == 1) {
            System.out.println("Move 1 disk from peg " + from + " to peg " + to);
            return;
        }

        solution(n-1, from, aux, to);

        System.out.println("Move " + n + " disk(s) from peg " + from + " to peg " + to);

        solution(n-1, aux, to, from);
    }
}
