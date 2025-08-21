INSERT INTO departments(code, name) VALUES ('ENG','Engineering') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('OPS','Operations') ON CONFLICT DO NOTHING;

INSERT INTO tags(name) VALUES ('faq') ON CONFLICT DO NOTHING;
