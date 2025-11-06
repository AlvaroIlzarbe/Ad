// Constants for Reader module
const READER_API_BASE_URL = "http://localhost:8080";
const READER_ENDPOINTS = {
  ARTICLE_READERS: (articleId) => `${READER_API_BASE_URL}/articles/${articleId}/readers`,
  ALL_READERS: `${READER_API_BASE_URL}/readers`,
  READER_DETAILS: (readerId) => `${READER_API_BASE_URL}/articles/${readerId}/reader`,
  ARTICLE_READER_CRUD: `${READER_API_BASE_URL}/articles/readers`
};

// DOM Elements for Reader module
const readerElements = {
  infoContainer: document.getElementById("infoContainer"),
  addRatingForm: document.getElementById("addReadArticleForm"),
  updateRatingForm: document.getElementById("updateReadArticleForm"),
  modals: {
    addRating: new bootstrap.Modal('#addReadArticleModal'),
    updateRating: new bootstrap.Modal('#updateReadArticleModal')
  }
};

// Utility function specific for Reader module
const fetchReaderData = async (url, options = {}) => {
  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    });

    if (!response.ok) {
      const errorData = await response.text();
      throw new Error(errorData || 'Network response was not ok');
    }

    return response.json();
  } catch (error) {
    console.error(`Reader fetch error for ${url}:`, error);
    throw error;
  }
};

// Reader Functions
const getArticleInfo = async (button) => {
  const row = button.closest('tr');
  const articleId = row.querySelector('td:first-child').textContent.trim();
  const articleName = row.querySelector('td:nth-child(2)').textContent.trim();

  try {
    const readers = await fetchReaderData(READER_ENDPOINTS.ARTICLE_READERS(articleId));
    renderReadersTable(articleId, articleName, readers);
  } catch (error) {
    console.error('Error fetching readers:', error);
    showReaderError("Failed to fetch readers data");
  }
};

const renderReadersTable = (articleId, articleName, readers) => {
  readerElements.infoContainer.innerHTML = '';

  if (!readers || readers.length === 0) {
    readerElements.infoContainer.innerHTML = `
      <div class="alert alert-info">
        No readers found for this article
        <button class="btn btn-info btn-sm ms-2" 
                onclick="showAddRatingModal('${articleId}')">
          Add Rating
        </button>
      </div>
    `;
    return;
  }

  const tableHTML = `
    <table id="readersTable" class="table table-bordered table-striped" data-article-id="${articleId}">
      <thead>
        <tr>
          <th colspan="7">${articleName}'s Readers</th>
        </tr>
        <tr>
          <th hidden>ID</th>
          <th>Name</th>
          <th>DOB</th>
          <th>Rating</th>
          <th>Subscriptions</th>
          <th colspan="2">
            <button class="btn btn-info btn-sm" 
                    onclick="showAddRatingModal('${articleId}')">
              Add Rating
            </button>
          </th>
        </tr>
      </thead>
      <tbody>
        ${readers.map(reader => `
          <tr>
            <td hidden>${reader.idReader}</td>
            <td>${reader.nameReader}</td>
            <td>${reader.dobReader}</td>
            <td>${reader.rating}</td>
            <td>${formatSubscriptions(reader.subscriptionsReader)}</td>
            <td>
              <button class="btn btn-danger btn-sm" 
                      onclick="deleteReaderRating(this, ${reader.idReader}, ${articleId})">
                Delete
              </button>
            </td>
            <td>
              <button class="btn btn-primary btn-sm" 
                      onclick="showUpdateRatingModal(this, ${reader.idReader}, '${reader.nameReader}', ${reader.rating}, ${articleId})">
                Update
              </button>
            </td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;

  readerElements.infoContainer.innerHTML = tableHTML;
};

const formatSubscriptions = (subscriptions) => {
  return subscriptions?.join(', ') || '';
};

