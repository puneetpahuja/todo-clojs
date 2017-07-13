(def todoListE1 ($ "#my-todo-list"))

(defn drawList []
  (let temp ($ "#searchbox"))
  (def searchText (temp.val))
  ($.ajax {url "/todos"
           type "get"
           dataType "json"
           data {searchtext searchText}
           success (fn [todos]
                     (let temp1 ($ "#my-todo-list"))
                     (temp1.html "")
                     (todos.forEach (fn [todoitem]
                                      (def li ($ (+ "<li id"
                                                    todoItem.id
                                                    "><input type = checkbox>"
                                                    todoItem.message
                                                    "<button class='delete'>X</button></li>")))
                                      (def input (li.find "input"))
                                      (input.prop "checked" todoItem.completed)
                                      (let temp2 ($ "#my-todo-list"))
                                      (temp2.append li))))
           error (fn [data]
                   (alert "Error searching"))}))

(defn addToDo []
  (let temp ($ "#savebox"))
  (def saveText (temp.val))
  (let temp1 ($ "#savebox"))
  (temp1.val "")
  ($.ajax {url "/todos"
           type "post"
           dataType "json"
           data (JSON.stringify {message saveText
                                 completed false})
           success (fn [todos]
                     (drawList))
           error (fn [data]
                   (alert "Error"))}))

(defn deleteItem [todoItemID]
  ($.ajax {url (+ "/todos/" todoItemID)
           type "delete"
           success (fn [data]
                     (drawList))
           error (fn [data]
                   (alert "Error deleting the item"))}))

(defn updateList [todoItemID]
  ($.ajax {url (+ "/todos/" todoItemID)
           type "put"
           success (fn [data]
                     (drawList))
           error (fn [data]
                   (alert "Error updating the item"))}))

(drawList)

(def gtemp ($ "#searchbut"))
(gtemp.on "click" (fn []
                    (drawList)))

(def gtemp1 ($ "#savebut"))
(gtemp1.on "click" (fn []
                     (addToDo)))

(def gtemp2 ($ "ul"))
(gtemp2.on "click"
           ".delete"
           (fn [event]
             (let temp ($ this)
                  temp2 (temp.parent))
             (deleteItem (temp2.attr "id"))))

(def gtemp3 ($ "ul"))
(gtemp3.on "change"
           "input"
           (fn [event]
             (let temp ($ this)
                  temp2 (temp.parent))
             (updateList (temp2.attr "id"))))
