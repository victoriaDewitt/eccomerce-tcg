// ====== Variables modal login ======
const btnLogin = document.getElementById('btn-login');
const modal = document.getElementById('modal-login');
const modalClose = document.getElementById('modal-close');

btnLogin.addEventListener('click', () => modal.style.display = 'block');
modalClose.addEventListener('click', () => modal.style.display = 'none');
window.addEventListener('click', e => { if(e.target === modal) modal.style.display = 'none'; });

// ====== Animación slide-in ======
function checkSlide() {
  const elements = document.querySelectorAll('.slide-in');
  elements.forEach(el => {
    const rect = el.getBoundingClientRect();
    if (rect.top < window.innerHeight && rect.bottom > 0) el.classList.add('active');
  });
}
window.addEventListener('scroll', checkSlide);
window.addEventListener('load', checkSlide);

// ====== Renderizar productos con imágenes y carrito ======
function renderProducts(products) {
  const container = document.getElementById('product-list');
  if (!container) return;

  container.innerHTML = '';

  if (!products || products.length === 0) {
    container.innerHTML = '<p>No hay productos en esta categoría.</p>';
    return;
  }

  products.forEach(product => {
    const imagePng = `/img/${product.category}/${product.id}.png`;
    const imageJpg = `/img/${product.category}/${product.id}.jpg`;
    const fallbackImage = `/img/default-product.png`;

    const card = document.createElement('div');
    card.classList.add('product-card');
    card.innerHTML = `
      <div class="product-image">
        <img src="${imagePng}" alt="${product.name}"
             onerror="this.onerror=null; this.src='${imageJpg}'; this.onerror=function(){this.src='${fallbackImage}'};" 
             class="slide-in"/>
      </div>
      <div class="product-info">
        <h3>${product.name}</h3>
        <p>${product.description}</p>
        <p class="price">$${product.price.toFixed(2)}</p>
        <button class="add-to-cart" onclick="addToCart(${product.id})">Add to cart</button>
      </div>
    `;
    container.appendChild(card);
  });

  // Activar animación después de renderizar
  const slideElements = container.querySelectorAll('.slide-in');
  slideElements.forEach(el => {
    void el.offsetWidth; // fuerza reflow
    el.classList.add('active');
  });
}

// ====== Cargar productos ======
function loadAllProducts() {
  fetch('http://localhost:8081/api/products')
    .then(res => res.json())
    .then(products => renderProducts(products))
    .catch(() => {
      const container = document.getElementById('product-list');
      if (container) container.innerHTML = '<p>Error al cargar productos.</p>';
    });
}

function loadCategory(category) {
  fetch(`http://localhost:8081/api/products/category/${category}`)
    .then(res => res.json())
    .then(products => renderProducts(products))
    .catch(() => {
      const container = document.getElementById('product-list');
      if (container) container.innerHTML = '<p>Error al cargar productos.</p>';
    });
}

// ====== Inicializar productos según URL ======
window.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);
  const category = params.get('category');

  if (category) loadCategory(category);
  else loadAllProducts();
});

// ====== Gestión de usuario y carrito ======
function getUserId() {
  return sessionStorage.getItem('userId');
}

function updateUI() {
  const username = sessionStorage.getItem('username');
  if (username) btnLogin.textContent = `Hi, ${username}`;
  else btnLogin.textContent = 'Sign In';
}

// Cargar carrito según usuario
async function loadCart() {
  const userId = sessionStorage.getItem('userId');
  if (!userId) return;

  try {
    const res = await fetch(`http://localhost:8081/api/cart/${userId}`);
    const items = await res.json();
    renderCart(items);
  } catch (err) {
    console.error('Error al cargar carrito:', err);
  }
}

// Renderizar carrito
function renderCart(items) {
  const container = document.getElementById('cart-container');
  if (!container) return;

  container.innerHTML = '';
  if (!items || items.length === 0) {
    container.innerHTML = '<p>The cart is empty.</p>';
    return;
  }

  let total = 0;
  items.forEach(item => {
    const itemTotal = item.product.price * item.quantity;
    total += itemTotal;

    const div = document.createElement('div');
    div.className = 'cart-item';
    div.innerHTML = `
      <p>${item.product.name} x ${item.quantity} - $${itemTotal.toFixed(2)}</p>
      <button onclick="removeFromCart(${item.product.id})">Eliminar</button>
    `;
    container.appendChild(div);
  });

  // Mostrar total
  const totalDiv = document.createElement('div');
  totalDiv.className = 'cart-total';
  totalDiv.innerHTML = `<strong>Total: $${total.toFixed(2)}</strong>`;
  container.appendChild(totalDiv);
}

// Agregar al carrito
async function addToCart(productId, quantity = 1) {
  const userId = getUserId();
  if (!userId) return alert('Inicia sesión primero.');

  try {
    await fetch(`http://localhost:8081/api/cart/add?userId=${userId}&productId=${productId}&quantity=${quantity}`, { method: 'POST' });
    loadCart();
  } catch (err) {
    console.error('Error al agregar al carrito:', err);
  }
}

// Eliminar del carrito
async function removeFromCart(productId) {
  const userId = getUserId();
  if (!userId) return;

  try {
    await fetch(`http://localhost:8081/api/cart/remove?userId=${userId}&productId=${productId}`, { method: 'DELETE' });
    loadCart();
  } catch (err) {
    console.error('Error al eliminar del carrito:', err);
  }
}

// ====== Inicializar UI y carrito al cargar la página ======
window.addEventListener('DOMContentLoaded', () => {
  updateUI();
  loadCart();
});
