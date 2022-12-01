import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";

import SignInForm from "../components/SignInForm";
import SignUpForm from "../components/SignUpForm";

const AuthScreen = ({ isAuthenticated }) => {
	const [isRegister, setIsRegister] = useState(true);

	if (isAuthenticated) {
		return <Navigate to="/dashboard" replace={true} />;
	}

	return (
		<>
			{isRegister ? (
				<SignInForm setIsRegister={setIsRegister} />
			) : (
				<SignUpForm setIsRegister={setIsRegister} />
			)}
		</>
	);
};

AuthScreen.propTypes = {
	isAuthenticated: PropTypes.bool,
};

const mapStateToProps = (state) => ({
	isAuthenticated: state.auth.isAuthenticated,
});

export default connect(mapStateToProps)(AuthScreen);
