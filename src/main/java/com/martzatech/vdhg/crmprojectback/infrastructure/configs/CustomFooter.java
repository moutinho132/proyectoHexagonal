package com.martzatech.vdhg.crmprojectback.infrastructure.configs;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class CustomFooter extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // Agrega el pie de página
        Phrase footer = new Phrase("Pie de página - Página " + writer.getPageNumber());
        com.itextpdf.text.Rectangle page = document.getPageSize();
        com.itextpdf.text.pdf.ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer,
                (page.getLeft() + page.getRight()) / 2, page.getBottom() - 20, 0);
    }
}