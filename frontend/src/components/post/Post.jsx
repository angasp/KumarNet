import React, { useState } from "react";
import { Avatar, Button, Card, Collapse, Image } from "antd";
import {
	CommentOutlined,
	HeartFilled ,
	HeartOutlined ,
	DeleteOutlined,
} from "@ant-design/icons";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

import { addLike, removeLike } from "../../actions/post";
import { deletePost } from "../../actions/profile";
import LikeModal from "../like/LikeModal";
import CommentForm from "../comment/CommentForm";
import CommentItem from "../comment/CommentItem";

const { Panel } = Collapse;

const Post = ({ data, userId, deletePost, addLike, removeLike }) => {
	const isLiked = Boolean(data.likes.find((like) => like.userId === userId));
	const [isModalOpen, setIsModalOpen] = useState(false);

	const showModal = () => {
		setIsModalOpen(true);
	};

	const { Meta } = Card;
	const isUsersPost = userId === data.userId;
	const isAvatarNull = data?.avatar_url === null;
	const isImgNull = data?.img_url === null;

	const avatarUrl = isAvatarNull ? (
		<Avatar src={<Image src="https://joeschmoe.io/api/v1/random" />} />
	) : (
		<Avatar src={<Image src={data.avatar_url} />} />
	);

	const deleteIcon = isUsersPost && (
		<DeleteOutlined 
			key="delete"
			onClick={() => deletePost(data.id)}
			className="delete-icon"
		/>
	);

	return (
		<div className="post">
			<Card
				className="post-card"
				bordered={false}
				actions={[<CommentForm postId={data.id} />]}
			>
				{deleteIcon}
				<Link to={`/profile/${data.userId}`} className="btn btn-primary">
					<Meta
						className="meta-post-card"
						avatar={avatarUrl}
						title={data.username}
						description={data.date}
					/>
				</Link>
				<p>{data.text}</p>
				<div className="img-post">
					{!isImgNull && (
						<Image
							alt="img"
							src={data?.img_url}
							style={{ maxHeight: 450 }}
							placeholder={<Image preview={false} src={data?.img_url} />}
						/>
					)}
				</div>
				<LikeModal
					likes={data.likes}
					setIsModalOpen={setIsModalOpen}
					isModalOpen={isModalOpen}
				/>
			</Card>
			<div className="post-actions">
				<div className="like-action">
					{isLiked ? (
						<HeartFilled 
							className="like-icon"
							onClick={() => removeLike(data?.id, userId)}
						/>
					) : (
						<HeartOutlined 
							className="like-icon"
							onClick={() => addLike(data.id)}
						/>
					)}
					<Button onClick={showModal} className="like-btn">
					{data.likes?.length} likes
				</Button>
				</div>
				<Collapse
					expandIcon={() => (
						<CommentOutlined
							key="comment"
							className="comment-icon"
							style={{ boxShadow: "none", background: "white" }}
						/>
					)}
				>
					<Panel header={`${data.comments?.length} comments`}>
						{data?.comments?.length > 0 ? (
							data?.comments?.map((item) => (
								<CommentItem
									data={item}
									userId={userId}
									key={item.id}
									postUserId={data?.userId}
								/>
							))
						) : (
							<h4 style={{ color: "black" }}>No Comments</h4>
						)}
					</Panel>
				</Collapse>
			</div>
		</div>
	);
};

const mapStateToProps = (state) => ({
	auth: state.auth,
});

export default connect(mapStateToProps, { addLike, removeLike, deletePost })(
	Post
);
