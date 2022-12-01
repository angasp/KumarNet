import { SearchOutlined } from "@ant-design/icons";
import { Input } from "antd";
import React, { useEffect } from "react";
import { connect } from "react-redux";

import { getSearchUsers } from "../actions/profile";

const UserSearch = ({ getSearchUsers }) => {
	useEffect(() => {
		getSearchUsers();
	}, []);

	const onSearch = (e) => getSearchUsers(e.target.value);

	return (
		<Input
			placeholder="Search for users by username"
			className="user-search"
			onChange={onSearch}
			style={{
				width: 500,
			}}
			prefix={<SearchOutlined />}
		/>
	);
};

const mapStateToProps = (state) => ({
	users: state.profile?.profiles,
	userId: state.auth.user?.id,
});

export default connect(mapStateToProps, { getSearchUsers })(UserSearch);
