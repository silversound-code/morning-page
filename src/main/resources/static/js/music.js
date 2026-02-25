// YouTube URL에서 비디오 ID 추출
function extractYoutubeId(url) {
    const patterns = [
        /youtube\.com\/watch\?v=([a-zA-Z0-9_-]{11})/,
        /youtu\.be\/([a-zA-Z0-9_-]{11})/,
        /youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/
    ];
    for (const pattern of patterns) {
        const match = url.match(pattern);
        if (match) return match[1];
    }
    return null;
}

// YouTube 플레이어 렌더링
function renderYoutubePlayer(videoId, containerId) {
    const player = document.getElementById(containerId || 'youtubePlayer');
    player.innerHTML = `
        <div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; border-radius: 8px;">
            <iframe
                style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"
                src="https://www.youtube.com/embed/${videoId}?autoplay=0"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen>
            </iframe>
        </div>
    `;
}

// 추천곡 설정 (localStorage + 백엔드 저장)
async function setYoutubeMusic() {
    const url = document.getElementById('youtubeUrl').value.trim();
    if (!url) {
        showToast('YouTube URL을 입력해주세요.', 'error');
        return;
    }
    const videoId = extractYoutubeId(url);
    if (!videoId) {
        showToast('올바른 YouTube URL이 아닙니다.', 'error');
        return;
    }

    const storageKey = 'morningpage_music_' + (currentUser ? currentUser.id : 'guest');
    localStorage.setItem(storageKey, url);

    await apiRequest('/users/me/music', {
        method: 'PATCH',
        body: JSON.stringify({ youtubeUrl: url })
    });

    renderYoutubePlayer(videoId, 'youtubePlayer');
    showToast('추천곡이 저장되었습니다! 🎵', 'success');
    loadPublicMusic();
}

// 추천곡 삭제
async function clearYoutubeMusic() {
    const storageKey = 'morningpage_music_' + (currentUser ? currentUser.id : 'guest');
    localStorage.removeItem(storageKey);
    document.getElementById('youtubeUrl').value = '';
    document.getElementById('youtubePlayer').innerHTML = '';

    await apiRequest('/users/me/music', {
        method: 'PATCH',
        body: JSON.stringify({ youtubeUrl: null })
    });

    showToast('추천곡이 삭제되었습니다.', 'success');
    loadPublicMusic();
}

// 저장된 내 추천곡 불러오기
function loadSavedMusic() {
    if (!currentUser) return;
    const storageKey = 'morningpage_music_' + currentUser.id;
    const saved = localStorage.getItem(storageKey);
    if (saved) {
        document.getElementById('youtubeUrl').value = saved;
        const videoId = extractYoutubeId(saved);
        if (videoId) renderYoutubePlayer(videoId, 'youtubePlayer');
    }
    loadPublicMusic();
}

// 모두의 추천곡 불러오기
async function loadPublicMusic() {
    try {
        const response = await apiRequest('/users/music/public');
        if (!response.ok) return;
        const users = await response.json();
        displayPublicMusic(users);
    } catch (error) {
        console.error('공개 음악 로드 실패:', error);
    }
}

// 모두의 추천곡 표시
function displayPublicMusic(users) {
    const container = document.getElementById('publicMusicFeed');
    if (!container) return;

    if (users.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">🎵</div>
                <p>아직 추천곡이 없습니다.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = users.map(user => {
        const videoId = extractYoutubeId(user.youtubeUrl);
        if (!videoId) return '';
        return `
            <div class="music-item">
                <div class="music-author">🎵 @${user.username}</div>
                <div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; border-radius: 8px; margin-top: 8px;">
                    <iframe
                        style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"
                        src="https://www.youtube.com/embed/${videoId}?autoplay=0"
                        frameborder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen>
                    </iframe>
                </div>
            </div>
        `;
    }).join('');
}
