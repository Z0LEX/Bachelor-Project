package main.datatypes;

import javax.print.PrintService;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Print implements Printable {

    @Override
    public int print(Graphics g, PageFormat pf, int page) {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now we perform our rendering */
        g.drawString("Hello world!", 100, 100);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public void performPrint() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        for (PrintService printService : PrinterJob.lookupPrintServices()) {
            System.out.println("Print service available: " + printService.getName());
        }

        System.out.println("Selected service: " + job.getPrintService().getName());

        try {
            job.print();
        } catch (PrinterException ex) {
            throw new RuntimeException(ex);
        }
    }
}
