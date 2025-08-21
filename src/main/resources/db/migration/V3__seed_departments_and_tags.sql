-- V3: seed new departments and tag
--INSERT INTO departments(code, name) VALUES ('ENG','Engineering') ON CONFLICT DO NOTHING;
--INSERT INTO departments(code, name) VALUES ('OPS','Operations') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('HR','Human Resources') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('FIN','Finance') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('MKT','Marketing') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('SALES','Sales') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('IT','Information Technology') ON CONFLICT DO NOTHING;
INSERT INTO departments(code, name) VALUES ('RND','Research and Development') ON CONFLICT DO NOTHING;

INSERT INTO tags(name) VALUES ('faq') ON CONFLICT DO NOTHING;
