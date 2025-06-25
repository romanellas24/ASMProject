CREATE DATABASE IF NOT EXISTS restaurant;
USE restaurant;

CREATE TABLE IF NOT EXISTS orders (
    id INT NOT NULL AUTO_INCREMENT,
    delivery_time DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS dish (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(512),
    price DECIMAL(16, 2) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS dish_order (
    dish_id INT NOT NULL,
    order_id INT NOT NULL,
    mult INT NOT NULL DEFAULT 1,
    FOREIGN KEY (dish_id) REFERENCES dish(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE IF NOT EXISTS daily_menu (
    day DATE NOT NULL,
    dish_id INT NOT NULL,
    PRIMARY KEY (day, dish_id),
    FOREIGN KEY (dish_id) REFERENCES dish(id)
);

CREATE TABLE IF NOT EXISTS order_mapping (
    local_order_id INT NOT NULL,
    company_id INT NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (local_order_id) REFERENCES orders(id),
    UNIQUE (company_id, company_name)
);


DELETE FROM daily_menu;
DELETE FROM dish;

INSERT INTO dish (name, description, price)
SELECT name, description, price
FROM (
  SELECT 'Pizza Margherita', 'Pomodoro, mozzarella, basilico', 7.50 UNION ALL
  SELECT 'Spaghetti Carbonara', 'Uova, pecorino, guanciale', 8.00 UNION ALL
  SELECT 'Risotto ai funghi', 'Funghi porcini, riso, parmigiano', 9.00 UNION ALL
  SELECT 'Lasagne', 'Ragù, besciamella, pasta', 9.50 UNION ALL
  SELECT 'Pollo al curry', 'Pollo, curry, riso', 8.50 UNION ALL
  SELECT 'Insalata greca', 'Feta, olive, cetrioli', 6.50 UNION ALL
  SELECT 'Zuppa di lenticchie', 'Lenticchie, carote, sedano', 6.00 UNION ALL
  SELECT 'Bistecca alla fiorentina', 'Manzo, rosmarino, sale grosso', 15.00 UNION ALL
  SELECT 'Frittata di patate', 'Uova, patate, cipolle', 5.50 UNION ALL
  SELECT 'Gnocchi al pesto', 'Pesto, patate, gnocchi', 7.00 UNION ALL
  SELECT 'Panino con prosciutto', 'Pane, prosciutto, formaggio', 5.00 UNION ALL
  SELECT 'Ravioli burro e salvia', 'Ravioli, burro, salvia', 7.50 UNION ALL
  SELECT 'Melanzane alla parmigiana', 'Melanzane, pomodoro, formaggio', 8.00 UNION ALL
  SELECT 'Cous cous di verdure', 'Cous cous, verdure miste', 6.50 UNION ALL
  SELECT 'Calamari fritti', 'Calamari, limone, farina', 9.00 UNION ALL
  SELECT 'Tacos di pollo', 'Pollo, spezie, tortillas', 7.50 UNION ALL
  SELECT 'Hamburger classico', 'Pane, carne, insalata', 6.50 UNION ALL
  SELECT 'Sushi misto', 'Riso, pesce crudo, alghe', 12.00 UNION ALL
  SELECT 'Paella', 'Riso, frutti di mare, zafferano', 11.00 UNION ALL
  SELECT 'Polenta e salsiccia', 'Polenta, salsiccia, sugo', 7.50 UNION ALL
  SELECT 'Omelette', 'Uova, formaggio, prosciutto', 6.00 UNION ALL
  SELECT 'Minestrone', 'Verdure miste, brodo', 5.50 UNION ALL
  SELECT 'Tagliatelle al ragù', 'Pasta all’uovo, ragù', 8.00 UNION ALL
  SELECT 'Cotoletta alla milanese', 'Carne panata, limone', 9.00 UNION ALL
  SELECT 'Tramezzino', 'Pane morbido, tonno, maionese', 4.50 UNION ALL
  SELECT 'Bruschette miste', 'Pane, pomodoro, funghi', 5.00 UNION ALL
  SELECT 'Risotto allo zafferano', 'Riso, zafferano, parmigiano', 8.50 UNION ALL
  SELECT 'Verdure grigliate', 'Zucchine, melanzane, peperoni', 6.00 UNION ALL
  SELECT 'Insalata di riso', 'Riso, mais, tonno', 5.50 UNION ALL
  SELECT 'Focaccia farcita', 'Focaccia, mortadella, stracchino', 5.50 UNION ALL
  SELECT 'Crepes salate', 'Uova, latte, prosciutto', 6.00 UNION ALL
  SELECT 'Sformato di patate', 'Patate, formaggio, uova', 6.50 UNION ALL
  SELECT 'Zuppa di zucca', 'Zucca, cipolla, panna', 6.00 UNION ALL
  SELECT 'Torta salata', 'Pasta sfoglia, verdure', 5.50 UNION ALL
  SELECT 'Arancini di riso', 'Riso, ragù, mozzarella', 6.00 UNION ALL
  SELECT 'Filetto di salmone', 'Salmone, limone, erbe', 13.00 UNION ALL
  SELECT 'Panino vegetariano', 'Pane, verdure, hummus', 5.00 UNION ALL
  SELECT 'Insalata di farro', 'Farro, pomodorini, feta', 6.50 UNION ALL
  SELECT 'Ravioli di zucca', 'Zucca, ricotta, burro', 7.00 UNION ALL
  SELECT 'Tagliata di manzo', 'Carne, rucola, grana', 14.00 UNION ALL
  SELECT 'Fusilli al tonno', 'Pasta, tonno, olive', 7.00 UNION ALL
  SELECT 'Polpette al sugo', 'Carne, pane, salsa', 7.50 UNION ALL
  SELECT 'Uova sode ripiene', 'Uova, maionese, tonno', 4.50 UNION ALL
  SELECT 'Risotto agli asparagi', 'Asparagi, riso, parmigiano', 8.00 UNION ALL
  SELECT 'Spiedini di carne', 'Carne mista, spezie', 9.00 UNION ALL
  SELECT 'Zuppa di ceci', 'Ceci, rosmarino, aglio', 5.50 UNION ALL
  SELECT 'Bocconcini di pollo', 'Pollo, curry, yogurt', 7.00 UNION ALL
  SELECT 'Frittelle di zucchine', 'Zucchine, farina, uova', 5.00 UNION ALL
  SELECT 'Piadina romagnola', 'Piadina, crudo, squacquerone', 5.50 UNION ALL
  SELECT 'Riso alla cantonese', 'Riso, piselli, prosciutto', 6.50
) AS dish_pool(name, description, price)
ORDER BY RAND()
LIMIT 30;


/* INSERISCI 15 PIATTI RANDOM NEL MENU DEL GIORNO */
INSERT INTO daily_menu (day, dish_id)
SELECT CURDATE(), id
FROM dish
ORDER BY RAND()
LIMIT 15;
