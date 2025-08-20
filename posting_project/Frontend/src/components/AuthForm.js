// components/AuthForm.js
import React, { useState } from 'react';

function AuthForm({ onLogin }) {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // 임시 저장: localStorage 이용
    const users = JSON.parse(localStorage.getItem('users') || '{}');

    if (isLogin) {
      if (users[username] && users[username] === password) {
        onLogin(username);
      } else {
        alert('로그인 실패: 아이디나 비밀번호가 틀렸습니다.');
      }
    } else {
      if (users[username]) {
        alert('이미 존재하는 아이디입니다.');
      } else {
        users[username] = password;
        localStorage.setItem('users', JSON.stringify(users));
        alert('회원가입 완료! 로그인 해주세요.');
        setIsLogin(true);
      }
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>{isLogin ? '로그인' : '회원가입'}</h2>
      <input placeholder="아이디" value={username} onChange={(e) => setUsername(e.target.value)} required />
      <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} required />
      <button type="submit">{isLogin ? '로그인' : '회원가입'}</button>
      <p onClick={() => setIsLogin(!isLogin)} style={{ cursor: 'pointer', color: 'blue' }}>
        {isLogin ? '회원가입 하시겠어요?' : '로그인으로 돌아가기'}
      </p>
    </form>
  );
}

export default AuthForm;
