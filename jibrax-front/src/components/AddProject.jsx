import React, { useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/projects";

export default function AddProject() {
    const [form, setForm] = useState({
        projectName: "",
        projectDescription: "",
        projectStartDate: "",
        projectLeaderId: ""
    });
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const reset = () => setForm({ projectName: "", projectDescription: "", projectStartDate: "", projectLeaderId: "" });

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.projectName.trim() || !form.projectStartDate) { alert("name and beginning date are mandatory"); return; }
        setLoading(true);
        try {
            const payload = {
                projectName: form.projectName,
                projectDescription: form.projectDescription,
                projectStartDate: form.projectStartDate,
                projectLeaderId: form.projectLeaderId ? Number(form.projectLeaderId) : null
            };
            const res = await axios.post(API_URL, payload);
            alert(`Projet créé : ${res.data.projectName}`);
            reset();
        } catch (err) {
            console.error(err);
            alert("error during project creation");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto bg-white/90 p-6 rounded-2xl shadow-md">
            <h2 className="text-2xl font-semibold mb-4 text-amber-800">Project's informations</h2>

            <form onSubmit={handleSubmit} className="space-y-4">
                <input name="projectName" value={form.projectName} onChange={handleChange} required
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="Project name" />

                <textarea name="projectDescription" value={form.projectDescription} onChange={handleChange}
                          className="w-full p-3 rounded-lg border border-gray-200" placeholder="Description (optionnal)" rows="3" />

                <input name="projectStartDate" type="date" value={form.projectStartDate} onChange={handleChange} required
                       className="w-full p-3 rounded-lg border border-gray-200" />

                <input name="projectLeaderId" value={form.projectLeaderId} onChange={handleChange}
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="Leader's ID (user or team) - optionnal" />

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
