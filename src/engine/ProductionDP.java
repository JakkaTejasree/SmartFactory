
package engine;

import java.util.Arrays;
import java.util.List;
import data.DatasetReader.MachineRecord;

public class ProductionDP {

    public static void run(List<MachineRecord> records) {
        if (records.size() < 3) {
            System.out.println("At least 3 records are required.");
            return;
        }

        String[] types = {"L", "M", "H"};
        double[] averageSpeed = new double[3];
        int[] counts = new int[3];

        for (MachineRecord r : records) {
            for (int j = 0; j < types.length; j++) {
                if (r.type.equals(types[j])) {
                    averageSpeed[j] += r.rotationalSpeed;
                    counts[j]++;
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            if (counts[j] > 0) {
                averageSpeed[j] /= counts[j];
            }
        }

        int n = 3;
        int states = 1 << n;
        double[] dp = new double[states];
        int[] previousMask = new int[states];
        int[] chosenType = new int[states];

        Arrays.fill(dp, Double.POSITIVE_INFINITY);
        Arrays.fill(previousMask, -1);
        Arrays.fill(chosenType, -1);
        dp[0] = 0;

        for (int mask = 0; mask < states; mask++) {
            if (Double.isInfinite(dp[mask])) continue;

            int recordIndex = Integer.bitCount(mask);
            if (recordIndex >= n) continue;

            MachineRecord r = records.get(recordIndex);

            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) continue;
                if (counts[j] == 0) continue;

                double cost = Math.abs(
                    r.rotationalSpeed - averageSpeed[j]);

                int nextMask = mask | (1 << j);

                if (dp[mask] + cost < dp[nextMask]) {
                    dp[nextMask] = dp[mask] + cost;
                    previousMask[nextMask] = mask;
                    chosenType[nextMask] = j;
                }
            }
        }

        int fullMask = states - 1;

        if (Double.isInfinite(dp[fullMask])) {
            System.out.println("Assignment not possible.");
            return;
        }

        System.out.printf("Minimum total speed difference: %.2f%n",
                dp[fullMask]);

        int mask = fullMask;
        while (mask != 0) {
            int typeIndex = chosenType[mask];
            int oldMask = previousMask[mask];
            int recordIndex = Integer.bitCount(oldMask);

            MachineRecord r = records.get(recordIndex);

            System.out.println(
                "UDI " + r.udi + " (speed " + r.rotationalSpeed +
                ") -> Type " + types[typeIndex]);

            mask = oldMask;
        }
    }
}
