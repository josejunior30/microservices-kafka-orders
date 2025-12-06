package com.junior.NotaFiscal.util;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.junior.NotaFiscal.entities.NotaFiscal;

public final class NotaFiscalXmlUtil {

    private NotaFiscalXmlUtil() {}

    public static String montaXml(NotaFiscal nf) {
        String data = nf.getDataEmissao()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
      
        BigDecimal v = NumberFormatUtil.toMoney(nf.getValorTotal());
        String vStr = v.toPlainString();

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">\n" +
                "  <infNFe Id=\"NFe" + nf.getChaveAcesso() + "\" versao=\"4.00\">\n" +
                "    <ide>\n" +
                "      <cUF>35</cUF>\n" +
                "      <cNF>" + NumberFormatUtil.geraNumero(8) + "</cNF>\n" +
                "      <natOp>VENDA</natOp>\n" +
                "      <mod>55</mod>\n" +
                "      <serie>" + nf.getSerie() + "</serie>\n" +
                "      <nNF>" + nf.getNumero() + "</nNF>\n" +
                "      <dhEmi>" + data + "</dhEmi>\n" +
                "    </ide>\n" +
                "    <emit>\n" +
                "      <CNPJ>11111111111111</CNPJ>\n" +
                "      <xNome>Empresa Exemplo LTDA</xNome>\n" +
                "    </emit>\n" +
                "    <det nItem=\"1\">\n" +
                "      <prod>\n" +
                "        <cProd>123</cProd>\n" +
                "        <xProd>Produto Exemplo</xProd>\n" +
                "        <qCom>1.0000</qCom>\n" +
                "        <vUnCom>" + vStr + "</vUnCom>\n" +
                "        <vProd>" + vStr + "</vProd>\n" +
                "      </prod>\n" +
                "    </det>\n" +
                "    <total>\n" +
                "      <ICMSTot>\n" +
                "        <vProd>" + vStr + "</vProd>\n" +
                "        <vNF>" + vStr + "</vNF>\n" +
                "      </ICMSTot>\n" +
                "    </total>\n" +
                "  </infNFe>\n" +
                "</NFe>";
    }
}
