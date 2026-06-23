-- ============================================================
-- Sets (확장팩)
-- 출처: https://github.com/PokemonTCG/pokemon-tcg-data (MIT)
-- ============================================================
INSERT INTO sets (id, name, series, printed_total, total, ptcgo_code, release_date, symbol_image_url, logo_image_url) VALUES
('base1',   'Base Set',          'Base',         102, 102, 'BS',  '1999-01-09', 'https://images.pokemontcg.io/base1/symbol.png',   'https://images.pokemontcg.io/base1/logo.png'),
('base2',   'Jungle',            'Base',          64,  64, 'JU',  '1999-06-16', 'https://images.pokemontcg.io/base2/symbol.png',   'https://images.pokemontcg.io/base2/logo.png'),
('basep',   'Wizards Black Star Promos', 'Base',  53,  53, 'PR',  '1999-01-09', 'https://images.pokemontcg.io/basep/symbol.png',   'https://images.pokemontcg.io/basep/logo.png'),
('gym1',    'Gym Heroes',        'Gym',          132, 132, 'G1',  '2000-08-14', 'https://images.pokemontcg.io/gym1/symbol.png',    'https://images.pokemontcg.io/gym1/logo.png'),
('gym2',    'Gym Challenge',     'Gym',          132, 132, 'G2',  '2000-10-16', 'https://images.pokemontcg.io/gym2/symbol.png',    'https://images.pokemontcg.io/gym2/logo.png'),
('neo1',    'Neo Genesis',       'Neo',          111, 111, 'N1',  '2000-12-16', 'https://images.pokemontcg.io/neo1/symbol.png',    'https://images.pokemontcg.io/neo1/logo.png'),
('neo2',    'Neo Discovery',     'Neo',           75,  75, 'N2',  '2001-06-01', 'https://images.pokemontcg.io/neo2/symbol.png',    'https://images.pokemontcg.io/neo2/logo.png'),
('neo3',    'Neo Revelation',    'Neo',           66,  66, 'N3',  '2001-09-21', 'https://images.pokemontcg.io/neo3/symbol.png',    'https://images.pokemontcg.io/neo3/logo.png'),
('neo4',    'Neo Destiny',       'Neo',          113, 113, 'N4',  '2002-02-28', 'https://images.pokemontcg.io/neo4/symbol.png',    'https://images.pokemontcg.io/neo4/logo.png'),
('ecard1',  'Expedition Base Set', 'E-Card',     165, 165, 'EX',  '2002-09-15', 'https://images.pokemontcg.io/ecard1/symbol.png',  'https://images.pokemontcg.io/ecard1/logo.png'),
('ex1',     'Ruby & Sapphire',   'EX',           109, 109, 'RS',  '2003-06-18', 'https://images.pokemontcg.io/ex1/symbol.png',     'https://images.pokemontcg.io/ex1/logo.png'),
('ex2',     'Sandstorm',         'EX',           100, 100, 'SS',  '2003-09-18', 'https://images.pokemontcg.io/ex2/symbol.png',     'https://images.pokemontcg.io/ex2/logo.png'),
('dp1',     'Diamond & Pearl',   'Diamond & Pearl', 130, 130, 'DP', '2007-05-23', 'https://images.pokemontcg.io/dp1/symbol.png',  'https://images.pokemontcg.io/dp1/logo.png'),
('pl1',     'Platinum',          'Platinum',     133, 133, 'PL',  '2009-02-11', 'https://images.pokemontcg.io/pl1/symbol.png',    'https://images.pokemontcg.io/pl1/logo.png'),
('hgss1',   'HeartGold & SoulSilver', 'HeartGold & SoulSilver', 123, 123, 'HS', '2010-02-10', 'https://images.pokemontcg.io/hgss1/symbol.png', 'https://images.pokemontcg.io/hgss1/logo.png'),
('bw1',     'Black & White',     'Black & White', 115, 115, 'BW', '2011-04-25', 'https://images.pokemontcg.io/bw1/symbol.png',    'https://images.pokemontcg.io/bw1/logo.png'),
('xy1',     'XY',                'XY',           146, 146, 'XY',  '2014-02-05', 'https://images.pokemontcg.io/xy1/symbol.png',    'https://images.pokemontcg.io/xy1/logo.png'),
('xy2',     'Flashfire',         'XY',           106, 106, 'FLF', '2014-05-07', 'https://images.pokemontcg.io/xy2/symbol.png',    'https://images.pokemontcg.io/xy2/logo.png'),
('sm1',     'Sun & Moon',        'Sun & Moon',   149, 149, 'SUM', '2017-02-03', 'https://images.pokemontcg.io/sm1/symbol.png',    'https://images.pokemontcg.io/sm1/logo.png'),
('sm2',     'Guardians Rising',  'Sun & Moon',   145, 145, 'GRI', '2017-05-05', 'https://images.pokemontcg.io/sm2/symbol.png',    'https://images.pokemontcg.io/sm2/logo.png'),
('sm3',     'Burning Shadows',   'Sun & Moon',   147, 147, 'BUS', '2017-08-05', 'https://images.pokemontcg.io/sm3/symbol.png',    'https://images.pokemontcg.io/sm3/logo.png'),
('sm35',    'Shining Legends',   'Sun & Moon',    73,  73, 'SLG', '2017-10-06', 'https://images.pokemontcg.io/sm35/symbol.png',   'https://images.pokemontcg.io/sm35/logo.png'),
('sm4',     'Crimson Invasion',  'Sun & Moon',   124, 124, 'CIN', '2017-11-03', 'https://images.pokemontcg.io/sm4/symbol.png',    'https://images.pokemontcg.io/sm4/logo.png'),
('sm5',     'Ultra Prism',       'Sun & Moon',   173, 173, 'UPR', '2018-02-02', 'https://images.pokemontcg.io/sm5/symbol.png',    'https://images.pokemontcg.io/sm5/logo.png'),
('sm6',     'Forbidden Light',   'Sun & Moon',   146, 146, 'FLI', '2018-05-04', 'https://images.pokemontcg.io/sm6/symbol.png',    'https://images.pokemontcg.io/sm6/logo.png'),
('sm7',     'Celestial Storm',   'Sun & Moon',   183, 183, 'CES', '2018-08-03', 'https://images.pokemontcg.io/sm7/symbol.png',    'https://images.pokemontcg.io/sm7/logo.png'),
('sm8',     'Lost Thunder',      'Sun & Moon',   236, 236, 'LOT', '2018-11-02', 'https://images.pokemontcg.io/sm8/symbol.png',    'https://images.pokemontcg.io/sm8/logo.png'),
('sm9',     'Team Up',           'Sun & Moon',   196, 196, 'TEU', '2019-02-01', 'https://images.pokemontcg.io/sm9/symbol.png',    'https://images.pokemontcg.io/sm9/logo.png'),
('sm10',    'Unbroken Bonds',    'Sun & Moon',   214, 214, 'UNB', '2019-05-03', 'https://images.pokemontcg.io/sm10/symbol.png',   'https://images.pokemontcg.io/sm10/logo.png'),
('sm11',    'Unified Minds',     'Sun & Moon',   236, 236, 'UNM', '2019-08-02', 'https://images.pokemontcg.io/sm11/symbol.png',   'https://images.pokemontcg.io/sm11/logo.png'),
('sm115',   'Hidden Fates',      'Sun & Moon',    69,  69, 'HIF', '2019-08-23', 'https://images.pokemontcg.io/sm115/symbol.png',  'https://images.pokemontcg.io/sm115/logo.png'),
('sm12',    'Cosmic Eclipse',    'Sun & Moon',   272, 272, 'CEC', '2019-11-01', 'https://images.pokemontcg.io/sm12/symbol.png',   'https://images.pokemontcg.io/sm12/logo.png'),
('swsh1',   'Sword & Shield',    'Sword & Shield', 216, 216, 'SSH', '2020-02-07', 'https://images.pokemontcg.io/swsh1/symbol.png', 'https://images.pokemontcg.io/swsh1/logo.png'),
('swsh2',   'Rebel Clash',       'Sword & Shield', 209, 209, 'RCL', '2020-05-01', 'https://images.pokemontcg.io/swsh2/symbol.png', 'https://images.pokemontcg.io/swsh2/logo.png'),
('swsh3',   'Darkness Ablaze',   'Sword & Shield', 201, 201, 'DAA', '2020-08-14', 'https://images.pokemontcg.io/swsh3/symbol.png', 'https://images.pokemontcg.io/swsh3/logo.png'),
('swsh35',  'Champions Path',    'Sword & Shield',  73,  73, 'CPA', '2020-09-25', 'https://images.pokemontcg.io/swsh35/symbol.png','https://images.pokemontcg.io/swsh35/logo.png'),
('swsh4',   'Vivid Voltage',     'Sword & Shield', 203, 203, 'VIV', '2020-11-13', 'https://images.pokemontcg.io/swsh4/symbol.png', 'https://images.pokemontcg.io/swsh4/logo.png'),
('swsh45',  'Shining Fates',     'Sword & Shield',  73,  73, 'SHF', '2021-02-19', 'https://images.pokemontcg.io/swsh45/symbol.png','https://images.pokemontcg.io/swsh45/logo.png'),
('swsh5',   'Battle Styles',     'Sword & Shield', 183, 183, 'BST', '2021-03-19', 'https://images.pokemontcg.io/swsh5/symbol.png', 'https://images.pokemontcg.io/swsh5/logo.png'),
('swsh6',   'Chilling Reign',    'Sword & Shield', 233, 233, 'CRE', '2021-06-18', 'https://images.pokemontcg.io/swsh6/symbol.png', 'https://images.pokemontcg.io/swsh6/logo.png'),
('swsh7',   'Evolving Skies',    'Sword & Shield', 237, 237, 'EVS', '2021-08-27', 'https://images.pokemontcg.io/swsh7/symbol.png', 'https://images.pokemontcg.io/swsh7/logo.png'),
('swsh8',   'Fusion Strike',     'Sword & Shield', 284, 284, 'FST', '2021-11-12', 'https://images.pokemontcg.io/swsh8/symbol.png', 'https://images.pokemontcg.io/swsh8/logo.png'),
('swsh9',   'Brilliant Stars',   'Sword & Shield', 186, 186, 'BRS', '2022-02-25', 'https://images.pokemontcg.io/swsh9/symbol.png', 'https://images.pokemontcg.io/swsh9/logo.png'),
('swsh10',  'Astral Radiance',   'Sword & Shield', 246, 246, 'ASR', '2022-05-27', 'https://images.pokemontcg.io/swsh10/symbol.png','https://images.pokemontcg.io/swsh10/logo.png'),
('swsh11',  'Lost Origin',       'Sword & Shield', 217, 217, 'LOR', '2022-09-09', 'https://images.pokemontcg.io/swsh11/symbol.png','https://images.pokemontcg.io/swsh11/logo.png'),
('swsh12',  'Silver Tempest',    'Sword & Shield', 215, 215, 'SIT', '2022-11-11', 'https://images.pokemontcg.io/swsh12/symbol.png','https://images.pokemontcg.io/swsh12/logo.png'),
('sv1',     'Scarlet & Violet',  'Scarlet & Violet', 258, 258, 'SVI', '2023-03-31', 'https://images.pokemontcg.io/sv1/symbol.png',  'https://images.pokemontcg.io/sv1/logo.png'),
('sv2',     'Paldea Evolved',    'Scarlet & Violet', 279, 279, 'PAL', '2023-06-09', 'https://images.pokemontcg.io/sv2/symbol.png',  'https://images.pokemontcg.io/sv2/logo.png'),
('sv3',     'Obsidian Flames',   'Scarlet & Violet', 230, 230, 'OBF', '2023-08-11', 'https://images.pokemontcg.io/sv3/symbol.png',  'https://images.pokemontcg.io/sv3/logo.png'),
('sv3pt5',  '151',               'Scarlet & Violet', 207, 207, 'MEW', '2023-09-22', 'https://images.pokemontcg.io/sv3pt5/symbol.png','https://images.pokemontcg.io/sv3pt5/logo.png'),
('sv4',     'Paradox Rift',      'Scarlet & Violet', 266, 266, 'PAR', '2023-11-03', 'https://images.pokemontcg.io/sv4/symbol.png',  'https://images.pokemontcg.io/sv4/logo.png'),
('sv4pt5',  'Paldean Fates',     'Scarlet & Violet',  91,  91, 'PAF', '2024-01-26', 'https://images.pokemontcg.io/sv4pt5/symbol.png','https://images.pokemontcg.io/sv4pt5/logo.png'),
('sv5',     'Temporal Forces',   'Scarlet & Violet', 218, 218, 'TEF', '2024-03-22', 'https://images.pokemontcg.io/sv5/symbol.png',  'https://images.pokemontcg.io/sv5/logo.png'),
('sv6',     'Twilight Masquerade','Scarlet & Violet', 167, 167, 'TWM', '2024-05-24', 'https://images.pokemontcg.io/sv6/symbol.png',  'https://images.pokemontcg.io/sv6/logo.png'),
('sv6pt5',  'Shrouded Fable',    'Scarlet & Violet',  99,  99, 'SFA', '2024-08-02', 'https://images.pokemontcg.io/sv6pt5/symbol.png','https://images.pokemontcg.io/sv6pt5/logo.png'),
('sv7',     'Stellar Crown',     'Scarlet & Violet', 175, 175, 'SCR', '2024-09-13', 'https://images.pokemontcg.io/sv7/symbol.png',  'https://images.pokemontcg.io/sv7/logo.png'),
('sv8',     'Surging Sparks',    'Scarlet & Violet', 252, 252, 'SSP', '2024-11-08', 'https://images.pokemontcg.io/sv8/symbol.png',  'https://images.pokemontcg.io/sv8/logo.png'),
('sv8pt5',  'Prismatic Evolutions','Scarlet & Violet', 131, 131, 'PRE', '2025-01-17', 'https://images.pokemontcg.io/sv8pt5/symbol.png','https://images.pokemontcg.io/sv8pt5/logo.png'),
('sv9',     'Journey Together',  'Scarlet & Violet', 190, 190, 'JTG', '2025-03-28', 'https://images.pokemontcg.io/sv9/symbol.png',  'https://images.pokemontcg.io/sv9/logo.png');

