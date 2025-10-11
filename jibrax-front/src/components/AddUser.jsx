import React, { useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/users";

export default function AddUser() {
    const [form, setForm] = useState({
        firstname: "",
        lastname: "",
        username: "",
        password: "",
        email: "",
        teamId: ""
    });
    const [imageBase64, setImageBase64] = useState(null);
    const [preview, setPreview] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

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
        setForm({ firstname: "", lastname: "", username: "", password: "", email: "", teamId: "" });
        setImageBase64(null);
        setPreview(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const payload = {
                ...form,
                teamId: form.teamId ? Number(form.teamId) : null,
                image: imageBase64
            };
            const res = await axios.post(API_URL, payload);
            alert(`Utilisateur créé : ${res.data.username}`);
            reset();
        } catch (err) {
            console.error(err);
            alert("Error during user creation");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto bg-white/90 p-6 rounded-2xl shadow-md">
            <h2 className="text-2xl font-semibold mb-4 text-amber-800">User's informations</h2>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <input name="firstname" required value={form.firstname} onChange={handleChange}
                           className="p-3 rounded-lg border border-gray-200" placeholder="firstname" />
                    <input name="lastname" required value={form.lastname} onChange={handleChange}
                           className="p-3 rounded-lg border border-gray-200" placeholder="lastname" />
                </div>

                <input name="username" required value={form.username} onChange={handleChange}
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="username" />

                <input name="email" type="email" required value={form.email} onChange={handleChange}
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="Email" />

                <input name="password" type="password" required value={form.password} onChange={handleChange}
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="password (min 8 characteres)" />

                <input name="teamId" value={form.teamId} onChange={handleChange}
                       className="w-full p-3 rounded-lg border border-gray-200" placeholder="team id (optionnal)" />

                <div className="flex items-center gap-4">
                    <label className="flex items-center gap-3 cursor-pointer">
                        <input type="file" accept="image/*" onChange={handleFile} className="hidden" />
                        <span className="px-3 py-2 bg-amber-100 rounded-lg border border-amber-200">Profile Picture</span>
                    </label>

                    {preview ? (
                        <img src={preview} alt="preview" className="w-20 h-20 object-cover rounded-full border" />
                    ) : (
                        <div className="w-20 h-20 rounded-full bg-amber-50 border flex items-center justify-center text-sm text-amber-400">Preview</div>
                    )}
                </div>

                <div className="flex gap-3">
                    <button type="submit" disabled={loading}
                            className="px-4 py-2 bg-amber-600 text-white rounded-lg shadow hover:opacity-95">
                        {loading ? "Creation..." : "Create"}
                    </button>
                    <button type="button" onClick={reset}
                            className="px-4 py-2 bg-gray-100 rounded-lg">Cancel</button>
                </div>
            </form>
        </div>
    );
}
