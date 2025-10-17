import { useEffect, useRef, useMemo } from "react";
import { DataSet, Network } from "vis-network/standalone";
import "vis-network/styles/vis-network.css";

/** Parse your textarea topology into {nodes, edges} */
function parseConfig(text) {
  const lines = text
    .split(/\r?\n/)
    .map(l => l.trim())
    .filter(Boolean);

  if (lines.length === 0) return { nodes: [], edges: [] };

  // First line can be a count; ignore it if it's a number
  let start = 0;
  if (/^\d+$/.test(lines[0])) start = 1;

  const nodeSet = new Set();
  const edges = [];
  const seen = new Set(); // to avoid duplicate undirected edges

  for (let i = start; i < lines.length; i++) {
    // Example: R1: (R2, 140), (R4, 180)
    const m = lines[i].match(/^([A-Za-z0-9_-]+)\s*:\s*(.+)$/);
    if (!m) continue;
    const from = m[1];
    nodeSet.add(from);

    const pairs = m[2].split(/\)\s*,\s*\(/).map(s => s.replace(/[()]/g, "").trim());
    for (const p of pairs) {
      const pm = p.match(/^([A-Za-z0-9_-]+)\s*,\s*([0-9]+(?:\.[0-9]+)?)$/);
      if (!pm) continue;
      const to = pm[1];
      const weight = Number(pm[2]);
      nodeSet.add(to);

      const a = from < to ? from : to;
      const b = from < to ? to : from;
      const key = `${a}--${b}`;
      if (seen.has(key)) continue;
      seen.add(key);

      edges.push({ from, to, label: String(weight), width: 2 });
    }
  }

  const nodes = Array.from(nodeSet).map(id => ({ id, label: id, shape: "dot", size: 16 }));
  return { nodes, edges };
}

export default function GraphView({ configText }) {
  const containerRef = useRef(null);
  const data = useMemo(() => {
    const { nodes, edges } = parseConfig(configText || "");
    return { nodes: new DataSet(nodes), edges: new DataSet(edges) };
  }, [configText]);

  useEffect(() => {
    if (!containerRef.current) return;
    const network = new Network(
      containerRef.current,
      data,
      {
        physics: {
          enabled: true,
          solver: "forceAtlas2Based",
          stabilization: { iterations: 200, fit: true }
        },
        interaction: { hover: true, tooltipDelay: 120 },
        edges: { smooth: { enabled: true, type: "dynamic" }, arrows: { to: false } },
        nodes: { font: { size: 14 } }
      }
    );
    return () => network?.destroy();
  }, [data]);

  return (
    <div style={{ height: 420, border: "1px solid #ddd", borderRadius: 8, marginTop: 12 }} ref={containerRef} />
  );
}