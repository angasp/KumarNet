import * as Yup from "yup";
import moment from "moment";

const signUpSchema = Yup.object().shape({
	username: Yup.string().required("Username is a  field"),
	password: Yup.string()
		.required("Password is a required field")
		.min(8, "Password must be at least 8 characters"),
	name: Yup.string().required("Name is required"),
	surname: Yup.string().required("Surname is required"),
	birthDate: Yup.string().nullable()
	.test("date", "You must be 18 years or older", function (value) {
		return moment().diff(moment(value, "YYYY-MM-DD"), 'years') >= 18;
	}).required("Date is required"),
	email: Yup.string().email("Invalid email").required("Email is required"),
	gender: Yup.string().required("Gender is required")

});

export default signUpSchema;
