package ta4_1.MoneyFlow_Backend.Statements;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class StatementService {

    private HashMap<String, Double> financialMap;

    // Constructor to set all financials to 0.
    public StatementService() {
        financialMap = new HashMap<>();
        financialMap.put("home", 0.0);
        financialMap.put("work", 0.0);
        financialMap.put("income", 0.0);
        financialMap.put("other", 0.0);
        financialMap.put("personal", 0.0);
    }

    /**
     * Creates a new statement for a given user.
     *
     * @param user The user the statement is created for.
     * @param file The file with which the statement is created.
     * @return The newly created bank statement.
     */
    public Statement createBankStatement(User user, MultipartFile file) throws IOException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = date.format(formatter);


        Statement bankStatement = new Statement();
        byte[] contents = file.getBytes();
        bankStatement.setBankStatement(contents);
        bankStatement.setDate(formattedDate);
        bankStatement.setUser(user);

        user.getStatements().put(formattedDate, bankStatement);
        collectFinancials(contents);
        updateFinancials(user);

        return bankStatement;
    }

    /**
     * Collects user financials from the bank statement data.
     *
     * @param data The data in the bank statement in BLOB format.
     */
    public void collectFinancials(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 2; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            int k = 0;
            if (row != null) {
                Cell typeCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (typeCell != null) {
                    switch (typeCell.getStringCellValue()) {
                        case "home":
                        case "work":
                        case "other":
                        case "personal":
                            k = 4;
                            break;
                        case "income":
                            k = 3;
                            break;
                        default:
                            break;
                    }
                    if (k != 0) {
                        Cell numCell = row.getCell(k, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        String type = typeCell.getStringCellValue();
                        Double val = financialMap.get(type);
                        val += numCell.getNumericCellValue();
                        financialMap.put(type, val);
                    }
                }
            }
        }
        workbook.close();
    }

    /**
     * Updates user financials with financialMap hashmap.
     *
     * @param user The user whose financials are updated.
     */
    public void updateFinancials(User user) {
        Expenses userExpenses = user.getExpenses();
        user.setMonthlyIncome(user.getMonthlyIncome() + financialMap.get("income"));
        userExpenses.setPersonal(userExpenses.getPersonal() + financialMap.get("personal"));
        userExpenses.setWork(userExpenses.getWork() + financialMap.get("work"));
        userExpenses.setHome(userExpenses.getHome() + financialMap.get("home"));
        userExpenses.setOther(userExpenses.getOther() + financialMap.get("other"));
    }
}