-- ============================================================
-- Cards (대표 카드 샘플 — Base Set)
-- ============================================================
INSERT INTO cards (set_id, number, name, supertype, subtype, rarity, image_small_url, image_large_url) VALUES
('base1', '1',   'Alakazam',       'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/1.png',   'https://images.pokemontcg.io/base1/1_hires.png'),
('base1', '2',   'Blastoise',      'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/2.png',   'https://images.pokemontcg.io/base1/2_hires.png'),
('base1', '3',   'Chansey',        'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/3.png',   'https://images.pokemontcg.io/base1/3_hires.png'),
('base1', '4',   'Charizard',      'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/4.png',   'https://images.pokemontcg.io/base1/4_hires.png'),
('base1', '5',   'Clefairy',       'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/5.png',   'https://images.pokemontcg.io/base1/5_hires.png'),
('base1', '6',   'Gyarados',       'POKEMON', 'Stage 1', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/6.png',   'https://images.pokemontcg.io/base1/6_hires.png'),
('base1', '7',   'Hitmonchan',     'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/7.png',   'https://images.pokemontcg.io/base1/7_hires.png'),
('base1', '8',   'Machamp',        'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/8.png',   'https://images.pokemontcg.io/base1/8_hires.png'),
('base1', '9',   'Magneton',       'POKEMON', 'Stage 1', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/9.png',   'https://images.pokemontcg.io/base1/9_hires.png'),
('base1', '10',  'Mewtwo',         'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/10.png',  'https://images.pokemontcg.io/base1/10_hires.png'),
('base1', '11',  'Nidoking',       'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/11.png',  'https://images.pokemontcg.io/base1/11_hires.png'),
('base1', '12',  'Ninetales',      'POKEMON', 'Stage 1', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/12.png',  'https://images.pokemontcg.io/base1/12_hires.png'),
('base1', '13',  'Pidgeot',        'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/13.png',  'https://images.pokemontcg.io/base1/13_hires.png'),
('base1', '14',  'Poliwrath',      'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/14.png',  'https://images.pokemontcg.io/base1/14_hires.png'),
('base1', '15',  'Raichu',         'POKEMON', 'Stage 1', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/15.png',  'https://images.pokemontcg.io/base1/15_hires.png'),
('base1', '16',  'Scyther',        'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/16.png',  'https://images.pokemontcg.io/base1/16_hires.png'),
('base1', '17',  'Venusaur',       'POKEMON', 'Stage 2', 'RARE_HOLO',  'https://images.pokemontcg.io/base1/17.png',  'https://images.pokemontcg.io/base1/17_hires.png'),
('base1', '18',  'Zapdos',         'POKEMON', 'Basic',   'RARE_HOLO',  'https://images.pokemontcg.io/base1/18.png',  'https://images.pokemontcg.io/base1/18_hires.png'),
('base1', '19',  'Beedrill',       'POKEMON', 'Stage 2', 'RARE',       'https://images.pokemontcg.io/base1/19.png',  'https://images.pokemontcg.io/base1/19_hires.png'),
('base1', '20',  'Dragonair',      'POKEMON', 'Stage 1', 'RARE',       'https://images.pokemontcg.io/base1/20.png',  'https://images.pokemontcg.io/base1/20_hires.png'),
('base1', '21',  'Dugtrio',        'POKEMON', 'Stage 1', 'RARE',       'https://images.pokemontcg.io/base1/21.png',  'https://images.pokemontcg.io/base1/21_hires.png'),
('base1', '22',  'Electabuzz',     'POKEMON', 'Basic',   'RARE',       'https://images.pokemontcg.io/base1/22.png',  'https://images.pokemontcg.io/base1/22_hires.png'),
('base1', '23',  'Electrode',      'POKEMON', 'Stage 1', 'RARE',       'https://images.pokemontcg.io/base1/23.png',  'https://images.pokemontcg.io/base1/23_hires.png'),
('base1', '24',  'Pidgeotto',      'POKEMON', 'Stage 1', 'RARE',       'https://images.pokemontcg.io/base1/24.png',  'https://images.pokemontcg.io/base1/24_hires.png'),
('base1', '25',  'Arcanine',       'POKEMON', 'Stage 1', 'RARE',       'https://images.pokemontcg.io/base1/25.png',  'https://images.pokemontcg.io/base1/25_hires.png'),
('base1', '26',  'Charmeleon',     'POKEMON', 'Stage 1', 'UNCOMMON',   'https://images.pokemontcg.io/base1/26.png',  'https://images.pokemontcg.io/base1/26_hires.png'),
('base1', '44',  'Bulbasaur',      'POKEMON', 'Basic',   'COMMON',     'https://images.pokemontcg.io/base1/44.png',  'https://images.pokemontcg.io/base1/44_hires.png'),
('base1', '46',  'Charmander',     'POKEMON', 'Basic',   'COMMON',     'https://images.pokemontcg.io/base1/46.png',  'https://images.pokemontcg.io/base1/46_hires.png'),
('base1', '58',  'Pikachu',        'POKEMON', 'Basic',   'COMMON',     'https://images.pokemontcg.io/base1/58.png',  'https://images.pokemontcg.io/base1/58_hires.png'),
('base1', '63',  'Squirtle',       'POKEMON', 'Basic',   'COMMON',     'https://images.pokemontcg.io/base1/63.png',  'https://images.pokemontcg.io/base1/63_hires.png'),
('base1', '77',  'Computer Search','TRAINER', NULL,      'RARE',       'https://images.pokemontcg.io/base1/77.png',  'https://images.pokemontcg.io/base1/77_hires.png'),
('base1', '78',  'Devolution Spray','TRAINER',NULL,      'RARE',       'https://images.pokemontcg.io/base1/78.png',  'https://images.pokemontcg.io/base1/78_hires.png'),
('base1', '82',  'Professor Oak',  'TRAINER', NULL,      'UNCOMMON',   'https://images.pokemontcg.io/base1/82.png',  'https://images.pokemontcg.io/base1/82_hires.png'),
('base1', '88',  'Energy Retrieval','TRAINER',NULL,      'UNCOMMON',   'https://images.pokemontcg.io/base1/88.png',  'https://images.pokemontcg.io/base1/88_hires.png'),
('base1', '97',  'Fire Energy',    'ENERGY',  'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/97.png',  'https://images.pokemontcg.io/base1/97_hires.png'),
('base1', '98',  'Grass Energy',   'ENERGY',  'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/98.png',  'https://images.pokemontcg.io/base1/98_hires.png'),
('base1', '99',  'Lightning Energy','ENERGY', 'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/99.png',  'https://images.pokemontcg.io/base1/99_hires.png'),
('base1', '100', 'Psychic Energy', 'ENERGY',  'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/100.png', 'https://images.pokemontcg.io/base1/100_hires.png'),
('base1', '101', 'Fighting Energy','ENERGY',  'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/101.png', 'https://images.pokemontcg.io/base1/101_hires.png'),
('base1', '102', 'Water Energy',   'ENERGY',  'Basic Energy', 'COMMON', 'https://images.pokemontcg.io/base1/102.png', 'https://images.pokemontcg.io/base1/102_hires.png');

-- ============================================================
-- Card Types (속성)
-- ============================================================
INSERT INTO card_types (card_id, type)
SELECT c.id, 'PSYCHIC'   FROM cards c WHERE c.set_id = 'base1' AND c.number = '1';   -- Alakazam
INSERT INTO card_types (card_id, type)
SELECT c.id, 'WATER'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '2';   -- Blastoise
INSERT INTO card_types (card_id, type)
SELECT c.id, 'COLORLESS' FROM cards c WHERE c.set_id = 'base1' AND c.number = '3';   -- Chansey
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIRE'      FROM cards c WHERE c.set_id = 'base1' AND c.number = '4';   -- Charizard
INSERT INTO card_types (card_id, type)
SELECT c.id, 'COLORLESS' FROM cards c WHERE c.set_id = 'base1' AND c.number = '5';   -- Clefairy
INSERT INTO card_types (card_id, type)
SELECT c.id, 'WATER'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '6';   -- Gyarados
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIGHTING'  FROM cards c WHERE c.set_id = 'base1' AND c.number = '7';   -- Hitmonchan
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIGHTING'  FROM cards c WHERE c.set_id = 'base1' AND c.number = '8';   -- Machamp
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '9';   -- Magneton
INSERT INTO card_types (card_id, type)
SELECT c.id, 'PSYCHIC'   FROM cards c WHERE c.set_id = 'base1' AND c.number = '10';  -- Mewtwo
INSERT INTO card_types (card_id, type)
SELECT c.id, 'PSYCHIC'   FROM cards c WHERE c.set_id = 'base1' AND c.number = '11';  -- Nidoking
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIRE'      FROM cards c WHERE c.set_id = 'base1' AND c.number = '12';  -- Ninetales
INSERT INTO card_types (card_id, type)
SELECT c.id, 'COLORLESS' FROM cards c WHERE c.set_id = 'base1' AND c.number = '13';  -- Pidgeot
INSERT INTO card_types (card_id, type)
SELECT c.id, 'WATER'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '14';  -- Poliwrath
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '15';  -- Raichu
INSERT INTO card_types (card_id, type)
SELECT c.id, 'GRASS'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '16';  -- Scyther
INSERT INTO card_types (card_id, type)
SELECT c.id, 'GRASS'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '17';  -- Venusaur
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '18';  -- Zapdos
INSERT INTO card_types (card_id, type)
SELECT c.id, 'GRASS'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '19';  -- Beedrill
INSERT INTO card_types (card_id, type)
SELECT c.id, 'COLORLESS' FROM cards c WHERE c.set_id = 'base1' AND c.number = '20';  -- Dragonair
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIGHTING'  FROM cards c WHERE c.set_id = 'base1' AND c.number = '21';  -- Dugtrio
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '22';  -- Electabuzz
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '23';  -- Electrode
INSERT INTO card_types (card_id, type)
SELECT c.id, 'COLORLESS' FROM cards c WHERE c.set_id = 'base1' AND c.number = '24';  -- Pidgeotto
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIRE'      FROM cards c WHERE c.set_id = 'base1' AND c.number = '25';  -- Arcanine
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIRE'      FROM cards c WHERE c.set_id = 'base1' AND c.number = '26';  -- Charmeleon
INSERT INTO card_types (card_id, type)
SELECT c.id, 'GRASS'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '44';  -- Bulbasaur
INSERT INTO card_types (card_id, type)
SELECT c.id, 'FIRE'      FROM cards c WHERE c.set_id = 'base1' AND c.number = '46';  -- Charmander
INSERT INTO card_types (card_id, type)
SELECT c.id, 'LIGHTNING' FROM cards c WHERE c.set_id = 'base1' AND c.number = '58';  -- Pikachu
INSERT INTO card_types (card_id, type)
SELECT c.id, 'WATER'     FROM cards c WHERE c.set_id = 'base1' AND c.number = '63';  -- Squirtle
