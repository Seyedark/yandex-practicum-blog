        function editComment(id) {
            event.preventDefault();

            var comment = document.getElementById("comment" + id);
            var newComment = document.createElement("textarea");
            newComment.innerHTML = comment.innerHTML;
            newComment.rows = 3;
            newComment.name = "text";
            newComment.style = "width:100%;";

            newComment.addEventListener('keydown', function(event) {
                if (event.key === 'Enter' && event.ctrlKey) {
                    event.preventDefault();
                    var form = this.closest('form');
                    if (form) {
                        if (!this.value.trim()) {
                            alert('Комментарий не может быть пустым!');
                            return;
                        }
                        form.submit();
                    }
                }
            });

            comment.parentNode.replaceChild(newComment, comment);

            var button = document.getElementById(id);
            button.remove();

            var newButton = document.createElement("button");
            newButton.innerHTML = "&#10003;";
            newButton.style = "float:right;";
            newButton.addEventListener('click', function(e) {
                e.preventDefault();
                if (!newComment.value.trim()) {
                    alert('Комментарий не может быть пустым!');
                    return;
                }
                var form = newComment.closest('form');
                if (form) {
                    form.submit();
                }
            });
            newComment.parentNode.appendChild(newButton);
        }

        function addComment() {
            var button = document.getElementById("addCommentButton");
            if (button) {
                button.remove();
            }

            var form = document.getElementById("addCommentForm");

            var newComment = document.createElement("textarea");
            newComment.rows = 3;
            newComment.name = "text";
            newComment.style = "width:100%;";

            newComment.addEventListener('keydown', function(event) {
                if (event.key === 'Enter' && event.ctrlKey) {
                    event.preventDefault();
                    if (!this.value.trim()) {
                        alert('Комментарий не может быть пустым!');
                        return;
                    }
                    form.submit();
                }
            });

            form.appendChild(newComment);

            var newButton = document.createElement("button");
            newButton.innerHTML = "&#10003;";
            newButton.style = "float:right;";
            newButton.addEventListener('click', function(e) {
                e.preventDefault();
                if (!newComment.value.trim()) {
                    alert('Комментарий не может быть пустым!');
                    return;
                }
                form.submit();
            });
            form.appendChild(newButton);
        }