(def todoListEl ($ "#my-todo-list"))

(defn drawList []
  (def searchText (.val ($ "#searchbox")))
  ($.ajax {url "/todos"
           type "get"
           dataType "json"
           data {searchtext searchText}
           success (fn [todos]
                     (.html ($ "#my-todo-list") "")
                     (.forEach todos (fn [todoItem]
                                       (def li ($ (+ "<li id="
                                                     todoItem.id
                                                     "><input type = checkbox>"
                                                     todoItem.message
                                                     "<button class='delete'>X</button></li>")))
                                       (def input (.find li "input"))
                                       (.prop input "checked" todoItem.completed)
                                       (.append ($ "#my-todo-list") li)
                                       undefined))
                     undefined)
           error (fn [data]
                   (alert "Error searching")
                   undefined)})
  undefined)

(defn addToDo []
  (def saveText (.val ($ "#savebox")))
  (.val ($ "#savebox") "")
  ($.ajax {url "/todos"
           type "post"
           dataType "json"
           data (JSON.stringify {message saveText
                                 completed false})
           success (fn [todos]
                     (drawList)
                     undefined)
           error (fn [data]
                   (alert "Error")
                   undefined)})
  undefined)

(defn deleteItem [todoItemID]
  ($.ajax {url (+ "/todos/" todoItemID)
           type "delete"
           success (fn [data]
                     (drawList)
                     undefined)
           error (fn [data]
                   (alert "Error deleting the item")
                   undefined)})
  undefined)

(defn updateList [todoItemID]
  ($.ajax {url (+ "/todos/" todoItemID)
           type "put"
           success (fn [data]
                     (drawList)
                     undefined)
           error (fn [data]
                   (alert "Error updating the item")
                   undefined)})
  undefined)

(drawList)

(.on ($ "#searchbut") "click" (fn []
                                (drawList)
                                undefined))

(.on ($ "#savebut")  "click" (fn []
                               (addToDo)
                               undefined))

(.on ($ "ul")
     "click"
     ".delete"
     (fn [event]
       (deleteItem (.attr (.parent ($ this)) "id"))
       undefined))

(.on ($ "ul")
     "change"
     "input"
     (fn [event]
       (updateList (.attr (.parent ($ this)) "id"))
       undefined))
