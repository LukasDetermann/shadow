package org.determann.shadow.api.converter.module;

import org.determann.shadow.api.shadow.module.*;

import java.util.Optional;

public interface DirectiveConverter
{
   Optional<Exports> toExports();

   Optional<Opens> toOpens();

   Optional<Provides> toProvides();

   Optional<Requires> toRequires();

   Optional<Uses> toUses();
}
