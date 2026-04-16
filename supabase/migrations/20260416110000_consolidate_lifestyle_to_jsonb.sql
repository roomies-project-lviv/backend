-- Перенесення даних у lifestyle_flags
UPDATE users
SET lifestyle_flags = COALESCE(lifestyle_flags, '{}'::jsonb) || jsonb_build_object(
        'sleepSchedule', sleep_schedule,
        'guestsFrequency', guests_frequency,
        'noiseTolerance', noise_tolerance,
        'cleanlinessLevel', cleanliness_level,
        'dietaryPreferences', dietary_preferences
                                                                );

-- Видалення застарілих колонок
ALTER TABLE users
DROP COLUMN sleep_schedule,
DROP COLUMN guests_frequency,
DROP COLUMN noise_tolerance,
DROP COLUMN cleanliness_level,
DROP COLUMN dietary_preferences;