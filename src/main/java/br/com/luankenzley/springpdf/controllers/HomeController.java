package br.com.luankenzley.springpdf.controllers;

import br.com.luankenzley.springpdf.models.Curso;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] gerarPdfSimples() {

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf("<h1>Olá Mundo</h1>", pdfStream);
        return pdfStream.toByteArray();
    }

    @GetMapping(value = "/relatorio", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] gerarPdfArquivoHTML() throws IOException {
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        Resource htmlStream = resourceLoader.getResource("classpath:relatorio.html");
        HtmlConverter.convertToPdf(htmlStream.getInputStream(), pdfStream);

        return pdfStream.toByteArray();
    }

    @GetMapping(value = "/cursos", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] gerarPdfTemplateThymeleaf() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso("Spring", "Luan Kenzley", 10));
        cursos.add(new Curso("Spring - JPA", "Luan Kenzley", 10));
        cursos.add(new Curso("Spring - Security", "Luan Kenzley", 10));
        cursos.add(new Curso("API - Streams Java", "Luan Kenzley", 10));
        cursos.add(new Curso("Collections - Java", "Luan Kenzley", 10));

        Context context = new Context();
        context.setVariable("cursos", cursos);

        String html = templateEngine.process("cursos.html", context);

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, pdfStream);

        return pdfStream.toByteArray();
    }

    @GetMapping(value = "/cursos/download", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<?> baixarPdfTemplateThymeleaf() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso("Spring", "Luan Kenzley", 10));
        cursos.add(new Curso("Spring - JPA", "Luan Kenzley", 10));
        cursos.add(new Curso("Spring - Security", "Luan Kenzley", 10));
        cursos.add(new Curso("API - Streams Java", "Luan Kenzley", 10));
        cursos.add(new Curso("Collections - Java", "Luan Kenzley", 10));

        Context context = new Context();
        context.setVariable("cursos", cursos);

        String html = templateEngine.process("cursos.html", context);

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, pdfStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cursos.pdf")
                .body(pdfStream.toByteArray());
    }
}
