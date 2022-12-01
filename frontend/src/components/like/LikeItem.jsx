import React from "react";
import { Link } from "react-router-dom";

import {  Avatar, Image} from "antd";

const LikeItem = ({ data }) => (
	<Link to={`/profile/${data?.userId}`} className="btn btn-primary">
		<div className="like-user">
			{data.avatarUrl === null ? (
				<Avatar src={<Image src="https://joeschmoe.io/api/v1/random" />} />
				) : (
					<Avatar src={<Image src={data.avatarUrl} />} />
				)}
			<p className="like-username">{data.username}</p>
		</div>
	</Link>
);

export default LikeItem;
