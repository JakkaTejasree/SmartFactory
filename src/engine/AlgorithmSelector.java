
package engine;

import java.util.List;
import data.DatasetReader.MachineRecord;

public class AlgorithmSelector {

    public static MachineRecord linearSearch(
            List<MachineRecord> records, int targetUDI) {

        for (MachineRecord r : records) {
            if (r.udi == targetUDI) {
                return r;
            }
        }
        return null;
    }

    public static MachineRecord binarySearch(
            List<MachineRecord> records, int targetUDI) {

        int left = 0;
        int right = records.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int value = records.get(mid).udi;

            if (value == targetUDI) {
                return records.get(mid);
            } else if (value < targetUDI) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null;
    }
}
