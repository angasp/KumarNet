import { Modal } from "antd";
import React from "react";

import LikeItem from "./LikeItem";

const LikeModal = (like) => {
	const { setIsModalOpen, isModalOpen, likes } = like ?? {};

	const likesIsNotEmpty = likes?.length > 0;

	const handleOk = () => {
		setIsModalOpen(false);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	return (
		<>
			<Modal
				title={<div
        style={{
          width: '100%',
          fontWeight: 'bold'
        }} >Likes</div>}
				open={isModalOpen}
				onOk={handleOk}
				onCancel={handleCancel}
        className="modal"
			>
				{likesIsNotEmpty ? (
					likes?.map((item) => <LikeItem data={item} key={item.id} />)
				) : (
					<p style={{ color: "black" }}>No Likes</p>
				)}
			</Modal>
		</>
	);
};

export default LikeModal;
