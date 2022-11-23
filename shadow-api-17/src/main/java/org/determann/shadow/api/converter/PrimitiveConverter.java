package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Class;

public interface PrimitiveConverter
{
   Class asBoxed();

   Array asArray();
}
