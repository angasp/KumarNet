import React, { useState } from "react";
import { Input } from "antd";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Avatar } from "antd";

import { addPost } from "../../actions/profile";
import FileUploader from "../FileUploader";

const PostForm = ({ addPost, user }) => {
	const { TextArea } = Input;
	const [text, setText] = useState("");
	const [selectedImage, setSelectedImage] = useState(null);


	return (
		<div className="post-form">
			<div className="bg-primary header-post-form">
				<h3>Say Something...</h3>
			</div>
			<form
				onSubmit={(e) => {
					e.preventDefault();
					addPost(text, selectedImage);
					setSelectedImage(null);
					setText("");
					
				}}
				className="post-form"
			>
				<div className="form-post">
					<div className="textarea">
						<TextArea
							rows={2}
							className="textArea-post"
							name="text"
							placeholder="Create a post"
							value={text}
							onChange={(e) => setText(e.target.value)}
						/>
					</div>
					<div className="btn-posts">
						<FileUploader onFileSelect={(file) => setSelectedImage(file)} />
						<input type="submit" className="submit-btn" value="Submit" />
					</div>
				</div>
			</form>
		</div>
	);
};

PostForm.propTypes = {
	addPost: PropTypes.func.isRequired,
};

const mapStateToProps = (state) => ({
	user: state.auth.user?.user,
});

export default connect(mapStateToProps, { addPost })(PostForm);
