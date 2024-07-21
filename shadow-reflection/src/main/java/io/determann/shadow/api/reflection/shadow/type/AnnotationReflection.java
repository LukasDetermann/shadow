package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.shadow.type.Annotation;

public interface AnnotationReflection extends Annotation,
                                              DeclaredReflection,
                                              StaticModifiableReflection
{
}
