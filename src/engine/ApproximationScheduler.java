
package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import data.DatasetReader.MachineRecord;

public class ApproximationScheduler {

    public static void run(List<MachineRecord> records) {
        List<MachineRecord> scheduled = new ArrayList<>(records);

        scheduled.sort(
            Comparator.comparingInt((MachineRecord r) -> r.toolWear)
                      .thenComparingInt(r -> r.udi)
        );

        System.out.println("Top 10 records by lowest tool wear:");
        System.out.println("UDI\tProduct ID\tTool Wear\tFailure");

        int limit = Math.min(10, scheduled.size());

        for (int i = 0; i < limit; i++) {
            MachineRecord r = scheduled.get(i);

            System.out.println(
                r.udi + "\t" +
                r.productId + "\t\t" +
                r.toolWear + "\t\t" +
                r.machineFailure);
        }
    }
}
