package main.datatypes;

import java.awt.*;
import java.awt.print.*;
import javax.print.*;

public class Print {

    public static final double POINTS_PER_INCH = 72.0;

    public Print(String printText) {
        try {
            // Create a new print job
            PrinterJob job = PrinterJob.getPrinterJob();

            // Get the default printer
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();

            for (PrintService printService : PrinterJob.lookupPrintServices()) {
                if (printService.getName().contains("Epson")) {
                    service = printService;
                }
            }
            // Set the print service for the job
            job.setPrintService(service);

            // Create a new page format
            PageFormat format = new PageFormat();
            Paper paper = new Paper();
            double paperWidth = 3.15; // inches
            double paperHeight = 11.69; // inches
            double leftMargin = 0.12; // inches
            double rightMargin = 0.12; // inches
            double topMargin = 0; // inches
            double bottomMargin = 0; // inches
            paper.setSize(paperWidth * POINTS_PER_INCH, paperHeight * POINTS_PER_INCH);
            paper.setImageableArea(leftMargin * POINTS_PER_INCH, topMargin * POINTS_PER_INCH,
                    (paperWidth - leftMargin - rightMargin) * POINTS_PER_INCH,
                    (paperHeight - topMargin - bottomMargin) * POINTS_PER_INCH);
            format.setPaper(paper);

            // Set the printable object for the job
            PrintText printable = new PrintText(printText);
            job.setPrintable(printable, format);

            // Print the job
            job.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class PrintText implements Printable {
    private String printText;

    public PrintText(String printText) {
        this.printText = printText;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Split the receipt text into lines
        String[] lines = printText.split("\n");

        // Set the font and line spacing
        Font font = new Font("Monospaced", Font.PLAIN, 10);
        g2d.setFont(font);
        int lineHeight = g2d.getFontMetrics().getHeight() + 2;

        // Print each line of the receipt
        int y = 10;
        for (String line : lines) {
            g2d.drawString(line, 10, y);
            y += lineHeight;
        }

        // Return that this page is part of the printed document
        return Printable.PAGE_EXISTS;
    }
}
