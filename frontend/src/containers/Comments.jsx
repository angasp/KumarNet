import { Collapse } from 'antd';
import React from 'react';
import { connect } from 'react-redux';


import CommentItem from '../components/comment/CommentItem';

const { Panel } = Collapse;

const Comments = (comments , userId) => {

  return (
      <Panel key="1">
        {comments?.comments?.length > 0 ? (
          comments?.comments?.map((item) => (
            <CommentItem data={item} userId={userId} key={item.id} />
          ))
            ) : (
            <h4 style={{ color: "black" }}>No Comments</h4>
          )
        } 
      </Panel>
  );
};

const mapStateToProps = (state) => ({
	userId: state.auth.user?.id,
});

export default connect(mapStateToProps)(Comments);