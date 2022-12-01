import React, { useEffect } from "react";
import { Layout } from "antd";
import { connect } from "react-redux";
import { Navigate } from "react-router-dom";

import { getUsers } from "../actions/profile";
import UserItem from "../components/UserItem";
import UserSearch from "../components/UserSearch";

const { Content } = Layout;

const Posts = ({ getUsers, users, userId, isAuthenticated }) => {
	useEffect(() => {
		getUsers();
	}, []);


	if (!isAuthenticated) {
		return <Navigate to="/" replace={true} />;
	}

	return (
		<div>
			<Content className="users">
				<UserSearch />
				<div className="users-container">
					{users?.map((item) => (
						<UserItem data={item} userId={userId} key={item.id} />
					))}
				</div>
			</Content>
		</div>
	);
};

const mapStateToProps = (state) => ({
	users: state.profile?.profiles,
	userId: state.auth.user?.id,
	isAuthenticated: state.auth.isAuthenticated,
});

export default connect(mapStateToProps, { getUsers })(Posts);
