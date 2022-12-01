import {
	REGISTER_SUCCESS,
	REGISTER_FAIL,
	USER_LOADED,
	AUTH_ERROR,
	LOGIN_FAIL,
	LOGIN_SUCCESS,
	LOGOUT,
	ADD_AVATAR,
} from "../actions/types";

const initState = {
	token: localStorage.getItem("token"),
	isAuthenticated: null,
	loading: false,
	posts: [],
	user: null,
};

export default function (state = initState, action) {
	const { type, payload } = action;

	switch (type) {
		case REGISTER_SUCCESS:
		case LOGIN_SUCCESS:
			localStorage.setItem("token", payload.token);
			return { ...state, ...payload, isAuthenticated: true, loading: false };
		case AUTH_ERROR:
		case LOGOUT:
			localStorage.removeItem("token");
			return { ...state, token: null, isAuthenticated: false, loading: false };
		case REGISTER_FAIL:
		case LOGIN_FAIL:
			localStorage.removeItem("token");
			return { ...state, token: null, isAuthenticated: false, loading: false };

		case USER_LOADED:
			return {
				...state,
				isAuthenticated: true,
				loading: false,
				...payload,
			};
		case ADD_AVATAR:
			return {
				...state,
				user: {
					...state.user,
					avatarUrl: payload,
				},
				loading: false,
			};

		default:
			return state;
	}
}
