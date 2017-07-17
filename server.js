const fs = require('fs');
const http = require('http');
const url = require('url');
const querystring = require('querystring');
const todos = [
    {
        id: Math.random() + '',
        message: 'do morning exercises.',
        completed: false
    },
    {
        id: Math.random() + '',
        message: 'buy gift for mom',
        completed: false
    },
    {
        id: Math.random() + '',
        message: 'do homework.',
        completed: false
    }
];
const findIdIndex = (index, id) => {
    if (index >= todos.length)
        return null;
    else {
        let current = todos[index];
        if (id === current.id)
            return index;
        else
            return findIdIndex(index + 1, id);
    }
};
const serverCallback = (req, res) => {
    const filePath = './public' + req.url;
    fs.readFile(filePath, (err, data) => {
        if (err) {
            res.statusCode = 404;
            res.end('Page not found');
        } else {
            res.statusCode = 200;
            res.end(data);
        }
        return undefined;
    });
    const parsedUrl = url.parse(req.url);
    const parsedQuery = querystring.parse(parsedUrl.query);
    const method = req.method;
    if (method === 'GET')
        if (req.url.indexOf('/todos') === 0) {
            let localTodos = todos;
            res.setHeader('Content-Type', 'application/json');
            if (parsedQuery.searchtext) {
                localTodos = localTodos.filter(obj => {
                    return obj.message.toLowerCase().indexOf(parsedQuery.searchtext.toLowerCase()) >= 0;
                });
            }
            return res.end(JSON.stringify(localTodos));
        }
    if (method === 'POST')
        if (req.url.indexOf('/todos') === 0) {
            let body = '';
            req.on('data', chunk => {
                body = body + chunk;
                return undefined;
            });
            req.on('end', () => {
                let jsonObj = JSON.parse(body);
                if (jsonObj.message) {
                    jsonObj.id = Math.random() + '';
                    todos[todos.length] = jsonObj;
                    res.setHeader('Content-Type', 'application/json');
                    return res.end(JSON.stringify(todos));
                }
            });
            return undefined;
        }
    if (method === 'DELETE')
        if (req.url.indexOf('/todos/') === 0) {
            console.log(req.url);
            let id = req.url.substr(7), index = findIdIndex(0, id);
            if (index !== null) {
                todos.splice(index, 1);
                res.statusCode = 200;
                return res.end('Successfully removed');
            } else {
                res.statusCode = 404;
                return res.end('Data was not found');
            }
        }
    if (method === 'PUT')
        if (req.url.indexOf('/todos/') === 0) {
            let id = req.url.substr(7), index = findIdIndex(0, id);
            if (index !== null) {
                let update = todos[index];
                update.completed = !update.completed;
                res.statusCode = 200;
                return res.end('Successfully updated');
            } else {
                res.statusCode = 404;
                return res.end('Data was not found and can therefore not be updated');
            }
        }
};
const server = http.createServer(serverCallback);
server.listen(3001);
