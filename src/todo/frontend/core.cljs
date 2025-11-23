(ns todo.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]))

;; --- 1. Adicione este "Cérebro" Reativo ---
(defonce app-state (r/atom {:next-id 1
                            :input-text ""
                            :todos []}))

;; --- 2. Adicione esta Lógica de Negócios (Local) ---
(defn adicionar-todo-local []
  (swap! app-state
         (fn [estado-atual]
           (let [novo-titulo (:input-text estado-atual)
                 novo-id (:next-id estado-atual)]
             (if (str/blank? novo-titulo)
               estado-atual ;; Não faz nada se vazio
               ;; Retorna um NOVO estado
               {:next-id (inc novo-id)
                :input-text "" ;; Limpa o input
                :todos (conj (:todos estado-atual)
                             {:id novo-id
                              :title novo-titulo})}
               )))))
;; --- 1. Nossos Componentes (Estáticos) ---

(defn todo-form []
  [:div.todo-input
   [:input
    {:type "text"
     :placeholder "O que precisa ser feito?"
     ;; (Leitura): O valor do input vem do app-state
     :value (:input-text @app-state)
     ;; (Escrita): O on-change atualiza o app-state
     :on-change #(swap! app-state assoc :input-text (-> % .-target .-value))}]
   
   [:button
    ;; (Ação): O on-click agora chama nossa lógica
    {:on-click adicionar-todo-local}
    "Adicionar (Local)"]])

(defn todo-list [] ;; <-- Argumento "todos" REMOVIDO
  [:ul.todo-list
   ;; (Leitura): O 'for' agora observa o @app-state
   (for [todo (:todos @app-state)]
     ^{:key (:id todo)}
     [:li.todo-item
      (:title todo)])])

(defn app []
  [:div.todo-app
   [:h1 "Todo App (Somente Frontend)"]
   [:p "Isto é 100% local. Recarregue (F5) para ver os dados sumirem."]
   
   ;; Os componentes agora se viram sozinhos!
   [todo-form]
   [todo-list]
   ])

;; --- 2. A Inicialização (React 18) ---
(defn ^:export init []
  (println "Frontend 'Todo Estático' inicializado...")
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))