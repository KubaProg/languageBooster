-- migration: 20251009173117_create_collection_and_card_tables.sql
-- description: creates the initial collection and card tables for the language booster app.
--
-- collections are user-specific sets of flashcards.
-- cards belong to a collection and store the front/back content.
--
-- affected tables:
-- - public.collection
-- - public.card
--
-- security:
-- - enables row level security (rls) on both tables.
-- - policies restrict access to the owner of the collection.

-- step 1: create the 'collection' table
-- this table stores user-created collections of flashcards.
create table public.collection (
    -- unique identifier for the collection.
    id uuid primary key not null default gen_random_uuid(),

    -- id of the user who owns the collection, from supabase auth.
    owner_id uuid not null,

    -- name of the collection, must be unique per user.
    name varchar(120) not null,

    -- base language for the flashcards (e.g., 'en' for english).
    base_lang varchar(5) not null,

    -- target language for the flashcards (e.g., 'es' for spanish).
    target_lang varchar(5) not null,

    -- timestamp of when the collection was created.
    created_at timestamptz not null default now(),

    -- constraints
    -- ensure that each user has uniquely named collections.
    unique (owner_id, name),
    -- ensure that base and target languages are different.
    check (base_lang <> target_lang),
    -- constrain the supported languages for the mvp.
    check (base_lang in ('pl', 'en', 'es', 'de') and target_lang in ('pl', 'en', 'es', 'de'))
);

-- add comments to the columns of the 'collection' table.
comment on table public.collection is 'stores user-specific collections of flashcards.';
comment on column public.collection.id is 'pk: unique identifier for the collection.';
comment on column public.collection.owner_id is 'id of the user who owns the collection (from auth.users).';
comment on column public.collection.name is 'name of the collection, unique for each user.';
comment on column public.collection.base_lang is 'base language of the flashcards in this collection.';
comment on column public.collection.target_lang is 'target language for learning.';
comment on column public.collection.created_at is 'timestamp of when the collection was created.';


-- step 2: create the 'card' table
-- this table stores the individual flashcards within a collection.
create table public.card (
    -- unique identifier for the card.
    id uuid primary key not null default gen_random_uuid(),

    -- foreign key to the 'collection' table.
    collection_id uuid not null references public.collection(id) on delete cascade,

    -- the 'front' side of the flashcard (e.g., a word in the base language).
    front text not null,

    -- the 'back' side of the flashcard (e.g., the translation in the target language).
    back text not null,

    -- flag indicating if the user has marked this card as 'known'.
    known boolean not null default false,

    -- timestamp of the last update to the card.
    updated_at timestamptz not null default now(),

    -- timestamp of when the card was created.
    created_at timestamptz not null default now()
);

-- add comments to the columns of the 'card' table.
comment on table public.card is 'stores individual flashcards belonging to a collection.';
comment on column public.card.id is 'pk: unique identifier for the card.';
comment on column public.card.collection_id is 'fk: references the collection this card belongs to.';
comment on column public.card.front is 'the front content of the flashcard.';
comment on column public.card.back is 'the back content of the flashcard.';
comment on column public.card.known is 'true if the user has marked the card as known.';
comment on column public.card.updated_at is 'timestamp of the last update.';
comment on column public.card.created_at is 'timestamp of when the card was created.';


-- step 3: enable row level security (rls)
-- rls is enabled to ensure data privacy and security.
-- policies will be created to control access based on user roles and ownership.

-- enable rls for the 'collection' table.
alter table public.collection enable row level security;

-- enable rls for the 'card' table.
alter table public.card enable row level security;


-- step 4: create rls policies for the 'collection' table
-- these policies ensure that users can only access and manage their own collections.

-- policy: allow authenticated users to select their own collections.
create policy "allow_select_own_collection"
on public.collection for select
to authenticated
using (auth.uid() = owner_id);

-- policy: allow authenticated users to insert new collections for themselves.
create policy "allow_insert_own_collection"
on public.collection for insert
to authenticated
with check (auth.uid() = owner_id);

-- policy: allow authenticated users to update their own collections.
create policy "allow_update_own_collection"
on public.collection for update
to authenticated
using (auth.uid() = owner_id);

-- policy: allow authenticated users to delete their own collections.
create policy "allow_delete_own_collection"
on public.collection for delete
to authenticated
using (auth.uid() = owner_id);


-- step 5: create rls policies for the 'card' table
-- these policies ensure that users can only access and manage cards within their own collections.

-- policy: allow authenticated users to select cards from their own collections.
create policy "allow_select_own_card"
on public.card for select
to authenticated
using (exists (
    select 1 from public.collection
    where collection.id = card.collection_id and collection.owner_id = auth.uid()
));

-- policy: allow authenticated users to insert cards into their own collections.
create policy "allow_insert_own_card"
on public.card for insert
to authenticated
with check (exists (
    select 1 from public.collection
    where collection.id = card.collection_id and collection.owner_id = auth.uid()
));

-- policy: allow authenticated users to update cards in their own collections.
create policy "allow_update_own_card"
on public.card for update
to authenticated
using (exists (
    select 1 from public.collection
    where collection.id = card.collection_id and collection.owner_id = auth.uid()
));

-- policy: allow authenticated users to delete cards from their own collections.
create policy "allow_delete_own_card"
on public.card for delete
to authenticated
using (exists (
    select 1 from public.collection
    where collection.id = card.collection_id and collection.owner_id = auth.uid()
));

-- step 6: create indexes for performance
-- create an index on collection_id in the card table for faster lookups.
create index idx_card_collection_id on public.card(collection_id);

-- create a composite index on collection_id and known for filtering learning pool.
create index idx_card_collection_known on public.card(collection_id, known);
