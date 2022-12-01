// ! Make with Formik

import { connect } from "react-redux";
import { Formik } from "formik";
import React from "react";
import { Button, Input } from "antd";
import signInSchema from "../schemas/signInSchema";
import {
	EyeTwoTone,
	EyeInvisibleOutlined,
	UserOutlined,
	LockOutlined,
} from "@ant-design/icons";

import { signIn } from "../actions/auth.js";


const SignInForm = ({ setIsRegister, signIn }) => {
	const { Password } = Input;
	return (
		<>
			<Formik
				validationSchema={signInSchema}
				initialValues={{ username: "", password: "" }}
				onSubmit={signIn}
			>
				{({
					values,
					errors,
					touched,
					handleChange,
					handleBlur,
					handleSubmit,
				}) => (
					<div className="login">
						<div className="form sign-in-form">
							<form noValidate onSubmit={handleSubmit}>
								<img src="background.png" alt="" />
								<span className="auth-title">Login</span>
								<Input
									type="username"
									name="username"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.username}
									placeholder="Enter username"
									className="form-control inp_text"
									id="username"
									prefix={<UserOutlined />}
								/>
								{errors.username && touched.username && (
									<p className="error">{errors.username}</p>
								)}
								<Password
									prefix={<LockOutlined className="site-form-item-icon" />}
									name="password"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.password}
									placeholder="Enter password"
									className="form-control"
									iconRender={(visible) =>
										visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
									}
								/>

								{errors.password && touched.password && (
									<p className="error">{errors.password}</p>
								)}
								<button type="submit" className="btnLogin">
									Login
								</button>
							</form>
							<div className="question-div">
								<span className="question">Are you registered?</span>
								<Button
									className="login-form-button"
									onClick={() => setIsRegister(false)}
								>
									Sign Up
								</Button>
							</div>
						</div>
					</div>
				)}
			</Formik>
		</>
	);
};

const mapStateToProps = (state) => ({
	isAuthenticated: state.auth.isAuthenticated,
});

export default connect(mapStateToProps, { signIn })(SignInForm);
