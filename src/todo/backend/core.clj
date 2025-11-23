(ns todo.backend.core
  (:require [ring.adapter.jetty :as jetty]        ;; 1. O software do Servidor (Jetty)
            [reitit.ring :as ring]            ;; 2. O Roteador (Reitit)
            [todo.backend.handler :as handler])  ;; 3. Nossas funções (handler.clj)
  
  ;; 4. IMPORTANTE: Para o 'clj -M:run' funcionar
  (:gen-class)) 

;; --- 1. Definição das Rotas ---
;; Criamos um roteador Reitit.
;; Dizemos a ele que a URL "/api/hello", quando acessada
;; com o método :get, deve executar nossa função handler/hello-handler.
(def app-routes
  (ring/router
   [["/api/hello" {:get {:handler handler/hello-handler}}]]))

;; --- 2. Definição da Aplicação (App) ---
;; Criamos o 'app' final, que é a função Ring principal.
(def app
  (ring/ring-handler
   app-routes ;; Nossas rotas
   (ring/create-default-handler) ;; Um handler padrão para 404 (Not Found)
   ))

;; --- 3. Função para Iniciar o Servidor ---
;; Uma função auxiliar que inicia o Jetty.
(defn start-server [port]
  (println (str "Servidor iniciado na porta " port))
  ;; #'app é a forma de passar a "var" da nossa app para o Jetty
  ;; :join? false é importante para não bloquear o terminal.
  (jetty/run-jetty #'app {:port port :join? false}))

;; --- 4. Ponto de Entrada Principal (-main) ---
;; Esta é a função que o alias :run (do deps.edn) procura.
(defn -main [& args]
  ;; Permite que a porta seja passada como argumento (ex: clj -M:run 8080)
  ;; ou usa "3000" como padrão.
  (let [port (Integer/parseInt (or (first args) "3000"))]
    (start-server port)))