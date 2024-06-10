document.addEventListener("DOMContentLoaded", function() {
    fetch('/brand/all')
        .then(response => response.json())
        .then(data => {
            const brandList = document.getElementById('brandList');
            data.forEach(brand => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><a href="/brand/${brand.id}">${brand.brandName}</td>
                `;
                brandList.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching brand data:', error));
});
