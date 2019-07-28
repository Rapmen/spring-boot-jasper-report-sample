package com.example.jasper.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.example.jasper.service.ReportService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private DataSource dataSource;

    private JasperPrint fillReport(String jasperTemplatePath, Map<String, Object> parameters)
        throws JRException, IOException {
        log.info("fillReport() start");
        JasperReport jasperReport = null;
        Resource resource = new ClassPathResource(jasperTemplatePath);
        if (resource.exists()) {
            try (InputStream jasperStream = resource.getInputStream()) {
                JasperDesign jasperDesign = JRXmlLoader.load(jasperStream);
                jasperReport = JasperCompileManager.compileReport(jasperDesign);
                parameters.put("REPORT_LOCALE", Locale.ENGLISH);
                Connection conn = DataSourceUtils.getConnection(this.dataSource);
                JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
                log.info("fillReport() end");
                return print;
            }
        } else {
            log.error("Report template is not exist!");
        }
        log.info("fillReport() end");
        return null;
    }

    @Override
    public InputStream generaterPDF(String jasperTemplatePath, Map<String, Object> parameters, String fileName)
        throws JRException, IOException {
        log.info("generaterPDF() start");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JasperPrint print = fillReport(jasperTemplatePath, parameters);

            JasperExportManager.exportReportToPdfStream(print, out);

            try (InputStream in = new ByteArrayInputStream(out.toByteArray())) {
                log.info("generaterPDF() end");
                return in;
            }

        }
    }
}
