package io.determann.shadow.api.renderer;

public record NameRenderedEvent(String packageName,
                                String simpleName,
                                String result,
                                boolean qualified) {}
