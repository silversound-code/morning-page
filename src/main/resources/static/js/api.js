// API 기본 설정
const API_URL = 'http://43.201.10.52:8080/api';

// 전역 변수
let currentUser = null;
let categories = [];
let allRecords = [];
let currentYear = new Date().getFullYear();
let currentMonth = new Date().getMonth() + 1;
let monthRecords = [];

// API 호출 헬퍼 함수
async function apiRequest(endpoint, options = {}) {
    const defaultOptions = {
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            ...options.headers
        }
    };
    
    const response = await fetch(`${API_URL}${endpoint}`, {
        ...defaultOptions,
        ...options
    });
    
    return response;
}

// 날짜 포맷팅
function formatDate(dateString) {
    const date = new Date(dateString);
    const today = new Date();
    const yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);
    
    if (date.toDateString() === today.toDateString()) {
        return '오늘';
    } else if (date.toDateString() === yesterday.toDateString()) {
        return '어제';
    } else {
        return date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }
}
