public class Test {

    public static void main(String[] args) {
        int size = 100;
        MatrixL m = new MatrixL(size, size), n = new MatrixL(size, size);
        m.randomFill();
        n.randomFill();
        System.out.println(m.toString());
        System.out.println(n.toString());
        System.out.println();

        System.out.println("Normal: \n" + m.mult_n(n).toString());
        System.out.println("V2 Rekursiv: \n" + m.mult(n).toString());
    }

}
