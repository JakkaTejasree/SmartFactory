
package engine;

import java.util.List;
import data.DatasetReader.MachineRecord;

public class EdmondsKarp {

    private static final int SOURCE = 0;
    private static final int TYPE_START = 1;
    private static final int MODE_START = 4;
    private static final int SINK = 9;
    private static final int N = 10;

    private static final String[] TYPES = {"L", "M", "H"};
    private static final String[] MODES = {"TWF", "HDF", "PWF", "OSF", "RNF"};

    private static int modeValue(MachineRecord r, int mode) {
        switch (mode) {
            case 0: return r.twf;
            case 1: return r.hdf;
            case 2: return r.pwf;
            case 3: return r.osf;
            default: return r.rnf;
        }
    }

    public static void run(List<MachineRecord> records) {
        int[][] capacity = new int[N][N];

        for (MachineRecord r : records) {
            int typeIndex = -1;

            for (int t = 0; t < TYPES.length; t++) {
                if (r.type.equals(TYPES[t])) {
                    typeIndex = t;
                    break;
                }
            }

            if (typeIndex >= 0) {
                capacity[SOURCE][TYPE_START + typeIndex]++;

                for (int m = 0; m < MODES.length; m++) {
                    if (modeValue(r, m) == 1) {
                        capacity[TYPE_START + typeIndex][MODE_START + m]++;
                    }
                }
            }

            for (int m = 0; m < MODES.length; m++) {
                if (modeValue(r, m) == 1) {
                    capacity[MODE_START + m][SINK]++;
                }
            }
        }

        int[][] residual = new int[N][N];

        for (int i = 0; i < N; i++) {
            residual[i] = capacity[i].clone();
        }

        int maxFlow = 0;
        int[] parent = new int[N];

        while (bfs(residual, parent)) {
            int pathFlow = Integer.MAX_VALUE;

            for (int v = SINK; v != SOURCE; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residual[u][v]);
            }

            for (int v = SINK; v != SOURCE; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        System.out.println("Maximum flow: " + maxFlow);
        System.out.println("Type-to-failure network built from CSV counts.");
    }

    private static boolean bfs(int[][] residual, int[] parent) {
        boolean[] visited = new boolean[N];
        int[] queue = new int[N];
        int front = 0, rear = 0;

        queue[rear++] = SOURCE;
        visited[SOURCE] = true;
        parent[SOURCE] = -1;

        while (front < rear) {
            int u = queue[front++];

            for (int v = 0; v < N; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    queue[rear++] = v;
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[SINK];
    }
}
