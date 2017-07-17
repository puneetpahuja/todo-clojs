(def fs (require "fs"))
(def http (require "http"))
(def url (require "url"))
(def querystring (require "querystring"))

(def todos [{id (+ (Math.random) "")
             message "do morning exercises."
             completed false}
            {id (+ (Math.random) "")
             message "buy gift for mom"
             completed false}
            {id (+ (Math.random) "")
             message "do homework."
             completed false}])

(defn findIdIndex [index id]
  (if (>= index todos.length)
    nil
    (do
      (let current todos[index])
      (if (= id current.id)
        index
        (findIdIndex (+ index 1) id)))))

(defn serverCallback [req res]
  (def filePath (+ "./public" req.url))
  (.readFile fs
             filePath
             (fn [err data]
               (if err
                 (do
                   (assign res.statusCode 404)
                   (.end res "Page not found"))
                 (do
                   (assign res.statusCode 200)
                   (.end res data)))
               undefined))
  (def parsedUrl (.parse url req.url))
  (def parsedQuery (.parse querystring parsedUrl.query))
  (def method req.method)
  (if (= method "GET")
    (if (= (.indexOf req.url "/todos") 0)
      (do (let localTodos todos)
          (.setHeader res "Content-Type" "application/json")
          (if parsedQuery.searchtext
            (do
              (assign localTodos (localTodos.filter (fn [obj]
                                                      (>= (.indexOf (.toLowerCase obj.message) (.toLowerCase parsedQuery.searchtext)) 0))))))
          (return (.end res (.stringify JSON localTodos))))))
  (if (= method "POST")
    (if (= (.indexOf req.url "/todos") 0)
      (do (let body "")
          (.on req "data" (fn [chunk]
                            (assign body (+ body chunk))
                            undefined))
          (.on req "end" (fn []
                           (let jsonObj (.parse JSON body))
                           (if jsonObj.message
                             (do (assign jsonObj.id (+ (Math.random) ""))
                                 (assign todos[todos.length] jsonObj)
                                 (.setHeader res "Content-Type" "application/json")
                                 (.end res (JSON.stringify todos))))))
          (return undefined))))
  (if (= method "DELETE")
    (if (= (.indexOf req.url "/todos/") 0)
      (do (console.log req.url)
          (let id (.substr req.url 7) index (findIdIndex 0 id))
          (if (!== index nil)
            (do (.splice todos index 1)
                (assign res.statusCode 200)
                (return (.end res "Successfully removed")))
            (do (assign res.statusCode 404)
                (return (.end res "Data was not found")))))))
  (if (= method "PUT")
    (if (= (req.url.indexOf "/todos/") 0)
      (do (let id (.substr req.url 7)
               index (findIdIndex 0 id))
          (if (!== index nil)
            (do (let update todos[index])
                (assign update.completed (not update.completed))
                (assign res.statusCode 200)
                (.end res "Successfully updated"))
            (do (assign res.statusCode 404)
                (.end res "Data was not found and can therefore not be updated")))))))

(def server (.createServer http serverCallback))

(.listen server 3001)
