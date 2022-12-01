import {
	GET_PROFILE,
	PROFILE_ERROR,
	CLEAR_PROFILE,
	UPDATE_PROFILE,
	GET_PROFILES,
	GET_REPOS,
	DELETE_POST,
	ADD_POST,
	ADD_AVATAR,
	ADD_LIKE,
	REMOVE_LIKE,
	ADD_COMMENT,
	REMOVE_COMMENT,
} from "../actions/types";

const initState = {
	profile: null,
	profiles: [],
	repos: [],
	loading: false,
	error: null,
};

export default function (state = initState, action) {
	const { type, payload } = action;

	switch (type) {
		case GET_PROFILE:
		case UPDATE_PROFILE:
			return {
				...state,
				profile: payload,
				loading: false,
			};
		case GET_PROFILES:
			return {
				...state,
				profiles: payload,
				loading: false,
			};

		case ADD_POST:
			return {
				...state,
				...(state?.profile && {
					profile: {
						...state?.profile,
						...(state?.profile?.posts && {
							posts: [payload, ...state?.profile?.posts],
						}),
					},
				}),
				loading: false,
			};
		case DELETE_POST:
			return {
				...state,
				...(state?.profile && {
					profile: {
						...state.profile,
						...(state?.profile?.posts && {
							posts: state?.profile?.posts.filter(
								(post) => post.id !== payload
							),
						}),
					},
				}),
				loading: false,
			};
		case PROFILE_ERROR:
			return {
				...state,
				error: payload,
				loading: false,
			};
		case CLEAR_PROFILE:
			return {
				...state,
				profile: null,
				repos: [],
				loading: false,
			};
		case GET_REPOS:
			return {
				...state,
				repos: payload,
				loading: false,
			};

		case ADD_AVATAR:
			return {
				...state,
				profile: {
					...state.profile,
					user: {
						...state.profile?.user,
						avatarUrl: payload,
					},
				},
				loading: false,
			};
		case ADD_LIKE:
			return {
				...state,
				profile: {
					...state.profile,
					posts: state.profile?.posts?.map((post) =>
						post.id === payload.postId
							? { ...post, likes: [...post.likes, payload.like] }
							: post
					),
				},

				loading: false,
			};
		case REMOVE_LIKE:
			return {
				...state,
				profile: {
					...state.profile,
					posts: state.profile?.posts?.map((post) =>
						post.id === payload.postId
							? {
									...post,
									likes: post.likes.filter(
										(like) => like.userId !== payload.userId
									),
							  }
							: post
					),
				},
				loading: false,
			};
		case ADD_COMMENT:
			return {
				...state,
				profile: {
					...state?.profile,
					posts: state.profile?.posts?.map((post) =>
						post.id === payload.postId
							? { ...post, comments: [...post.comments, payload] }
							: post
					),
				},
				loading: false,
			};
		case REMOVE_COMMENT:
			return {
				...state,
				profile: {
					...state?.profile,
					posts: state.profile?.posts?.map((post) =>
						post.id !== payload.postId
							? post
							: {
									...post,
									comments: post.comments.filter(
										(comment) => comment.id !== payload.commentId
									),
							  }
					),
				},
				loading: false,
			};
		default:
			return state;
	}
}
