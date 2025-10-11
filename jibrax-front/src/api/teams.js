import axios from "axios";
const API_URL = "http://localhost:8080/api/teams";

export const getTeams = () => axios.get(API_URL);
export const getTeamById = (id) => axios.get(`${API_URL}/${id}`);
export const createTeam = (data) => axios.post(API_URL, data);
export const updateTeam = (id, data) => axios.put(`${API_URL}/${id}`, data);
export const deleteTeam = (id) => axios.delete(`${API_URL}/${id}`);
export const getTeamMembers = (id) => axios.get(`${API_URL}/${id}/members`);
