import React, { useEffect, useState } from "react";
import { getTasks, deleteTask } from "../api/tasks";

export default function Tasks() {
    const [tasks, setTasks] = useState([]);

    const fetchTasks = async () => {
        const res = await getTasks();
        setTasks(res.data);
    };

    const handleDelete = async (id) => {
        await deleteTask(id);
        fetchTasks();
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Tasks</h1>
            {tasks.map(task => (
                <div key={task.taskId} className="bg-orange-100 p-4 rounded shadow mb-2 flex justify-between items-center">
                    <div>
                        <h2 className="font-bold">{task.taskName}</h2>
                        <p>{task.description}</p>
                        <p>Priority: {task.priority}</p>
                        <p>Status: {task.status}</p>
                    </div>
                    <button onClick={() => handleDelete(task.taskId)} className="bg-red-500 text-white p-2 rounded">Delete</button>
                </div>
            ))}
        </div>
    );
}
