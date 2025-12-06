package com.junior.NotaFiscal.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junior.NotaFiscal.DTO.NotaFiscalDTO;
import com.junior.NotaFiscal.entities.NotaFiscal;
import com.junior.NotaFiscal.repository.NotaFiscalRepository;
import com.junior.NotaFiscal.util.DanfePdfUtil;
import com.junior.NotaFiscal.util.NotaFiscalXmlUtil;
import com.junior.NotaFiscal.util.NumberFormatUtil;

@Service
public class NotaFiscalService {

    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalService.class);

    private final NotaFiscalRepository repository;
    private final Path invoicesDir = Paths.get("invoices");

    public NotaFiscalService(NotaFiscalRepository repository) {
        this.repository = repository;
        try {
            if (!Files.exists(invoicesDir)) Files.createDirectories(invoicesDir);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar diretório invoices", e);
        }
    }
	@Transactional(readOnly = true)
	public List<NotaFiscalDTO> findAll() {
		List<NotaFiscal> list = repository.findAll();
		return list.stream()
		           .map(x -> new NotaFiscalDTO(x))
		           .collect(Collectors.toList());
	}
	
    @Transactional
    public NotaFiscalDTO gerarNotaFiscal(Long pedidoId, BigDecimal valorTotal) throws Exception {
        var existente = repository.findByPedidoId(pedidoId);
        if (existente.isPresent()) {
            NotaFiscal nf = existente.get();

           
            if (nf.getPdfPath() == null || nf.getPdfPath().isBlank()) {
                String pdfPath = DanfePdfUtil.gerarPdfDANFe(nf, invoicesDir);
                nf.setPdfPath(pdfPath);
                repository.save(nf);
            }

            return new NotaFiscalDTO(nf);
        }

        try {
            NotaFiscal nf = new NotaFiscal();
            nf.setPedidoId(pedidoId);
            nf.setNumero(NumberFormatUtil.geraNumero(5));
            nf.setValorTotal(NumberFormatUtil.toMoney(valorTotal));
            nf.setChaveAcesso(NumberFormatUtil.geraNumerosString(44));
            nf.setSerie(1);
            nf.setDataEmissao(LocalDateTime.now());

            nf.setXml(NotaFiscalXmlUtil.montaXml(nf));
            nf = repository.save(nf);

            String pdfPath = DanfePdfUtil.gerarPdfDANFe(nf, invoicesDir);
            nf.setPdfPath(pdfPath);
            nf = repository.save(nf);

            logger.info("Nota fiscal gerada: id={}, pedidoId={}, pdf={}", nf.getId(), pedidoId, pdfPath);
            return new NotaFiscalDTO(nf);

        } catch (DataIntegrityViolationException e) {
         
            NotaFiscal nf = repository.findByPedidoId(pedidoId).orElseThrow(() -> e);

            if (nf.getPdfPath() == null || nf.getPdfPath().isBlank()) {
                String pdfPath = DanfePdfUtil.gerarPdfDANFe(nf, invoicesDir);
                nf.setPdfPath(pdfPath);
                nf = repository.save(nf);
            }
            return new NotaFiscalDTO(nf);
        }
    }

    @Transactional(readOnly = true)
    public byte[] obterPdf(Long id) throws Exception {
        NotaFiscal nf = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        if (nf.getPdfPath() == null || nf.getPdfPath().isBlank()) {
            throw new RuntimeException("PDF não disponível para a nota " + id);
        }
        Path path = Path.of(nf.getPdfPath());
        if (!Files.exists(path)) {
            throw new RuntimeException("Arquivo PDF não encontrado em disco");
        }
        return Files.readAllBytes(path);
    }
}