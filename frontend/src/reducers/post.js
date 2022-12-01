import {
	GET_POSTS,
	POST_ERROR,
	ADD_LIKE,
	REMOVE_LIKE,
	ADD_COMMENT,
	REMOVE_COMMENT,
	DELETE_POST,
	ADD_POST,
	RESET_POSTS,
} from "../actions/types";

const initState = {
	posts: [],
	hasNext: false,
	loading: false,
	error: {},
};

export default function (state = initState, action) {
	const { type, payload } = action;

	switch (type) {
		case GET_POSTS:
			return {
				...state,
				posts: [...state.posts, ...payload.posts],
				hasNext: payload.hasNext,
				loading: false,
			};
		case POST_ERROR:
			return {
				...state,
				error: payload,
				loading: false,
			};
		case ADD_LIKE:
			return {
				...state,
				posts: state?.posts?.map((post) =>
					post.id === payload.postId
						? { ...post, likes: [...post.likes, payload.like] }
						: post
				),
				loading: false,
			};
		case REMOVE_LIKE:
			return {
				...state,
				posts: state?.posts?.map((post) =>
					post.id === payload.postId
						? {
								...post,
								likes: post.likes.filter(
									(like) => like.userId !== payload.userId
								),
						  }
						: post
				),
				loading: false,
			};
		case ADD_COMMENT:
			return {
				...state,
				posts: state?.posts?.map((post) =>
					post.id === payload.postId
						? { ...post, comments: [...post.comments, payload] }
						: post
				),
				loading: false,
			};
		case REMOVE_COMMENT:
			return {
				...state,
				posts: state?.posts?.map((post) =>
					post.id !== payload.postId
						? post
						: {
								...post,
								comments: post.comments.filter(
									(comment) => comment.id !== payload.commentId
								),
						  }
				),
				loading: false,
			};
		case ADD_POST:
			return {
				...state,
				posts: [payload, ...state?.posts],
				loading: false,
			};
		case DELETE_POST:
			return {
				...state,
				posts: state?.posts?.filter((post) => post.id !== payload),
				loading: false,
			};
		case RESET_POSTS:
			return {
				...initState,
			};

		default:
			return state;
	}
}
