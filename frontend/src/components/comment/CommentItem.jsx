import { Avatar, Comment, Image } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import React from "react";
import { Link } from "react-router-dom";

import { connect } from "react-redux";

import { addLike, removeLike, deleteComment } from "../../actions/post";

const CommentItem = ({ data, userId, deleteComment, postUserId }) => {
	const isMyUser = userId === data?.userId;
	const isMyPost = userId === postUserId;
	const isAvatarNull = data?.avatar_url === null;


	const actions = (isMyUser || isMyPost) && [
		<DeleteOutlined onClick={() => deleteComment(data?.id, data?.postId)} />,
	];

	return (
		<Link to={`/profile/${data?.userId}`} className="btn btn-primary">
			<Comment
				actions={actions}
				author={<a>{data?.username}</a>}
				avatar={isAvatarNull ? (
					<Avatar src={<Image src="https://joeschmoe.io/api/v1/random" />} />
				) : (
					<Avatar src={<Image src={data?.avatar_url} />} />
				)}
				content={<p>{data?.text}</p>}
				datetime={data?.data}
			/>
		</Link>
	);
};

const mapStateToProps = (state) => ({
	auth: state.auth,
});

export default connect(mapStateToProps, { addLike, removeLike, deleteComment })(
	CommentItem
);
