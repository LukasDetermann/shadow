package org.determann.shadow.api.converter.module;

import org.determann.shadow.api.shadow.module.*;

import java.util.Optional;

public interface DirectiveConverter
{
   Exports toExportsOrThrow();

   Optional<Exports> toExports();

   Opens toOpensOrThrow();

   Optional<Opens> toOpens();

   Provides toProvidesOrThrow();

   Optional<Provides> toProvides();

   Requires toRequiresOrThrow();

   Optional<Requires> toRequires();

   Uses toUsesOrThrow();

   Optional<Uses> toUses();
}
