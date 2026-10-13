# RESTaurant Management System

A Java Spring Boot REST API with a React/TypeScript web interface and persistent H2 relational database.

## Requirements
- Java 17 or later
- Maven 3.9+
- Node.js 18+ and npm

## Run backend
```bash
cd backend
mvn spring-boot:run
```
Backend runs at `http://localhost:8080`. H2 data is saved to `backend/data/` (relative to the backend working directory).

## Run frontend (separate terminal)
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173`.

## Try it
1. Open restaurant.
2. Upload `samples/employees.csv`, `samples/ingredients.csv`, and `samples/recipes.csv` using the matching UI controls.
3. Hire an employee and add ingredient stock.
4. Select recipes and place an order. If there is insufficient stock, the order is saved as REFUSED; otherwise stock is deducted and the order is RECEIVED.
5. Complete a received order and rate it once, 0–5 stars.
6. Restart the backend and confirm records persist.

## Endpoints
- `GET /api/restaurant`, `POST /api/restaurant/open`, `POST /api/restaurant/close`
- `GET /api/employees`, `POST /api/employees/upload`, `POST /api/employees/{id}/hire`, `POST /api/employees/{id}/dismiss`
- `GET /api/ingredients`, `POST /api/ingredients/upload`, `POST /api/ingredients/{name}/stock` (JSON `{ "quantity": 5 }`)
- `GET /api/recipes`, `POST /api/recipes/upload`
- `GET /api/orders`, `POST /api/orders` (JSON `{ "recipes": ["Toast"] }`), `POST /api/orders/{id}/complete`, `POST /api/orders/{id}/rate` (JSON `{ "stars": 4 }`)

## CSV format
- Employees: `name,efficiency`
- Ingredients: `ingredient,cost`
- Recipes: `recipe,time,ingredients` with one or more ingredient columns, as in the assignment sample.

## Architecture
- REST controllers: `Api.java`
- JPA entities: `Model.java`
- Persistence repositories: `Repos.java`
- React TypeScript frontend: `frontend/src/main.tsx`

This starter implements the required workflows, but its backend is currently controller-centric. For strict layered-architecture compliance, move business logic into service classes before submitting. CSV parsing is intentionally simple and assumes unquoted fields. This project has not been integration-tested in this environment.

No bonus features (Docker, Swagger, automated tests, monitoring, WebSockets, queues, payroll, or automatic closure) are included.

## Refactored backend notes

The backend now separates HTTP controllers, application services, Spring Data repositories, and JPA models. All existing `/api` endpoints and JSON response shapes are retained. The existing H2 database configuration is unchanged; back up the local database files before switching versions.

Order creation, stock additions, and order completion coordinate through a pessimistic lock on the singleton RestaurantState row. CSV uploads validate rows before saving; transactions roll back failures. The CSV parser is intentionally simple and does not support quoted fields containing commas.

**Reopening semantics:** Opening resets the displayed completed-order count and average rating, as the assignment specifies, but retains historical orders and ratings in the database. Rating a historical completed order after reopening recalculates the average across all rated orders, including old ones. If you need per-opening-session statistics, that requires an additional session model and is not included here.

**Manual checks before publishing:** Start backend and frontend; verify existing records remain available; upload valid and invalid CSV files; hire and dismiss; add stock; place accepted and refused orders; complete and rate an order; restart backend and confirm persistence. Verify that a refused order does not deduct any inventory. The Java build and runtime tests have not been executed in this environment.
