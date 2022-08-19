package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Wildcard;

public interface AnnotationConverter
{
   Array asArray();

   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();
}
