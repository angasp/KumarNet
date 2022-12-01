import {
	REGISTER_SUCCESS,
	REGISTER_FAIL,
	USER_LOADED,
	AUTH_ERROR,
	LOGIN_FAIL,
	LOGIN_SUCCESS,
	LOGOUT,
	CLEAR_PROFILE,
} from "./types";
import setAuthToken from "../util/setAuthToken";
import axios from "../axiosClient";
import { alertError, alertSuccess } from "../util/alert";

//Load User
export const loadUser = () => async (dispatch) => {
	if (localStorage.token) {
		setAuthToken(localStorage.token);
	}
	try {
		const res = await axios.get("/user");

		dispatch({ type: USER_LOADED, payload: res.data });
	} catch (err) {
		dispatch({ type: AUTH_ERROR });
	}
};

//Register User
export const signUp =
	({
		username,
		password,
		name,
		surname,
		email,
		gender,
		bio,
		birthDate,
		profession,
	}) =>
	async (dispatch) => {
		const body = JSON.stringify({
			username,
			password,
			name,
			surname,
			email,
			gender,
			bio,
			birthDate,
			profession,
		});

		try {
			const res = await axios.post("/signUp", body);
			dispatch({ type: REGISTER_SUCCESS, payload: res.data });
			dispatch(loadUser());
			alertSuccess("Your account has been created")
		} catch (err) {
			const error = err.response.data;
			if (error) {
				alertError(error);
			}
			dispatch({ type: REGISTER_FAIL });
		}
	};

//Login User

export const signIn =
	({ username, password }) =>
	async (dispatch) => {
		const body = JSON.stringify({ username, password });
		try {
			const res = await axios.post("/signIn", body);
			dispatch({ type: LOGIN_SUCCESS, payload: res.data });
			dispatch(loadUser());
		} catch (err) {
			const error = err.response.data;
			if (error) {
				alertError(error);
			}
			dispatch({ type: LOGIN_FAIL });
		}
	};

//Logout / Clear Profile
export const logout = () => (dispatch) => {
	dispatch({ type: CLEAR_PROFILE });
	dispatch({ type: LOGOUT });
};
