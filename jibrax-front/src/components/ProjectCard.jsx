import React from "react";

export default function ProjectCard({ project }) {
    return (
        <div className="bg-orange-100 p-4 rounded shadow mb-4">
            <h2 className="font-bold text-lg">{project.projectName}</h2>
            <p>{project.projectDescription}</p>
            <p>Leader: {project.projectLeader?.name || "None"}</p>
            <p>Tasks: {project.tasks.length}</p>
        </div>
    );
}
