# Todo App

## Nome do Aluno
Israel Barbosa Silva

## Link do Tutorial
[Tutorial Clojure/ClojureScript: Construindo uma Aplicação Persistente e Reativa](https://profsergiocosta.notion.site/Tutorial-Clojure-ClojureScript-Construindo-uma-Aplica-o-Persistente-e-Reativa-2a5cce975093807aa9f0f0cb0cf69645)

## Descrição Breve
Este é um projeto de um aplicativo de lista de tarefas (Todo App) desenvolvido utilizando Clojure no backend e ClojureScript no frontend. Ele utiliza as seguintes tecnologias principais:

- **Backend:** Ring, Reitit, Jetty
- **Frontend:** Shadow-cljs, React
- **Banco de Dados:** SQLite

## Instruções de Execução

### Pré-requisitos
Certifique-se de ter os seguintes softwares instalados:

- **Clojure CLI**: [Instruções de instalação](https://clojure.org/guides/getting_started)
- **Node.js**: [Baixar aqui](https://nodejs.org/)
- **Java JDK**: Recomendado JDK 8 ou superior

### Passos para Rodar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/israel-bsi/todo-app-clojure.git
   cd todo-app-clojure
   ```

2. Instale as dependências do frontend:
   ```bash
   npm install
   ```

3. Inicie o backend:
   Abra um terminal e execute:
   ```bash
   clj -M:run
   ```

4. Inicie o frontend:
   Em outro terminal, execute:
   ```bash
   npx shadow-cljs watch app
   ```

5. Acesse o aplicativo:
   Abra o navegador e vá para [http://localhost:8000](http://localhost:8000).