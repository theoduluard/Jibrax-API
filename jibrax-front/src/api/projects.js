import axios from "axios";
const API_URL = "http://localhost:8080/api/projects";

export const getProjects = () => axios.get(API_URL);
export const getProjectById = (id) => axios.get(`${API_URL}/${id}`);
export const createProject = (data) => axios.post(API_URL, data);
export const updateProject = (id, data) => axios.put(`${API_URL}/${id}`, data);
export const deleteProject = (id) => axios.delete(`${API_URL}/${id}`);
export const getTasksByProject = (id) => axios.get(`${API_URL}/${id}/tasks`);
