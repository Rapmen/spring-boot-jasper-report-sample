package com.example.jasper.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {

    InputStream generaterPDF(String jasperTemplatePath, Map<String, Object> parameters, String fileName)
        throws JRException, IOException;
}
