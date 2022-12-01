import React, { useEffect, useState } from "react";
import { useParams, Navigate } from "react-router-dom";
import { connect } from "react-redux";
import { Avatar as AntAvatar, Image, Spin } from "antd";

import PostsForm from "../components/post/PostsForm";

import { getProfileById, addAvatar } from "../actions/profile";
import Post from "../components/post/Post";
import FileUploader from "../components/FileUploader";

const Profile = ({
	getProfileById,
	profile: { profile },
	userId,
	addAvatar,
	isAuthenticated
}) => {
	const params = useParams();

	const [selectedImage, setSelectedImage] = useState(null);

	useEffect(() => {
		getProfileById(params.id);
	}, [getProfileById, params.id]);


	if (!isAuthenticated) {
		return <Navigate to="/" replace={true} />;
	}

	const { user, posts, loading } = profile ?? {};
	const { id, surname, name, bio, username } = user ?? {};

	const postsNotEmpty = posts?.length > 0;

	const isMyUser = userId === id;

	const Avatar = () =>
		!user?.avatarUrl ? (
			<AntAvatar
				src={
					<Image
						src="https://joeschmoe.io/api/v1/random"
						style={{ width: 200 }}
					/>
				}
				size={200}
			/>
		) : (
			<AntAvatar
				src={<Image src={user?.avatarUrl} style={{ width: 200 }} />}
				size={200}
			/>
		);

	const AvatarDiv = () => (
		<div className="btn-posts">
			<FileUploader onFileSelect={(file) => setSelectedImage(file)} />
			<input type="submit" className="submit-btn" value="Submit" />
		</div>
	);

	return loading ? (
		<Spin size="large" />
	) : (
		<div className="profile-grid my-1">
			<div className="user-info">
				<div className="avatar-name">
					<Avatar />
					<p className="user-name">{`${name} ${surname}`}</p>
					{isMyUser && (
						<form
							onSubmit={(e) => {
								e.preventDefault();
								addAvatar(selectedImage);
							}}
							className="post-form"
						>
							<AvatarDiv />
						</form>
					)}
				</div>
				<div className="info">
					<p className="username">@{username}</p>
					<p className="post-count">Posts: {posts?.length}</p>
					<p className="bio">Bio: {bio}</p>
				</div>
			</div>

			<div className="profile-posts bg-white p-2">
				{isMyUser && <PostsForm userId={userId} />}
				<h2 className="text-primary">Posts</h2>

				{postsNotEmpty ? (
					posts?.map((item) => (
						<Post data={item} userId={userId} key={item.id} />
					))
				) : (
					<h4 style={{ color: "white" }}>No posts</h4>
				)}
			</div>
		</div>
	);
};

const mapStateToProps = (state) => ({
	state,
	profile: state.profile,
	userId: state.auth.user?.id,
	isAuthenticated: state.auth.isAuthenticated,

});

export default connect(mapStateToProps, { getProfileById, addAvatar })(Profile);
