INSERT INTO users (
    id, user_id, first_name, last_name, email, phone, bio, reference_id, image_url, created_by, updated_by, created_at, updated_at, account_non_expired, account_non_locked, enabled, mfa
) VALUES (
             0,
             gen_random_uuid(), -- Generates a unique UUID for the user_id field
             'Umidjon',
             'Muydinov',
             'tursunov.umidjon.uz@gmail.com',
             '+998900679575',
             'THIS USER IS NOT ACTUAL USER',
             gen_random_uuid(), -- Generates a unique UUID for the reference_id field
             'https://cdn-icons-png.flaticon.com/512/149/149071.png',
             0,
             0,
             CURRENT_TIMESTAMP, -- Sets the current timestamp for created_at
             CURRENT_TIMESTAMP, -- Sets the current timestamp for updated_at
             true, -- Sets the account_non_expired field to true
             true, -- Sets the account_non_locked field to true
             true, -- Sets the enabled field to true
             false -- Sets the mfa field to false (or true, depending on your requirements)
         );
