# socket#Network Routing Simulator

Interactive web-based tool for simulating and visualizing **network routing algorithms**.  
Supports **Distance Vector**, **Dijkstra (Link State)**, and **Bellman-Ford**, with real-time simulation, graph visualization, and exportable results.


## Features
- Upload network topologies from `.txt` or `.json`.
- Interactive **graph visualization** (nodes = routers, edges = links with costs).
- Run and compare multiple routing algorithms:
  - Distance Vector
  - Dijkstra (Link State)
  - Bellman-Ford
- Step-by-step simulation with animations of routing updates.
- Update link costs interactively and watch routes recalculate.
- Export results as **CSV, JSON, or graph images**.
- Cloud-ready deployment with **Spring Boot (Java)** backend and **React/D3.js** frontend.


## Tech Stack
- **Backend:** Java, Spring Boot, Maven  
- **Frontend:** React, D3.js (for graph visualization)  
- **Data Formats:** TXT, JSON, CSV  
- **Deployment:** Render/Heroku (backend), Netlify/Vercel (frontend)  


## Project Structure
```

network-routing-visualizer/
├── backend/     # Java Spring Boot API
├── frontend/    # React + D3.js UI
├── configs/     # Sample network topologies
├── docs/        # Screenshots, report, demo video
├── tests/       # Unit & integration tests
└── README.md

````


## Quick Start (Planned)
```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/network-routing-visualizer.git
cd network-routing-visualizer

# Backend (Java + Maven)
cd backend
mvn spring-boot:run

# Frontend (React + Vite)
cd frontend
npm install
npm run dev
````

##  Example

*Coming soon — screenshots and demo GIFs will go here.*

## Live Demo

*Coming soon — link to deployed site (Netlify/Vercel + Render/Heroku).*