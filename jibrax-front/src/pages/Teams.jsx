import React, { useEffect, useState } from "react";
import { getTeams, deleteTeam, getTeamMembers } from "../api/teams";

export default function Teams() {
    const [teams, setTeams] = useState([]);

    const fetchTeams = async () => {
        const res = await getTeams();
        setTeams(res.data);
    };

    const handleDelete = async (id) => {
        await deleteTeam(id);
        fetchTeams();
    };

    useEffect(() => {
        fetchTeams();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Teams</h1>
            {teams.map(team => (
                <div key={team.id} className="bg-orange-100 p-4 rounded shadow mb-2 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        {team.image && (
                            <img
                                src={`data:image/jpeg;base64,${team.image}`}
                                alt={team.id}
                                className="w-20 h-20 object-cover rounded-md border"
                            />
                        )}
                        <div>
                            <h2 className="font-bold">{team.teamname}</h2>
                            <p>Members: {team.teamMembers.length}</p>
                            <p className="text-sm text-gray-600">ID: {team.id}</p>
                        </div>
                    </div>
                    <button
                        onClick={() => handleDelete(team.id)}
                        className="bg-red-500 text-white p-2 rounded"
                    >
                        Delete
                    </button>
                </div>

            ))}
        </div>
    );
}
