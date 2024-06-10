document.addEventListener("DOMContentLoaded", function() {
    fetch('/brand/all')
        .then(response => response.json())
        .then(data => {
            const brandList = document.getElementById('brandList');
            data.forEach(brand => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${brand.id}</td>
                    <td>${brand.brandName}</td>
                    <td><a href="/brand/edit/${brand.id}">수정</a></td>
                    <td><a href="/brand/delete/${brand.id}">삭제</a></td>
                `;
                brandList.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching brand data:', error));
});
