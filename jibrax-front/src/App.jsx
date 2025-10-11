import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Header from "./components/Header";
import Projects from "./pages/Projects";
import Tasks from "./pages/Tasks";
import Teams from "./pages/Teams";
import Users from "./pages/Users";
import AddEntitiesPage from "./pages/AddEntitiesPage";

function App() {
    const [sidebarOpen, setSidebarOpen] = useState(true);

    return (
        <Router>
            <div className="flex">
                <Sidebar open={sidebarOpen} setOpen={setSidebarOpen} />
                <div className="flex-1 ml-64">
                    <Header setOpen={setSidebarOpen} />
                    <div className="p-4">
                        <Routes>
                            <Route path="/projects" element={<Projects />} />
                            <Route path="/tasks" element={<Tasks />} />
                            <Route path="/teams" element={<Teams />} />
                            <Route path="/users" element={<Users />} />
                            <Route path="/add" element={<AddEntitiesPage />} />
                            <Route path="*" element={<Projects />} />
                        </Routes>
                    </div>
                </div>
            </div>
        </Router>
    );
}

export default App;
