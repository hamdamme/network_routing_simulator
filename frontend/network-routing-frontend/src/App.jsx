import { useState } from 'react';

import GraphView from "./GraphView";


const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

function App() {
  const [config, setConfig] = useState("");
  const [algorithm, setAlgorithm] = useState("dijkstra");
  const [result, setResult] = useState(null);

  const runSimulation = async () => {
    const response = await fetch(`${API_BASE}/api/run`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ algorithm, config })
    });

    const data = await response.json();
    setResult(data);
  };
  const compareAlgorithms = () => {
    // TODO: implement API calls here
    console.log("Comparing all algorithms...");
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


      {result && (
        <pre>{JSON.stringify(result, null, 2)}</pre>
      )}
    </div>
  );
}

export default App;