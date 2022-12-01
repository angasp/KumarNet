import React from "react";
import { Link } from "react-router-dom";
import { Card, Image } from "antd";

const UserItem = ({ data }) => {
	const { Meta } = Card;

	const isAvatarNull = data.avatar_url === null;


	const avatar = isAvatarNull ? (
		<Image alt="example" className="users-photo" src="https://joeschmoe.io/api/v1/random"  />
	):(
		<Image alt="example" className="users-photo" src={data.avatar_url} />
	);

	return (
		<Link to={`/profile/${data.id}`} className="btn btn-primary">
			<Card
				className="user-card"
				bordered={false}
				hoverable={true}
				cover={ avatar}
			>
				<Meta
					title={data.name + " " + data.surname}
					description={data.username}
				/>
			</Card>
		</Link>
	);
};

export default UserItem;
