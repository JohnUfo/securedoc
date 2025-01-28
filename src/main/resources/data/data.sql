INSERT INTO users (
    id, user_id, first_name, last_name, email, phone, bio, reference_id, image_url, created_by, updated_by, created_at, updated_at, account_non_expired, account_non_locked, enabled, mfa
) VALUES (
             0,
             gen_random_uuid(),
             'Umidjon',
             'Muydinov',
             'tursunov.umidjon.uz@gmail.com',
             '+998900679575',
             'THIS USER IS NOT ACTUAL USER',
             gen_random_uuid(),
             'https://cdn-icons-png.flaticon.com/512/149/149071.png',
             0,
             0,
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP,
             true,
             true,
             true,
             false
         );
