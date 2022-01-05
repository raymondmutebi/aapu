package org.pahappa.systems.core.utils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.pahappa.systems.models.Member;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class ExcelUploadHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public List<Member> uploadMembersCSVFile(FileUploadEvent event) throws Exception {
        try {
            UploadedFile uploadedFile = event.getFile();

            if (!uploadedFile.getFileName().endsWith(".csv")) {
                UiUtils.showMessageBox("Invalid file type", "Action Failed");
                return null;
            }
            CSVReader reader = new CSVReader(new InputStreamReader(uploadedFile.getInputstream()));
            List<UploadedMember> uploadedMembers = new CsvToBeanBuilder(reader)
                    .withType(UploadedMember.class).build().parse();
            List<Member> clientList = new ArrayList<>();

            for (UploadedMember uploadedMember : uploadedMembers) {
                try {
                    clientList.add(uploadedMember.obtainMemberModel());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;

                }
            }

            System.out.println("--------------Got clients---------" + clientList);
            return clientList;
        } catch (Exception e) {
            UiUtils.showMessageBox("File Upload failed", "Action Failed");
            return null;
        }
    }

    public static void main(String args[]) throws IOException {

        try {

            String strFile = "C:/Users/HP/Desktop/crm_test_client_data.csv";
            CSVReader reader = new CSVReader(new FileReader(strFile));
            List<UploadedMember> beans = new CsvToBeanBuilder(reader)
                    .withType(UploadedMember.class).build().parse();
            System.out.println("--------------Got client---------" + beans);

        } catch (FileNotFoundException | IllegalStateException e) {

        }
    }
    //obtaining input bytes from a file
//        FileInputStream fis = new FileInputStream(new File("C:\\Users\\HP\\Desktop\\crm_test_client_data.xlsx"));
//        System.out.println("Uploaded fiel");
//        ExcelUploadHelper clientUploads = new ExcelUploadHelper();
//        try {
//            clientUploads.uploadExcelFile(fis);
//        } catch (Exception ex) {
//            Logger.getLogger(ExcelUploadHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.err.println("----------download method--------");
//        InputStream stream = ExcelUploadHelper.class.getResourceAsStream("/custom-files/crm_test_client_data.xlsx");
//        System.err.println("----------Gotten resource--------\n" + stream.toString());
//        StreamedContent streamedContent = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "clientsTemplate.xlsx");
//        System.err.println("----------StreamedContent length--------\n" + streamedContent.getContentLength());

    //obtaining input bytes from a file  
