import React, { useEffect, useRef, useState } from "react";
import { Layout, Spin } from "antd";
import { connect } from "react-redux";
import InfiniteScroll from "react-infinite-scroller";

import { getPosts, resetPosts } from "../actions/post";
import Post from "../components/post/Post";
import PostsForm from "../components/post/PostsForm";

const { Content } = Layout;

const Posts = ({ getPosts, posts, userId, hasNext, resetPosts }) => {
	useEffect(() => {
		getPosts(0);
		return () => resetPosts();
	}, []);

	return (
		<div className="posts">
			<PostsForm userId={userId} />
			<Content className="posts-container" >
				<InfiniteScroll
					hasMore={hasNext}
					loadMore={(page) => {
						getPosts(page);
					}}
					loader={<Spin size="large" />}
				>
					{posts?.map((item) => (
						<Post
							data={item}
							userId={userId}
							key={item?.id}
						/>
					))
					}
				</InfiniteScroll>
			</Content>
		</div>
	);
};



const mapStateToProps = (state) => ({
	posts: state.post?.posts,
	hasNext: state.post?.hasNext,
	userId: state.auth.user?.id,
});

export default connect(mapStateToProps, { getPosts, resetPosts })(Posts);
