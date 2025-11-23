(ns todo.backend.core
  (:require [ring.adapter.jetty :as jetty]        ;; 1. O software do Servidor (Jetty)
            [reitit.ring :as ring]            ;; 2. O Roteador (Reitit)
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [todo.backend.handler :as handler]
            [todo.backend.db :as db])  ;; 3. Nossas funções (handler.clj)
  
  ;; 4. IMPORTANTE: Para o 'clj -M:run' funcionar
  (:gen-class)) 

;; --- 1. Definição das Rotas ---
;; Criamos um roteador Reitit.
;; Dizemos a ele que a URL "/api/hello", quando acessada
;; com o método :get, deve executar nossa função handler/hello-handler.
(def app-routes
  (ring/router
   ;; Aninhamos tudo sob "/api"
   ["/api"
    ["/hello" {:get {:handler handler/hello-handler}}]
    
    ["/todos"
     {:get {:handler handler/list-todos-handler}   ;; GET chama 'list'
      :post {:handler handler/create-todo-handler}}]] ;; POST chama 'create'
   ))

(def app
  (ring/ring-handler
   app-routes
   (ring/create-default-handler)
   {:middleware [;; --- ADICIONE ESTE VETOR ---
                 ;; Ele deve ser o primeiro da lista
                 [wrap-cors :access-control-allow-origin [#"http://localhost:8000"]
                            :access-control-allow-methods [:get :post :put :delete]]

                 ;; O resto dos middlewares...
                 wrap-json-response
                 [wrap-json-body {:keywords? true}]
                 wrap-params
                 wrap-keyword-params
                ]}))

;; --- 3. Função para Iniciar o Servidor ---
;; Uma função auxiliar que inicia o Jetty.
(defn start-server [port]
  (println (str "Servidor iniciado na porta " port))
  ;; #'app é a forma de passar a "var" da nossa app para o Jetty
  ;; :join? false é importante para não bloquear o terminal.
  (jetty/run-jetty #'app {:port port :join? false}))

;; --- 4. Ponto de Entrada Principal (-main) ---
;; Esta é a função que o alias :run (do deps.edn) procura.
;; CÓDIGO CORRIGIDO
(defn -main [& args]
  (let [port (Integer/parseInt (or (first args) "3000"))]
    ;; --- ADICIONE ESTA LINHA ---
    (db/initialize-database!) ;; Garante que a tabela exista
    ;; --------------------------
    (start-server port)))