ALTER TABLE apartment_listings
    ADD COLUMN amenities JSONB DEFAULT '{
      "wifi": false,
      "washingMachine": false,
      "boiler": false,
      "airConditioner": false,
      "dishwasher": false,
      "elevator": false,
      "shelter": false,
      "parking": false,
      "security": false,
      "petFriendly": false,
      "kidsFriendly": false
    }'::jsonb;