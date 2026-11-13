CREATE TABLE products (
                          id              BIGSERIAL PRIMARY KEY,
                          name            VARCHAR(255)    NOT NULL,
                          description     VARCHAR(2000),
                          category        VARCHAR(100)    NOT NULL,
                          price           NUMERIC(10, 2)  NOT NULL CHECK (price > 0),
                          stock_quantity  INTEGER         NOT NULL CHECK (stock_quantity >= 0),
                          image_url       VARCHAR(500),
                          active          BOOLEAN         NOT NULL DEFAULT TRUE,
                          created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                          updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now()
);

CREATE INDEX idx_products_category ON products (category) WHERE active = TRUE;

INSERT INTO products (name, description, category, price, stock_quantity, image_url) VALUES
                                                                                         ('Wireless Mouse', 'Ergonomic 2.4GHz wireless mouse', 'Electronics', 19.99, 150, NULL),
                                                                                         ('Mechanical Keyboard', 'RGB backlit mechanical keyboard, blue switches', 'Electronics', 59.99, 80, NULL),
                                                                                         ('Running Shoes', 'Lightweight breathable running shoes', 'Footwear', 74.50, 60, NULL),
                                                                                         ('Coffee Maker', '12-cup programmable drip coffee maker', 'Home', 39.99, 45, NULL),
                                                                                         ('Yoga Mat', 'Non-slip 6mm yoga mat', 'Fitness', 24.99, 200, NULL);