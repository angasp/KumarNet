// ! Make with Formik

import { connect } from "react-redux";
import { Formik } from "formik";
import { Button, Input, DatePicker, Select } from "antd";
import moment from "moment";
import React from "react";
import {
	EyeTwoTone,
	EyeInvisibleOutlined,
	UserOutlined,
	LockOutlined,
} from "@ant-design/icons";

import { signUp } from "../actions/auth.js";
import signUpSchema from "../schemas/signUpSchema";

const SignUpForm = ({ setIsRegister, signUp }) => {
	const { Option } = Select;
	const { Password } = Input;

	return (
		<>
			<Formik
				validationSchema={signUpSchema}
				initialValues={{
					name: "",
					surname: "",
					username: "",
					email: "",
					password: "",
					gender: "",
					bio: "",
					birthDate: "",
					profession: "",
				}}
				onSubmit={signUp}
			>
				{({
					values,
					errors,
					touched,
					setFieldValue,
					handleChange,
					handleBlur,
					handleSubmit,
				}) => (
					<div className="register">
						<div className="form sign-up-form">
							<form noValidate onSubmit={handleSubmit}>
								<span className="auth-title">Register</span>
								<Input
									maxLength={20}
									type="name"
									name="name"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.name}
									placeholder="Enter your first name"
									className="form-control name-div"
								/>
								{errors.name && touched.name && (
									<p className="error">{errors.name}</p>
								)}
								<Input
									maxLength={20}
									type="surname"
									name="surname"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.surname}
									placeholder="Enter your last name"
									className="form-control surname-div"
								/>
								{errors.surname && touched.surname && (
									<p className="error">{errors.surname}</p>
								)}
								<DatePicker
									type="birthDate"
									name="birthDate"
									onChange={(date) =>
										setFieldValue(
											"birthDate",
											moment(date).format("YYYY-MM-DD")
										)
									}
									onBlur={handleBlur}
									value={values.birthDate ? moment(values.birthDate) : ""}
									placeholder="Enter your birthday"
									className="form-control"
									renderExtraFooter={() => "extra footer"}
								/>
								{errors.birthDate && touched.birthDate && (
									<p className="error">{errors.birthDate}</p>
								)}
								<Input
									type="email"
									name="email"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.email}
									placeholder="Enter your email"
									className="form-control"
								/>
								{errors.email && touched.email && (
									<p className="error">{errors.email}</p>
								)}

								<Select
									type="gender"
									name="gender"
									onChange={(item) => setFieldValue("gender", item)}
									onBlur={handleBlur}
									placeholder="Select your gender"
									value={values.gender || undefined}
									className="form-control"
								>
									<Option value="male">Male</Option>
									<Option value="female">Female</Option>
									<Option value="other">Other</Option>
								</Select>
								{errors.gender && touched.gender && (
									<p className="error">{errors.gender}</p>
								)}
								<Input
									type="profession"
									name="profession"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.profession}
									placeholder="Enter your profession"
									className="form-control"
								/>
								{errors.profession && touched.profession && (
									<p className="error">{errors.profession}</p>
								)}

								<Input
									type="bio"
									name="bio"
									onChange={handleChange}
									onBlur={handleBlur}
									value={values.bio}
									placeholder="Enter your bio"
									className="form-control"
								/>
								{errors.bio && touched.bio && (
									<p className="error">{errors.bio}</p>
								)}

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
									Register
								</button>
							</form>
							<div className="question-div">
								<span className="question">Are you registered?</span>
								<Button
									className="login-form-button"
									onClick={() => setIsRegister(true)}
								>
									Sign In
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

export default connect(mapStateToProps, { signUp })(SignUpForm);
