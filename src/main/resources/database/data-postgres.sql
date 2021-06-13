INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'superman@bluegems.com',
  'password123'
);

INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'batman@bluegems.com',
  'password123'
);

INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'wolverine@bluegems.com',
  'password123'
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'DC01',
    'superman',
    'Kal',
    'El',
    'Super strength, icy breath and lazer eyes. Allergic to green crystals',
    '1998-11-18',
    '0KahUrK',
    (SELECT id FROM account WHERE email='superman@bluegems.com')
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'DC02',
    'batman',
    'Bruce',
    'Wayne',
    'Resilient and rich ninja who wears a cape. Voice modulation changes my vibe',
    '1998-11-18',
    'ePGVUbd',
    (SELECT id FROM account WHERE email='batman@bluegems.com')
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'MV03',
    'wolverine',
    'Logan',
    NULL,
    'I don''t know where I''m from or who you are... You should be scared of me',
    '1998-11-18',
    'lCYDFSo',
    (SELECT id FROM account WHERE email='wolverine@bluegems.com')
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='superman' AND tag='DC01'),
    'Earth is home!',
    NULL
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='batman' AND tag='DC02'),
    'I hear this dude''s a ninja too',
    '8JPPEX2'
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='wolverine' AND tag='MV03'),
    NULL,
    'LUtoBfz'
);
