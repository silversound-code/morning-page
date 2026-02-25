// 카테고리 로드
async function loadCategories() {
    try {
        const response = await apiRequest('/categories');
        categories = await response.json();
        
        const select = document.getElementById('categoryId');
        select.innerHTML = '<option value="">선택하세요</option>';
        
        const filterSelect = document.getElementById('categoryFilter');
        filterSelect.innerHTML = '<option value="">전체 카테고리</option>';
        
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = `${cat.icon} ${cat.name}`;
            select.appendChild(option);
            
            const filterOption = document.createElement('option');
            filterOption.value = cat.name;
            filterOption.textContent = `${cat.icon} ${cat.name}`;
            filterSelect.appendChild(filterOption);
        });
    } catch (error) {
        console.error('카테고리 로드 실패:', error);
    }
}

// 기록 작성
async function createRecord() {
    const data = {
        recordDate: document.getElementById('recordDate').value,
        categoryId: parseInt(document.getElementById('categoryId').value),
        activity: document.getElementById('activity').value,
        duration: parseInt(document.getElementById('duration').value) || null,
        content: document.getElementById('content').value,
        isPublic: document.getElementById('isPublic').checked
    };
    
    try {
        const response = await apiRequest('/records', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            showToast('기록이 작성되었습니다! ✨', 'success');
            document.getElementById('recordForm').reset();
            document.getElementById('recordDate').valueAsDate = new Date();
            loadRecords();
            loadMonthRecords();
            loadPublicFeed();
            checkAuth();
        } else {
            const error = await response.json();
            showToast(error.message || '기록 작성에 실패했습니다.', 'error');
        }
    } catch (error) {
        showToast('오류가 발생했습니다.', 'error');
    }
}

// 기록 목록 로드
async function loadRecords() {
    try {
        const response = await apiRequest('/records/my');
        allRecords = await response.json();
        displayRecords(allRecords);
    } catch (error) {
        console.error('기록 로드 실패:', error);
    }
}

// 기록 표시
function displayRecords(records) {
    const container = document.getElementById('recordList');
    
    if (records.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📝</div>
                <p>아직 기록이 없습니다.<br>첫 기록을 작성해보세요!</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = records.map(record => `
        <div class="record-item">
            <div class="record-header">
                <div class="record-category">
                    <span class="category-icon">${record.categoryIcon}</span>
                    <span class="category-name">${record.categoryName}</span>
                </div>
                <div class="record-date">${formatDate(record.recordDate)}</div>
            </div>
            <div class="record-activity">${record.activity}</div>
            <div class="record-content">${record.content || ''}</div>
            <div class="record-meta">
                ${record.duration ? `⏱️ ${record.duration}분` : ''}
                ${record.isPublic ? '🌍 공개' : '🔒 비공개'}
            </div>
            <div class="record-actions">
                <button class="btn-edit btn-small" onclick="openEditModal(${record.id})">수정</button>
                <button class="btn-delete btn-small" onclick="deleteRecord(${record.id})">삭제</button>
            </div>
        </div>
    `).join('');
}

// 기록 삭제
async function deleteRecord(id) {
    if (!confirm('정말 삭제하시겠습니까?')) return;
    
    try {
        const response = await apiRequest(`/records/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            showToast('삭제되었습니다.', 'success');
            loadRecords();
            loadMonthRecords();
            loadPublicFeed();
            checkAuth();
        } else {
            showToast('삭제에 실패했습니다.', 'error');
        }
    } catch (error) {
        showToast('오류가 발생했습니다.', 'error');
    }
}

// 수정 모달
async function openEditModal(id) {
    try {
        const response = await apiRequest(`/records/${id}`);
        const record = await response.json();
        
        document.getElementById('editRecordId').value = record.id;
        document.getElementById('editRecordDate').value = record.recordDate;
        document.getElementById('editActivity').value = record.activity;
        document.getElementById('editDuration').value = record.duration || '';
        document.getElementById('editContent').value = record.content || '';
        document.getElementById('editIsPublic').checked = record.isPublic;
        
        const editCategorySelect = document.getElementById('editCategoryId');
        editCategorySelect.innerHTML = '<option value="">선택하세요</option>';
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = `${cat.icon} ${cat.name}`;
            if (cat.name === record.categoryName) {
                option.selected = true;
            }
            editCategorySelect.appendChild(option);
        });
        
        document.getElementById('editModal').classList.add('active');
    } catch (error) {
        showToast('기록을 불러오지 못했습니다.', 'error');
    }
}

function closeEditModal() {
    document.getElementById('editModal').classList.remove('active');
}

async function updateRecord(e) {
    e.preventDefault();
    
    const id = document.getElementById('editRecordId').value;
    const data = {
        recordDate: document.getElementById('editRecordDate').value,
        categoryId: parseInt(document.getElementById('editCategoryId').value),
        activity: document.getElementById('editActivity').value,
        duration: parseInt(document.getElementById('editDuration').value) || null,
        content: document.getElementById('editContent').value,
        isPublic: document.getElementById('editIsPublic').checked
    };
    
    try {
        const response = await apiRequest(`/records/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
        
        if (response.ok) {
            showToast('수정되었습니다! ✨', 'success');
            closeEditModal();
            loadRecords();
            loadMonthRecords();
            loadPublicFeed();
        } else {
            showToast('수정에 실패했습니다.', 'error');
        }
    } catch (error) {
        showToast('오류가 발생했습니다.', 'error');
    }
}

// 공개 피드
async function loadPublicFeed() {
    try {
        const response = await apiRequest('/records/public');
        const records = await response.json();
        displayPublicFeed(records);
    } catch (error) {
        console.error('공개 피드 로드 실패:', error);
    }
}

function displayPublicFeed(records) {
    const container = document.getElementById('publicFeed');
    
    if (records.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">🌍</div>
                <p>아직 공개된 기록이 없습니다.</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = records.map(record => `
        <div class="record-item public-record">
            <div class="record-author">@${record.username}</div>
            <div class="record-header">
                <div class="record-category">
                    <span class="category-icon">${record.categoryIcon}</span>
                    <span class="category-name">${record.categoryName}</span>
                </div>
                <div class="record-date">${formatDate(record.recordDate)}</div>
            </div>
            <div class="record-activity">${record.activity}</div>
            <div class="record-content">${record.content || ''}</div>
            <div class="record-meta">
                ${record.duration ? `⏱️ ${record.duration}분` : ''}
            </div>
        </div>
    `).join('');
}

// 검색 & 필터
function filterRecords() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const selectedCategory = document.getElementById('categoryFilter').value;
    
    let filtered = allRecords;
    
    if (selectedCategory !== '') {
        filtered = filtered.filter(r => r.categoryName === selectedCategory);
    }
    
    if (searchTerm !== '') {
        filtered = filtered.filter(r => 
            r.activity.toLowerCase().includes(searchTerm) ||
            (r.content && r.content.toLowerCase().includes(searchTerm))
        );
    }
    
    displayRecords(filtered);
}

function searchRecords() {
    filterRecords();
}

function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('categoryFilter').value = '';
    displayRecords(allRecords);
}
