// Constants
const ARTICLE_API_BASE_URL = "http://localhost:8080";
const ARTICLE_ENDPOINTS = {
  ARTICLES: `${ARTICLE_API_BASE_URL}/articles`,
  TYPES: `${ARTICLE_API_BASE_URL}/types`,
  NEWSPAPERS: `${ARTICLE_API_BASE_URL}/newspapers`
};

// DOM Elements
const articleElements = {
  articleTable: document.getElementById("articleTable"),
  addButton: document.getElementById("addButton"),
  addArticleForm: document.getElementById("addarticleForm"),
  updateArticleForm: document.getElementById("updatearticleForm"),
  infoContainer: document.getElementById("infoContainer"),
  modals: {
    add: new bootstrap.Modal('#addarticleModal'),
    update: new bootstrap.Modal('#updatearticleModal')
  }
};

// Utility Functions
const fetchArticleData = async (url, options = {}) => {
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
    console.error(`Fetch error for ${url}:`, error);
    throw error;
  }
};

// Article Functions
const getAllArticles = async () => {
  try {
    const articles = await fetchArticleData(ARTICLE_ENDPOINTS.ARTICLES);
    renderArticlesTable(articles);
  } catch (error) {
    console.error('Error fetching articles:', error);
    showArticleError("Failed to load articles");
  }
};

const renderArticlesTable = (articles) => {
  const tableBody = articleElements.articleTable.querySelector('tbody');
  tableBody.innerHTML = '';
  
  articles.forEach(article => {
    tableBody.appendChild(createArticleRow(article));
  });
};

const createArticleRow = (article) => {
  const row = document.createElement('tr');
  
  row.innerHTML = `
    <td>${article.id}</td>
    <td>${article.name}</td>
    <td>${article.typeUI?.name || ''}</td>
    <td>${article.npaperId}</td>
    <td>${article.avgRating || 0}</td>
    <td><button class="btn btn-info btn-sm">Get Info</button></td>
    <td><button class="btn btn-danger btn-sm">Delete</button></td>
    <td><button class="btn btn-primary btn-sm ms-2">Update</button></td>
  `;
  
  // Add event listeners
  const buttons = row.querySelectorAll('button');
  buttons[0].addEventListener('click', () => getArticleInfo(row, article.id));
  buttons[1].addEventListener('click', () => deleteArticle(row, article.id));
  buttons[2].addEventListener('click', () => showUpdateModal(article));
  
  return row;
};

