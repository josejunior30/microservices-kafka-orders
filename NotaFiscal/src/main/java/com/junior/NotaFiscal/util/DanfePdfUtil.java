package com.junior.NotaFiscal.util;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.junior.NotaFiscal.entities.NotaFiscal;

public final class DanfePdfUtil {

    private static final Logger logger = LoggerFactory.getLogger(DanfePdfUtil.class);

    private DanfePdfUtil() {}

    public static String gerarPdfDANFe(NotaFiscal nf, Path invoicesDir) throws Exception {
        if (!Files.exists(invoicesDir))
            Files.createDirectories(invoicesDir);

        String fileName = "nota-" + nf.getId() + ".pdf";
        Path out = invoicesDir.resolve(fileName);

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(40, 770);
                cs.showText("DANFE - Documento Auxiliar da Nota Fiscal (FAKE)");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(40, 745);
                cs.showText("Emitente: Empresa Exemplo LTDA - CNPJ: 11.111.111/1111-11");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(40, 725);
                cs.showText("Número: " + nf.getNumero() + "    Série: " + nf.getSerie() + "    Emissão: "
                        + nf.getDataEmissao().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                cs.endText();

                // ✅ usa o util central
                String valor = NumberFormatUtil.formatBR(nf.getValorTotal());

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(40, 705);
                cs.showText("Pedido ID: " + nf.getPedidoId() + "    Valor Total: R$ " + valor);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(40, 680);
                cs.showText("Itens:");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(45, 660);
                cs.showText("1 - Produto Exemplo | Qtd: 1 | V. Unit: R$ " + valor + " | V. Total: R$ " + valor);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                cs.newLineAtOffset(40, 630);
                cs.showText("Valor Total (R$): " + valor);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 8);
                cs.newLineAtOffset(40, 610);
                cs.showText("Chave de Acesso: " + nf.getChaveAcesso());
                cs.endText();

                BufferedImage qr = gerarQRCodeImage(nf.getChaveAcesso(), 120, 120);
                var pdImage = LosslessFactory.createFromImage(doc, qr);
                cs.drawImage(pdImage, 420, 600, 120, 120);
            }

            try (OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                doc.save(os);
            }
        }

        logger.info("PDF gerado em: {}", out.toAbsolutePath());
        return out.toAbsolutePath().toString();
    }

    private static BufferedImage gerarQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}