package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Wildcard;

public interface InterfaceConverter
{
   Array asArray();

   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();
}
