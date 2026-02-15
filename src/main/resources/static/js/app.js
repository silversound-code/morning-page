// 앱 초기화
document.addEventListener('DOMContentLoaded', () => {
    initDarkMode();
    checkAuth();
    setupEventListeners();
});

// 이벤트 리스너 설정
function setupEventListeners() {
    // 로그인
    document.getElementById('loginFormElement').addEventListener('submit', async (e) => {
        e.preventDefault();
        await login();
    });
    
    // 회원가입
    document.getElementById('registerFormElement').addEventListener('submit', async (e) => {
        e.preventDefault();
        await register();
    });
    
    // 기록 작성
    document.getElementById('recordForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        await createRecord();
    });
    
    // 기록 수정
    document.getElementById('editForm').addEventListener('submit', updateRecord);
    
    // 오늘 날짜 설정
    document.getElementById('recordDate').valueAsDate = new Date();
}
