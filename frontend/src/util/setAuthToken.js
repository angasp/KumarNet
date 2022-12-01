import axios from "../axiosClient";

const setAuthToken = (token) => {
	if (token) {
		axios.defaults.headers.common["X-auth-Token"] = token;
	} else {
		delete axios.defaults.headers.common["X-auth-Token"];
	}
};

export default setAuthToken;
