import axios from "../axiosClient";

import {
	GET_PROFILE,
	CLEAR_PROFILE,
	GET_PROFILES,
	DELETE_POST,
	ADD_POST,
	ADD_AVATAR,
} from "./types";

import { alertError, alertSuccess } from "../util/alert";

//Get current user's profile
export const getCurrentProfile = () => async (dispatch) => {
	try {
		const res = await axios.get("/api/profile/me");

		dispatch({
			type: GET_PROFILE,
			payload: res.data,
		});
	} catch (err) {
		alertError("Session expired");
	}
};

//Get all profiles
export const getUsers = () => async (dispatch) => {
	dispatch({ type: CLEAR_PROFILE });

	try {
		const res = await axios.get("/users");

		dispatch({
			type: GET_PROFILES,
			payload: res.data,
		});
	} catch (err) {
		alertError("Something went wrong");
	}
};

//Get Users by username
export const getSearchUsers = (username) => async (dispatch) => {
	dispatch({ type: CLEAR_PROFILE });

	try {
		const res = await axios.get(`/users?username=${username}`);

		dispatch({
			type: GET_PROFILES,
			payload: res.data,
		});
	} catch (err) {
		alertError("Something went wrong");
	}
};

//Get profile by ID
export const getProfileById = (userId) => async (dispatch) => {
	try {
		const res = await axios.get(`/user?id=${userId}`);

		dispatch({
			type: GET_PROFILE,
			payload: res.data,
		});
	} catch (err) {
		alertError("Something went wrong");
	}
};

//Add profile pic
export const addAvatar = (selectedImage) => async (dispatch) => {
	try {
		const formData = new FormData();
		formData.append("avatar", selectedImage);
		const config = {
			headers: {
				"Content-Type": "multipart/form-data",
				Accept: "application/json",
				type: "formData",
			},
		};

		const res = await axios.put(`/avatar`, formData, config);

		dispatch({
			type: ADD_AVATAR,
			payload: res?.data,
		});
		alertSuccess("Aavatar Added!");
	} catch (err) {
		alertError("Something went wrong with adding avatar");
	}
};

//Delete post
export const deletePost = (postId) => async (dispatch) => {
	try {
		await axios.delete(`/post?id=${postId}`);

		dispatch({
			type: DELETE_POST,
			payload: postId,
		});

		alertSuccess("Post Removed!");
	} catch (err) {
		alertError("Something went wrong with removing post");
	}
};

//Add Post
export const addPost = (text, selectedImage) => async (dispatch) => {
	const formData = new FormData();
	formData.append("img", selectedImage);
	formData.append("body", `{\"text\": \"${text}\"}`);
	const config = {
		headers: {
			"Content-Type": "multipart/form-data",
			Accept: "application/json",
			type: "formData",
		},
	};

	try {
		const res = await axios.post(`/post`, formData, config);
		dispatch({
			type: ADD_POST,
			payload: res.data,
		});


		alertSuccess("Post Created!");
	} catch (err) {
		alertError("Something went wrong with adding post");
	}
};
