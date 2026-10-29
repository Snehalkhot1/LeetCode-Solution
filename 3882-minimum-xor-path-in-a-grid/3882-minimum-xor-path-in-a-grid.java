
class Solution {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int limit = 1024;

        boolean[][] prev = new boolean[n][limit];

        for (int i = 0; i < m; i++) {
            boolean[][] curr = new boolean[n][limit];

            for (int j = 0; j < n; j++) {

                // Starting cell
                if (i == 0 && j == 0) {
                    curr[j][grid[i][j]] = true;
                    continue;
                }

                // Move down: from the previous row
                if (i > 0) {
                    for (int x = 0; x < limit; x++) {
                        if (prev[j][x]) {
                            curr[j][x ^ grid[i][j]] = true;
                        }
                    }
                }

                // Move right: from the previous column
                if (j > 0) {
                    for (int x = 0; x < limit; x++) {
                        if (curr[j - 1][x]) {
                            curr[j][x ^ grid[i][j]] = true;
                        }
                    }
                }
            }

            prev = curr;
        }

        // Find the minimum XOR at the destination
        for (int x = 0; x < limit; x++) {
            if (prev[n - 1][x]) {
                return x;
            }
        }

        return -1;
    }
}
