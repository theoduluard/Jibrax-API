import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/api", // backend Spring Boot
});

export default axiosInstance;
