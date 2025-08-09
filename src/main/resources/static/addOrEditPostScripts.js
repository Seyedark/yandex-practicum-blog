document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.querySelector('input[type="file"]');
    if (fileInput) {
        fileInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            const maxSize = 1048576;

            if (file && file.size > maxSize) {
                alert('Файл слишком большой! Максимальный размер: 1 МБ');
                e.target.value = '';
            }
        });
    }

    const postForm = document.getElementById('postForm');
    if (postForm) {
        postForm.addEventListener('submit', function(e) {
            const title = this.querySelector('textarea[name="title"]').value.trim();
            const content = this.querySelector('textarea[name="content"]').value.trim();

            if (!title) {
                alert('Название поста не может быть пустым!');
                e.preventDefault();
            } else if (!content) {
                alert('Текст поста не может быть пустым!');
                e.preventDefault();
            }
        });
    }
});