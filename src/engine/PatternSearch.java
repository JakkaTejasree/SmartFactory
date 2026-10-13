
package engine;

import java.util.List;
import data.DatasetReader.MachineRecord;

public class PatternSearch {

    public static int kmpSearch(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return 0;
        }

        if (text == null) {
            return -1;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] lps = new int[pattern.length()];
        int j = 0;

        for (int i = 1; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                lps[i++] = ++j;
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                lps[i++] = 0;
            }
        }

        int i = 0;
        j = 0;

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return i - j;
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }

        return -1;
    }

    public static void searchProducts(
            List<MachineRecord> records, String pattern) {

        int count = 0;

        for (MachineRecord r : records) {
            if (kmpSearch(r.productId, pattern) >= 0 ||
                kmpSearch(r.type, pattern) >= 0) {

                System.out.println(
                    "UDI: " + r.udi +
                    ", Product ID: " + r.productId +
                    ", Type: " + r.type);
                count++;
            }
        }

        System.out.println("Matching records: " + count);
    }
}
