// 달력 렌더링
function renderCalendar() {
    const calendar = document.getElementById('calendar');
    const title = document.getElementById('calendarTitle');
    
    title.textContent = `${currentYear}년 ${currentMonth}월`;
    
    const dayHeaders = ['일', '월', '화', '수', '목', '금', '토'];
    let html = dayHeaders.map(day => 
        `<div class="calendar-day-header">${day}</div>`
    ).join('');
    
    const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
    const lastDate = new Date(currentYear, currentMonth, 0).getDate();
    const prevLastDate = new Date(currentYear, currentMonth - 1, 0).getDate();
    
    for (let i = firstDay - 1; i >= 0; i--) {
        const day = prevLastDate - i;
        html += `
            <div class="calendar-day other-month">
                <div class="calendar-day-number">${day}</div>
            </div>
        `;
    }
    
    const today = new Date();
    for (let day = 1; day <= lastDate; day++) {
        const dateStr = `${currentYear}-${String(currentMonth).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const record = monthRecords.find(r => r.recordDate === dateStr);
        
        const isToday = today.getFullYear() === currentYear && 
                       today.getMonth() + 1 === currentMonth && 
                       today.getDate() === day;
        
        const classes = ['calendar-day'];
        if (isToday) classes.push('today');
        if (record) classes.push('has-record');
        
        html += `
            <div class="${classes.join(' ')}" onclick="selectDate('${dateStr}')">
                <div class="calendar-day-number">${day}</div>
                ${record ? `
                    <div class="calendar-day-icon">${record.categoryIcon}</div>
                    <div class="calendar-day-activity">${record.activity}</div>
                ` : ''}
            </div>
        `;
    }
    
    const remainingDays = 42 - (firstDay + lastDate);
    for (let day = 1; day <= remainingDays; day++) {
        html += `
            <div class="calendar-day other-month">
                <div class="calendar-day-number">${day}</div>
            </div>
        `;
    }
    
    calendar.innerHTML = html;
}

function previousMonth() {
    currentMonth--;
    if (currentMonth < 1) {
        currentMonth = 12;
        currentYear--;
    }
    loadMonthRecords();
}

function nextMonth() {
    currentMonth++;
    if (currentMonth > 12) {
        currentMonth = 1;
        currentYear++;
    }
    loadMonthRecords();
}

async function loadMonthRecords() {
    try {
        const response = await apiRequest(
            `/records/month?year=${currentYear}&month=${currentMonth}`
        );
        
        monthRecords = await response.json();
        renderCalendar();
        updateMonthStats();
    } catch (error) {
        console.error('월별 기록 로드 실패:', error);
    }
}

function updateMonthStats() {
    document.getElementById('monthRecordDays').textContent = 
        `${monthRecords.length}일`;
    
    if (monthRecords.length === 0) {
        document.getElementById('topCategory').textContent = '-';
        return;
    }
    
    const categoryCounts = {};
    monthRecords.forEach(record => {
        const key = record.categoryName;
        categoryCounts[key] = (categoryCounts[key] || 0) + 1;
    });
    
    const topCategory = Object.entries(categoryCounts)
        .sort((a, b) => b[1] - a[1])[0];
    
    const categoryRecord = monthRecords.find(r => r.categoryName === topCategory[0]);
    document.getElementById('topCategory').textContent = 
        `${categoryRecord.categoryIcon} ${topCategory[0]} (${topCategory[1]}회)`;
}

function selectDate(dateStr) {
    const record = monthRecords.find(r => r.recordDate === dateStr);
    
    if (record) {
        alert(`${record.activity}\n\n${record.content || ''}\n\n${record.duration ? `⏱️ ${record.duration}분` : ''}`);
    } else {
        document.getElementById('recordDate').value = dateStr;
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}
