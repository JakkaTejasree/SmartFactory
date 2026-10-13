
package engine;

import java.util.List;
import data.DatasetReader.MachineRecord;

public class ParallelAnalytics {

    public static void run(List<MachineRecord> records) {
        if (records.isEmpty()) {
            System.out.println("Dataset is empty.");
            return;
        }

        double sequentialAir = records.stream()
            .mapToDouble(r -> r.airTemperature)
            .average().orElse(0);

        double parallelAir = records.parallelStream()
            .mapToDouble(r -> r.airTemperature)
            .average().orElse(0);

        double sequentialSpeed = records.stream()
            .mapToDouble(r -> r.rotationalSpeed)
            .average().orElse(0);

        double parallelSpeed = records.parallelStream()
            .mapToDouble(r -> r.rotationalSpeed)
            .average().orElse(0);

        long sequentialFailures = records.stream()
            .filter(r -> r.machineFailure == 1)
            .count();

        long parallelFailures = records.parallelStream()
            .filter(r -> r.machineFailure == 1)
            .count();

        System.out.println("Total records: " + records.size());

        System.out.printf("Sequential average air temperature: %.3f K%n",
                sequentialAir);
        System.out.printf("Parallel average air temperature: %.3f K%n",
                parallelAir);

        System.out.printf("Sequential average rotational speed: %.3f rpm%n",
                sequentialSpeed);
        System.out.printf("Parallel average rotational speed: %.3f rpm%n",
                parallelSpeed);

        System.out.println("Sequential machine failures: " +
                sequentialFailures);
        System.out.println("Parallel machine failures: " +
                parallelFailures);
    }
}
