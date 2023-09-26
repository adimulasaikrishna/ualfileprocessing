package com.ual.fileprocessing.controller;

import au.com.bytecode.opencsv.CSVReader;
import com.ual.fileprocessing.service.UALService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/process")
public class UalFileProcessingController {

    Logger LOGGER = LoggerFactory.getLogger(UalFileProcessingController.class);

    @Autowired
    private UALService ualService;

    /**
     * This Method used to upload and process spring shot excel
     *
     * @return
     */
    @PostMapping("/springshotexcel")
    public ResponseEntity<String> processSpringShotExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {

        LOGGER.info("Started processing Spring Shot Excel file Upload");
        List<String> results = new ArrayList<>();
        File convertedFile = new File(file.getOriginalFilename());
        convertedFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(convertedFile))) {
            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {
                results.add(currentLine);
            }
            ualService.processSpringShotExcel(results);
        } catch (IOException e) {
            LOGGER.error("Failed to process Spring shot Excel file with an Exception " + e.getMessage());
            String errorMessage = e.getLocalizedMessage();
        }
        return ResponseEntity.ok("Processed Successfully");
    }

    /**
     * This Method used to upload and process AV Tech excel
     *
     * @return
     */
    @PostMapping("/avtechexcel")
    public ResponseEntity<String> processAvTechShotExcel(@RequestParam(value = "file", required = true) MultipartFile file) {

        boolean isProcessingFailed= false;
        LOGGER.info("Started processing AV Tech  Excel file Upload");
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())){
            //XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            //XSSFSheet sheet = workbook.getSheetAt(0);

                int  numberOfSheet = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheet; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("=> " + sheet.getSheetName());
                DataFormatter dataFormatter = new DataFormatter();
                System.out.println("Iterating over Rows and Columns using Iterator");
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);
                        System.out.print(cellValue + "\t");
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error("Failed to process AV Tech Excel file with an Exception " + e.getMessage());
            String errorMessage = e.getLocalizedMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok("Processed Successfully");
    }

    /**
     * This Method used to upload and process UDH excel
     *
     * @return
     */
    @PostMapping("/udhexcel")
    public ResponseEntity<String> processUDHExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {

        List<String> results = new ArrayList<>();
        File convertedFile = new File(file.getOriginalFilename());
        convertedFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        try (CSVReader reader = new CSVReader(new FileReader(convertedFile))) {
            String[] currentLine;

            while ((currentLine = reader.readNext()) != null) {
                results.add(Arrays.toString(currentLine));
            }
            ualService.processSpringShotExcel(results);
        } catch (IOException e) {
            LOGGER.error("Failed to process Spring shot Excel file with an Exception " + e.getMessage());
            String errorMessage = e.getLocalizedMessage();
        }

        return ResponseEntity.ok("Processed Successfully");
    }

    /**
     * This Method used to upload and process UDH excel
     *
     * @return
     */
    @PostMapping("/tchexcel")
    public ResponseEntity<String> processTCHExcel(@RequestParam(value = "file") MultipartFile file) {

        LOGGER.info("Started processing TCH Excel file Upload");
        try {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            LOGGER.info(convFile.toString());
            fos.close();

            File file1 = new File(convFile.toString());
            String data = FileUtils.readFileToString(file1, "UTF-8");
            LOGGER.info(data);

        } catch (IOException e) {
            LOGGER.error("Failed to process TCH Excel file with an Exception " + e.getMessage());
            e.printStackTrace();
            String errorMessage = e.getLocalizedMessage();
        }
        return ResponseEntity.ok("Processed Successfully");
    }
}

