package com.myapp.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.myapp.domain.IzlaznaFaktura;
import com.myapp.domain.StavkaFakture;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class PdfGenerator {

    private IzlaznaFaktura faktura;

    public PdfGenerator(IzlaznaFaktura faktura) {
        this.faktura = faktura;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Broj fakture", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Datum izdavanja", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ukupno PDV", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ukupan Iznos", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Stavke", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
            table.addCell(faktura.getBrojFakture());
            table.addCell(faktura.getDatumIzdavanja().toString());
            table.addCell(faktura.getUkupanPdv().toString());
            table.addCell(faktura.getUkupanIznos().toString());
            String formattedText = "";
            for(StavkaFakture stavka : faktura.getStavkaFaktures()){
                formattedText.concat("Usluga: " + stavka.getRobaIliUsluga().getNaziv() + "Kolicina: " + stavka.getKolicina() + "Jedinicna cena: " + stavka.getJedinicnaCena());
            }
            table.addCell(formattedText);

    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }

}
