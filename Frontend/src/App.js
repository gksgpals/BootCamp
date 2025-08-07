import React, { useState } from 'react';
import './App.css';
import AuthForm from './components/AuthForm';
import PostForm from './components/PostForm';
import PostItem from './components/PostItem';

function App() {
  const [user, setUser] = useState(null); // 로그인 사용자
  const [posts, setPosts] = useState([]);

  const addPost = (newPost) => {
    setPosts([
      ...posts,
      {
        ...newPost,
        id: Date.now(),
        author: user,
        likes: 0,
        likedUsers: [], // 👍 좋아요 누른 사용자
        comments: [],
        createdAt: new Date().toISOString()
      }
    ]);
  };

  const deletePost = (id) => {
    setPosts(posts.filter(post => post.id !== id));
  };

  const updatePost = (id, newContent) => {
    setPosts(posts.map(post =>
      post.id === id ? { ...post, content: newContent } : post
    ));
  };

  const handleLike = (postId, hasLiked) => {
    setPosts(prevPosts =>
      prevPosts.map(post => {
        if (post.id === postId) {
          const updatedLikes = hasLiked ? post.likes - 1 : post.likes + 1;
          const updatedLikedUsers = hasLiked
            ? post.likedUsers.filter(u => u !== user)
            : [...post.likedUsers, user];

          return {
            ...post,
            likes: updatedLikes,
            likedUsers: updatedLikedUsers,
          };
        }
        return post;
      })
    );
  };

  const addComment = (id, comment) => {
    setPosts(posts.map(post =>
      post.id === id
        ? {
            ...post,
            comments: [...post.comments, { user, content: comment }]
          }
        : post
    ));
  };

  return (
    <div className="app">
      <header className="app-header">📸 Small Talk 📸</header>
      <main className="feed">
        {!user ? (
          <AuthForm onLogin={setUser} />
        ) : (
          <>
            <div className="user-bar">
              <span>👤 {user}</span>
              <button onClick={() => setUser(null)}>로그아웃</button>
            </div>

            <PostForm onAddPost={addPost} />

            {posts.slice().reverse().map(post => (
              <PostItem
                key={post.id}
                post={post}
                currentUser={user}
                onDelete={deletePost}
                onUpdate={updatePost}
                onLike={handleLike}
                onComment={addComment}
              />
            ))}
          </>
        )}
      </main>
    </div>
  );
}

export default App;
