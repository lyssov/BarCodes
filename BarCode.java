import BarReader;

/**
 * Created with IntelliJ IDEA.
 * User: Ilya
 * Date: 20.01.2017
 * Time: 11:25
 *Test of Bar Reader
 */
public class BarCode {

        public static void main(String[] args) throws SQLException {

        BarReader bar = new BarReader();
        bar.DoBarRead("bar.pdf", tools);

        String [] barcodes = bar.getBarCodes();

            for (int i = 0; i < barcodes.length; i++) {
                System.out.println("    [" + barcodes[i] + "]");
            }

        }

}
