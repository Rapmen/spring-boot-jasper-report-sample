package com.example.jasper.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jasper.service.ReportService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@CrossOrigin
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/downloadReport")
    public ResponseEntity<ByteArrayResource> downloadReport(HttpServletRequest request, HttpServletResponse response)
        throws IOException, JRException {
        log.info("downloadReport() start");
        ResponseEntity<ByteArrayResource> re = null;

        String fileName = "Course.pdf";
        Map<String, Object> params = new HashMap<>();
        InputStream in = reportService.generaterPDF("/report/course.jrxml", params, fileName);
        ByteArrayResource report = new ByteArrayResource(IOUtils.toByteArray(in));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));

        re = ResponseEntity.ok().headers(headers).contentLength(report.contentLength()).body(report);

        log.info("downloadReport() end");
        return re;
    }

}
