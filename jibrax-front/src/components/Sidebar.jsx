import React from "react";
import { NavLink } from "react-router-dom";

export default function Sidebar({ open, setOpen }) {
    return (
        <div className={`fixed top-0 left-0 h-full w-64 bg-orange-800 text-white transform ${open ? "translate-x-0" : "-translate-x-full"} transition-transform duration-300`}>
            <div className="p-6 font-bold text-2xl">JIBRAX</div>
            <nav className="flex flex-col space-y-2 p-4">
                <NavLink to="/projects" className="hover:bg-orange-700 p-2 rounded">Projects</NavLink>
                <NavLink to="/tasks" className="hover:bg-orange-700 p-2 rounded">Tasks</NavLink>
                <NavLink to="/teams" className="hover:bg-orange-700 p-2 rounded">Teams</NavLink>
                <NavLink to="/users" className="hover:bg-orange-700 p-2 rounded">Users</NavLink>
                <NavLink to="/add" className="hover:bg-orange-700 p-2 rounded">Add Entity</NavLink>
            </nav>
        </div>
    );
}
