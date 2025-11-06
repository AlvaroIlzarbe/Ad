// Constants
const API_URL = "http://localhost:8080/login";
const ERROR_MESSAGES = {
  INVALID_CREDENTIALS: "Invalid username or password",
  NETWORK_ERROR: "Network error. Please try again later."
};

// DOM Elements
const elements = {
  form: document.querySelector("form"), // Agregar un formulario en el HTML sería otra mejora
  username: document.getElementById("username"),
  password: document.getElementById("password"),
  submitButton: document.getElementById("bCheck"),
  errorMessage: document.getElementById("WrongCred")
};

/**
 * Handles the login form submission
 * @param {Event} event - The form submission event
 */
async function handleLogin(event) {
  event.preventDefault(); // Prevenir el comportamiento por defecto del formulario
  
  const { username, password, errorMessage } = elements;
  
  try {
    // Basic client-side validation
    if (!username.value.trim() || !password.value.trim()) {
      errorMessage.textContent = "Please fill in all fields";
      return;
    }
    
    errorMessage.textContent = ""; // Clear previous error
    elements.submitButton.disabled = true; // Disable button during request
    
    const isAuthenticated = await authenticateUser(username.value, password.value);
    
    if (isAuthenticated) {
      window.location.href = "mainPage.html";
    } else {
      errorMessage.textContent = ERROR_MESSAGES.INVALID_CREDENTIALS;
    }
  } catch (error) {
    console.error("Login error:", error);
    errorMessage.textContent = ERROR_MESSAGES.NETWORK_ERROR;
  } finally {
    elements.submitButton.disabled = false;
  }
}

/**
 * Authenticates the user with the server
 * @param {string} username 
 * @param {string} password 
 * @returns {Promise<boolean>} - True if authentication was successful
 */
async function authenticateUser(username, password) {
  const credentials = { username, password };
  
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  });
  
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  const data = await response.text();
  return data === "true";
}

// Event Listeners
document.addEventListener("DOMContentLoaded", () => {
  elements.submitButton.addEventListener("click", handleLogin);
  
  // Optional: Add keyboard support (Enter key to submit)
  elements.password.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
      handleLogin(e);
    }
  });
});

