INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'blossom@bluegems.com',
  'password123'
);

INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'bubbles@bluegems.com',
  'password123'
);

INSERT INTO account VALUES (
  DEFAULT, DEFAULT, DEFAULT,
  'buttercup@bluegems.com',
  'password123'
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'PNK1',
    'blossomIsPink',
    'Blossom',
    'X',
    'The tactician and self-proclaimed leader of the Powerpuff Girls',
    '1998-11-18',
    'BWkr3du',
    (SELECT id FROM account WHERE email='blossom@bluegems.com')
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'GRN2',
    'buttercupIsGreen',
    'Buttercup',
    'X',
    'Loves to get dirty, fights hard and plays rough. She does not plan and is all action',
    '1998-11-18',
    'YOL4mMo',
    (SELECT id FROM account WHERE email='buttercup@bluegems.com')
);

INSERT INTO users VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    'BLU3',
    'bubblesIsBlue',
    'Bubbles',
    'X',
    'Kind and very sweet but capable of extreme rage and can fight monsters just as well as her sisters',
    '1998-11-18',
    'pGFyviQ',
    (SELECT id FROM account WHERE email='bubbles@bluegems.com')
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='bubblesIsBlue' AND tag='BLU3'),
    'Octi says I''m a good girl :)',
    NULL
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='blossomIsPink' AND tag='PNK1'),
    'Isn''t this beautiful. Really brings out the red!',
    'KTtRBzq'
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='buttercupIsGreen' AND tag='GRN2'),
    NULL,
    'A93HjP9'
);
