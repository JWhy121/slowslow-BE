document.addEventListener("DOMContentLoaded", function() {
    fetch('/category/all')
        .then(response => response.json())
        .then(data => {
            const categoryList = document.getElementById('categoryList');
            data.forEach(category => {
                const row = document.createElement('tr');
                row.innerHTML = `
                            <td>${category.id}</td>
                            <td>${category.categoryName}</td>
                            <td><a href="/category/edit/${category.id}">수정</a></td>
                            <td><a href="/category/edit/${category.id}">삭제</a></td>
                        `;
                categoryList.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching category data:', error));
});