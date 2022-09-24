package org.determann.shadow.api.converter.module;

import org.determann.shadow.api.shadow.module.*;

import java.util.Optional;

public interface DirectiveConverter
{
   Exports toExports();

   Optional<Exports> toOptionalExports();

   Opens toOpens();

   Optional<Opens> toOptionalOpens();

   Provides toProvides();

   Optional<Provides> toOptionalProvides();

   Requires toRequires();

   Optional<Requires> toOptionalRequires();

   Uses toUses();

   Optional<Uses> toOptionalUses();
}
