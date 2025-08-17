package io.determann.shadow.api.dsl;

public record NameRenderedEvent(String packageName,
                                String simpleName,
                                String result,
                                boolean qualified) {}
