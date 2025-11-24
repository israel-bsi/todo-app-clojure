(ns todo.backend.handler
  "Este namespace define nossas 'funções de resposta' (Handlers)."
  (:require [todo.backend.db :as db]
            [clojure.string :as str]))

(defn hello-handler
  "Nosso primeiro handler. Ele apenas diz 'Olá, Mundo!'"
  [_request]
  {:status 200
   :body "Hello, World!"
  })

(defn list-todos-handler
  "Handler para a rota GET /api/todos."
  [_request]
  {:status 200
   :body {:todos (db/get-all-todos)}})

(defn create-todo-handler
  "Handler para a rota POST /api/todos."
  [request]
  (let [todo-data (:body request)
        title (:title todo-data)]

    (if (and title (not (str/blank? title)))
      (let [new-todo (db/create-todo todo-data)]
        {:status 201
         :body new-todo})
      
      {:status 400
       :body {:error "O 'título' (title) é obrigatório"}})))

(defn toggle-todo-handler
  "Handler para 'alternar' o status de um todo."
  [request]
  (let [id (-> request :path-params :id Integer/parseInt)]
    (if-let [updated-todo (db/toggle-todo! id)]
      {:status 200 :body updated-todo}
      {:status 404 :body {:error "Todo não encontrado"}})))

(defn delete-todo-handler
  "Handler para deletar um todo pelo ID."
  [request]
  (let [id (-> request :path-params :id)]
    (try
      (db/delete-todo! id)
      {:status 200
       :body {:message (str "Todo com ID " id " deletado com sucesso.")}}
      (catch Exception e
        {:status 500
         :body {:error "Erro ao deletar o todo."}}))))