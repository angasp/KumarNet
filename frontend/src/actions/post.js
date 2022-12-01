import axios from "../axiosClient";
import {
	GET_POSTS,
	POST_ERROR,
	ADD_LIKE,
	REMOVE_LIKE,
	ADD_COMMENT,
	REMOVE_COMMENT,
	RESET_POSTS,
} from "./types";

import { alertError, alertSuccess } from "../util/alert";

//Get Posts
export const getPosts = (currPage) => async (dispatch) => {
	try {
		const res = await axios.get(`/posts?per_page=10&&page=${currPage}`);
		dispatch({
			type: GET_POSTS,
			payload: res.data,
		});
	} catch (err) {
		dispatch({
			type: POST_ERROR,
			payload: { msg: err.response.statusText, status: err.response.status },
		});
	}
};

//Add like
export const addLike = (postId) => async (dispatch) => {
	try {
		const res = await axios.post(`/like?postId=${postId}`);

		dispatch({
			type: ADD_LIKE,
			payload: { postId, like: res.data },
		});
	} catch (err) {
		alertError("Something went wron");
	}
};

//Remove like
export const removeLike = (postId, userId) => async (dispatch) => {
	try {
		await axios.delete(`/like?postId=${postId}`);
		dispatch({
			type: REMOVE_LIKE,
			payload: { postId, userId },
		});
	} catch (err) {
		alertError("Something went wron");
	}
};

//Add comment
export const addComment = (postId, text) => async (dispatch) => {
	const config = {
		headers: {
			"Content-Type": "application/json",
		},
	};

	const formData = new FormData();
	formData.append("text", text);

	try {
		const res = await axios.post(`/comment?postId=${postId}`, formData, config);

		dispatch({
			type: ADD_COMMENT,
			payload: res.data,
		});

		alertSuccess("Comment Added!");
	} catch (err) {
		alertError("Something went wron");
	}
};

//Delete comment
export const deleteComment = (commentId, postId) => async (dispatch) => {
	try {
		await axios.delete(`comment?id=${commentId}`);

		dispatch({
			type: REMOVE_COMMENT,
			payload: { commentId, postId },
		});

		alertSuccess("Comment Removed!");
	} catch (err) {
		alertError("Something went wrong");
	}
};

export const resetPosts = () => (dispatch) => {
	dispatch({
		type: RESET_POSTS,
	});
};
