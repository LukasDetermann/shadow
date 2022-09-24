package org.determann.shadow.api.converter.module;

import org.determann.shadow.api.shadow.module.*;

import java.util.Optional;

public interface DirectiveConverter
{
   Optional<Exports> toOptionalExports();

   Optional<Opens> toOptionalOpens();

   Optional<Provides> toOptionalProvides();

   Optional<Requires> toOptionalRequires();

   Optional<Uses> toOptionalUses();
}
