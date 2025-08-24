package io.determann.shadow.internal.dsl;

import org.jetbrains.annotations.Nullable;


record Padding(@Nullable String beforeAll,
               @Nullable String beforeEach,
               @Nullable String afterEach,
               @Nullable String afterAll) {}
