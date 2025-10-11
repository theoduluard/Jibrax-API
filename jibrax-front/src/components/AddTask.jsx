import React, { useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/tasks";

export default function AddTask() {
    const [form, setForm] = useState({
        taskName: "",
        description: "",
        priority: "LOW",
        status: "NEW",
        type: "BUGFIX",
        assigneeId: "",
        projectId: ""
    });
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const reset = () => setForm({ taskName: "", description: "", priority: "LOW", status: "NEW", type: "BUGFIX", assigneeId: "", projectId: "" });

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.taskName.trim()) { alert("Task name is mandatory"); return; }
        setLoading(true);
        try {
            const payload = {
                ...form,
                assigneeId: form.assigneeId ? Number(form.assigneeId) : null,
                projectId: form.projectId ? Number(form.projectId) : null
            };
            const res = await axios.post(API_URL, payload);
            alert(`Tâche créée : ${res.data.taskName}`);
            reset();
        } catch (err) {
            console.error(err);
            alert("Error during task creation");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto bg-white/90 p-6 rounded-2xl shadow-md">
            <h2 className="text-2xl font-semibold mb-4 text-amber-800">Task's informations</h2>

            <form onSubmit={handleSubmit} className="space-y-4">
                <input name="taskName" value={form.taskName} onChange={handleChange} required
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="Task's name" />

                <textarea name="description" value={form.description} onChange={handleChange}
                          className="w-full p-3 rounded-lg border border-gray-200" placeholder="Description (optionnal)" rows="3" />

                <div className="grid grid-cols-3 gap-3">
                    <select name="priority" value={form.priority} onChange={handleChange}
                            className="p-3 rounded-lg border border-gray-200">
                        <option>LOW</option>
                        <option>MEDIUM</option>
                        <option>HIGH</option>
                        <option>URGENT</option>
                    </select>

                    <select name="status" value={form.status} onChange={handleChange}
                            className="p-3 rounded-lg border border-gray-200">
                        <option>NEW</option>
                        <option>IN_PROGRESS</option>
                        <option>DONE</option>
                        <option>CLOSED</option>
                    </select>

                    <select name="type" value={form.type} onChange={handleChange}
                            className="p-3 rounded-lg border border-gray-200">
                        <option>BUGFIX</option>
                        <option>NEW_FEATURE</option>
                        <option>TECHNICAL_ANALYSIS</option>
                        <option>DOCUMENTATION</option>
                    </select>
                </div>

                <div className="grid grid-cols-2 gap-3">
                    <input name="assigneeId" value={form.assigneeId} onChange={handleChange}
                           className="p-3 rounded-lg border border-gray-200" placeholder="Assignee's ID (optionnal)" />
                    <input name="projectId" value={form.projectId} onChange={handleChange}
                           className="p-3 rounded-lg border border-gray-200" placeholder="Project's ID (optionnal)" />
                </div>

                <div className="flex gap-3">
                    <button type="submit" disabled={loading}
                            className="px-4 py-2 bg-amber-600 text-white rounded-lg shadow hover:opacity-95">
                        {loading ? "Creation..." : "Create"}
                    </button>
                    <button type="button" onClick={reset} className="px-4 py-2 bg-gray-100 rounded-lg">Cancel</button>
                </div>
            </form>
        </div>
    );
}
