import React, { useEffect, useState } from "react";
import { getUsers, deleteUser } from "../api/users";

export default function Users() {
    const [users, setUsers] = useState([]);

    const fetchUsers = async () => {
        const res = await getUsers();
        setUsers(res.data);
    };

    const handleDelete = async (id) => {
        await deleteUser(id);
        fetchUsers();
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (<div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Users</h1>
            {users.map(user => (
                <div key={user.id} className="bg-orange-100 p-4 rounded shadow mb-2 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        {user.image && (
                            <img
                                src={`data:image/jpeg;base64,${user.image}`}
                                alt={user.username}
                                className="w-24 h-24 rounded-full object-cover"
                            />
                        )}
                        <div className="flex flex-col">
                            <h2 className="font-bold">{user.username}</h2>
                            <p>{user.firstname} {user.lastname}</p>
                            <p>Email: {user.email}</p>
                            <p>Team: {user.team?.teamname || "None"}</p>
                            <p className="text-sm text-gray-600">ID: {user.id}</p>
                        </div>
                    </div>
                    <button onClick={() => handleDelete(user.id)} className="bg-red-500 text-white p-2 rounded ml-4">Delete</button>
                </div>

            ))}
        </div>
    );
}
