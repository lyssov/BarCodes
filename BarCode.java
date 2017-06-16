import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import com.roncemer.barcode.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;


/**
 * User: Ilya
 * Date: 20.01.2017
 * Time: 11:25
 * Bar Reader scan PDF, JPG, PNG files, recognise and decode Barcode in 3of9 or UPC-A formats
 */

class Filename {
    private String fullPath;
    private char pathSeparator,
            extensionSeparator;

    public Filename(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    // get file name
    public String filename() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }
}



class BarReader {
    public static Logger log;

    ArrayList<String> foundBarCodes = new ArrayList<String>();

    public String[] getBarCodes() {
        String[] barcodes = new String[foundBarCodes.size()];
        int i=0;
        for (String bar : foundBarCodes) {
            barcodes [i] = bar;
            i++;
        }
        return barcodes;
    }

    public void  DoBarRead (String imageFile) {
        try{
            BufferedImage[] image;

            Filename name = new Filename(imageFile, '/', '.');

            if (name.extension().toLowerCase().equals("pdf")) {
                PDDocument document = PDDocument.load(new File(imageFile));
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                image =  new BufferedImage[document.getNumberOfPages()];

                for (int page = 0; page < document.getNumberOfPages(); ++page) {
                    image[page] = pdfRenderer.renderImageWithDPI(0, 300, ImageType.BINARY);
                }
                System.out.println("Number of pages: " +document.getNumberOfPages());
                document.close();

            } else {
                image =  new BufferedImage[1];

                image[0] = ImageIO.read(new File(imageFile));

            }


/**
 * Scan an image for bar codes and return an array of all decoded
 * bar codes.
 * @param pixels An array containing the monochrome source pixels.  Each
 * element in this array should be in the range of 0-255.
 * @param w The width of the image, in pixels.
 * @param h The height of the image, in pixels.
 * @param includeCheckDigits <code>true</code> to return check digits;
 * <code>false</code> to strip them off.
 * @param listener The <code>BarCodeDecoderListener</code> to be notified
 * each time a bar code is decoded, or <code>null</code> if none.
 * @return An array of <code>String</code> objects containing the scanned
 * bar codes.
 */
            for (BufferedImage img : image) {
                ImageBarCodeScanner scanner = new ImageBarCodeScanner();
                int w = img.getWidth(null);
                int h = img.getHeight(null);
                int npix = w * h;
                int[] pixels = new int[npix];
                PixelGrabber grabber = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
                try { grabber.grabPixels(); } catch(InterruptedException e) { e.printStackTrace(); }
/*                for (int i = 0; i < npix; i++) {
                    pixels[i] = ImageUtils.rgbToGrayscale(pixels[i]);
                }*/
                String [] barCodes = scanner.decodeBarCodesFromImage(pixels, w, h, false, null);
                for (String bar : barCodes) {
                    if (bar.matches("[0-9,-]+")) {
                        foundBarCodes.add(bar);
                        System.out.println(" Found Barcode   [" + bar + "]");
                    }

                }

            }

            if  (foundBarCodes.size() == 0) System.out.println("Barcode not found");

        }    catch (Exception e) {
            log.info(e.getMessage(),e);
            System.out.println("Barcode not found");

        }


    }

}


public class BarTest {


    public static void main(String[] args) {

        BarReader bar = new BarReader();
        bar.DoBarRead("/home/ilysov/Downloads/bar.pdf");

        String [] barcodes = bar.getBarCodes();

        for (int i = 0; i < barcodes.length; i++) {
            System.out.println("    [" + barcodes[i] + "]");
        }

    }

}