//        FileInputStream fis = new FileInputStream(new File("C:\\Users\\HP\\Desktop\\crm_test_client_data.xlsx"));
//        System.out.println("Uploaded fiel");
//        ExcelUploadHelper clientUploads = new ExcelUploadHelper();
//        try {
//            clientUploads.uploadExcelFile(fis);
//        } catch (Exception ex) {
//            Logger.getLogger(ExcelUploadHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.err.println("----------download method--------");
//        InputStream stream = ExcelUploadHelper.class.getResourceAsStream("/custom-files/crm_test_client_data.xlsx");
//        System.err.println("----------Gotten resource--------\n" + stream.toString());
//        StreamedContent streamedContent = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "clientsTemplate.xlsx");
//        System.err.println("----------StreamedContent length--------\n" + streamedContent.getContentLength());
//
//    }
//
//    public void test() {
//
//    }
//
//    public List<BusinessTransaction> uploadExcelFile(FileInputStream fileInputStream) throws Exception {
//
//        uploadedDatasets = new ArrayList<BusinessTransaction>();
//        //creating workbook instance that refers to .xls file  
//        XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
//        //creating a Sheet object to retrieve the object  
//        XSSFSheet sheet = wb.getSheetAt(0);
//        //evaluating cell type   
//        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
//        for (Row row : sheet) //iteration over row using for each loop  
//        {
//            if (row.getRowNum() != 0) {
//                System.out.println("============Started new Row=====================");
//                readRow(row);
//            }
//        }
//        System.out.println();
//        System.out.println("Successsfully uploaded " + uploadedDatasets.size() + " clients");
//        System.out.println(Arrays.toString(uploadedDatasets.toArray()));
//        return uploadedDatasets;
//    }
//    private void extractXSSF(UploadedFile uploadedFile) throws Exception {
//        this.file = uploadedFile.getInputstream();
//        @SuppressWarnings("resource")
//        XSSFWorkbook workbook = new XSSFWorkbook(file);
//        XSSFSheet sheet = workbook.getSheetAt(0);
////		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//
//        for (Row row : sheet) {
//            System.out.println("\n***Start row***\n");
//            if (row.getRowNum() != 0) {
//                System.out.println("============Started new Row=====================");
//                readRow(row);
//            }
//        }
//        uploadSuccessful();
//    }
//
//    private void extractHSSF(UploadedFile uploadedFile) throws Exception {
//        this.file = uploadedFile.getInputstream();
//        @SuppressWarnings("resource")
//        HSSFWorkbook workbook = new HSSFWorkbook(file);
//        HSSFSheet sheet = workbook.getSheetAt(0);
//
//        for (Row row : sheet) {
//            System.out.println("\n***Start row***\n");
//
//            if (row.getRowNum() != 0) {
//                System.out.println("============Started new Row=====================");
//                readRow(row);
//            }
//        }
//
//        uploadSuccessful();
//    }
//
//    public void readRow(Row row) throws Exception {
////		Iterator<Cell> cellIterator = row.cellIterator();
//        BusinessTransaction uplaodModel = new BusinessTransaction();
//
//        for (int cn = 0; cn < 6; cn++) {
//            // If the cell is missing from the file, generate a blank one
//            // (Works by specifying a MissingCellPolicy)
//            Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
//            // Print the cell for debugging
//            String cellValue = "";
//            switch (cell.getColumnIndex()) {
//                case 0:
//                    if (getTextualDataFromCell(cell).toString().isEmpty()) {
//
//                        cellValue = getTextualDataFromCell(cell).toString();
//                        System.out.println("Cell value for 0 empty aftr " + cellValue);
//                        break;
//                    }
//                    cellValue = getTextualDataFromCell(cell).toString();
//                    System.out.println("Cell value for 0 " + cellValue);
//                    uplaodModel.setCompanyName(cellValue);
//                    break;
//                case 1:
//		try {
//                    if (getTextualDataFromCell(cell).toString().isEmpty()) {
//                        System.out.println("Cell value for 1 empty b4 " + cellValue);
//                        cellValue = getTextualDataFromCell(cell).toString();
//                        System.out.println("Cell value for 1 empty aftr " + cellValue);
//                        break;
//                    }
//                    System.out.println("Cell value for 1 b4" + cellValue);
//                    cellValue = getTextualDataFromCell(cell).toString();
//                    System.out.println("Cell value for 1 aftr " + cellValue);
//                    uplaodModel.setCompanyPhoneNumber(cellValue);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                case 2:
//		try {
//                    if (getTextualDataFromCell(cell).toString().isEmpty()) {
//                        System.out.println("Cell value for 2 empty b4 " + cellValue);
//                        cellValue = getTextualDataFromCell(cell).toString();
//                        System.out.println("Cell value for 2 empty aftr " + cellValue);
//                        break;
//                    }
//                    System.out.println("Cell value for 2 b4" + cellValue);
//                    cellValue = getTextualDataFromCell(cell).toString();
//                    System.out.println("Cell value for 2 aftr " + cellValue);
//                    uplaodModel.setCompanyEmailAddress(cellValue);
//                    break;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                case 3:
//                    try {
//                    if (getTextualDataFromCell(cell).toString().isEmpty()) {
//                        System.out.println("Cell value for 3 empty b4 " + cellValue);
//                        cellValue = getTextualDataFromCell(cell).toString();
//                        System.out.println("Cell value for 3 empty aftr " + cellValue);
//                        break;
//                    }
//                    System.out.println("Cell value for 3 b4" + cellValue);
//                    cellValue = getTextualDataFromCell(cell).toString();
//                    System.out.println("Cell value for 3 aftr " + cellValue);
//                    uplaodModel.setCompanyLocation(cellValue);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                case 4:
//                    try {
//
//                    System.out.println("Cell value for 4 b4" + cellValue);
//                    cellValue = new BigDecimal(getTextualDataFromCell(cell).toString()).toPlainString();
//                    System.out.println("Cell value for 4 aftr " + cellValue);
//                    uplaodModel.setCompanyRegistrationNumber(cellValue);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                case 5:
//                    try {
//
//                    System.out.println("Cell value for 5 b4" + cellValue);
//                    cellValue = getTextualDataFromCell(cell).toString();
//                    System.out.println("Cell value for 5 aftr " + cellValue);
//                    uplaodModel.setOtherDetails(cellValue);
//                    break;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//                default:
//                    break;
//            }
//            System.out.println(uplaodModel.toString());
//        }
//        this.uploadedDatasets.add(uplaodModel);
//    }
//
//    private Object getTextualDataFromCell(Cell cell) throws Exception {
//        if (cell != null) {
//            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
//                return "";
//            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                return cell.getStringCellValue();
//            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                return cell.getNumericCellValue();
//            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                switch (cell.getCachedFormulaResultType()) {
//                    case Cell.CELL_TYPE_NUMERIC:
//                        return cell.getNumericCellValue();
//                    case Cell.CELL_TYPE_STRING:
//                        return cell.getStringCellValue();
//                }
//            }
//
//        }
//        return null;
//    }
//
//    /**
//     * @return the row
//     */
//    public Row getRow() {
//        return row;
//    }
//
//    /**
//     * @param row the row to set
//     */
//    public void setRow(Row row) {
//        this.row = row;
//    }
//
//    /**
//     * @return the file
//     */
//    public InputStream getFile() {
//        return file;
//    }
//
//    /**
//     * @param file the file to set
//     */
//    public void setFile(InputStream file) {
//        this.file = file;
//    }
//
//    private void uploadSuccessful() {
//        UiUtils.showMessageBox("Only valid phoneNumbers were saved", "Upload Successful");
//    }
//    public List<BusinessTransaction> uploadExcelFile(FileUploadEvent event) throws Exception {
//        try {
//            UploadedFile uploadedFile = event.getFile();
//            uploadedDatasets = new ArrayList<BusinessTransaction>();
//
//            if (uploadedFile.getFileName().endsWith(".xls")) {
//                uploadedDatasets.clear();
//                extractHSSF(uploadedFile);
//            } else if (uploadedFile.getFileName().endsWith(".xlsx")) {
//                uploadedDatasets.clear();
//                extractXSSF(uploadedFile);
//            } else {
//                UiUtils.showMessageBox("Unknown File Format<br>Upload .xls and .xlsx formats only.", "Success");
//            }
//        } catch (IOException e) {
//            UiUtils.showMessageBox("File Upload failed", "Action Failed");
//            return null;
//        } finally {
//            this.file.close();
//        }
//        System.out.println("Successsfully uploaded " + uploadedDatasets.size() + " clients");
//        System.out.println(Arrays.toString(uploadedDatasets.toArray()));
//        return uploadedDatasets;
//    }
}
