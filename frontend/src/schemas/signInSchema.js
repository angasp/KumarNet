import * as Yup from "yup";


const signInSchema = Yup.object().shape({
	username: Yup.string().required("Username is a required field"),
	password: Yup.string()
		.required("Password is a required field")
		.min(8, "Password must be at least 8 characters"),
});

export default signInSchema;