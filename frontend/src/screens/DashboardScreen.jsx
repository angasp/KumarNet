import React from "react";
import { connect } from "react-redux";
import { Navigate } from "react-router-dom";

import Posts from "../containers/Posts";

const Dashboard = ({ isAuthenticated }) => {
	if (!isAuthenticated) {
		return <Navigate to="/" replace={true} />;
	}

	return <Posts />;
};

const mapStateToProps = (state) => ({
	isAuthenticated: state.auth.isAuthenticated,
});

export default connect(mapStateToProps)(Dashboard);
