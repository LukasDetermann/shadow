package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Array;
import org.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;

public interface PrimitiveConverter
{
   Shadow<TypeMirror> asBoxed();

   Array asArray();
}
