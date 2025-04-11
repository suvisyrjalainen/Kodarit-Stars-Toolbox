/**
 * image_resizer.js
 * This script handles the front-end logic for the Image Resizer Tool.
 * It sets up event listeners to manage form submission, display a loading spinner,
 * update cursor coordinates over the resized image, handle image download, and preset dimension selection.
 */

document.addEventListener('DOMContentLoaded', function() {
  // Retrieve necessary DOM elements
  const form = document.getElementById('resizeForm');
  const spinner = document.getElementById('loadingSpinner');
  const buttonText = document.getElementById('buttonText');
  const downloadBtn = document.getElementById('downloadBtn');
  const widthInput = document.getElementById('width');
  const heightInput = document.getElementById('height');
  const presetSelect = document.getElementById('presetSelect');
  const imageInput = document.getElementById('imageFile');
  const resizeButton = document.getElementById('resizeButton');

  // Initially disable the resize button if no image selected
  resizeButton.disabled = true;

  // Enable the button when a file is selected, disable if none
  imageInput.addEventListener('change', function() {
    if (this.files && this.files.length > 0) {
      resizeButton.disabled = false;
    } else {
      resizeButton.disabled = true;
    }
  });

  // When the form is submitted, show the spinner and update the button text
  form.addEventListener('submit', function() {
    spinner.classList.remove('d-none');
    buttonText.textContent = 'Resizing...';
  });

  const resizedImageOutput = document.getElementById('resizedImageOutput');
  const cursorPosition = document.getElementById('cursorPosition');

  // When the resized image is loaded, hide the spinner
  resizedImageOutput.addEventListener('load', function() {
    spinner.classList.add('d-none');
  });

  // Update cursor position overlay when moving the mouse over the image
  resizedImageOutput.addEventListener('mousemove', function(e) {
    const rect = resizedImageOutput.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    const naturalW = resizedImageOutput.naturalWidth;
    const naturalH = resizedImageOutput.naturalHeight;
    const scaleX = naturalW / resizedImageOutput.clientWidth;
    const scaleY = naturalH / resizedImageOutput.clientHeight;
    cursorPosition.textContent = `X: ${Math.round(x * scaleX)}, Y: ${Math.round(y * scaleY)}`;
  });

  // Handle download button click to download the resized image
  if (downloadBtn) {
    downloadBtn.addEventListener('click', function(e) {
      e.preventDefault();
      if (!resizedImageOutput || !resizedImageOutput.src) return;
      const link = document.createElement('a');
      link.href = resizedImageOutput.src;
      link.download = 'resized-image';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  }

  // Handle preset dimension selection to update width and height inputs
  if (presetSelect) {
    presetSelect.addEventListener('change', function() {
      if (this.value) {
        const [presetWidth, presetHeight] = this.value.split(',').map(val => Number(val.trim()));
        widthInput.value = presetWidth;
        heightInput.value = presetHeight;
        // Only update resolution text if no output image exists
        const resolutionElem = document.getElementById('resolution');
        if (resolutionElem && !(resizedImageOutput && resizedImageOutput.src)) {
          resolutionElem.textContent = `Resolution: ${presetWidth} x ${presetHeight}`;
        }
      }
    });
  }
});
