document.addEventListener("DOMContentLoaded", function() {
    fetch('/category/all')
        .then(response => response.json())
        .then(data => {
            const categoryList = document.getElementById('categoryList');
            data.forEach(category => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><a href="/category/${category.id}">${category.categoryName}</td>
                `;
                categoryList.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching category data:', error));
});
