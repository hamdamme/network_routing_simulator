import { useState } from 'react';

import GraphView from "./GraphView";


const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

function App() {
  const [config, setConfig] = useState("");
  const [algorithm, setAlgorithm] = useState("dijkstra");
  const [result, setResult] = useState(null);
  const [compareResults, setCompareResults] = useState(null);
  const [loadingCompare, setLoadingCompare] = useState(false);


  const runSimulation = async () => {
    const response = await fetch(`${API_BASE}/api/run`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ algorithm, config })
    });

    const data = await response.json();
    setResult(data);
  };
  const compareAlgorithms = async () => {
  if (!config.trim()) { alert("Paste a topology first."); return; }
  try {
    setLoadingCompare(true);
    const [dj, dv, bf] = await Promise.all([
      runAlgo("dijkstra"),
      runAlgo("dv"),
      runAlgo("bf")
    ]);
    setCompareResults({ dijkstra: dj, dv, bf });
  } catch (e) {
    console.error(e);
    alert("Compare failed. Check console for details.");
  } finally {
    setLoadingCompare(false);
  }
};


  const runAlgo = async (algo) => {
  const resp = await fetch(`${API_BASE}/api/run`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ algorithm: algo, config })
  });
  if (!resp.ok) throw new Error(`API ${algo} failed`);
  return resp.json();
};



  return (
    <div style={{ padding: "20px" }}>
      <h1>Network Routing Visualizer</h1>

      <label>Routing Config:</label>
      <textarea
        rows="8"
        style={{ width: "100%" }}
        value={config}
        onChange={(e) => setConfig(e.target.value)}
      />
      <GraphView configText={config} />

      <label>Algorithm:</label>
      <select value={algorithm} onChange={(e) => setAlgorithm(e.target.value)}>
        <option value="dijkstra">Dijkstra</option>
        <option value="dv">Distance Vector</option>
        <option value="bf">Bellman-Ford</option>
      </select>

      <button onClick={runSimulation}>Run Simulation</button>
      <button onClick={compareAlgorithms}>Compare All</button>
      {loadingCompare && <p>Comparing…</p>}

{compareResults && (
  <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: 12, marginTop: 12 }}>
    <div><h3>Dijkstra</h3><pre>{JSON.stringify(compareResults.dijkstra, null, 2)}</pre></div>
    <div><h3>Distance Vector</h3><pre>{JSON.stringify(compareResults.dv, null, 2)}</pre></div>
    <div><h3>Bellman-Ford</h3><pre>{JSON.stringify(compareResults.bf, null, 2)}</pre></div>
  </div>
)}



      {result && (
        <pre>{JSON.stringify(result, null, 2)}</pre>
      )}
    </div>
  );
}

export default App;