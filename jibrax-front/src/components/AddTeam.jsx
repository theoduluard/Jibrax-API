import React, { useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/teams";

export default function AddTeam() {
    const [teamname, setTeamname] = useState("");
    const [imageBase64, setImageBase64] = useState(null);
    const [preview, setPreview] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleFile = (e) => {
        const file = e.target.files[0];
        if (!file) return;
        const reader = new FileReader();
        reader.onloadend = () => {
            const b64 = reader.result.split(",")[1];
            setImageBase64(b64);
            setPreview(reader.result);
        };
        reader.readAsDataURL(file);
    };

    const reset = () => {
        setTeamname("");
        setImageBase64(null);
        setPreview(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!teamname.trim()) { alert("Team's name is mandatory"); return; }
        setLoading(true);
        try {
            const payload = { teamname, image: imageBase64 };
            const res = await axios.post(API_URL, payload);
            alert(`Equipe créée : ${res.data.teamname}`);
            reset();
        } catch (err) {
            console.error(err);
            alert("Error during team creation");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-xl mx-auto bg-white/90 p-6 rounded-2xl shadow-md">
            <h2 className="text-2xl font-semibold mb-4 text-amber-800">Team's informations</h2>

            <form onSubmit={handleSubmit} className="space-y-4">
                <input value={teamname} onChange={(e) => setTeamname(e.target.value)} required
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="Team's name" />

                <div className="flex items-center gap-4">
                    <label className="flex items-center gap-3 cursor-pointer">
                        <input type="file" accept="image/*" onChange={handleFile} className="hidden" />
                        <span className="px-3 py-2 bg-amber-100 rounded-lg border border-amber-200">Team's image</span>
                    </label>

                    {preview ? (
                        <img src={preview} alt="preview" className="w-20 h-20 object-cover rounded-md border" />
                    ) : (
                        <div className="w-20 h-20 rounded-md bg-amber-50 border flex items-center justify-center text-sm text-amber-400">Preview</div>
                    )}
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
