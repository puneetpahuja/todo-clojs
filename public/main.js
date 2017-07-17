const todoListEl = $('#my-todo-list');
const drawList = () => {
    const searchText = $('#searchbox').val();
    $.ajax({
        url: '/todos',
        type: 'get',
        dataType: 'json',
        data: { searchtext: searchText },
        success: todos => {
            $('#my-todo-list').html('');
            todos.forEach(todoItem => {
                const li = $('<li id=' + todoItem.id + '><input type = checkbox>' + todoItem.message + '<button class=\'delete\'>X</button></li>');
                const input = li.find('input');
                input.prop('checked', todoItem.completed);
                $('#my-todo-list').append(li);
                return undefined;
            });
            return undefined;
        },
        error: data => {
            alert('Error searching');
            return undefined;
        }
    });
    return undefined;
};
const addToDo = () => {
    const saveText = $('#savebox').val();
    $('#savebox').val('');
    $.ajax({
        url: '/todos',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify({
            message: saveText,
            completed: false
        }),
        success: todos => {
            drawList();
            return undefined;
        },
        error: data => {
            alert('Error');
            return undefined;
        }
    });
    return undefined;
};
const deleteItem = todoItemID => {
    $.ajax({
        url: '/todos/' + todoItemID,
        type: 'delete',
        success: data => {
            drawList();
            return undefined;
        },
        error: data => {
            alert('Error deleting the item');
            return undefined;
        }
    });
    return undefined;
};
const updateList = todoItemID => {
    $.ajax({
        url: '/todos/' + todoItemID,
        type: 'put',
        success: data => {
            drawList();
            return undefined;
        },
        error: data => {
            alert('Error updating the item');
            return undefined;
        }
    });
    return undefined;
};
drawList();
$('#searchbut').on('click', () => {
    drawList();
    return undefined;
});
$('#savebut').on('click', () => {
    addToDo();
    return undefined;
});
$('ul').on('click', '.delete', function (event) {
    deleteItem($(this).parent().attr('id'));
    return undefined;
});
$('ul').on('change', 'input', function (event) {
    updateList($(this).parent().attr('id'));
    return undefined;
});
