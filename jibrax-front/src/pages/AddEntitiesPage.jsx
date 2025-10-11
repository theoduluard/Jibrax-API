import React, { useState } from "react";
import AddUser from "../components/AddUser";
import AddTeam from "../components/AddTeam";
import AddProject from "../components/AddProject";
import AddTask from "../components/AddTask";

export default function AddEntitiesPage() {
    const [active, setActive] = useState("user");

    const Button = ({ id, children }) => (
        <button onClick={() => setActive(id)}
                className={`px-3 py-2 rounded-lg font-medium ${active===id ? "bg-amber-600 text-white" : "bg-amber-50 text-amber-800"}`}>
            {children}
        </button>
    );

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold text-amber-800 mb-4">Add a new {active}</h1>
            <div className="flex gap-3 mb-6">
                <Button id="user">User</Button>
                <Button id="team">Team</Button>
                <Button id="project">Project</Button>
                <Button id="task">Task</Button>
            </div>

            <div>
                {active === "user" && <AddUser />}
                {active === "team" && <AddTeam />}
                {active === "project" && <AddProject />}
                {active === "task" && <AddTask />}
            </div>
        </div>
    );
}
