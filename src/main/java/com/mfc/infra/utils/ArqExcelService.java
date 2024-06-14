package com.mfc.infra.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ArqExcelService {

    Logger logger = LoggerFactory.getLogger(ArqExcelService.class);

    public final void writeToExcel(final String filePath) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.getSheet("MACA");

            // Etiquetas de las columnas
            Row headerRow = sheet.createRow(0);
            Cell sp1v3Cell = headerRow.createCell(0);
            sp1v3Cell.setCellValue("SP1V3 - Horas realizadas");

            Cell v2sp4Cell = headerRow.createCell(1);
            v2sp4Cell.setCellValue("V2SP4 - Horas realizadas");

            // Aquí puedes agregar código para escribir tus datos en la hoja de Excel.
            // Por ejemplo:
            Row dataRow = sheet.createRow(1);

            // Agregar datos a la columna 'SP1V3 - Horas realizadas'
            Cell sp1v3DataCell = dataRow.createCell(0);
            sp1v3DataCell.setCellValue(10); // Puedes reemplazar 10 con el valor real que desees.

            // Agregar datos a la columna 'V2SP4 - Horas realizadas'
            Cell v2sp4DataCell = dataRow.createCell(1);
            v2sp4DataCell.setCellValue(20); // Puedes reemplazar 20 con el valor real que desees.

            // Guarda el libro de trabajo
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

}
