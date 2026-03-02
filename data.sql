CREATE TABLE IF NOT EXISTS plants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(100) NOT NULL,
    path_gambar VARCHAR(255) NOT NULL,
    deskripsi TEXT NOT NULL,
    manfaat TEXT NOT NULL,
    efek_samping TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS rumah_adat (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(120) NOT NULL,
    path_gambar VARCHAR(255) NOT NULL,
    asal VARCHAR(120) NOT NULL,
    deskripsi TEXT NOT NULL,
    ciri_khas TEXT NOT NULL,
    fungsi TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
