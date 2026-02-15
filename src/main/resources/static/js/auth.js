// 인증 확인
async function checkAuth() {
    try {
        const response = await apiRequest('/users/me');
        
        if (response.ok) {
            currentUser = await response.json();
            showMainScreen();
        } else {
            showAuthScreen();
        }
    } catch (error) {
        showAuthScreen();
    }
}

// 로그인
async function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        const response = await apiRequest('/users/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            currentUser = await response.json();
            showToast('로그인 성공! 🎉', 'success');
            showMainScreen();
        } else {
            showToast('로그인에 실패했습니다.', 'error');
        }
    } catch (error) {
        showToast('오류가 발생했습니다.', 'error');
    }
}

// 회원가입
async function register() {
    const username = document.getElementById('registerUsername').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    
    try {
        const response = await apiRequest('/users/register', {
            method: 'POST',
            body: JSON.stringify({ username, email, password })
        });
        
        if (response.ok) {
            showToast('회원가입 완료! 로그인해주세요.', 'success');
            showLogin();
        } else {
            const error = await response.json();
            showToast(error.message || '회원가입에 실패했습니다.', 'error');
        }
    } catch (error) {
        showToast('오류가 발생했습니다.', 'error');
    }
}

// 로그아웃
async function logout() {
    await apiRequest('/users/logout', { method: 'POST' });
    currentUser = null;
    showToast('로그아웃되었습니다.', 'success');
    showAuthScreen();
}

// 화면 전환
function showAuthScreen() {
    document.getElementById('authScreen').classList.remove('hidden');
    document.getElementById('mainScreen').classList.add('hidden');
}

function showMainScreen() {
    document.getElementById('authScreen').classList.add('hidden');
    document.getElementById('mainScreen').classList.remove('hidden');
    loadCategories();
    loadRecords();
    loadMonthRecords();
    loadPublicFeed();
    updateUserInfo();
}

function showLogin() {
    document.getElementById('loginForm').classList.remove('hidden');
    document.getElementById('registerForm').classList.add('hidden');
}

function showRegister() {
    document.getElementById('loginForm').classList.add('hidden');
    document.getElementById('registerForm').classList.remove('hidden');
}

// 사용자 정보 업데이트
function updateUserInfo() {
    document.getElementById('welcomeMessage').textContent = 
        `${currentUser.username}님, 환영합니다! 👋`;
    document.getElementById('currentStreak').textContent = 
        `${currentUser.currentStreak}일 🔥`;
    document.getElementById('totalRecords').textContent = 
        `${currentUser.totalRecords}개`;
}
