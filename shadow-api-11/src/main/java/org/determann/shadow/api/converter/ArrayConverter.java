package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Wildcard;

public interface ArrayConverter
{
   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();

   Array asArray();
}