const deleteArticle = async (row, articleId) => {
  try {
    // First try without confirmation
    const response = await fetch(`${ARTICLE_ENDPOINTS.ARTICLES}/${articleId}?confirm=false`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (response.status === 204) {
      // Successfully deleted
      row.remove();
      articleElements.infoContainer.innerHTML = '';
      return;
    }

    if (response.status === 409) {
      // Article has associated readers
      const message = await response.text();
      const confirmDelete = confirm(message);
      
      if (confirmDelete) {
        // Try again with confirmation
        await fetch(`${ARTICLE_ENDPOINTS.ARTICLES}/${articleId}?confirm=true`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        row.remove();
        articleElements.infoContainer.innerHTML = '';
      }
    } else {
      throw new Error('Failed to delete article');
    }
  } catch (error) {
    console.error('Error deleting article:', error);
    showArticleError("Failed to delete article");
  }
};

const addArticle = async (event) => {
  event.preventDefault();
  
  const formData = {
    name: document.getElementById("addarticleName").value,
    typeUI: {
      id: document.getElementById("addTypeNames").value,
      name: document.getElementById("addTypeNames").options[document.getElementById("addTypeNames").selectedIndex].text
    },
    npaperId: document.getElementById("addNPaperNames").value,
    avgRating: 0
  };

  try {
    const newArticle = await fetchArticleData(ARTICLE_ENDPOINTS.ARTICLES, {
      method: 'POST',
      body: JSON.stringify(formData)
    });

    // Ensure we have the ID
    const articleWithId = { ...formData, id: newArticle.id || newArticle };
    
    articleElements.articleTable.querySelector('tbody').appendChild(
      createArticleRow(articleWithId)
    );
    
    articleElements.addArticleForm.reset();
    articleElements.modals.add.hide();
  } catch (error) {
    showArticleError("Failed to add article");
  }
};

const updateArticle = async (event) => {
  event.preventDefault();
  
  const formData = {
    id: document.getElementById("updatearticleId").value,
    name: document.getElementById("updatearticleName").value,
    typeUI: {
      id: document.getElementById("updateTypeNames").value,
      name: document.getElementById("updateTypeNames").options[document.getElementById("updateTypeNames").selectedIndex].text
    },
    npaperId: document.getElementById("updateNPaperNames").value,
    avgRating: 0
  };

  try {
    const response = await fetch(ARTICLE_ENDPOINTS.ARTICLES, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json', // Añade headers
      },
      body: JSON.stringify(formData)
    });

    if (!response.ok) { // Verifica si la respuesta es exitosa
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    modifyArticleInTable(formData);
    $('#updatearticleModal').modal('hide'); // Usa el mismo método que funciona
  } catch (error) {
    console.error('Error updating article:', error);
    showArticleError("Failed to update article: " + error.message);
  }
};

const modifyArticleInTable = (article) => {
  const rows = articleElements.articleTable.querySelectorAll('tbody tr');
  
  for (const row of rows) {
    if (row.cells[0].textContent == article.id) {
      row.cells[1].textContent = article.name;
      row.cells[2].textContent = article.typeUI.name;
      row.cells[3].textContent = article.npaperId;
      break;
    }
  }
};

const showUpdateModal = async (article) => {
  document.getElementById("updatearticleId").value = article.id;
  document.getElementById("updatearticleName").value = article.name;
  
  try {
    await fillArticleSelect('updateTypeNames', ARTICLE_ENDPOINTS.TYPES, article.typeUI?.id);
    await fillArticleSelect('updateNPaperNames', ARTICLE_ENDPOINTS.NEWSPAPERS, article.npaperId);
    
    articleElements.modals.update.show();
  } catch (error) {
    showArticleError("Failed to load data for update");
  }
};

// Combo Box Functions
const fillArticleSelect = async (selectId, endpoint, selectedValue = null) => {
  try {
    const data = await fetchArticleData(endpoint);
    const select = document.getElementById(selectId);
    
    select.innerHTML = data.map(item => 
      `<option value="${item.id}" ${item.id == selectedValue ? 'selected' : ''}>
        ${item.name}
      </option>`
    ).join('');
  } catch (error) {
    console.error(`Error filling ${selectId}:`, error);
    throw error;
  }
};

// Error Handling
const showArticleError = (message) => {
  const alert = document.createElement('div');
  alert.className = 'alert alert-danger';
  alert.textContent = message;
  
  articleElements.infoContainer.innerHTML = '';
  articleElements.infoContainer.appendChild(alert);
  
  setTimeout(() => alert.remove(), 5000);
};

// Initialization
const initializeArticleModule = async () => {
  try {
    await getAllArticles();
    
    // Setup add button
    articleElements.addButton.addEventListener("click", async () => {
      await Promise.all([
        fillArticleSelect('addTypeNames', ARTICLE_ENDPOINTS.TYPES),
        fillArticleSelect('addNPaperNames', ARTICLE_ENDPOINTS.NEWSPAPERS)
      ]);
      articleElements.modals.add.show();
    });
    
    // Form submissions
    articleElements.addArticleForm.addEventListener("submit", addArticle);
    articleElements.updateArticleForm.addEventListener("submit", updateArticle);
    
  } catch (error) {
    showArticleError("Failed to initialize article module");
  }
};

// Start the module when DOM is ready
document.addEventListener("DOMContentLoaded", initializeArticleModule);