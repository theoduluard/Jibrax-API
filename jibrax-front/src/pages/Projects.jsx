import React, { useEffect, useState } from "react";
import { getProjects, deleteProject } from "../api/projects";
import ProjectCard from "../components/ProjectCard";

export default function Projects() {
    const [projects, setProjects] = useState([]);

    const fetchProjects = async () => {
        const res = await getProjects();
        setProjects(res.data);
    };

    const handleDelete = async (id) => {
        await deleteProject(id);
        fetchProjects();
    };

    useEffect(() => {
        fetchProjects();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Projects</h1>
            {projects.map((project) => (
                <div key={project.projectId} className="flex justify-between items-start">
                    <ProjectCard project={project} />
                    <button onClick={() => handleDelete(project.projectId)} className="bg-red-500 text-white p-2 rounded h-12 self-start">Delete</button>
                </div>
            ))}
        </div>
    );
}
