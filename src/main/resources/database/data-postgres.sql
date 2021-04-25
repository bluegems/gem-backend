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
    'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/i/7d13c5f2-28c5-468e-9d9b-f0048ce7921b/dbcgfy8-c910d8e7-f3a4-4b39-b1be-49806742b25c.png',
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
    'https://images.unsplash.com/photo-1617129936634-06cd10f4497f?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1317&q=80',
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
    'https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/ac7d183b-035e-4133-8302-bcc92a267aa7/de39xro-99cc65ba-171c-40f9-be66-46e7e0abc116.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOiIsImlzcyI6InVybjphcHA6Iiwib2JqIjpbW3sicGF0aCI6IlwvZlwvYWM3ZDE4M2ItMDM1ZS00MTMzLTgzMDItYmNjOTJhMjY3YWE3XC9kZTM5eHJvLTk5Y2M2NWJhLTE3MWMtNDBmOS1iZTY2LTQ2ZTdlMGFiYzExNi5qcGcifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6ZmlsZS5kb3dubG9hZCJdfQ.MAbGFurj4rqE2jQj3YEpQ4e6xMdRGwc1m__UUJyIp-U',
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
    'https://images.unsplash.com/photo-1590808100071-3654286139a4?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'
);

INSERT INTO post VALUES (
    DEFAULT, DEFAULT, DEFAULT,
    (SELECT id FROM users WHERE username='buttercupIsGreen' AND tag='GRN2'),
    NULL,
    'https://images.unsplash.com/photo-1612718908786-de5aeecfab79?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80'
);
