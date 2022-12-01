import axios from "axios";

const axiosClient = axios.create({
	// baseURL: "http://10.212.20.49:9998",
	baseURL: "http://localhost:7070",
});

export default axiosClient;
