
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

(console.log todos)









