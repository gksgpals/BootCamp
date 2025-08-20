import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

function PostForm({ onAddPost, currentUser }) {
  const [content, setContent] = useState('');
  const [, setImage] = useState(null); // 이미지 파일 상태
  const [preview, setPreview] = useState(null); // 미리보기 URL

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImage(file);
      setPreview(URL.createObjectURL(file)); // 미리보기용 URL 생성
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!content.trim()) return;

    const newPost = {
      id: uuidv4(),
      content,
      author: currentUser,
      likes: 0,
      likedUsers: [],
      createdAt: new Date().toISOString(),
      comments: [],
      image: preview, // 프론트엔드에서는 미리보기 URL로 저장 (백엔드 없을 경우)
    };

    onAddPost(newPost);
    setContent('');
    setImage(null);
    setPreview(null);
  };

  return (
    <form onSubmit={handleSubmit}>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="게시글을 작성하세요"
      />
      <input type="file" accept="image/*" onChange={handleImageChange} />
      {preview && <img src={preview} alt="미리보기" style={{ maxWidth: '200px', marginTop: '10px' }} />}
      <br />
      <button type="submit">등록</button>
    </form>
  );
}

export default PostForm;
