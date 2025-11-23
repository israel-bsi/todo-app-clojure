(ns todo.backend.db
  "Este namespace gerencia os dados dos 'todos'
   usando um banco de dados persistente SQLite.")

(require '[next.jdbc :as jdbc]
         '[clojure.string :as str])

(def db-spec {:dbtype "sqlite"
              :dbname "prod.db"})

(defn initialize-database!
  "Cria a tabela 'todos' no banco de dados se ela não existir."
  []
  (jdbc/execute! db-spec ["
    CREATE TABLE IF NOT EXISTS todos (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      title TEXT,
      description TEXT,
      completed BOOLEAN DEFAULT 0,
      created_at TEXT
    )
  "]))

(defn get-all-todos
  "Retorna uma lista com todos os 'todos' no banco."
  []
  (jdbc/execute! db-spec ["SELECT * FROM todos ORDER BY created_at DESC"]))

(defn create-todo
  "Cria um novo 'todo', salva no banco e o retorna."
  [todo-data]
  (let [
        todo-map (assoc todo-data
                        :completed 0
                        :created-at (str (java.time.Instant/now)))
        result (jdbc/execute-one! db-spec ["
          INSERT INTO todos (title, description, completed, created_at)
          VALUES (?, ?, ?, ?)"
          (:title todo-map)
          (:description todo-map)
          (:completed todo-map)
          (:created-at todo-map)
          ] {:returning "*"})]
    result))

(defn toggle-todo!
  "Alterna o status 'completed' de um todo no banco."
  [id]
  (jdbc/execute-one! db-spec ["
    UPDATE todos
    SET completed = (1 - completed)
    WHERE id = ?
    RETURNING *"
    id
  ]))