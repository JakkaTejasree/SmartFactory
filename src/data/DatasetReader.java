
package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatasetReader {

    public static final String FILE_PATH = "dataset/ai4i2020.csv";

    public static class MachineRecord {
        public int udi;
        public String productId;
        public String type;
        public double airTemperature;
        public double processTemperature;
        public int rotationalSpeed;
        public double torque;
        public int toolWear;
        public int machineFailure;
        public int twf, hdf, pwf, osf, rnf;

        public MachineRecord(String[] row) {
            udi = Integer.parseInt(row[0].trim());
            productId = row[1].trim();
            type = row[2].trim();
            airTemperature = Double.parseDouble(row[3].trim());
            processTemperature = Double.parseDouble(row[4].trim());
            rotationalSpeed = Integer.parseInt(row[5].trim());
            torque = Double.parseDouble(row[6].trim());
            toolWear = Integer.parseInt(row[7].trim());
            machineFailure = Integer.parseInt(row[8].trim());
            twf = Integer.parseInt(row[9].trim());
            hdf = Integer.parseInt(row[10].trim());
            pwf = Integer.parseInt(row[11].trim());
            osf = Integer.parseInt(row[12].trim());
            rnf = Integer.parseInt(row[13].trim());
        }
    }

    private static String detectDelimiter(String header) {
        String[] delimiters = {",", ";", "\t", "|"};

        String selected = ",";
        int maxColumns = 0;

        for (String delimiter : delimiters) {
            int columns = header.split(
                java.util.regex.Pattern.quote(delimiter), -1
            ).length;

            if (columns > maxColumns) {
                maxColumns = columns;
                selected = delimiter;
            }
        }

        return selected;
    }

    public static List<MachineRecord> loadData() throws IOException {
        List<MachineRecord> records = new ArrayList<>();

        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE_PATH))) {

            String header = br.readLine();

            if (header == null) {
                throw new IOException("CSV file is empty.");
            }

            // Remove UTF-8 BOM if present
            header = header.replace("\uFEFF", "").trim();

            String delimiter = detectDelimiter(header);

            String[] headerColumns = header.split(
                java.util.regex.Pattern.quote(delimiter), -1
            );

            if (headerColumns.length != 14) {
                throw new IOException(
                    "CSV header contains " + headerColumns.length +
                    " columns, but 14 are required. " +
                    "Please check the actual dataset file."
                );
            }

            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] row = line.split(
                    java.util.regex.Pattern.quote(delimiter), -1
                );

                if (row.length != 14) {
                    throw new IOException(
                        "Invalid CSV line " + lineNumber +
                        ". Expected 14 columns but found " +
                        row.length + ". Check the dataset format."
                    );
                }

                try {
                    records.add(new MachineRecord(row));
                } catch (NumberFormatException e) {
                    throw new IOException(
                        "Invalid numeric value at line " +
                        lineNumber + ": " + line, e
                    );
                }
            }
        }

        if (records.isEmpty()) {
            throw new IOException("No data records found in CSV.");
        }

        System.out.println("Dataset loaded successfully!");
        System.out.println("Total records: " + records.size());

        return records;
    }
}
