// components/PostItem.js
import React, { useState } from 'react';
import CommentForm from './CommentForm';

function PostItem({ post, currentUser, onDelete, onUpdate, onLike, onComment }) {
  const [editing, setEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(post.content);

  const hasLiked = post.likedUsers.includes(currentUser);

  const handleUpdate = () => {
    onUpdate(post.id, editedContent);
    setEditing(false);
  };

  const handleLikeClick = () => {
    onLike(post.id, hasLiked);
  };

  const handleShare = () => {
    const shareUrl = `${window.location.origin}/?postId=${post.id}`;
    navigator.clipboard
      .writeText(shareUrl)
      .then(() => {
        alert('📋 게시글 링크가 복사되었습니다!');
      })
      .catch(() => {
        alert('❌ 링크 복사에 실패했어요.');
      });
  };

  const formatDate = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div style={{ border: '1px solid gray', padding: '10px', marginTop: '10px' }}>
      <p><strong>{post.author}</strong> 님의 게시글</p>
      <small>🕒 {formatDate(post.createdAt)}</small>

      {editing ? (
        <>
          <textarea
            value={editedContent}
            onChange={(e) => setEditedContent(e.target.value)}
          />
          <button onClick={handleUpdate}>저장</button>
        </>
      ) : (
        <>
          <p>{post.content}</p>
          {post.image && (
            <img
              src={post.image}
              alt="업로드 이미지"
              style={{ maxWidth: '200px', display: 'block', marginTop: '10px' }}
            />
          )}
        </>
      )}

      <p>
        공감: {post.likes}
        <button onClick={handleLikeClick}>
          {hasLiked ? '💔 공감취소' : '❤️ 공감하기'}
        </button>
        <button onClick={handleShare}>🔗 공유</button> {/* ✅ 공유 버튼 추가 */}
      </p>

      {post.author === currentUser && (
        <>
          <button onClick={() => setEditing(!editing)}>
            {editing ? '취소' : '수정'}
          </button>
          <button onClick={() => onDelete(post.id)}>삭제</button>
        </>
      )}

      <CommentForm onSubmit={(comment) => onComment(post.id, comment)} />
      <ul>
        {post.comments.map((c, i) => (
          <li key={i}>
            <strong>{c.user}:</strong> {c.content}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PostItem;