const deleteReaderRating = async (button, readerId, articleId) => {
  if (!confirm('Are you sure you want to delete this rating?')) return;

  const row = button.closest('tr');
  
  try {
    const response = await fetch(READER_ENDPOINTS.ARTICLE_READER_CRUD, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json' // ← ¡Header añadido!
      },
      body: JSON.stringify({
        idArticle: articleId,
        idReader: readerId,
        rating: 0
      })
    });

    if (!response.ok) { // ← Verificación añadida
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    // Mover el remove() DESPUÉS de confirmar el éxito
    row.remove();
    
    // Mostrar estado vacío si es necesario
    if (!document.querySelector("#readersTable tbody tr")) {
      readerElements.infoContainer.innerHTML = `
        <div class="alert alert-info">
          No readers found for this article
          <button class="btn btn-info btn-sm ms-2" 
                  onclick="showAddRatingModal('${articleId}')">
            Add Rating
          </button>
        </div>
      `;
    }
  } catch (error) {
    console.error('Error deleting rating:', error);
    showReaderError("Failed to delete rating. Please refresh the page.");
    // IMPORTANTE: Aquí podrías volver a cargar los datos desde el servidor
    // para restaurar el estado si falló el DELETE
  }
};

const updateRating = async (event) => {
  event.preventDefault();

  const formData = {
    idArticle: document.getElementById("updateArticleId").value,
    idReader: document.getElementById("updateReaderId").value,
    rating: document.getElementById("updateRating").value
  };

  try {
    const response = await fetch(READER_ENDPOINTS.ARTICLE_READER_CRUD, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData)
    });

    if (!response.ok) { // Verificación explícita
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update rating');
    }

    // Actualizar tabla
    const rows = document.querySelectorAll("#readersTable tbody tr");
    for (const row of rows) {
      if (row.cells[0].textContent == formData.idReader) {
        row.cells[3].textContent = formData.rating;
        break;
      }
    }

    // Cerrar modal con jQuery (como en el código funcional)
    $('#updateReadArticleModal').modal('hide');
    
  } catch (error) {
    console.error('Error updating rating:', error);
    showReaderError(error.message);
  }
};

const addRating = async (event) => {
  event.preventDefault();

  const formData = {
    idArticle: document.getElementById("addRAArticle").value,
    idReader: document.getElementById("addRAReaders").value,
    rating: document.getElementById("addRARating").value
  };
 

  try {
    const result = await fetchReaderData(READER_ENDPOINTS.ARTICLE_READER_CRUD, {
      method: 'POST',
      body: JSON.stringify(formData)
    });

    if (result === -2) {
      alert("This reader already has a rating for this article.");
      readerElements.modals.addRating.hide();
      return;
    }

    // Refresh the readers table
    const readers = await fetchReaderData(READER_ENDPOINTS.ARTICLE_READERS(formData.idArticle));
    
    renderReadersTable(formData.idArticle, '', readers);

    
    readerElements.addRatingForm.reset();
    readerElements.modals.addRating.hide();
  } catch (error) {
    console.error('Error adding rating:', error);
    showReaderError("Failed to add rating");
  }
};

const showAddRatingModal = (articleId) => {
  document.getElementById("addRAArticle").value = articleId;
  fillReadersCombo();
  readerElements.modals.addRating.show();
};

const showUpdateRatingModal = (button, readerId, readerName, currentRating, articleId) => {
  document.getElementById("updateReaderId").value = readerId;
  document.getElementById("updateArticleId").value = articleId;
  document.getElementById("updateReaderName").value = readerName;
  document.getElementById("updateRating").value = currentRating;
  
  readerElements.modals.updateRating.show();
};

const fillReadersCombo = async () => {
  try {
    const readers = await fetchReaderData(READER_ENDPOINTS.ALL_READERS);
    const select = document.getElementById("addRAReaders");
    
    select.innerHTML = readers.map(reader => 
      `<option value="${reader.idReader}">${reader.nameReader}</option>`
    ).join('');
  } catch (error) {
    console.error('Error fetching readers:', error);
    showReaderError("Failed to load readers");
  }
};

const showReaderError = (message) => {
  const alert = document.createElement('div');
  alert.className = 'alert alert-danger';
  alert.textContent = message;
  
  readerElements.infoContainer.innerHTML = '';
  readerElements.infoContainer.appendChild(alert);
  
  setTimeout(() => alert.remove(), 5000);
};

// Initialize event listeners for Reader module
document.addEventListener("DOMContentLoaded", () => {
  if (readerElements.updateRatingForm) {
    readerElements.updateRatingForm.addEventListener("submit", updateRating);
  }
  if (readerElements.addRatingForm) {
    readerElements.addRatingForm.addEventListener("submit", addRating);
  }
});

// Public API for HTML onclick handlers
window.showAddRatingModal = showAddRatingModal;
window.showUpdateRatingModal = showUpdateRatingModal;
window.deleteReaderRating = deleteReaderRating;