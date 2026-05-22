document.addEventListener('DOMContentLoaded', function () {
    const menuGrid = document.querySelector('.menu-grid');
    const orderList = document.getElementById('order-items');
    const subtotalEl = document.getElementById('subtotal');
    const taxEl = document.getElementById('tax');
    const totalEl = document.getElementById('total');
    const chargeToRoomBtn = document.getElementById('charge-to-room-btn');
    const orderedItemsInput = document.getElementById('ordered-items-input');

    let currentOrder = [];

    menuGrid.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('add-to-order-btn')) {
            const card = e.target.closest('.menu-item-card');
            const name = card.dataset.name;
            const price = parseFloat(card.dataset.price);
            const id = parseInt(card.dataset.id);

            addToOrder({ id, name, price });
        }
    });

    chargeToRoomBtn.addEventListener('click', function() {
        const itemIds = currentOrder.map(item => item.id);
        orderedItemsInput.value = JSON.stringify(itemIds);
    });

    function addToOrder(item) {
        currentOrder.push(item);
        renderOrder();
    }

    function renderOrder() {
        orderList.innerHTML = '';
        let subtotal = 0;
        currentOrder.forEach(item => {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex justify-content-between';
            li.innerHTML = `<span>${item.name}</span><strong>$${item.price.toFixed(2)}</strong>`;
            orderList.appendChild(li);
            subtotal += item.price;
        });

        const tax = subtotal * 0.10;
        const total = subtotal + tax;

        subtotalEl.innerText = `$${subtotal.toFixed(2)}`;
        taxEl.innerText = `$${tax.toFixed(2)}`;
        totalEl.innerText = `$${total.toFixed(2)}`;
    }
});
