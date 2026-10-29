
import java.util.*;

class Solution {
    List<Integer>[] adj;
    int[] parent, depth, size, heavy, head, pos;
    int[] seg, inversePos, val;
    int timer;

    public List<Boolean> palindromePath(int n, int[][] edges, String s,
                                        String[] queries) {
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        parent = new int[n];
        depth = new int[n];
        size = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];
        inversePos = new int[n];
        val = new int[n];

        Arrays.fill(heavy, -1);

        for (int i = 0; i < n; i++) {
            val[i] = 1 << (s.charAt(i) - 'a');
        }

        dfs(0, -1);
        timer = 0;
        decompose(0, 0);

        // Initialize inverse positions before building the segment tree.
        for (int i = 0; i < n; i++) {
            inversePos[pos[i]] = i;
        }

        seg = new int[4 * n];
        build(1, 0, n - 1);

        List<Boolean> answer = new ArrayList<>();

        for (String q : queries) {
            String[] p = q.split(" ");
            int u = Integer.parseInt(p[1]);

            if (p[0].equals("update")) {
                int bit = 1 << (p[2].charAt(0) - 'a');
                val[u] = bit;
                update(1, 0, n - 1, pos[u], bit);
            } else {
                int v = Integer.parseInt(p[2]);
                int mask = 0;

                while (head[u] != head[v]) {
                    if (depth[head[u]] < depth[head[v]]) {
                        int temp = u;
                        u = v;
                        v = temp;
                    }

                    mask ^= query(1, 0, n - 1,
                                  pos[head[u]], pos[u]);
                    u = parent[head[u]];
                }

                if (depth[u] > depth[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }

                mask ^= query(1, 0, n - 1, pos[u], pos[v]);

                answer.add(Integer.bitCount(mask) <= 1);
            }
        }

        return answer;
    }

    int dfs(int u, int p) {
        parent[u] = p;
        size[u] = 1;
        int maxSize = 0;

        for (int v : adj[u]) {
            if (v == p) continue;

            depth[v] = depth[u] + 1;
            int sub = dfs(v, u);
            size[u] += sub;

            if (sub > maxSize) {
                maxSize = sub;
                heavy[u] = v;
            }
        }

        return size[u];
    }

    void decompose(int u, int h) {
        head[u] = h;
        pos[u] = timer++;

        if (heavy[u] != -1) {
            decompose(heavy[u], h);
        }

        for (int v : adj[u]) {
            if (v != parent[u] && v != heavy[u]) {
                decompose(v, v);
            }
        }
    }

    void build(int node, int l, int r) {
        if (l == r) {
            seg[node] = val[inversePos[l]];
            return;
        }

        int mid = (l + r) / 2;
        build(node * 2, l, mid);
        build(node * 2 + 1, mid + 1, r);

        seg[node] = seg[node * 2] ^ seg[node * 2 + 1];
    }

    void update(int node, int l, int r, int idx, int value) {
        if (l == r) {
            seg[node] = value;
            return;
        }

        int mid = (l + r) / 2;

        if (idx <= mid) {
            update(node * 2, l, mid, idx, value);
        } else {
            update(node * 2 + 1, mid + 1, r, idx, value);
        }

        seg[node] = seg[node * 2] ^ seg[node * 2 + 1];
    }

    int query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return seg[node];
        }

        if (r < ql || l > qr) {
            return 0;
        }

        int mid = (l + r) / 2;

        return query(node * 2, l, mid, ql, qr)
             ^ query(node * 2 + 1, mid + 1, r, ql, qr);
    }
}
