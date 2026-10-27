
class Solution {
    public String[] createGrid(int m, int n) {
        String[] grid = new String[m];

        for (int i = 0; i < m; i++) {
            char[] row = new char[n];

            for (int j = 0; j < n; j++) {
                row[j] = '.';
            }

            grid[i] = new String(row);
        }

        // Block cells below the first row
        for (int i = 1; i < m; i++) {
            char[] row = grid[i].toCharArray();
            for (int j = 0; j < n - 1; j++) {
                row[j] = '#';
            }
            grid[i] = new String(row);
        }

        return grid;
    }
}
