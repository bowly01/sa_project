// Add this JavaScript code to your HTML (e.g., in a separate .js file or within <script> tags)
document.addEventListener('DOMContentLoaded', function () {
    const deleteLink = document.getElementById('deleteLink');
    const deleteButton = document.getElementById('deleteButton');
    const cancelButton = document.getElementById('cancelButton');

    deleteLink.addEventListener('click', function () {
        const deleteModal = document.querySelector('.delete-modal');
        deleteModal.style.display = 'block';

        const categoryId = deleteLink.getAttribute('data-category-id');

        deleteButton.addEventListener('click', function () {
            // Create a Fetch request to delete the category
            fetch(`/categories/delete/${categoryId}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.status === 200) {
                        // Delete successful, close the modal
                        deleteModal.style.display = 'none';
                        // You can also reload the category list on success
                        window.location.reload();
                    } else {
                        // Handle the error (e.g., show an error message)
                    }
                })
                .catch(error => {
                    // Handle any network or fetch errors
                });
        });


        cancelButton.addEventListener('click', function () {
            deleteModal.style.display = 'none';
        });
    });
});
