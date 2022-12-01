import React, { useState } from "react";
import { Input } from "antd";
import PropTypes from "prop-types";
import { connect } from "react-redux";

import { addComment } from "../../actions/post";

const CommentForm = ({ addComment, postId }) => {
	const { TextArea } = Input;
	const [text, setText] = useState("");

	return (
		<div className="comment-form">
			<form
				onSubmit={(e) => {
					e.preventDefault();
					addComment(postId, text);
					setText("");
				}}
				className="comments-form"
			>
				<TextArea
					rows={1}
					className="textArea-ant"
					name="text"
					placeholder="Add a comment ..."
					value={text}
					onChange={(e) => setText(e.target.value)}
					required
				/>
				<div className="btn-comments">
					<input type="submit" className="submit-btn-comment" value="Add" />
				</div>
			</form>
		</div>
	);
};

CommentForm.propTypes = {
	addComment: PropTypes.func.isRequired,
};

export default connect(null, { addComment })(CommentForm);
